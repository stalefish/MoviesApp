package com.example.android.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movies.model.MovieDBResult;
import com.example.android.movies.utils.MovieDBUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieDBAdapter.MovieDBAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MovieDBAdapter movieDBAdapter;
    private ProgressBar mLoadingIndicator;
    private RecyclerView recyclerView;
    private TextView errorMessage;
    private List<MovieDBResult> movieDBResultData;
    private static final String DB_RESULT_DATA = "movieDBResultData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorMessage = (TextView) findViewById(R.id.tv_error_message);

        movieDBAdapter = new MovieDBAdapter(getApplicationContext(), new ArrayList<MovieDBResult>(), this);
        recyclerView.setAdapter(movieDBAdapter);
        int columns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 3;

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, columns);
        recyclerView.setLayoutManager(layoutManager);
        if( savedInstanceState == null || !savedInstanceState.containsKey(DB_RESULT_DATA)) {
            loadMovieData(MovieDBUtils.MovieSortOrder.POPULAR);
        } else {
            movieDBResultData = savedInstanceState.getParcelableArrayList(DB_RESULT_DATA);
            movieDBAdapter.setData(movieDBResultData);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DB_RESULT_DATA, (ArrayList)movieDBResultData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_rating:
                loadMovieData(MovieDBUtils.MovieSortOrder.TOP_RATED);
                break;
            case R.id.action_sort_popular:
                loadMovieData(MovieDBUtils.MovieSortOrder.POPULAR);
        }
        return true;
    }

    private void loadMovieData(MovieDBUtils.MovieSortOrder sortOrder) {
        new QueryMovieTask().execute(sortOrder);
    }

    @Override
    public void onClick(MovieDBResult movieDBResult) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("details", movieDBResult);
        startActivity(intent);

    }

    private class QueryMovieTask extends AsyncTask<MovieDBUtils.MovieSortOrder, Void, List<MovieDBResult>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieDBResult> doInBackground(MovieDBUtils.MovieSortOrder... params) {
            List<MovieDBResult> results = new ArrayList<>();
            try {
                URL url = MovieDBUtils.buildURL(params[0]);
                Log.d(TAG, "doInBackground - " + url);
                String response = MovieDBUtils.getResponseFromHttpUrl(url);
                results.addAll(MovieDBUtils.parseJsonString(response));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<MovieDBResult> movieDBResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDBResults != null && !movieDBResults.isEmpty()) {
                showPosterView();
                movieDBResultData = movieDBResults;
                movieDBAdapter.setData(movieDBResultData);
            } else {
                showErrorMessage();
            }

        }
    }

    private void showPosterView() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }


}
