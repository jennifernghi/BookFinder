package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    final static String LOG_TAG = BookLoader.class.getSimpleName();
    private String mUrl;
    private String mSearchTerm;
    private int mStartIndex;

    public BookLoader(Context context, String url, String searchTerm, int startIndex) {
        super(context);
        this.mUrl = url;
        this.mSearchTerm = searchTerm;
        this.mStartIndex = startIndex;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        ArrayList<Book> books;
        if (mUrl.length() < 1 || mUrl == null) {
            return null;
        }
        String url = Utils.buildURL(mUrl, mSearchTerm, mStartIndex);
        Log.i(LOG_TAG, "url: " + url);

        books = Utils.fetchBookData(url);

        return books;

    }


}
