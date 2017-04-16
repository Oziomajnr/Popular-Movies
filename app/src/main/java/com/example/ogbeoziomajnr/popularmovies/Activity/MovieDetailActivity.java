package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.R;
import com.squareup.picasso.Picasso;

import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.IMAGE_BASE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    TextView textViewReleaseDate;
    TextView textViewOverview;
    TextView textViewMovieTitle;
    TextView textViewVoteAverage;

    ImageView imageViewBackDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        textViewReleaseDate = (TextView) findViewById(R.id.txt_release_date);
        textViewOverview = (TextView) findViewById(R.id.txt_overview);
        textViewMovieTitle = (TextView) findViewById(R.id.txt_movie_title);
        textViewVoteAverage = (TextView) findViewById(R.id.txt_vote_average);

        imageViewBackDrop = (ImageView) findViewById(R.id.img_back_drop);

        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewMovieTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

       textViewVoteAverage.setText(Double.toString(movie.getVoteAverage()) + "/13");

        Picasso.with(this).load(IMAGE_BASE_URL+movie.getBackDropPath()).into(imageViewBackDrop);
    }
}
