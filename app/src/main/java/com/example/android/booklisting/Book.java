package com.example.android.booklisting;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class Book {
    private String mTitle;
    private ArrayList<Author> mAuthors;
    private ArrayList<ISBN> mIsbn;
    private Bitmap mImage;

    public Book(String title, ArrayList<Author> authors, ArrayList<ISBN> isbn, Bitmap image) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImage = image;
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
        String str = "";
        for (int i = 0; i < mAuthors.size(); i++) {
            if (i != mAuthors.size() - 1) {
                str += mAuthors.get(i).getName() + ", ";
            } else {
                str += mAuthors.get(i).getName() + ". ";
            }
        }
        return str;
    }

    public String printISBN() {
        String str = "";
        for (int i = 0; i < mIsbn.size(); i++) {
            if (i != mIsbn.size() - 1) {
                if(!mIsbn.get(i).getType().equals("")||!mIsbn.get(i).getIsbn().equals("")){
                    str += mIsbn.get(i).getType() + ": " + mIsbn.get(i).getIsbn() + ", ";
                }else{
                    str += "";
                }
            } else {
                if(!mIsbn.get(i).getType().equals("")||!mIsbn.get(i).getIsbn().equals("")) {
                    str += mIsbn.get(i).getType() + ": " + mIsbn.get(i).getIsbn() + ". ";
                }else{
                    str += "";
                }
            }
        }
        return str;
    }
}
