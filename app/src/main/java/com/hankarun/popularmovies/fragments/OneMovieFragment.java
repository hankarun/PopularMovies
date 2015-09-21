package com.hankarun.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.hankarun.popularmovies.lib.AppController;
import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.StaticTexts;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class OneMovieFragment extends Fragment implements OnClickListener {
    private Boolean mIsFavorite = false;
    private ImageView mStarImageView;
    private Toast mFavoriteMessage;
    private Movie mMovie;

    public OneMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        OneMovieActivity activity = (OneMovieActivity) getActivity();
        this.mMovie = activity.mMovie;

        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.posterImageView);
        TextView mRatingView = (TextView) rootView.findViewById(R.id.ratingTextView);
        TextView mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDateViewText);
        WebView mOverview = (WebView) rootView.findViewById(R.id.overViewWebView);
        mStarImageView = (ImageView) rootView.findViewById(R.id.starImageView);
        mStarImageView.setOnClickListener(this);

        Picasso.with(getActivity().getApplicationContext())
                .load(StaticTexts.mImageBaseUrl+ mMovie.getMoviePosterUrl())
                .into(mImageView);

        String text = "<html><body>" +
                "<h1 align=\"center\">"+activity.getString(R.string.overview)+"</h1>"
                         + "<p align=\"justify\">"
                         + mMovie.getOverview()
                         + "</p> "
                         + "</body></html>";

        mOverview.loadData(text, "text/html", "utf-8");
        mRatingView.setText(mMovie.getUserRating() + "/10");
        mReleaseDate.setText(mMovie.getReleaseDate().subSequence(0, 4));

        mImageView.setAdjustViewBounds(true);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        return rootView;
    }

    private void showToast(String message){
        if(mFavoriteMessage != null)
            mFavoriteMessage.cancel();
        mFavoriteMessage = Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT);
        mFavoriteMessage.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.starImageView:
                String message = getString(R.string.add_to_the_favorites);
                if(mIsFavorite){
                    mStarImageView.setImageResource(R.drawable.ic_star_outline_yellow_400_36dp);
                    message = getString(R.string.removed_from_favorites);
                }else {
                    mStarImageView.setImageResource(R.drawable.ic_star_yellow_400_36dp);
                }
                showToast(message);

                mIsFavorite = ! mIsFavorite;
                break;
        }
    }

    private void makeJsonArrayRequest(final int requestType) {
        String url = "";
        if(requestType == StaticTexts.GET_VIDEOS) {
            url = StaticTexts.mMovieBaseUrl + mMovie.getId() + StaticTexts.mMovieVideoUrl;
        }else{
            url = StaticTexts.mMovieBaseUrl + mMovie.getId() + StaticTexts.mMovieReviewUrl;
        }

        final JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(requestType == StaticTexts.GET_VIDEOS) {
                            getVideos(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(req,requestType+"");
    }

    private void getVideos(JSONObject object){
        try {
            mMovie.setVideos(object.getJSONArray("results"));

            //List view ile guncellenecek.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
    }
}
