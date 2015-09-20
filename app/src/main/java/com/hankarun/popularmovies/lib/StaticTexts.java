package com.hankarun.popularmovies.lib;

public class StaticTexts {
    private static final String mMovieApiKey = "";
    public static final String mImageBaseUrl = "http://image.tmdb.org/t/p/w185";
    private static final String mApiBaseUrl = "https://api.themoviedb.org/3";
    private static final String mMovieDiscoveryUrl = mApiBaseUrl + "/discover/movie";
    public static final String mPopularMoviesUrl = mMovieDiscoveryUrl + "?sort_by=popularity.desc&api_key=" + mMovieApiKey;
    public static final String mRatingsMoviesUrl = mApiBaseUrl + "/movie/top_rated?api_key=" + mMovieApiKey;


    public static final String apiOriginalTitle = "original_title";
    public static final String apiOverview = "overview";
    public static final String apiRelease_date = "release_date";
    public static final String apiUserRating = "vote_average";
    public static final String apiPoster = "poster_path";
    public static final String apiId = "id";

    public static final int SORT_BY_POPULAR = 0;
    public static final int SORT_BY_RATING = 1;

}
