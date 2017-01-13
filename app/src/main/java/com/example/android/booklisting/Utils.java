package com.example.android.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
                Log.i(LOG_TAG, i + "title" + title);

                //get array of authors
                authors = new ArrayList<>();
                JSONArray authorArray = null;
                try {
                     authorArray = volumeInfo.getJSONArray("authors");
                }catch (Exception e){
                    Log.e(LOG_TAG, i+ " this book has no info about authors");
                }
                if(authorArray!=null) {
                    for (int k = 0; k < authorArray.length(); k++) {
                        authors.add(new Author(authorArray.getString(k)));
                        Log.i(LOG_TAG, i + "author: " + k + ": " + authorArray.getString(k));
                    }
                }else{
                    authors.add(new Author("unknown"));
                }
                //get array list of isbns: type and code
                isbns = new ArrayList<>();
                JSONArray industryIdentifiers = null;
                try {
                    industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + " this book has no industryIdentifiers");
                }

                if (industryIdentifiers != null) {
                    for (int j = 0; j < industryIdentifiers.length(); j++) {
                        JSONObject industryIdentifiersObject = (JSONObject) industryIdentifiers.get(j);
                        String isbnType = industryIdentifiersObject.getString("type");
                        String isbn = industryIdentifiersObject.getString("identifier");
                        isbns.add(new ISBN(isbnType, isbn));
                        Log.i(LOG_TAG, i + "isbn: " + j + ": " + isbnType + " " + isbn);
                    }
                } else {
                    String isbnType = "";
                    String isbn = "";
                    isbns.add(new ISBN(isbnType, isbn));
                }

                //get book bitmap image from url link
                JSONObject imageLinks = null;
                String imageUrl = null;

                try {
                    imageLinks = volumeInfo.getJSONObject("imageLinks");
                    imageUrl = imageLinks.getString("thumbnail").trim();
                    Log.i(LOG_TAG, i + "imgurl: " + imageUrl);
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + " this book has no thumbnail image");
                }

                if (imageLinks != null) {
                    //convert imageUrl to Bitmap img
                    InputStream in = new URL(imageUrl).openStream();
                    image = BitmapFactory.decodeStream(in);
                    //add book to arrayList
                    books.add(new Book(title, authors, isbns, image));
                } else {
                    //add book to arrayList
                    books.add(new Book(title, authors, isbns));
                }

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
            Log.i(LOG_TAG, response);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOEXception: downloadJsonResponse(url) ");
        }


        //step 4
        ArrayList<Book> books = extractBook(response);


        return books;
    }

    public static String buildURL(String urlString, String searchterms) {
        String[] params = searchterms.trim().split(" ");
        String keyword = "";
        for (int i = 0; i < params.length; i++) {
            if (i != params.length - 1) {
                keyword += params[i] + "+";
            } else {
                keyword += params[i];
            }
        }

        Uri base = Uri.parse(urlString);
        Uri.Builder builder = base.buildUpon();
        builder.appendQueryParameter("q", keyword);
        builder.appendQueryParameter("maxResults", "15");
        String url = builder.toString().replace("%2B", "+");
        return url;

    }
}
