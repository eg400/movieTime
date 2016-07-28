package com.example.egibson.movietime;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by egibson on 7/11/2016.
 */
public class Movie implements Parcelable{

    String mImage;
    String mOriginalTitle;
    String mOverview;
    String mVoteAverage;
    String mReleaseDate;

    public Movie(String image, String title, String overview, String voteAv, String releaseDate) {
        this.mImage = image;
        this.mOriginalTitle = title;
        this.mOverview = overview;
        this.mVoteAverage = voteAv;
        this.mReleaseDate = releaseDate;
    }

    private Movie(Parcel in){
        mImage = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return mImage + "--" + mOriginalTitle + "--" + mOverview + "--" + mVoteAverage + "--" + mReleaseDate; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImage);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mReleaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
