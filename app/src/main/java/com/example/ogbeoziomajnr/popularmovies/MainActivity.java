package com.example.ogbeoziomajnr.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiClient;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiInterface;
import com.example.ogbeoziomajnr.popularmovies.Util.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ogbeoziomajnr.popularmovies.CONSTANTS.API_KEY;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    GridLayoutManager layoutManager ;
    private int current_page = 1;
    private int total_pages = 1;
    private boolean top_rated = true;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    List<Movie> movies = new ArrayList<>();

    Call<MovieResponse> call;

    private String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        layoutManager = new GridLayoutManager(this, 2);
        mMovieList.setLayoutManager(layoutManager);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        //Initialise the api interface
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        call = apiService.getTopRatedMovies(API_KEY,current_page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                current_page = 1;
                movies = response.body().getResults();
                total_pages = response.body().getTotalPages();
                mAdapter.setImageUrl(movies);

                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                current_page = 1;
            }
        });


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

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");

                            if (current_page < total_pages) {
                                current_page++;
                                if (top_rated)
                                getPopularMovies(CONSTANTS.category.TOP_RATED) ;
                                else{
                                    getPopularMovies(CONSTANTS.category.POPULAR) ;
                                }
                            }
                        }
                    }
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
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.by_rating:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                getPopularMovies(CONSTANTS.category.TOP_RATED) ;
                top_rated = true;
                return true;

            case R.id.by_popularity:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                getPopularMovies(CONSTANTS.category.POPULAR);
                top_rated = false;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movieToView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("Movie", movieToView);

        startActivity(intent);
    }


private void getPopularMovies (CONSTANTS.category category) {
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
            current_page = 1;
            total_pages = response.body().getTotalPages();
            movies = response.body().getResults();
            mAdapter.setImageUrl(movies);

            Log.d(TAG, "Number of movies received: " + movies.size());
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
            // Log error here since request failed
            Log.e(TAG, t.toString());
            current_page = 1;
        }
    });

}


}
