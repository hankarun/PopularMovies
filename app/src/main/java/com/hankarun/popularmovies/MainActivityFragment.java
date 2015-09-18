package com.hankarun.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hankarun.popularmovies.adapters.PosterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private List<Movie> mMovies;
    private PosterAdapter mAdapter;
    private GridView mMovieGridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovies = new ArrayList<>();
        mAdapter = new PosterAdapter(getActivity().getApplicationContext(),mMovies);

        return inflater.inflate(R.layout.fragment_main, container, false);
        //Get all of the movies
        //Load images from urls.
    }

    private void makeJsonArrayRequest(Integer sortType) {
        mMovies.clear();
        String url = "";
        if(sortType == StaticTexts.SORT_BY_POPULAR){
            url = StaticTexts.mBaseUrl+StaticTexts.mPopularMoviesUrl;
        }else{
            url = StaticTexts.mBaseUrl+StaticTexts.mHighestRatedMoviesUrl;
        }

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                Movie movie = new Movie((JSONObject) response.get(i));

                                mMovies.add(movie);
                                //Add movie to the array
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e){

                        }

                        //Close vaiting dialog
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }
}
