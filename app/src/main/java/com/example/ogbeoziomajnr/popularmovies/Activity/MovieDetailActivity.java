package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.R;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.IMAGE_BASE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.txt_release_date) TextView textViewReleaseDate;
    @BindView(R.id.txt_overview) TextView textViewOverview;
    @BindView(R.id.txt_movie_title) TextView textViewMovieTitle;
    @BindView(R.id.txt_vote_average) TextView textViewVoteAverage;

    @BindView(R.id.img_back_drop) ImageView imageViewBackDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getSerializableExtra("Movie");

        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewMovieTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

       textViewVoteAverage.setText(Double.toString(movie.getVoteAverage()) + "/13");

        Picasso.with(this).load(IMAGE_BASE_URL+movie.getBackDropPath()).into(imageViewBackDrop);
    }
}
