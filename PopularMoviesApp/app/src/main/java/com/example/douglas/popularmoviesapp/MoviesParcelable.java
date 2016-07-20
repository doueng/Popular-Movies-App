package com.example.douglas.popularmoviesapp;


import android.os.Parcel;
import android.os.Parcelable;

public class MoviesParcelable implements Parcelable {
    private int id;
    private String posterUrl;

    public MoviesParcelable(int id, String posterUrl) {
        this.id = id;
        this.posterUrl = posterUrl;
    }


    protected MoviesParcelable(Parcel in) {
        id = in.readInt();
        posterUrl = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(posterUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoviesParcelable> CREATOR = new Parcelable.Creator<MoviesParcelable>() {
        @Override
        public MoviesParcelable createFromParcel(Parcel in) {
            return new MoviesParcelable(in);
        }

        @Override
        public MoviesParcelable[] newArray(int size) {
            return new MoviesParcelable[size];
        }
    };

    public int getId() {
        return this.id;
    }


    public String getUrl() {
        return this.posterUrl;
    }



}
