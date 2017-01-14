package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
    final static int LOADER_CONSTANT = 1;
    static final String URL = "https://www.googleapis.com/books/v1/volumes";
    private BookAdapter mAdapter = null;

    private int counter = 0;
    private TextView mEmptyTextView;

    private ProgressBar progressBar;

    private String searchTerm = "love";

    private LoaderManager loaderManager;

    private View footView;
    private Button nextButton;
    private Button previousButton;
    private ListView listView;
    private EditText searchEditText;

    private int indexStart = 0;
    private int booksSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        if (savedInstanceState != null) {
            //orientationChanged=1;
            int savedCounter = savedInstanceState.getInt("counter");
            counter = savedCounter;
            Log.i(LOG_TAG, "counter after orientation changed= " + counter);

            int savedIndexStart = savedInstanceState.getInt("indexStart");
            indexStart = savedIndexStart;
            Log.i(LOG_TAG, "indexstart after orientation changed= " + indexStart);

            int savedBooksSize = savedInstanceState.getInt("booksSize");
            booksSize = savedBooksSize;
            Log.i(LOG_TAG, "booksSize after orientation changed= " + booksSize);

            String savedSearchTerm = savedInstanceState.getString("searchTerm");
            searchTerm = savedSearchTerm;
            searchEditText = (EditText) findViewById(R.id.search_input);
            searchEditText.setText(searchTerm);
            Log.i(LOG_TAG, "searchterms after orientation changed= " + searchTerm);


        } else {
            Log.i(LOG_TAG, "savedInstanceState =  null");
        }
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        footView = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_list_view_footer, null, false);

        nextButton = (Button) footView.findViewById(R.id.next);
        previousButton = (Button) footView.findViewById(R.id.previous);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);

        listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(mEmptyTextView);


        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);


        Button searchButton = (Button) findViewById(R.id.search_button);
        searchEditText = (EditText) findViewById(R.id.search_input);
        searchEditText.setText(searchTerm);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                if (searchEditText.getText() != null || searchEditText.getText().toString().equals("")) {
                    mAdapter.clear();
                    orientationChanged = -1;
                    indexStart = 0;
                    searchTerm = searchEditText.getText().toString().trim();
                    searchBook();
                }
            }
        });

        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_CONSTANT, null, this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ++counter;
                indexStart += booksSize;
                Log.i(LOG_TAG, "next: indexStart =" + indexStart);
                Log.i(LOG_TAG, "next: counter =" + counter);
                orientationChanged = -1;
                loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                --counter;
                indexStart -= booksSize;
                orientationChanged = -1;

                Log.i(LOG_TAG, "previous: indexStart =" + indexStart);
                Log.i(LOG_TAG, "previous: counter =" + counter);
                loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
            }
        });
    }

    private void searchBook() {

        loaderManager.restartLoader(LOADER_CONSTANT, null, this);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
        return new BookLoader(this, URL, searchTerm, indexStart);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        mEmptyTextView.setText("No book found!");

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            /*if(orientationChanged == -1) {
                ++counter;
                indexStart += books.size();
            }*/
            booksSize = books.size();
            Log.i(LOG_TAG, "onLoadFinished: indexStart = " + indexStart);
            Log.i(LOG_TAG, "onLoadFinished: counter = " + counter);
            mAdapter.addAll(books);
            mAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);
            listView.removeFooterView(footView);
            listView.addFooterView(footView);
            nextButton.setVisibility(View.VISIBLE);
            if (counter >= 1) {
                previousButton.setVisibility(View.VISIBLE);
            } else {
                previousButton.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
        Log.i(LOG_TAG, "save counter = " + outState.getInt("counter"));
        outState.putInt("indexStart", indexStart);
        Log.i(LOG_TAG, "save instart = " + outState.getInt("indexStart"));
        outState.putInt("booksSize", booksSize);
        Log.i(LOG_TAG, "save booksSize = " + outState.getInt("booksSize"));
        outState.putString("searchTerm", searchTerm);
        Log.i(LOG_TAG, "save searchTerm = " + outState.getString("searchTerm"));
    }
}
