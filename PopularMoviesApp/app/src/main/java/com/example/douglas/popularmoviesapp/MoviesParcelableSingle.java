package com.example.douglas.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 04/05/2016.
 */
public class MoviesParcelableSingle implements Parcelable {

    private String trailerURL;
    private String review;

    private int id;
    private String overview;
    private String releaseDate;
    private String title;
    private Double voteAverage;
    private String posterURL;

    public MoviesParcelableSingle(String trailerURL, String review, String posterURL,
                                  int id, String overview,
                                  String releaseDate, String title, double voteAverage) {
        this.trailerURL = trailerURL;
        this.review = review;
        this.posterURL = posterURL;
        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    protected MoviesParcelableSingle(Parcel in) {
        trailerURL = in.readString();
        review = in.readString();

        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        posterURL = in.readString();
        voteAverage = in.readByte() == 0x00 ? null : in.readDouble();
        overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerURL);
        dest.writeString(review);

        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(posterURL);
        if (voteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(voteAverage);
        }
        dest.writeString(overview);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MoviesParcelableSingle> CREATOR = new Parcelable.Creator<MoviesParcelableSingle>() {
        @Override
        public MoviesParcelableSingle createFromParcel(Parcel in) {
            return new MoviesParcelableSingle(in);
        }

        @Override
        public MoviesParcelableSingle[] newArray(int size) {
            return new MoviesParcelableSingle[size];
        }
    };
    public String getTrailerURL() {
        return this.trailerURL;
    }

    public String getReview() {
        return this.review;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public Double getVoteAverage() {
        return this.voteAverage;
    }

    public String getOverview() {
        return this.overview;
    }


}
