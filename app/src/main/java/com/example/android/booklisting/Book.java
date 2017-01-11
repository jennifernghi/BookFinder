package com.example.android.booklisting;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class Book {
    private String mTitle;
    private String[] mAuthors;
    private String[] mIsbn;
    private String mImageLink;

    public Book(String title, String[] authors, String[] isbn, String imageLink) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mIsbn = isbn;
        this.mImageLink = imageLink;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String[] getAuthors() {
        return this.mAuthors;
    }

    public String[] getIsbn() {
        return this.mIsbn;
    }

    public String getImageLink() {
        return this.mImageLink;
    }
}
