package com.hankarun.popularmovies.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.R;

import org.json.JSONObject;

public class OneMovieActivity extends AppCompatActivity {
    public Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        try{
            mMovie = new Movie(new JSONObject(intent.getStringExtra("movie")));
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }

        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle("Movie");

        setContentView(R.layout.activity_main2);
    }
}
