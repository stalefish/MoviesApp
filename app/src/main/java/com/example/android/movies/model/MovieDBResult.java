package com.example.android.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class MovieDBResult implements Parcelable {

    private int voteCount;
    private double voteAverage;
    private String title;
    private double popularity;
    private Date releaseDate;
    private String overview;
    private String posterPath;


    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public MovieDBResult() {}


    private MovieDBResult(Parcel in) {
        voteCount = in.readInt();
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        releaseDate = new Date(in.readLong());
        overview = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<MovieDBResult> CREATOR = new Creator<MovieDBResult>() {
        @Override
        public MovieDBResult createFromParcel(Parcel in) {
            return new MovieDBResult(in);
        }

        @Override
        public MovieDBResult[] newArray(int size) {
            return new MovieDBResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeLong(releaseDate.getTime());
        dest.writeString(overview);
        dest.writeString(posterPath);
    }

    public String toString() {
        return "MovieDBResult - title: " + title + ", voteCount: " + voteCount + ", voteAverage: " + voteAverage + ", popularity: " + popularity + ", releaseDate: " + releaseDate + ", posterPath: " + posterPath;

    }
}
