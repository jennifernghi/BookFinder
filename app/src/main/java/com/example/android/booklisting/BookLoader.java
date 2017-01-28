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
    private Context context;

    public BookLoader(Context context, String url, String searchTerm, int startIndex) {
        super(context);
        this.context = context;
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
        String url = Utils.buildURL(context, mUrl, mSearchTerm, mStartIndex);
        Log.i(LOG_TAG,url);
        books = Utils.fetchBookData(url);

        return books;

    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Book> data) {
        super.onCanceled(data);

        data.clear();
    }
}
