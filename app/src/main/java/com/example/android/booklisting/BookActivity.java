package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {
    final static String LOG_TAG = BookActivity.class.getSimpleName();

    final static int LOADER_CONSTANT = 1;

    private BookAdapter mAdapter = null;
    private ProgressBar progressBar;
    private LoaderManager loaderManager;
    private View footView;
    private TextView moreButton;
    private ListView listView;
    private EditText searchEditText;
    private ImageView searchButton;
    private View emptyView;//emptyview
    private TextView emptyTextView;
    private Button emptyButton;
    private ImageView emptyImage;

    //following variables need to be saved
    private int indexStart = 0;// parameters indexStart in json response
    private int booksSize = 0;
    private String searchTerm = ""; //value for searching after q=...
    private int counter = 0;
    private int orientationChanged = 0; //0 for original state and 1 indicate that the orientation just changed
    private boolean lock = false;
    private String url = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);
        if (url == null) {
            url = getString(R.string.url).trim();
        }
        //retrieve saved variables
        if (savedInstanceState != null) {

            indexStart = savedInstanceState.getInt(getString(R.string.index_start));

            booksSize = savedInstanceState.getInt(getString(R.string.book_size));

            searchTerm = savedInstanceState.getString(getString(R.string.search_term));
            searchEditText = (EditText) findViewById(R.id.search_input);
            searchEditText.setText(searchTerm);

            counter = savedInstanceState.getInt(getString(R.string.counter));

            orientationChanged = savedInstanceState.getInt(getString(R.string.orientationChanged));
            lock = savedInstanceState.getBoolean(getString(R.string.lock));
            url = savedInstanceState.getString(getString(R.string.url));

        }
        //initialize views
        populateViews();

        //set empty view for list view
        listView.setEmptyView(emptyView);

        //initialize adapter with empty ArrayList<Book>
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mAdapter.getItem(position);
                if (!currentBook.getInfoLink().equals(getString(R.string.empty_string))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getInfoLink())));
                } else {
                    Toast.makeText(BookActivity.this, getString(R.string.no_url_available), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //searchEditText shows searchterm
        searchEditText.setText(searchTerm);

        //cursor is focused at the end of the word
        searchEditText.setSelection(searchTerm.length());

        // initialize loaderManager
        loaderManager = getLoaderManager();
        //loaderManager.initLoader(LOADER_CONSTANT, null, this);
        //event for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (lock = true) {
                    lock = false;
                }
                if (checkNetWorkConnection()) {
                    if (searchEditText.getText() != null || searchEditText.getText().toString().equals(getString(R.string.empty_string))) {
                        mAdapter.clear(); //clear adapter
                        indexStart = 0;//reset indexStart
                        searchTerm = searchEditText.getText().toString().trim();// get new searchTerm
                        searchBook();
                    }
                } else {
                    disConnectEmptyView();
                }
            }
        });


        //event for more button
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lock = true) {
                    lock = false;
                }
                if (checkNetWorkConnection()) {
                    indexStart += booksSize; //new indexStart

                    //restart loader
                    loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
                } else {
                    disConnectEmptyView();
                }

            }
        });
        //hide progress bar
        progressBar.setVisibility(View.GONE);


        if (checkNetWorkConnection()) {
            //if network connection exist, show empty view requires user input a keyword to search
            startingEmptyView();
        } else {
            //if not
            //sho empty view of disconnected network
            disConnectEmptyView();


        }
        Log.i(LOG_TAG, getString(R.string.orientationChanged) + orientationChanged);
        Log.i(LOG_TAG, getString(R.string.lock) + lock);
        //if orientation just changed, restart loader
        if (orientationChanged == 1) {
            lock = false;
            if (checkNetWorkConnection()) {
                loaderManager.restartLoader(LOADER_CONSTANT, null, this);
            } else {
                disConnectEmptyView();
            }

            orientationChanged = 0;
        }

    }

    /**
     * call when search button is clicked
     */
    private void searchBook() {
        if (counter == 0) { // counter =0 means Loader has never been called
            loaderManager.initLoader(LOADER_CONSTANT, null, BookActivity.this);
        } else {
            loaderManager.restartLoader(LOADER_CONSTANT, null, this);
        }
    }


    @Override
    /**
     * fork off loader background thread
     */
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        ++counter;
        //show progressBar
        progressBar.setVisibility(View.VISIBLE);
        //hide empty view
        emptyView.setVisibility(View.GONE);
        return new BookLoader(this, url, searchTerm, indexStart);

    }

    @Override
    /**
     * after finish loading
     * update UI
     */
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        //hide progressBar
        progressBar.setVisibility(View.GONE);

        if (books.isEmpty()) {
            //set text for empty view
            bookNotFoundEmptyView(searchTerm);
        }


        //if books sucessfully downloaded
        if (books != null && !books.isEmpty()) {
            if (!lock) {
                //add all books to adapter
                mAdapter.addAll(books);
                mAdapter.notifyDataSetChanged();
            }
            //initialize books size
            booksSize = mAdapter.getCount();


            //set footview
            listView.removeFooterView(footView);
            listView.addFooterView(footView);

            //show more button
            if (books.size() >= 10) {
                moreButton.setVisibility(View.VISIBLE);
            } else {
                moreButton.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.end_of_the_list_inform), Toast.LENGTH_SHORT).show();
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
        orientationChanged = 1;
        outState.putInt(getString(R.string.index_start), indexStart);
        outState.putInt(getString(R.string.book_size), booksSize);
        outState.putString(getString(R.string.search_term), searchTerm);
        outState.putInt(getString(R.string.counter), counter);
        outState.putInt(getString(R.string.orientationChanged), orientationChanged);
        outState.putBoolean(getString(R.string.lock), lock);
        outState.putString(getString(R.string.url), url);

    }

    /**
     * initialize all available views
     */
    private void populateViews() {

        //foot view
        footView = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_list_view_footer, null, false);
        //empty view
        emptyView = findViewById(R.id.empty_view);
        emptyTextView = (TextView) emptyView.findViewById(R.id.empty_info);
        emptyButton = (Button) emptyView.findViewById(R.id.empty_button);
        emptyImage = (ImageView) emptyView.findViewById(R.id.empty_image);

        //listview
        moreButton = (TextView) footView.findViewById(R.id.more);
        listView = (ListView) findViewById(R.id.list);
        searchButton = (ImageView) findViewById(R.id.search_button);
        searchEditText = (EditText) findViewById(R.id.search_input);
        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
    }

    /**
     * hide keyboard
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * empty view for disconnect network
     */
    private void disConnectEmptyView() {
        listView.getEmptyView().setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyImage.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.VISIBLE);
        emptyImage.setImageResource(R.drawable.disconnect);
        emptyTextView.setText(getString(R.string.check_network_connection));
        emptyButton.setText(getString(R.string.try_again));

        //try again button
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkNetWorkConnection()) {
                    //if network now available
                    emptyView.setVisibility(View.GONE);
                    if (counter == 0) { // counter =0 means Loader has never been called
                        searchTerm = searchEditText.getText().toString().trim();
                        loaderManager.initLoader(LOADER_CONSTANT, null, BookActivity.this);
                    } else {
                        String temp = searchEditText.getText().toString().trim();
                        if (!temp.equals(searchTerm)) {
                            //if searchterm in edittext has changed
                            searchTerm = temp;
                            loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
                        } else {
                            //if not just show loaded list view
                            listView.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    //if network not available
                    disConnectEmptyView();
                }
            }
        });
    }

    /**
     * empty view for book not found situation
     *
     * @param searchTerm
     */
    private void bookNotFoundEmptyView(String searchTerm) {
        listView.getEmptyView().setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.GONE);
        emptyImage.setImageResource(R.drawable.book_mark);
        emptyImage.setVisibility(View.VISIBLE);
        if (searchTerm.equals(getString(R.string.empty_string))) {
            emptyTextView.setText(getString(R.string.keyword_cant_be_blank));
        } else {
            emptyTextView.setText(getString(R.string.no_book_found_part_1) + searchTerm + getString(R.string.no_book_found_part_2));
        }
    }

    /**
     * empty view for beginning situation
     */
    private void startingEmptyView() {
        listView.getEmptyView().setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.GONE);
        emptyImage.setImageResource(R.drawable.book_mark);
        emptyImage.setVisibility(View.VISIBLE);
        emptyTextView.setText(getString(R.string.starting_empty_view_text));
    }

    /**
     * check network connection
     *
     * @return boolean
     */
    private boolean checkNetWorkConnection() {

        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lock = true;
    }
}


