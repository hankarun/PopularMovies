package com.hankarun.popularmovies.lib;

public class StaticTexts {
    //themoviedb url references.
    public static final String mImageBaseUrl = "http://image.tmdb.org/t/p/w185";
    private static final String mApiBaseUrl = "https://api.themoviedb.org/3";
    private static final String mMovieDiscoveryUrl = mApiBaseUrl + "/discover/movie";
    public static final String mPopularMoviesUrl = mMovieDiscoveryUrl + "?sort_by=popularity.desc&api_key=" + ApiKey.mMovieApiKey;
    public static final String mRatingsMoviesUrl = mApiBaseUrl + "/movie/top_rated?api_key=" + ApiKey.mMovieApiKey;
    public static final String mMovieBaseUrl = mApiBaseUrl + "/movie";
    public static final String mMovieVideoUrl = "/videos?api_key=" + ApiKey.mMovieApiKey;
    public static final String mMovieReviewUrl = "/reviews?api_key=" + ApiKey.mMovieApiKey;

    //themoviedb api references.
    public static final String apiOriginalTitle = "original_title";
    public static final String apiOverview = "overview";
    public static final String apiRelease_date = "release_date";
    public static final String apiUserRating = "vote_average";
    public static final String apiPoster = "poster_path";
    public static final String apiId = "id";
    public static final String apiKey = "key";
    public static final String apiName = "name";
    public static final String apiSite = "site";
    public static final String apiType = "type";

    //youtube base video url
    public static final String mYoutubeBaseUrl = "https://www.youtube.com/watch?v=";

    public static final int SORT_BY_POPULAR = 0;
    public static final int SORT_BY_RATING = 1;
    public static final int GET_VIDEOS = 2;
    public static final int GET_REVIEWS = 3;



}
