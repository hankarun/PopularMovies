package com.hankarun.popularmovies;

import org.json.JSONObject;

public class Movie {
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String userRating;
    private String moviePoster;

    public Movie(JSONObject movie) throws Exception{
        originalTitle = movie.getString(StaticTexts.apiOriginalTitle);
        overview = movie.getString(StaticTexts.apiOverview);
        releaseDate = movie.getString(StaticTexts.apiRelease_date);
        userRating = movie.getString(StaticTexts.apiUserRating);
        moviePoster = movie.getString(StaticTexts.apiPoster);
    }

    public String getMoviePosterUrl(){ return moviePoster;}
    public String getOriginalTitle(){return originalTitle;}

}
