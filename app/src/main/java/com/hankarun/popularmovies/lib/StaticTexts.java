package com.hankarun.popularmovies.lib;

public class StaticTexts {
    public static String mMovieApiKey = "";
    public static String mImageBaseUrl = "http://image.tmdb.org/t/p/w185";
    public static String mApiBaseUrl = "https://api.themoviedb.org/3";
    public static String mMovieDiscoveryUrl = mApiBaseUrl + "/discover/movie";
    public static String mPopularMoviesUrl = mMovieDiscoveryUrl + "?sort_by=popularity.desc&api_key=" + mMovieApiKey;
    public static String mRatingsMoviesUrl = mApiBaseUrl + "/movie/top_rated?api_key=" + mMovieApiKey;


    public static String apiOriginalTitle = "original_title";
    public static String apiOverview = "overview";
    public static String apiRelease_date = "release_date";
    public static String apiUserRating = "vote_average";
    public static String apiPoster = "poster_path";
    public static String apiId = "id";

    public static int SORT_BY_POPULAR = 0;
    public static int SORT_BY_RATING = 1;

}
