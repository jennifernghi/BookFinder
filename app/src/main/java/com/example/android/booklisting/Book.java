package com.example.android.booklisting;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class Book {
    private String mTitle;
    private String[] mAuthors;
    private ArrayList<ISBN> mIsbn;
    private Bitmap mImage;

    public Book(String title, String[] authors, ArrayList<ISBN> isbn, Bitmap image) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImage = image;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String[] getAuthors() {
        return this.mAuthors;
    }

    public ArrayList<ISBN> getIsbn() {
        return this.mIsbn;
    }


    public Bitmap getmImage() {
        return this.mImage;
    }

    public String printAuthors() {
        String str = "";
        for (String author : mAuthors) {
            str += author + " ";
        }
        return str;
    }


}
