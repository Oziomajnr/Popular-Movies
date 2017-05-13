package com.example.ogbeoziomajnr.popularmovies.Model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;

/**
 * Created by SQ-OGBE PC on 13/04/2017.
 */

public class Movie implements Parcelable {
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("backdrop_path")
    private String backDropPath;

    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Movie mMovie = new Movie();
            mMovie.title = source.readString();
            mMovie.overview = source.readString();
            mMovie.backDropPath = source.readString();
            mMovie.voteAverage = source.readDouble();
            mMovie.releaseDate = source.readString();
            mMovie.id = source.readInt();
            return  mMovie;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(backDropPath);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(id);
    }
}
