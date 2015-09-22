package com.hankarun.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.hankarun.popularmovies.activites.ShowMoviesActivity;
import com.hankarun.popularmovies.adapters.PosterAdapter;
import com.hankarun.popularmovies.lib.AppController;
import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.MovieContentProvider;
import com.hankarun.popularmovies.helpers.MovieTable;
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

    public ShowMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if( savedInstanceState != null ) {
            mCurrentScreen = savedInstanceState.getInt("data");
        }


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovies = new ArrayList<>();

        final GridView mMovieGridView = (GridView) rootView.findViewById(R.id.gridView);
        mAdapter = new PosterAdapter(getActivity().getApplicationContext(),mMovies);
        mMovieGridView.setAdapter(mAdapter);
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OneMovieFragment oneMovieFragment = (OneMovieFragment) getFragmentManager()
                        .findFragmentById(R.id.fragment2);

                if (oneMovieFragment == null) {
                    Intent myIntent = new Intent(getActivity(), OneMovieActivity.class);
                    myIntent.putExtra("movie", mMovies.get(position).toString());
                    getActivity().startActivity(myIntent);
                }else{
                    oneMovieFragment.setMovie(mMovies.get(position));
                }
            }
        });

        if(!isInternetAvailable())
            mCurrentScreen = StaticTexts.SORT_FAVORITES;

        loadMovies(mCurrentScreen);

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
        if(!isInternetAvailable()){
            Toast.makeText(getActivity().getApplicationContext(), R.string.check_your_internet_connection,Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }else {
            switch (item.getItemId()) {
                case R.id.action_sort_pop:
                    ((ShowMoviesActivity) getActivity()).setTitle(getString(R.string.popular_movies));
                    mCurrentScreen = StaticTexts.SORT_BY_POPULAR;
                    loadMovies(StaticTexts.SORT_BY_POPULAR);
                    return true;
                case R.id.action_sort_rate:
                    ((ShowMoviesActivity) getActivity()).setTitle(getString(R.string.highest_rated_movies));
                    mCurrentScreen = StaticTexts.SORT_BY_RATING;
                    loadMovies(StaticTexts.SORT_BY_RATING);
                    return true;
                case R.id.action_show_favorites:
                    ((ShowMoviesActivity) getActivity()).setTitle(getString(R.string.favorite_movies_title));
                    mCurrentScreen = StaticTexts.SORT_FAVORITES;
                    loadMovies(StaticTexts.SORT_FAVORITES);
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    public void updateFavoriteMovieList(){
        if(mCurrentScreen == StaticTexts.SORT_FAVORITES) {
            mMovies.clear();
            mCurrentScreen = StaticTexts.SORT_FAVORITES;
            loadFavoriteMovies();
        }
    }

    private void loadFavoriteMovies(){
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor == null) {
            Log.d("Database"," Problem");
            // Problem
        } else if (cursor.getCount() > 0) {
            while(cursor.moveToNext())
            {
                try {
                    Movie movie = new Movie(new JSONObject(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_JSON_MOVIE))));
                    mMovies.add(movie);
                }catch (Exception e){
                    Log.d("Json favorite", " movie");
                }
            }
            cursor.close();
        }

        mAdapter.notifyDataSetChanged();
    }

    private void loadMovies(Integer sortType) {
        mMovies.clear();

        if (sortType == StaticTexts.SORT_FAVORITES) {
            loadFavoriteMovies();
        }else {

            String url;
            if (sortType == StaticTexts.SORT_BY_POPULAR) {
                url = StaticTexts.mPopularMoviesUrl;
            } else {
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

    private boolean isInternetAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
    }
}
