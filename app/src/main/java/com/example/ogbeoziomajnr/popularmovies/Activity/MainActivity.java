package com.example.ogbeoziomajnr.popularmovies.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ogbeoziomajnr.popularmovies.Constants;
import com.example.ogbeoziomajnr.popularmovies.Model.Movie;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieContract;
import com.example.ogbeoziomajnr.popularmovies.MovieAdapter;
import com.example.ogbeoziomajnr.popularmovies.R;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiClient;
import com.example.ogbeoziomajnr.popularmovies.Util.ApiInterface;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ogbeoziomajnr.popularmovies.Constants.API_KEY;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter mAdapter;
    GridLayoutManager layoutManager;

    @BindView(R.id.rv_movies)
    RecyclerView mMovieList;
    @BindView(R.id.btn_try_again)
    Button btnTryAgain;
    @BindView(R.id.txt_error_message)
    TextView txtErrorMessage;
    @BindView(R.id.txt_current_category)
    TextView txtCurrentCategory;

    // variables to help with pagination
    private int current_page = 1;
    private int total_pages = 1;

    // variable to help keep track of category
    private boolean top_rated = true;
    private static final int TASK_LOADER_ID = 0;

    // to help regulate loading of more items
    private boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    List<Movie> movies = new ArrayList<>();

    Call<MovieResponse> call;

    private String TAG = this.getClass().getName();
    private Constants.category currentCategory = Constants.category.TOP_RATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind all views to object using butter knife
        ButterKnife.bind(this);
        mMovieList.setNestedScrollingEnabled(false);

        layoutManager = new GridLayoutManager(this,this.getResources().getInteger(R.integer.layout_manager_number_of_colums));
        mMovieList.setLayoutManager(layoutManager);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        String category;

        if (savedInstanceState != null) {
            category = savedInstanceState.getString("currentCategory");
            if (category == Constants.category.POPULAR.toString()) {
                currentCategory = Constants.category.POPULAR;
            } else if (category == Constants.category.TOP_RATED.toString()) {
                currentCategory = Constants.category.TOP_RATED;
            } else if (category == Constants.category.FAVOURITE.toString()) {
                currentCategory = Constants.category.FAVOURITE;
            }
        }
        if (currentCategory.equals(Constants.category.FAVOURITE)) {
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        }
        else  {
            getPopularMovies(currentCategory);
        }

        mMovieList.setAdapter(mAdapter);

        mMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        loading = true;
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            if (current_page < total_pages) {
                                current_page++;
                                if (currentCategory.equals(Constants.category.TOP_RATED))
                                    getPopularMovies(Constants.category.TOP_RATED);
                                else if (currentCategory.equals(Constants.category.POPULAR)) {
                                    getPopularMovies(Constants.category.POPULAR);
                                } else {

                                }

                            }
                        }
                        loading = false;
                    }
                }
            }
        });
        setTxtCurrentCategory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentCategory", currentCategory.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       String category = savedInstanceState.getString("currentCategory");
        if (category == Constants.category.POPULAR.toString()) {
            currentCategory = Constants.category.POPULAR;
        }
        else if (category == Constants.category.TOP_RATED.toString()) {
            currentCategory = Constants.category.TOP_RATED;
        }
        else if (category == Constants.category.FAVOURITE.toString()) {
            currentCategory = Constants.category.FAVOURITE;
        }

    }

    @OnClick(R.id.btn_try_again)
    public void reloadData() {
        current_page = 1;
        movies = new ArrayList<>();
        if (currentCategory.equals(Constants.category.FAVOURITE)) {
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
            setTxtCurrentCategory();
        } else {
            getPopularMovies(currentCategory);
        }
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

                current_page = 1;
                movies = new ArrayList<>();
                currentCategory = Constants.category.TOP_RATED;
                setTxtCurrentCategory();
                getPopularMovies(currentCategory);

                return true;

            case R.id.by_popularity:
                movies = new ArrayList<>();
                current_page = 1;
                currentCategory = Constants.category.POPULAR;
                setTxtCurrentCategory();
                getPopularMovies(currentCategory);
                return true;

            case R.id.refresh:
                current_page = 1;
                movies = new ArrayList<>();
                 getPopularMovies(currentCategory);
                return true;

            case R.id.favorite:
                movies = new ArrayList<>();
                currentCategory = Constants.category.FAVOURITE;
                setTxtCurrentCategory();
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
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

    @Override
    protected void onResume() {
        super.onResume();

        movies = new ArrayList<>();
        current_page = 1;

        if (currentCategory.equals(Constants.category.FAVOURITE)) {
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
        }
        else  {
            getPopularMovies(currentCategory);
        }
    }


    private void getPopularMovies(Constants.category category) {
        hideErrorMessage();
        //Initialise the api interface
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        if (category == Constants.category.TOP_RATED) {
            call = apiService.getTopRatedMovies(API_KEY, current_page);
        } else if (category == Constants.category.POPULAR) {
            call = apiService.getPopularRatedMovies(API_KEY, current_page);
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                total_pages = response.body().getTotalPages();
                movies.addAll(response.body().getResults());
                mAdapter.setImageUrl(movies);
                setTxtCurrentCategory();
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


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     * <p>
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                hideErrorMessage();
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    showErrorMessage();
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            @Override
            public void deliverResult(Cursor data) {

                // Indices for the _id, description, and priority columns
                movies = new ArrayList<>();
                int idIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
                int posterIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
                int overviewIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                int releaseDateIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                int titleIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
                int averageIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                int backDropIndex = data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACK_DROP_PATH);

                while (data.moveToNext()) {
                    Movie movie = new Movie();
                    movie.setId(data.getInt(idIndex));
                    movie.setOverview(data.getString(overviewIndex));
                    movie.setPosterPath(data.getString(posterIndex));
                    movie.setReleaseDate(data.getString(releaseDateIndex));
                    movie.setTitle(data.getString(titleIndex));
                    movie.setVoteAverage(data.getDouble(averageIndex));
                    movie.setBackDropPath(data.getString(backDropIndex));

                    movies.add(movie);
                }
                hideErrorMessage();

                mAdapter.setImageUrl(movies);

                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideErrorMessage();

        mAdapter.setImageUrl(movies);
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setTxtCurrentCategory() {
        if (currentCategory.equals(Constants.category.POPULAR)) {
            txtCurrentCategory.setText(getString(R.string.showing_top_popular));
        } else if (currentCategory.equals(Constants.category.TOP_RATED)) {
            txtCurrentCategory.setText(getString(R.string.showing_top_rated));
        } else if (currentCategory.equals(Constants.category.FAVOURITE)) {
            txtCurrentCategory.setText(getString(R.string.showing_fav));
        }
    }

    private void showErrorMessage() {
        txtErrorMessage.setGravity(Gravity.CENTER);
        btnTryAgain.setGravity(Gravity.CENTER);
        txtErrorMessage.setVisibility(View.VISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        txtErrorMessage.setVisibility(View.INVISIBLE);
        btnTryAgain.setVisibility(View.INVISIBLE);
    }
}
