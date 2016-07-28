package com.example.egibson.movietime;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by egibson on 7/1/2016.
 */
public class ImageAdaptor extends ArrayAdapter<Movie>{

    private final String LOG_TAG = "ImageAdaptor";

    public ImageAdaptor(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(LOG_TAG, "getView");
        ImageView imageView;
        Movie movie = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_image, parent,false);
        }

        imageView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        imageView.setAdjustViewBounds(true);
        Picasso.with(getContext()).load(movie.mImage).into(imageView);

        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
    }
}

