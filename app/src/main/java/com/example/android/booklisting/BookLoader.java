package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    final static String LOG_TAG = BookLoader.class.getSimpleName();
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        this.mUrl=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books=null;
        if(this.mUrl==null){
            return null;
        }

        //fetch data from the internet

        return books;
    }
}
