package com.example.egibson.movietime;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class movie_detail_fragment extends Fragment {


    public movie_detail_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Movie movie = intent.getParcelableExtra("com.example.egibson.movietime.Movie");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ((TextView) rootView.findViewById(R.id.movie_title))
                .setText(movie.mOriginalTitle);
        ((TextView) rootView.findViewById(R.id.movie_overview))
                .setText(movie.mOverview);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_thumb);
        Picasso.with(getContext()).load(movie.mImage).into(imageView);
        ((TextView) rootView.findViewById(R.id.movie_release_date))
                .setText(movie.mReleaseDate);
        ((TextView) rootView.findViewById(R.id.movie_vote_average))
                .setText(movie.mVoteAverage+"/10");

        return rootView;
    }

}
