package com.example.ogbeoziomajnr.popularmovies.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ogbeoziomajnr.popularmovies.Model.MovieContract.MovieEntry;

/**
 * Created by SQ-OGBE PC on 12/05/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context,DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_ID               + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACK_DROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
