package com.hankarun.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.hankarun.popularmovies.helpers.MovieTable;
import com.hankarun.popularmovies.lib.AppController;
import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.MovieContentProvider;
import com.hankarun.popularmovies.lib.Review;
import com.hankarun.popularmovies.lib.StaticTexts;
import com.hankarun.popularmovies.lib.Video;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class OneMovieFragment extends Fragment implements OnClickListener {
    private Boolean mIsFavorite = false;
    private ImageView mStarImageView;
    private Toast mFavoriteMessage;
    private Movie mMovie;
    private View mRootView;
    private LinearLayout mVideoList;
    private LinearLayout mReviewList;
    private TextView mReviewTextHeader;
    private TextView mVideoTextHeader;

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
        }

        setHasOptionsMenu(true);

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.single_movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if(mMovie.getmVideos().size() > 0) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share) + mMovie.getOriginalTitle() + " - " + mMovie.getmVideos().get(0).getName());

                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mMovie.getOriginalTitle() + " - " + mMovie.getmVideos().get(0).getName() + " " + mMovie.getmVideos().get(0).getUrl());
                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), R.string.nothing_to_share,Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeDataRequests(){
        makeJsonArrayRequest(StaticTexts.GET_VIDEOS);
        makeJsonArrayRequest(StaticTexts.GET_REVIEWS);
    }

    private void setViews(View rootView){
        // TODO fix dis
        RelativeLayout mMainLayout = (RelativeLayout) rootView.findViewById(R.id.mainMovieLayout);
        mMainLayout.setVisibility(View.VISIBLE);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.posterImageView);
        TextView mRatingView = (TextView) rootView.findViewById(R.id.ratingTextView);
        TextView mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDateViewText);
        TextView mMovieName = (TextView) rootView.findViewById(R.id.movieNameView);
        TextView mOverview = (TextView) rootView.findViewById(R.id.overViewTextView);

        mReviewTextHeader = (TextView) rootView.findViewById(R.id.reviewTextView);
        mVideoTextHeader = (TextView) rootView.findViewById(R.id.videosTextView);


        mStarImageView = (ImageView) rootView.findViewById(R.id.starImageView);
        mStarImageView.setOnClickListener(this);
        mIsFavorite = isMovieFavorite(mMovie);
        if(mIsFavorite){
            mStarImageView.setImageResource(R.drawable.ic_star_yellow_400_36dp);
        }else {
            mStarImageView.setImageResource(R.drawable.ic_star_outline_yellow_400_36dp);
        }

        mVideoList = (LinearLayout) rootView.findViewById(R.id.videosLinearContainer);
        mReviewList = (LinearLayout) rootView.findViewById(R.id.reviewsLinearContainer);

        File offlineImage = new File(getActivity().getFilesDir().getPath() + mMovie.getId() +".png");

        if(offlineImage.exists()){
            Picasso.with(getActivity().getApplicationContext())
                    .load(offlineImage)
                    .placeholder(R.drawable.ic_file_big)
                    .error(R.drawable.ic_cloud_big)
                    .into(mImageView);
        }else{
            Picasso.with(getActivity().getApplicationContext())
                    .load(StaticTexts.mImageBaseUrl + mMovie.getMoviePosterUrl())
                    .placeholder(R.drawable.ic_file_big)
                    .error(R.drawable.ic_cloud_big)
                    .into(mImageView);
        }



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
                    deleteFavoriteMovie(mMovie);
                }else {
                    mStarImageView.setImageResource(R.drawable.ic_star_yellow_400_36dp);
                    insertFavoriteMovie(mMovie);
                }
                showToast(message);
                mIsFavorite = !mIsFavorite;

                ShowMoviesFragment leftBarFragment = (ShowMoviesFragment) getFragmentManager()
                        .findFragmentById(R.id.fragment1);

                if (leftBarFragment != null) {
                    leftBarFragment.updateFavoriteMovieList();
                }
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
                        getReviewsAndVideos(response, requestType);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(req, requestType + "");
    }

    private void getReviewsAndVideos(JSONObject object, int requestType){
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
        mReviewTextHeader.setVisibility(View.VISIBLE);
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
        mVideoTextHeader.setVisibility(View.VISIBLE);
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

    private void deleteFavoriteMovie(Movie movie){
        String selectionClause = MovieTable.COLUMNT_MOVIE_ID + " = ?";
        String[] selectionArgs = {movie.getId()};


        getActivity().getContentResolver().delete(
                MovieContentProvider.CONTENT_URI,
                selectionClause,
                selectionArgs
        );

        File file = new File(getActivity().getFilesDir().getPath() + mMovie.getId() +".png");
        if(file.exists())
            file.delete();

    }

    private void insertFavoriteMovie(Movie movie){
        ContentValues newValues = new ContentValues();

        newValues.put(MovieTable.COLUMN_MOVIE_POSTER_URL, movie.getMoviePosterUrl());
        newValues.put(MovieTable.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        newValues.put(MovieTable.COLUMNT_MOVIE_ID, movie.getId());
        newValues.put(MovieTable.COLUMN_OVERVIEW, movie.getOverview());
        newValues.put(MovieTable.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        newValues.put(MovieTable.COLUMN_USER_RATING, movie.getUserRating());
        newValues.put(MovieTable.COLUMN_JSON_MOVIE, movie.toString());

        getActivity().getContentResolver().insert(
                MovieContentProvider.CONTENT_URI,
                newValues
        );


        Target target = new Target() {
            @Override
            public void onPrepareLoad(Drawable arg0) {
            }

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom arg1) {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {

                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(getActivity().getFilesDir().getPath() + mMovie.getId() +".png");
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void info) {
                    }
                }.execute();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
            }
        };

        //Save with picasso
        Picasso.with(getActivity().getApplicationContext())
                .load(StaticTexts.mImageBaseUrl + mMovie.getMoviePosterUrl())
                .into(target);
    }

    private boolean isMovieFavorite(Movie movie){

        String[] projection =
                {
                        MovieTable.COLUMNT_MOVIE_ID
                };

        String selectionClause = null;

        String[] selectionArgs = null;

        String searchString = movie.getId();

        if (!TextUtils.isEmpty(searchString)) {
            selectionClause = MovieTable.COLUMNT_MOVIE_ID + " = ?";

            selectionArgs = new String[]{ searchString };
        }

        Cursor cursor = getActivity().getContentResolver().query(
                MovieContentProvider.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null);

        if (cursor == null) {
            // Problem
            return false;
        } else if (cursor.getCount() < 1) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie",mMovie);
    }
}
