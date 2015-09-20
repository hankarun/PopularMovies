package com.hankarun.popularmovies.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.R;
import com.hankarun.popularmovies.lib.StaticTexts;
import com.hankarun.popularmovies.activites.OneMovieActivity;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class OneMovieFragment extends Fragment {
    public OneMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        OneMovieActivity activity = (OneMovieActivity) getActivity();
        Movie mMovie = activity.mMovie;

        View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.posterImageView);
        TextView mRatingView = (TextView) rootView.findViewById(R.id.ratingTextView);
        TextView mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDateViewText);
        WebView mOverview = (WebView) rootView.findViewById(R.id.overViewWebView);

        Picasso.with(getActivity().getApplicationContext()).load(StaticTexts.mImageBaseUrl+ mMovie.getMoviePosterUrl()).into(mImageView);

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
}
