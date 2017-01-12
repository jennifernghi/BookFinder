package com.example.android.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    final static String LOG_TAG = BookActivity.class.getSimpleName();
    private BookAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);

        ArrayList<Book> books = Utils.extractBook();
        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this,books);
        listView.setAdapter(mAdapter);


    }
}
