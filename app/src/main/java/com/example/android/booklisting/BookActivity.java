package com.example.android.booklisting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    final static String LOG_TAG = BookActivity.class.getSimpleName();
    private BookAdapter mAdapter = null;
    static final String URL="https://www.googleapis.com/books/v1/volumes?q=tech&maxResults=6";
    private ArrayList<Book> books =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);

        //ArrayList<Book> books = Utils.extractBook();

        new BookDownloader().execute(URL);
        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this,new ArrayList<Book>());
        listView.setAdapter(mAdapter);

    }

    private class BookDownloader extends AsyncTask<String, Void, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {

            if(urls.length<1|| urls[0]==null){
                return null;
            }

            books = Utils.fetchBookData(urls[0]);

            return books;

        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
           mAdapter.clear();

            if(books!=null && !books.isEmpty()){
                mAdapter.addAll(books);
            }
        }
    }

}
