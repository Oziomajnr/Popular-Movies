package com.example.ogbeoziomajnr.popularmovies.Model;

/**
 * Created by SQ-OGBE PC on 12/05/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("results")
    private List<MovieTrailers> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieTrailers> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailers> results) {
        this.results = results;
    }
}
