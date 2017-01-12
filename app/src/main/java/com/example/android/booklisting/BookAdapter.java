package com.example.android.booklisting;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.resource;

/**
 * Created by jennifernghinguyen on 1/11/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    final static String LOG_TAG = BookAdapter.class.getSimpleName();
    private Book currentBook;
    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.book_list_item_view,parent,false);
        }

        currentBook = getItem(position);

        //Show book image
        ImageView imageView = (ImageView) convertView.findViewById(R.id.book_image);
        imageView.setImageBitmap(currentBook.getmImage());

        //show book title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.book_title);
        titleTextView.setText(currentBook.getTitle());

        //show book author(s)
        TextView authorTextView = (TextView) convertView.findViewById(R.id.book_author);
        authorTextView.setText(currentBook.printAuthors());

        //show book isbn(s)
        TextView isbnTextView = (TextView) convertView.findViewById(R.id.book_isbn);
        isbnTextView.setText(currentBook.printIsbn());

        return convertView;
    }
}
