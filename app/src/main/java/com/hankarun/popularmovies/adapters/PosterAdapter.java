package com.hankarun.popularmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hankarun.popularmovies.lib.Movie;
import com.hankarun.popularmovies.lib.StaticTexts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mMovies;

    public PosterAdapter(Context c, List<Movie> _movies) {
        mMovies = _movies;
        mContext = c;
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(StaticTexts.mImageBaseUrl+mMovies.get(position).getMoviePosterUrl()).into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
}
