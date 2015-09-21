package com.hankarun.popularmovies.lib;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie implements Parcelable{
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String userRating;
    private String moviePoster;
    private String id;

    private JSONObject mMovie;

    private ArrayList<Video> mVideos;

    private ArrayList<Review> mReviews;

    public Movie(JSONObject movie) throws Exception{
        mMovie = movie;

        originalTitle = movie.getString(StaticTexts.apiOriginalTitle);
        overview = movie.getString(StaticTexts.apiOverview);
        releaseDate = movie.getString(StaticTexts.apiRelease_date);
        userRating = movie.getString(StaticTexts.apiUserRating);
        moviePoster = movie.getString(StaticTexts.apiPoster);
        id = movie.getString(StaticTexts.apiId);

        mVideos = new ArrayList<>();
        mReviews = new ArrayList<>();
    }

    public String getMoviePosterUrl(){ return moviePoster;}
    public String getOriginalTitle(){ return originalTitle;}
    public String getOverview(){ return overview;}
    public String getReleaseDate(){ return releaseDate;}
    public String getUserRating(){ return userRating;}
    public String getId(){ return id;}
    public ArrayList<Video> getmVideos(){ return mVideos;}
    public ArrayList<Review> getmReviews(){ return mReviews;}

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

    public void setReviews(JSONArray revies){
        try {
            for(int i = 0; i < revies.length(); i++){
                Review review = new Review((JSONObject) revies.get(i));
                mReviews.add(review);
            }
        } catch (Exception e){
            Log.d("Exception", e.getMessage());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovie.toString());
        dest.writeString(originalTitle);
        dest.writeString(releaseDate);
        dest.writeString(userRating);
        dest.writeString(moviePoster);
        dest.writeString(id);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel pc) {
            return new Movie(pc);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel pc){
        try {
            mMovie = new JSONObject(pc.readString());
        }catch (Exception e){
            Log.d("Parcel error", e.getMessage());
        }
        originalTitle = pc.readString();
        releaseDate = pc.readString();
        userRating = pc.readString();
        moviePoster = pc.readString();
        id = pc.readString();
    }
}