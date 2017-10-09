package com.example.android.movies;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movies.model.MovieDBResult;
import com.example.android.movies.utils.MovieDBUtils;
import com.squareup.picasso.Picasso;

import static com.example.android.movies.utils.MovieDBUtils.RELEASE_DATE_FORMAT;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView synopsisTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;

    private static final String THUMBNAIL_SIZE = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        posterImageView = (ImageView) findViewById(R.id.iv_details_poster);
        titleTextView = (TextView) findViewById(R.id.tv_details_title);
        synopsisTextView = (TextView) findViewById(R.id.tv_details_synopsis);
        ratingTextView = (TextView) findViewById(R.id.tv_details_rating_val);
        releaseDateTextView = (TextView) findViewById(R.id.tv_details_release_date);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("details")) {
                MovieDBResult movieDBResult = intentThatStartedThisActivity.getParcelableExtra("details");
                releaseDateTextView.setText("(" + RELEASE_DATE_FORMAT.format(movieDBResult.getReleaseDate()) + ")");
                titleTextView.setText(movieDBResult.getTitle());
                String url = MovieDBUtils.BASE_IMAGE_URL + "/" + THUMBNAIL_SIZE + "/" + movieDBResult.getPosterPath();
                Picasso.with(MovieDetailsActivity.this).load(url).into(posterImageView);
                synopsisTextView.setText(movieDBResult.getOverview());
                ratingTextView.setText( Double.toString(movieDBResult.getVoteAverage()) );
            }
        }
    }
}
