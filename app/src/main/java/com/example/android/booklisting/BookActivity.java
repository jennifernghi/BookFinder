package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    final static String LOG_TAG = BookActivity.class.getSimpleName();
    //static final String URL = "https://www.googleapis.com/books/v1/volumes?q=let's+get+naked&maxResults=20";
    static final String URL = "https://www.googleapis.com/books/v1/volumes";
    private BookAdapter mAdapter = null;
    private ArrayList<Book> books = null;
    private TextView mEmptyTextView;
    private boolean loading = true;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        if(loading){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(mEmptyTextView);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);



        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchEditText = (EditText) findViewById(R.id.search_input);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchEditText.getText()!=null || searchEditText.getText().toString().equals("")) {
                    searchBook(searchEditText.getText().toString().trim());
                }
            }
        });

        new BookDownloader("android platforms").execute(URL);

    }

    private void searchBook(String searchterms) {
        new BookDownloader(searchterms).execute(URL);
    }

    private class BookDownloader extends AsyncTask<String, Integer, ArrayList<Book>> {

        private String searchTerm;

        public BookDownloader(String searchTerm){
            this.searchTerm=searchTerm;
        }
        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            loading=true;

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            String url = Utils.buildURL(urls[0],searchTerm);
            Log.i(LOG_TAG, "url: "+url);
            publishProgress(0);
            books = Utils.fetchBookData(url);

            return books;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loading=true;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            loading=false;
            progressBar.setVisibility(View.GONE);
            mEmptyTextView.setText("No book found!");

            mAdapter.clear();

            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);
            }
        }
    }

}
