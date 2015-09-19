package com.hankarun.popularmovies;

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
import com.hankarun.popularmovies.adapters.PosterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivityFragment extends Fragment {
    private List<Movie> mMovies;
    private PosterAdapter mAdapter;
    private GridView mMovieGridView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovies = new ArrayList<>();

        mMovieGridView = (GridView) rootView.findViewById(R.id.gridView);
        mAdapter = new PosterAdapter(getActivity().getApplicationContext(),mMovies);
        mMovieGridView.setAdapter(mAdapter);
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ITEM_CLICKED", "" + mMovies.get(position).getOriginalTitle());
            }
        });



        makeJsonArrayRequest(StaticTexts.SORT_BY_POPULAR);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_sort_pop:
                makeJsonArrayRequest(StaticTexts.SORT_BY_POPULAR);
                return true;
            case R.id.action_sort_rate:
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

                        try {
                            JSONArray array = response.getJSONArray("results");
                            for (int i = 0; i < array.length(); i++) {

                                Movie movie = new Movie((JSONObject) array.get(i));
                                mMovies.add(movie);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){

                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }
}
