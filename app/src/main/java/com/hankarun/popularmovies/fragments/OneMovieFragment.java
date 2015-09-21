package com.hankarun.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.hankarun.popularmovies.lib.AppController;
import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.Review;
import com.hankarun.popularmovies.lib.StaticTexts;
import com.hankarun.popularmovies.lib.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OneMovieFragment extends Fragment implements OnClickListener {
    private Boolean mIsFavorite = false;
    private ImageView mStarImageView;
    private Toast mFavoriteMessage;
    private Movie mMovie;
    private View mRootView;
    private LinearLayout mVideoList;
    private LinearLayout mReviewList;

    public OneMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if( savedInstanceState != null){
            mMovie = savedInstanceState.getParcelable("movie");
        }

        mRootView = inflater.inflate(R.layout.fragment_main2, container, false);

        //Check if tablet ui is present.
        if(!getActivity().getTitle().toString().equals("Popular Movies")){
            OneMovieActivity activity = (OneMovieActivity) getActivity();
            this.mMovie = activity.mMovie;
            setViews(mRootView);
            makeDataRequests();
        }
        else{
            if(mMovie != null){
                //Oriantation change
                setViews(mRootView);
                makeDataRequests();
            }
            // TODO This is tablet ui. Show empty layout
        }

        return mRootView;
    }

    private void makeDataRequests(){
        makeJsonArrayRequest(StaticTexts.GET_VIDEOS);
        makeJsonArrayRequest(StaticTexts.GET_REVIEWS);
    }

    private void setViews(View rootView){
        // TODO fix dis
        //RelativeLayout mMainLayout = (RelativeLayout) rootView.findViewById(R.id.mainMovieLayout);
        //mMainLayout.setVisibility(View.VISIBLE);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.posterImageView);
        TextView mRatingView = (TextView) rootView.findViewById(R.id.ratingTextView);
        TextView mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDateViewText);
        TextView mMovieName = (TextView) rootView.findViewById(R.id.movieNameView);
        TextView mOverview = (TextView) rootView.findViewById(R.id.overViewTextView);


        mStarImageView = (ImageView) rootView.findViewById(R.id.starImageView);
        mStarImageView.setOnClickListener(this);

        mVideoList = (LinearLayout) rootView.findViewById(R.id.videosLinearContainer);
        mReviewList = (LinearLayout) rootView.findViewById(R.id.reviewsLinearContainer);

        Picasso.with(getActivity().getApplicationContext())
                .load(StaticTexts.mImageBaseUrl + mMovie.getMoviePosterUrl())
                .placeholder(R.drawable.ic_a10)
                .into(mImageView);

        String text = "<html><body>" +
                "<h1 align=\"center\">"+getActivity().getString(R.string.overview)+"</h1>"
                + "<p align=\"justify\">"
                + mMovie.getOverview()
                + "</p> "
                + "</body></html>";

        mOverview.setText(Html.fromHtml(text));
        mRatingView.setText(mMovie.getUserRating() + "/10");
        mReleaseDate.setText(mMovie.getReleaseDate().subSequence(0, 4));
        mMovieName.setText(mMovie.getOriginalTitle());

        mImageView.setAdjustViewBounds(true);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void showToast(String message){
        if(mFavoriteMessage != null)
            mFavoriteMessage.cancel();
        mFavoriteMessage = Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_SHORT);
        mFavoriteMessage.show();
    }

    //When tablet ui present activity set fragment.
    public void setMovie(Movie movie){
        mMovie = movie;
        setViews(mRootView);
        makeDataRequests();
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
                // TODO save current movie to the database check for duoble entry
                mIsFavorite = ! mIsFavorite;
                break;
        }
    }

    private void makeJsonArrayRequest(final int requestType) {
        String url;
        if(requestType == StaticTexts.GET_VIDEOS) {
            url = StaticTexts.mMovieBaseUrl + mMovie.getId() + StaticTexts.mMovieVideoUrl;
        }else{
            url = StaticTexts.mMovieBaseUrl + mMovie.getId() + StaticTexts.mMovieReviewUrl;
        }

        final JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            getChilds(response, requestType);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(req,requestType+"");
    }

    private void getChilds(JSONObject object, int requestType){
        try {
            if(requestType == StaticTexts.GET_VIDEOS){
                mMovie.setVideos(object.getJSONArray("results"));
                fillVideoLayout(mMovie.getmVideos());
            } else {
                mMovie.setReviews(object.getJSONArray("results"));
                fillReviewLayout(mMovie.getmReviews());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
    }

    private void fillReviewLayout(List<Review> reviews){
        mReviewList.removeAllViews();
        if(reviews.size() == 0){
            TextView noReview = new TextView(getActivity().getApplicationContext());
            noReview.setText(R.string.there_is_no_review);
            noReview.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            mReviewList.addView(noReview);
        }else{
            for(int i = 0; i < reviews.size(); i++){
                LayoutInflater infalInflater = (LayoutInflater) getActivity().getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View item = infalInflater.inflate(R.layout.review_list_item, null);

                TextView authorName = (TextView) item.findViewById(R.id.authorTextView);
                TextView reviewText = (TextView) item.findViewById(R.id.reviewTextView);

                authorName.setText(reviews.get(i).getAuthor());
                reviewText.setText(reviews.get(i).getContent());

                mReviewList.addView(item);
            }
        }

    }

    private void fillVideoLayout(List<Video> videos){
        mVideoList.removeAllViews();
        if(videos.size() == 0){
            TextView noVideo = new TextView(getActivity().getApplicationContext());
            noVideo.setText(R.string.there_is_no_trailer);
            noVideo.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            mVideoList.addView(noVideo);
        }else{
            for(int i=0; i < videos.size(); i++){
                LayoutInflater infalInflater = (LayoutInflater) getActivity().getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View item = infalInflater.inflate(R.layout.video_list_item, null);
                TextView videoName = (TextView) item.findViewById(R.id.videoNameText);
                TextView videoType = (TextView) item.findViewById(R.id.videoTypeText);

                videoName.setText(videos.get(i).getName());
                videoType.setText(videos.get(i).getType()+ " - " + videos.get(i).getSize());

                final String url = videos.get(i).getUrl();

                item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

                mVideoList.addView(item);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie",mMovie);
    }
}
