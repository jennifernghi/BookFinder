package com.example.android.booklisting;

import android.content.Context;
import android.content.res.Resources;
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
    static Context context;
    static final int DEFAULT_MAX_RESULTS =10;
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
                Log.e(LOG_TAG, context.getString(R.string.error_create_url));
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
        String reponse = context.getString(R.string.empty_string);
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
                Log.e(LOG_TAG, context.getString(R.string.error_response_code) + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.error_http_connection));
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

            // array "items"
            JSONArray items = root.getJSONArray(context.getString(R.string.items));
            for (int i = 0; i < items.length(); i++) {

                JSONObject item = (JSONObject) items.get(i);
                JSONObject volumeInfo = item.getJSONObject(context.getString(R.string.volume_info));

                // get book title
                String title = null;
                try {
                    title = volumeInfo.getString(context.getString(R.string.title));
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + context.getString(R.string.error_no_title));
                }

                if (title == null) {
                    title = context.getString(R.string.unknown);
                }
                //get array of authors
                authors = new ArrayList<>();
                JSONArray authorArray = null;
                try {
                    authorArray = volumeInfo.getJSONArray(context.getString(R.string.authors));
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + context.getString(R.string.error_no_authors));
                }
                if (authorArray != null) {
                    for (int k = 0; k < authorArray.length(); k++) {
                        authors.add(new Author(authorArray.getString(k)));
                    }
                } else {
                    authors.add(new Author(context.getString(R.string.unknown)));
                }
                //get array list of isbns: type and code
                isbns = new ArrayList<>();
                JSONArray industryIdentifiers = null;
                try {
                    industryIdentifiers = volumeInfo.getJSONArray(context.getString(R.string.industryIdentifiers));
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + context.getString(R.string.error_no_identifiers));
                }

                if (industryIdentifiers != null) {
                    for (int j = 0; j < industryIdentifiers.length(); j++) {
                        JSONObject industryIdentifiersObject = (JSONObject) industryIdentifiers.get(j);
                        String isbnType = industryIdentifiersObject.getString( context.getString(R.string.type));
                        String isbn = industryIdentifiersObject.getString( context.getString(R.string.identifier));
                        isbns.add(new ISBN(isbnType, isbn));
                    }
                } else {
                    String isbnType =  context.getString(R.string.empty_string);
                    String isbn =  context.getString(R.string.empty_string);
                    isbns.add(new ISBN(isbnType, isbn));
                }

                //get book bitmap image from url link
                JSONObject imageLinks = null;
                String imageUrl = null;

                try {
                    imageLinks = volumeInfo.getJSONObject(context.getString(R.string.imageLinks));
                    imageUrl = imageLinks.getString(context.getString(R.string.thumbnail)).trim();
                } catch (Exception e) {
                    Log.e(LOG_TAG, i + context.getString(R.string.error_no_image));
                }

                String infoLink = null;
                try {
                    infoLink = volumeInfo.getString(context.getString(R.string.infoLink)).trim();
                } catch (Exception e) {
                    Log.e(LOG_TAG, i+ context.getString(R.string.error_no_link));
                }

                if (infoLink == null) {
                    infoLink = context.getString(R.string.empty_string);
                }


                if (imageLinks != null) {
                    //convert imageUrl to Bitmap img
                    InputStream in = new URL(imageUrl).openStream();
                    image = BitmapFactory.decodeStream(in);
                    //add book to arrayList
                    books.add(new Book(context,title, authors, isbns, image, infoLink));
                } else {
                    //add book w/o images to arrayList
                    books.add(new Book(context, title, authors, isbns, infoLink));
                }

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, context.getString(R.string.error_parsing));

        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.error_decode_bitmap));
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
            Log.e(LOG_TAG, context.getString(R.string.error_io_exception));
        }


        //step 4
        ArrayList<Book> books = extractBook(response);


        return books;
    }

    /**
     * build URL string from baseurl, searchtime, startIndex
     *
     * @param urlString
     * @param searchterms
     * @param startIndex
     * @return
     */
    public static String buildURL(Context ct, String urlString, String searchterms, int startIndex) {
        context = ct;
        String[] params = searchterms.trim().split(context.getString(R.string.blank_space));
        String keyword = context.getString(R.string.empty_string);
        for (int i = 0; i < params.length; i++) {
            if (i != params.length - 1) {
                keyword += params[i] + "+";
            } else {
                keyword += params[i];
            }
        }

        Uri base = Uri.parse(urlString);
        Uri.Builder builder = base.buildUpon();
        builder.appendQueryParameter(context.getString(R.string.param_q), keyword);
        builder.appendQueryParameter(context.getString(R.string.param_max_result), String.valueOf(DEFAULT_MAX_RESULTS));
        builder.appendQueryParameter(context.getString(R.string.param_start_index), String.valueOf(startIndex));
        String url = builder.toString().replace("%2B", "+");
        return url;
    }

}
