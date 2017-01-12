package com.example.android.booklisting;

import java.util.ArrayList;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class Book {
    private String mTitle;
    private ArrayList<Author> mAuthors;
    private ArrayList<ISBN> mIsbn;
    private String mImageURL;

    public Book(String title, ArrayList<Author> authors, ArrayList<ISBN> isbn, String imageURL) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImageURL = imageURL;
    }

    public Book(String title, ArrayList<Author> authors, ArrayList<ISBN> isbn) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
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


    public String getmImageURL() {
        return this.mImageURL;
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
                str += mIsbn.get(i).getType() + ": " + mIsbn.get(i).getIsbn() + ", ";
            } else {
                str += mIsbn.get(i).getType() + ": " + mIsbn.get(i).getIsbn() + ". ";
            }
        }
        return str;
    }
}
