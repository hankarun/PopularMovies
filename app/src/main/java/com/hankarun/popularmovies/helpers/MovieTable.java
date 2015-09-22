package com.hankarun.popularmovies.helpers;

import android.database.sqlite.SQLiteDatabase;

import com.hankarun.popularmovies.lib.StaticTexts;

public class MovieTable {

    public static final String TABLE_MOVIE = "favoritemovies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ORIGINAL_TITLE = StaticTexts.apiOriginalTitle;
    public static final String COLUMN_OVERVIEW = StaticTexts.apiOverview;
    public static final String COLUMN_RELEASE_DATE = StaticTexts.apiRelease_date;
    public static final String COLUMN_USER_RATING = StaticTexts.apiUserRating;
    public static final String COLUMNT_MOVIE_ID = StaticTexts.apiId;
    public static final String COLUMN_MOVIE_POSTER_URL = StaticTexts.apiPoster;
    public static final String COLUMN_JSON_MOVIE = "json_object";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ORIGINAL_TITLE + " text not null, "
            + COLUMN_OVERVIEW + " text not null, "
            + COLUMN_RELEASE_DATE + " text not null, "
            + COLUMN_USER_RATING + " text not null, "
            + COLUMNT_MOVIE_ID + " text not null, "
            + COLUMN_MOVIE_POSTER_URL + " text not null, "
            + COLUMN_JSON_MOVIE + " text not null"
            + ");";

    // Storing full json is not a solution. Build json from text in movie object.

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(database);
    }
}
