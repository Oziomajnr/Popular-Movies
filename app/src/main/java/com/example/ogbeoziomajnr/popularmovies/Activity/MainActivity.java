package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ogbeoziomajnr.popularmovies.CONSTANTS;
import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.MovieAdapter;
import com.example.ogbeoziomajnr.popularmovies.R;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiClient;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiInterface;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.API_KEY;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter mAdapter;
    GridLayoutManager layoutManager ;

    @BindView(R.id.rv_movies) RecyclerView mMovieList;
    @BindView(R.id.btn_try_again) Button btnTryAgain;
    @BindView(R.id.txt_error_message) TextView txtErrorMessage;

    // variables to help with pagination
    private int current_page = 1;
    private int total_pages = 1;

    // variable to help keep track of category
    private boolean top_rated = true;

    // to help regulate loading of more items
    private boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    // progress dialog to show loading status
    ProgressDialog mProgressDialog;

    List<Movie> movies = new ArrayList<>();

    Call<MovieResponse> call;

    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind all views to object using butter knife
        ButterKnife.bind(this);
        mMovieList.setNestedScrollingEnabled(false);

        // initialize important variables
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(true);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

        layoutManager = new GridLayoutManager(this, 2);
        mMovieList.setLayoutManager(layoutManager);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        if (top_rated)
            getPopularMovies(CONSTANTS.category.TOP_RATED) ;
        else
            getPopularMovies(CONSTANTS.category.POPULAR) ;


        mMovieList.setAdapter(mAdapter);

        mMovieList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!loading)
                    {
                        loading = true;
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {

                            if (current_page < total_pages) {
                                current_page++;
                                if (top_rated)
                                getPopularMovies(CONSTANTS.category.TOP_RATED) ;
                                else{
                                    getPopularMovies(CONSTANTS.category.POPULAR) ;
                                }

                            }
                        }
                        loading = false;
                    }
                }
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                current_page = 1;
                movies = new ArrayList<>();
                if (top_rated) {
                    getPopularMovies(CONSTANTS.category.TOP_RATED);
                }
                else{
                    getPopularMovies(CONSTANTS.category.POPULAR) ;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.by_rating:
                if (top_rated && !movies.isEmpty()){

                }
                else {
                    current_page =1;
                    movies = new ArrayList<>();
                    getPopularMovies(CONSTANTS.category.TOP_RATED);
                }
                top_rated = true;
                return true;

            case R.id.by_popularity:
                if (!top_rated && !movies.isEmpty()) {

                }
                else {
                    movies = new ArrayList<>();
                    current_page =1;
                    getPopularMovies(CONSTANTS.category.POPULAR);
                }
                top_rated = false;
                return true;

            case R.id.refresh:
                if(top_rated) {
                    current_page = 1;
                    movies = new ArrayList<>();
                    getPopularMovies(CONSTANTS.category.TOP_RATED);
                }
                else{
                    current_page = 1;
                    movies = new ArrayList<>();
                    getPopularMovies(CONSTANTS.category.POPULAR);
                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movieToView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("Movie", movieToView);

        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        movies = new ArrayList<>();
        current_page = 1;
        if (top_rated) {
            getPopularMovies(CONSTANTS.category.TOP_RATED);
        }
        else{
            getPopularMovies(CONSTANTS.category.POPULAR) ;
        }
    }





private void getPopularMovies (CONSTANTS.category category) {
    hideErrorMessage();
    //Initialise the api interface
    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

     if (category == CONSTANTS.category.TOP_RATED) {
         call = apiService.getTopRatedMovies(API_KEY, current_page);
     }
    else if (category == CONSTANTS.category.POPULAR){
         call = apiService.getPopularRatedMovies(API_KEY, current_page);
    }
        else{
         call = apiService.getTopRatedMovies(API_KEY, current_page);
     }
    call.enqueue(new Callback<MovieResponse>() {
        @Override
        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            total_pages = response.body().getTotalPages();
            movies.addAll(response.body().getResults());
            mAdapter.setImageUrl(movies);

        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
            // Log error here since request failed
            Log.e(TAG, t.toString());
            showErrorMessage();
            current_page = 1;
        }
    });
  }

//    private   void showprogressDialog() {
//        if (!mProgressDialog.isShowing())
//            mProgressDialog.show();
//    }
//
//    private   void hideprogressDialog() {
//        if (mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//            mProgressDialog.cancel();
//        }
//    }

    private void showErrorMessage () {
        txtErrorMessage.setGravity(Gravity.CENTER);
        btnTryAgain.setGravity(Gravity.CENTER);
        txtErrorMessage.setVisibility(View.VISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage () {
        txtErrorMessage.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.INVISIBLE);
    }
}
