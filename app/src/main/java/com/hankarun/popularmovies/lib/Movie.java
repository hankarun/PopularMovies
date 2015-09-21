package com.hankarun.popularmovies.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String userRating;
    private String moviePoster;
    private String id;

    private final JSONObject mMovie;

    private ArrayList<Video> mVideos;

    public Movie(JSONObject movie) throws Exception{
        mMovie = movie;

        originalTitle = movie.getString(StaticTexts.apiOriginalTitle);
        overview = movie.getString(StaticTexts.apiOverview);
        releaseDate = movie.getString(StaticTexts.apiRelease_date);
        userRating = movie.getString(StaticTexts.apiUserRating);
        moviePoster = movie.getString(StaticTexts.apiPoster);
        id = movie.getString(StaticTexts.apiId);

        mVideos = new ArrayList<>();
    }

    public String getMoviePosterUrl(){ return moviePoster;}
    public String getOriginalTitle(){ return originalTitle;}
    public String getOverview(){ return overview;}
    public String getReleaseDate(){ return releaseDate;}
    public String getUserRating(){ return userRating;}
    public String getId(){ return id;}
    public ArrayList<Video> getmVideos(){ return mVideos;}

    //This must handle all tree json object video + review + movie
    public String toString(){ return mMovie.toString();}

    public void setVideos(JSONArray videos){
        try {
            for(int i = 0; i < videos.length(); i++){
                Video video = new Video((JSONObject) videos.get(i));
                mVideos.add(video);
            }
        } catch (Exception e){
            Log.d("Exception", e.getMessage());
        }
    }
}
