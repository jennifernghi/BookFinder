package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    static final String URL = "https://www.googleapis.com/books/v1/volumes";

    private BookAdapter mAdapter = null;
    private ProgressBar progressBar;
    private LoaderManager loaderManager;
    private View footView;
    private Button moreButton;
    private ListView listView;
    private EditText searchEditText;
    private Button searchButton;
    private View emptyView;//emptyview
    private TextView emptyTextView;
    private Button emptyButton;
    private ImageView emptyImage;
    private  NetworkInfo activeNetwork;


    //following variables need to be saved
    private int indexStart = 0;// parameters indexStart in json response
    private int booksSize = 0;
    private String searchTerm = ""; //value for searching after q=...
    private int counter =0;

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
        listView.setEmptyView(emptyView);

        //initialize adapter with empty ArrayList<Book>
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

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
                if(checkNetWorkConnection()) {
                    if (searchEditText.getText() != null || searchEditText.getText().toString().equals("")) {
                        mAdapter.clear(); //clear adapter
                        indexStart = 0;//reset indexStart
                        searchTerm = searchEditText.getText().toString().trim();// get new searchTerm
                        searchBook();
                    }
                }else{
                    disConnectEmptyView();
                }
            }
        });


        //event for more button
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexStart += booksSize; //new indexStart

                //restart loader
                loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);

            }
        });

        progressBar.setVisibility(View.GONE);

        if(checkNetWorkConnection()){
           bookNotFoundEmptyView(searchTerm);
        }else {
            disConnectEmptyView();
            emptyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkNetWorkConnection()) {
                        if (counter == 0) {
                            loaderManager.initLoader(LOADER_CONSTANT, null, BookActivity.this);
                        } else {
                            String temp = searchEditText.getText().toString().trim();
                            if(!temp.equals(searchTerm)){
                                loaderManager.restartLoader(LOADER_CONSTANT, null, BookActivity.this);
                            }else {
                                listView.setVisibility(View.VISIBLE);
                            }

                        }
                    }else {
                        disConnectEmptyView();
                    }
                }
            });
        }

    }

    /**
     * call when search button is clicked
     */
    private void searchBook() {

        //restart loader with new search term
        //loaderManager.restartLoader(LOADER_CONSTANT, null, this);
        loaderManager.initLoader(LOADER_CONSTANT, null, this);
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
        bookNotFoundEmptyView(searchTerm);


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
            if (books.size() >= 10) {
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

        //foot view
        footView = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_list_view_footer, null, false);
        //empty view
        emptyView = findViewById(R.id.empty_view);
        emptyTextView = (TextView) emptyView.findViewById(R.id.empty_info);
        emptyButton = (Button) emptyView.findViewById(R.id.empty_button);
        emptyImage = (ImageView) emptyView.findViewById(R.id.empty_image);

        //listview
        moreButton = (Button) footView.findViewById(R.id.more);
        listView = (ListView) findViewById(R.id.list);
        searchButton = (Button) findViewById(R.id.search_button);
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
    private void disConnectEmptyView(){
        listView.getEmptyView().setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyImage.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.VISIBLE);
        emptyImage.setImageResource(R.drawable.disconnect);
        emptyTextView.setText("Check network connection!");
        emptyButton.setText("Try Again!");
    }

    private void bookNotFoundEmptyView(String searchTerm){
        listView.getEmptyView().setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyButton.setVisibility(View.GONE);
        emptyImage.setImageResource(R.drawable.book_mark);
        emptyImage.setVisibility(View.VISIBLE);
        if(searchTerm.equals("")){
            emptyTextView.setText("Keyword can't be blank!");
        }else{
            emptyTextView.setText("Keyword: '"+ searchTerm +"' No book found!");
        }
    }


    private boolean checkNetWorkConnection(){

        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }
}
