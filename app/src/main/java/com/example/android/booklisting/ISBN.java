package com.example.android.booklisting;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class ISBN {
    private String mType;
    private String mIsbn;

    public ISBN(String type, String isbn){
        this.mType = type;
        this.mIsbn = isbn;
    }

    public String getType(){
        return this.mType;
    }

    public String getIsbn(){
        return this.mIsbn;
    }
}
