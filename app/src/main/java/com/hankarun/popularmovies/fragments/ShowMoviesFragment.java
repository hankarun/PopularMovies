package com.hankarun.popularmovies.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.hankarun.popularmovies.activites.ShowMoviesActivity;
import com.hankarun.popularmovies.adapters.PosterAdapter;
import com.hankarun.popularmovies.lib.AppController;
import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.StaticTexts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowMoviesFragment extends Fragment {
    private List<Movie> mMovies;
    private PosterAdapter mAdapter;
    private int mCurrentScreen = StaticTexts.SORT_BY_POPULAR;
    private ShowMoviesActivity mainActivity;

    public ShowMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if( savedInstanceState != null ) {
            mCurrentScreen = savedInstanceState.getInt("data");
        }

        mainActivity = (ShowMoviesActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovies = new ArrayList<>();

        GridView mMovieGridView = (GridView) rootView.findViewById(R.id.gridView);
        mAdapter = new PosterAdapter(mainActivity.getApplicationContext(),mMovies);
        mMovieGridView.setAdapter(mAdapter);
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), OneMovieActivity.class);
                myIntent.putExtra("movie", mMovies.get(position).toString());
                getActivity().startActivity(myIntent);
            }
        });

        Log.d("json",mCurrentScreen+"");
        makeJsonArrayRequest(mCurrentScreen);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_pop:
                mCurrentScreen = StaticTexts.SORT_BY_POPULAR;
                makeJsonArrayRequest(StaticTexts.SORT_BY_POPULAR);
                return true;
            case R.id.action_sort_rate:
                mCurrentScreen = StaticTexts.SORT_BY_RATING;
                makeJsonArrayRequest(StaticTexts.SORT_BY_RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeJsonArrayRequest(Integer sortType) {
        mMovies.clear();

        String url;
        if(sortType == StaticTexts.SORT_BY_POPULAR){
            url = StaticTexts.mPopularMoviesUrl;
        }else{
            url = StaticTexts.mRatingsMoviesUrl;
        }

        final JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getMovies(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(req, sortType + "");
    }

    private void getMovies(JSONObject object){
        try {
            JSONArray array = object.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {

                Movie movie = new Movie((JSONObject) array.get(i));
                mMovies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            Log.e("Exception",e.getMessage());
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("data", mCurrentScreen);
    }

}
