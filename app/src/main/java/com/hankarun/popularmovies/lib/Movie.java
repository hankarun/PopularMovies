package com.hankarun.popularmovies.lib;

import org.json.JSONObject;

public class Movie {
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String userRating;
    private String moviePoster;
    private String id;

    private JSONObject mMovie;

    public Movie(JSONObject movie) throws Exception{
        mMovie = movie;

        originalTitle = movie.getString(StaticTexts.apiOriginalTitle);
        overview = movie.getString(StaticTexts.apiOverview);
        releaseDate = movie.getString(StaticTexts.apiRelease_date);
        userRating = movie.getString(StaticTexts.apiUserRating);
        moviePoster = movie.getString(StaticTexts.apiPoster);
        id = movie.getString(StaticTexts.apiId);
    }

    public String getMoviePosterUrl(){ return moviePoster;}
    public String getOriginalTitle(){ return originalTitle;}
    public String getOverview(){ return overview;}
    public String getReleaseDate() { return releaseDate;}
    public String getUserRating() { return userRating;}

    public String toString(){ return mMovie.toString();}

}
