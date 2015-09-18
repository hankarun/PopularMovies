package com.hankarun.popularmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hankarun.popularmovies.Movie;
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
        //Movies.getsize();
        return 0;
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
            //Movie.get(position).Url;
            Picasso.with(mContext).load("").into(imageView);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
}
