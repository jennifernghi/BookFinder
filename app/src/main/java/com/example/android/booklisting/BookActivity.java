package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button moreButton;
    private ListView listView;
    private EditText searchEditText;
    private Button searchButton;

    //following variables need to be saved
    private int indexStart = 0;// parameters indexStart in json response
    private int booksSize = 0;
    private String searchTerm = "love"; //value for searching after q=...



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        //retrieve saved variables
        if (savedInstanceState != null) {

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
                hideKeyboard();
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

        //event for more button
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    indexStart += booksSize; //new indexStart

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
        mEmptyTextView.setVisibility(View.VISIBLE);
        mEmptyTextView.setText("No book found!");
        Log.i(LOG_TAG,mEmptyTextView.getText().toString());

        //if books sucessfully downloaded
        if (books != null && !books.isEmpty()) {

            //add all books to adapter
            mAdapter.addAll(books);
            mAdapter.notifyDataSetChanged();

            //initialize books size
            booksSize = mAdapter.getCount();


            //set footview
            listView.removeFooterView(footView);
            listView.addFooterView(footView);

            //show more button
            if (books.size()>=10) {
                moreButton.setVisibility(View.VISIBLE);
            } else {
               moreButton.setVisibility(View.GONE);
                Toast.makeText(this, "you've reached the end of the list", Toast.LENGTH_SHORT).show();
            }



        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    /**
     * save 4 variables:  indexStart, booksSize, searchTerm
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

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

        moreButton = (Button) footView.findViewById(R.id.more);


        mEmptyTextView = (TextView) findViewById(R.id.empty_view);

        listView = (ListView) findViewById(R.id.list);
        searchButton = (Button) findViewById(R.id.search_button);
        searchEditText = (EditText) findViewById(R.id.search_input);
    }

    /**
     * hide keyboard
     */
    private void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
