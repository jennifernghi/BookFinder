package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class Book {
    private Context context;
    private String mTitle;
    private ArrayList<Author> mAuthors;
    private ArrayList<ISBN> mIsbn;
    private Bitmap mImage;
    private String mInfoLink;

    public Book(Context context, String title, ArrayList<Author> authors, ArrayList<ISBN> isbn, Bitmap image, String mInfoLink) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImage = image;
        this.mInfoLink = mInfoLink;
        this.context = context;
    }

    public Book(Context context, String title, ArrayList<Author> authors, ArrayList<ISBN> isbn, String mInfoLink) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImage = null;
        this.mInfoLink = mInfoLink;
        this.context = context;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public ArrayList<Author> getAuthors() {
        return this.mAuthors;
    }

    public ArrayList<ISBN> getIsbn() {
        return this.mIsbn;
    }


    public Bitmap getmImage() {
        return this.mImage;
    }

    public String printAuthors() {
        String str = context.getString(R.string.empty_string);
        for (int i = 0; i < mAuthors.size(); i++) {
            if (i != mAuthors.size() - 1) {
                str += mAuthors.get(i).getName() + context.getString(R.string.comma);
            } else {
                str += mAuthors.get(i).getName() + context.getString(R.string.period);
            }
        }
        return str;
    }

    public String printISBN() {
        String str = context.getString(R.string.empty_string);
        for (int i = 0; i < mIsbn.size(); i++) {
            if (i != mIsbn.size() - 1) {
                if (!mIsbn.get(i).getType().equals(context.getString(R.string.empty_string)) || !mIsbn.get(i).getIsbn().equals(context.getString(R.string.empty_string))) {
                    str += mIsbn.get(i).getType() + context.getString(R.string.colon) + mIsbn.get(i).getIsbn() + context.getString(R.string.comma);
                } else {
                    str += context.getString(R.string.empty_string);
                }
            } else {
                if (!mIsbn.get(i).getType().equals(context.getString(R.string.empty_string)) || !mIsbn.get(i).getIsbn().equals(context.getString(R.string.empty_string))) {
                    str += mIsbn.get(i).getType() + context.getString(R.string.colon) + mIsbn.get(i).getIsbn() + context.getString(R.string.period);
                } else {
                    str += context.getString(R.string.empty_string);
                }
            }
        }
        return str;
    }

    public String getInfoLink() {
        return this.mInfoLink;
    }
}
