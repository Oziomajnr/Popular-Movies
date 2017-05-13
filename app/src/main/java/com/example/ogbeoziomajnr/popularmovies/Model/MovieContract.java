package com.example.ogbeoziomajnr.popularmovies.Model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SQ-OGBE PC on 12/05/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.ogbeoziomajnr.movies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // This is the path for the "movie" directory
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry  {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // Task table and column names
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_OVERVIEW= "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERAGE= "vote_average";
        public static final String COLUMN_BACK_DROP_PATH= "backdrop_path";
    }
}
