 package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieTrailerResponse;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieTrailers;
import com.example.ogbeoziomajnr.popularmovies.Model.Review;
import com.example.ogbeoziomajnr.popularmovies.Model.ReviewResponse;
import com.example.ogbeoziomajnr.popularmovies.R;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiClient;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.API_KEY;
import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.IMAGE_BASE_URL;
import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.YOUTUBE_BASE_URL;


 public class MovieDetailActivity extends AppCompatActivity {
    private String TAG = this.getClass().getName();

    @BindView(R.id.txt_release_date) TextView textViewReleaseDate;
    @BindView(R.id.txt_overview) TextView textViewOverview;
    @BindView(R.id.txt_movie_title) TextView textViewMovieTitle;
    @BindView(R.id.txt_vote_average) TextView textViewVoteAverage;

    @BindView(R.id.img_back_drop) ImageView imageViewBackDrop;

     private Call<MovieTrailerResponse> call;
     private Call<ReviewResponse> call1;
     private Uri videoUri = null;
     private Uri reviewUri = null;
     private Movie movie;
     List<MovieTrailers> movieTrailers = new ArrayList<>();
     List<Review> movieReviews = new ArrayList<>();
     ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

        movie =  getIntent().getParcelableExtra("Movie");
        //Initialise the api interface
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        textViewReleaseDate.setText(movie.getReleaseDate().substring(0,4));
        textViewMovieTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

       textViewVoteAverage.setText(Double.toString(movie.getVoteAverage()) +getResources().getString(R.string.vote_total));

        Picasso.with(this).load(IMAGE_BASE_URL+movie.getBackDropPath())
                .placeholder(R.drawable.loading_image).into(imageViewBackDrop);
        getMovieTrailers();
        getReviews();

    }

     private void getMovieTrailers () {


             call = apiService.getMovieVideos(movie.getId(), API_KEY);

            call.enqueue(new Callback<MovieTrailerResponse>() {
             @Override
             public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {
                 movieTrailers = response.body().getResults();

                 for (MovieTrailers movieTrailer : movieTrailers) {
                     // get the first movie trailer but if the trailer does not exist then use a clip

                     if (movieTrailer.getType().equals("Trailer")) {
                         videoUri = Uri.parse(YOUTUBE_BASE_URL+movieTrailer.getKey());
                         Log.e("Logging response ", String.valueOf(movieTrailers.get(0).getKey()));
                         break;
                     }
                     else if (movieTrailer.getType() == "Clip") {
                         videoUri = Uri.parse(YOUTUBE_BASE_URL+movieTrailer.getKey());
                     }
                 }
             }

             @Override
             public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {
                 // Log error here since request failed
                 t.printStackTrace();
                 Log.e(TAG, t.toString());
             }
         });
     }

     private void getReviews () {

      call1 = apiService.getMovieReviews(movie.getId(), API_KEY);

         call1.enqueue(new Callback<ReviewResponse>() {
             @Override
             public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                 movieReviews = response.body().getResults();

                 if (movieReviews.size() > 0) {
                     reviewUri = Uri.parse(movieReviews.get(0).getUrl());
                 }
             }

             @Override
             public void onFailure(Call<ReviewResponse> call, Throwable t) {
                 // Log error here since request failed
                 t.printStackTrace();
                 Log.e(TAG, t.toString());
             }
         });
     }


     @OnClick (R.id.img_button_trailer)
     public void watchTrailer() {
         if (videoUri != null) {
             Intent intent = new Intent (Intent.ACTION_VIEW, videoUri);
             startActivity(intent);
         }
         else {
             Toast.makeText(this, "Trailer Unavailable please try again", Toast.LENGTH_SHORT).show();
             getMovieTrailers();
         }
     }

     @OnClick (R.id.btn_read_review)
     public void readReview () {

         if (reviewUri == null) {
             Toast.makeText(this, "Review is Unavailable please try again", Toast.LENGTH_SHORT).show();
             getReviews();
         }
         else {

             Intent intent = new Intent (Intent.ACTION_VIEW, reviewUri);
             startActivity(intent);
         }
     }
}
