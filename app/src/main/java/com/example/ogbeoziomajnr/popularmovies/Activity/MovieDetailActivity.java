 package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieContract;
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
    @BindView(R.id.btn_favorite) Button buttonFavorite;


    @BindView(R.id.img_back_drop) ImageView imageViewBackDrop;

     private Call<MovieTrailerResponse> call;
     private Call<ReviewResponse> call1;
     private Uri videoUri = null;
     private Uri reviewUri = null;
     private Movie movie;
     List<MovieTrailers> movieTrailers = new ArrayList<>();
     List<Review> movieReviews = new ArrayList<>();
     ApiInterface apiService;
     boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

        movie =  getIntent().getParcelableExtra("Movie");

        Toast.makeText(this,String.valueOf(isFavorite(movie.getId())), Toast.LENGTH_LONG).show();
        isFavorite = isFavorite(movie.getId());
        if ( isFavorite ) {
            buttonFavorite.setText(getString(R.string.unfav_text));
        }
        else  {
            buttonFavorite.setText(getString(R.string.fav_text));
        }
        //Initialise the api interface
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        textViewReleaseDate.setText(movie.getReleaseDate().substring(0,4));
        textViewMovieTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

       textViewVoteAverage.setText(Double.toString(movie.getVoteAverage()) +getResources().getString(R.string.vote_total));

        Picasso.with(this).load(IMAGE_BASE_URL+movie.getBackDropPath())
                .placeholder(R.drawable.loading_image).error(R.drawable.error_image).into(imageViewBackDrop);
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

     @OnClick(R.id.btn_favorite)
     public void addMovieToDatabase () {

         if (isFavorite) {
             // Build appropriate uri with String row id appended
             String stringId = String.valueOf(movie.getId());
             Uri uri = MovieContract.MovieEntry.CONTENT_URI;
             uri = uri.buildUpon().appendPath(stringId).build();
             // COMPLETED (2) Delete a single row of data using a ContentResolver
            if (getContentResolver().delete(uri, null, null) > 0) {
                isFavorite = false;
                buttonFavorite.setText(getString(R.string.fav_text));
            }
             else {
                Toast.makeText(this, "Unable To Remove Movie", Toast.LENGTH_SHORT).show();
            }
         }
         else {

             // Insert new movie data via a ContentResolver
             // Create new empty ContentValues object
             ContentValues contentValues = new ContentValues();

             contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
             contentValues.put(MovieContract.MovieEntry.COLUMN_BACK_DROP_PATH, movie.getBackDropPath());
             contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
             contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
             contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
             contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
             contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

             // Insert the content values via a ContentResolver
             Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

             // Display the URI that's returned with a Toast
             // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
             if(uri != null) {
                buttonFavorite.setText(getString(R.string.unfav_text));
                 isFavorite = true;
             }
         }


     }


     private boolean isFavorite(int movie_id) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie_id)).build();
         try {
           Cursor cursor = getContentResolver().query(uri, null, "id=" + String.valueOf(movie_id), null, MovieContract.MovieEntry.COLUMN_ID);
             if (cursor != null && cursor.getCount() > 0) {
                 return  true;
             }
             return false;
         }
         catch (Exception ex) {
             Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
             Log.e(TAG, ex.toString());
             return false;
         }
     }


}
