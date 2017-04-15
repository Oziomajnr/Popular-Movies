package com.example.ogbeoziomajnr.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 1;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

//        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
//        mMovieList.setLayoutManager(layoutManager);
//
//        mMovieList.setHasFixedSize(true);
//
//        mAdapter = new MovieAdapter(NUM_LIST_ITEMS);
//
//        mMovieList.setAdapter(mAdapter);
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
            case R.id.by_category:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                Toast.makeText(this, "By Category", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.by_popularity:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                Toast.makeText(this, "By Popularity", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
