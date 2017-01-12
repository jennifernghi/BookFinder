package com.example.android.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public final class Utils {
    final static String LOG_TAG = Utils.class.getSimpleName();


    private Utils() {
    }

    /**
     * step 1 - create URL given a urlString
     *
     * @param urlString
     * @return URL
     */
    public static URL createURL(String urlString) {
        URL url = null;
        if (url == null) {
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error create URL");
            }
        }

        return url;
    }

    /**
     * step 2 - download Json responses from URL
     *
     * @param url
     * @return
     */
    public static String downloadJsonResponse(URL url) throws IOException {
        String reponse = "";
        if (url == null) {
            return reponse;
        }
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                reponse = getResponseFromStream(inputStream); //step 3
            } else {
                Log.e(LOG_TAG, "Error: response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: make HTTP URL connection");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return reponse;
    }

    /**
     * step 3 - get jsonResponse if httpurlconnection success
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String getResponseFromStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                response.append(line);
                line = reader.readLine();
            }

        }
        return response.toString();
    }

    /**
     * step 4 - extract book data from json response
     *
     * @param response
     * @return
     */
    public static ArrayList<Book> extractBook(String response) {
        ArrayList<Book> books = new ArrayList<>();
        ArrayList<ISBN> isbns;
        ArrayList<Author> authors;
        Bitmap image;
        try {
            JSONObject root = new JSONObject(response);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                // get book title
                String title = volumeInfo.getString("title");

                //get array of authors
                authors = new ArrayList<>();
                JSONArray authorArray = volumeInfo.getJSONArray("authors");
                for (int k = 0; k < authorArray.length(); k++) {
                    authors.add(new Author(authorArray.getString(k)));
                }

                //get array list of isbns: type and code
                isbns = new ArrayList<>();
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                for (int j = 0; j < industryIdentifiers.length(); j++) {
                    JSONObject industryIdentifiersObject = (JSONObject) industryIdentifiers.get(j);
                    String isbnType = industryIdentifiersObject.getString("type");
                    String isbn = industryIdentifiersObject.getString("identifier");
                    isbns.add(new ISBN(isbnType, isbn));
                }

                //get book bitmap image from url link
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String imageUrl = imageLinks.getString("thumbnail").trim();


                //convert imageUrl to Bitmap img
                InputStream in = new URL(imageUrl).openStream();
                image = BitmapFactory.decodeStream(in);

                //add book to arrayList
                books.add(new Book(title, authors, isbns, image));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Parsing error");

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error decoding bitmap");
        }

        return books;
    }


    public static ArrayList<Book> fetchBookData(String urlString) {
        //step 1
        URL url = createURL(urlString);


        String response = null;
        try {
            //step 2 and 3
            response = downloadJsonResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOEXception: downloadJsonResponse(url) ");
        }


        //step 4
        ArrayList<Book> books = extractBook(response);


        return books;
    }
}
