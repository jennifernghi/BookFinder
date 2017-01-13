package com.example.android.booklisting;

import android.content.Loader;
import android.os.Bundle;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    final static String LOG_TAG = BookActivity.class.getSimpleName();
    //static final String URL = "https://www.googleapis.com/books/v1/volumes?q=let's+get+naked&maxResults=20";
    static final String URL = "https://www.googleapis.com/books/v1/volumes";
    private BookAdapter mAdapter = null;
    //private ArrayList<Book> books = null;
    private TextView mEmptyTextView;
    //private boolean loading = true;
    private ProgressBar progressBar;

    private String searchTerm = "android programming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

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
                    mAdapter.clear();
                    searchTerm = searchEditText.getText().toString().trim();
                    searchBook();
                }
            }
        });


        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(1,null,this);

    }

    private void searchBook() {
        getLoaderManager().restartLoader(1, null, this);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        return new BookLoader(this, URL, searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        mEmptyTextView.setText("No book found!");

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        mAdapter.clear();
    }


}
