package com.example.android.movies.utils;

import android.net.Uri;
import android.util.Log;

import com.example.android.movies.model.MovieDBResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MovieDBUtils {
    private static final String TAG = MovieDBUtils.class.getSimpleName();

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    public static final String THE_MOVIE_DB_API_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "<APIKEY>";

    public enum MovieSortOrder { POPULAR,  TOP_RATED }
    public static final SimpleDateFormat RELEASE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static URL buildURL(MovieSortOrder sortOrder) {
        Uri uri = Uri.parse(THE_MOVIE_DB_API_URL + sortOrder.name().toLowerCase() + "/").buildUpon().appendQueryParameter("api_key", API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<MovieDBResult> parseJsonString(String jsonString) {
        List<MovieDBResult> movieDBResults = new ArrayList<>();
        try {
            JSONObject page = new JSONObject(jsonString);
            JSONArray results = page.getJSONArray("results");
            for ( int i = 0; i < results.length(); i++ ) {
                JSONObject result = results.getJSONObject(i);
                MovieDBResult movieDBResult = new MovieDBResult();
                movieDBResult.setVoteCount(result.getInt("vote_count"));
                movieDBResult.setVoteAverage(result.getDouble("vote_average"));
                movieDBResult.setTitle(result.getString("original_title"));
                movieDBResult.setPopularity(result.getDouble("popularity"));
                movieDBResult.setReleaseDate(RELEASE_DATE_FORMAT.parse(result.getString("release_date")));
                movieDBResult.setOverview(result.getString("overview"));
                movieDBResult.setPosterPath(result.getString("poster_path"));
                movieDBResults.add(movieDBResult);
            }
        } catch (JSONException|ParseException e) {
            e.printStackTrace();
        }
        return movieDBResults;
    }

}
