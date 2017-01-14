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
    private TextView mEmptyTextView;//emptyview
    private ProgressBar progressBar;
    private LoaderManager loaderManager;
    private View footView;
    private Button nextButton;
    private Button previousButton;
    private ListView listView;
    private EditText searchEditText;
    private Button searchButton;

    //following variables need to be saved
    private int counter = 0; // +1 when next is clicked and -1 when previous is clicked
    private int indexStart = 0;// parameters indexStart in json response
    private int booksSize = 0;
    private String searchTerm = "love"; //value for searching after q=...
    private int totalItems = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        //retrieve saved variables
        if (savedInstanceState != null) {

            counter = savedInstanceState.getInt("counter");

            indexStart = savedInstanceState.getInt("indexStart");

            booksSize = savedInstanceState.getInt("booksSize");

            searchTerm = savedInstanceState.getString("searchTerm");
            searchEditText = (EditText) findViewById(R.id.search_input);
            searchEditText.setText(searchTerm);


        } else {
            Log.i(LOG_TAG, "savedInstanceState =  null");
        }
        //initialize views
        populateViews();
        //set empty view for list view
        listView.setEmptyView(mEmptyTextView);

        //initialize adapter with empty ArrayList<Book>
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        //searchEditText shows searchterm
        searchEditText.setText(searchTerm);
        //cursor is focused at the end of the word
        searchEditText.setSelection(searchTerm.length());

        //event for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0; //reset counter
                if (searchEditText.getText() != null || searchEditText.getText().toString().equals("")) {
                    mAdapter.clear(); //clear adapter
                    indexStart = 0;//reset indexStart
                    searchTerm = searchEditText.getText().toString().trim();// get new searchTerm
                    searchBook();
                }
            }
        });

        // initialize loaderManager
        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_CONSTANT, null, this);

        //event for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booksSize == 10) {
                    ++counter; //+1 when clicked
                    indexStart += booksSize; //new indexStart
                    Log.i(LOG_TAG, "next: booksize = " + booksSize);
                    Log.i(LOG_TAG, "next: indexstart = " + indexStart);

                    //restart loader
                    loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
                }
            }
        });

        //event for previous button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                --counter; //-1 when clicked
                if (booksSize == 10) {
                    indexStart -= booksSize;//new indexStart
                } else {
                    indexStart = 0;
                }
                Log.i(LOG_TAG, "previous: indexstart = " + indexStart);
                Log.i(LOG_TAG, "previous: booksize = " + booksSize);

                //restart loader
                loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
            }
        });
    }

    /**
     * call when search button is clicked
     */
    private void searchBook() {

        //restart loader with new search term
        loaderManager.restartLoader(LOADER_CONSTANT, null, this);
    }


    @Override
    /**
     * fork off loader background thread
     */
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        //show progressBar
        progressBar.setVisibility(View.VISIBLE);
        //hide empty view
        mEmptyTextView.setVisibility(View.GONE);
        return new BookLoader(this, URL, searchTerm, indexStart);
    }

    @Override
    /**
     * after finish loading
     * update UI
     */
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        //hide progressBar
        progressBar.setVisibility(View.GONE);
        //set text for empty view
        mEmptyTextView.setText("No book found!");

        mAdapter.clear();
        //if books sucessfully downloaded
        if (books != null && !books.isEmpty()) {
            //get totalItem from Util
            totalItems = Utils.getTotalItems();

            Log.i(LOG_TAG, "totalItems = " + totalItems);


            //add all books to adapter
            mAdapter.addAll(books);
            mAdapter.notifyDataSetChanged();

            //initialize books size
            booksSize = mAdapter.getCount();

            //automatically scroll to the top of the listview
            listView.smoothScrollToPosition(0);

            //set footview
            listView.removeFooterView(footView);
            listView.addFooterView(footView);

            //show next button
            if (booksSize == 10) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.GONE);
            }


            //show previous button
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
    /**
     * save 4 variables: counter, indexStart, booksSize, searchTerm
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);

        outState.putInt("indexStart", indexStart);

        outState.putInt("booksSize", booksSize);

        outState.putString("searchTerm", searchTerm);

    }

    /**
     * initialize all available views
     */
    private void populateViews() {
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        footView = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_list_view_footer, null, false);

        nextButton = (Button) footView.findViewById(R.id.next);
        previousButton = (Button) footView.findViewById(R.id.previous);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);

        listView = (ListView) findViewById(R.id.list);
        searchButton = (Button) findViewById(R.id.search_button);
        searchEditText = (EditText) findViewById(R.id.search_input);
    }
}
