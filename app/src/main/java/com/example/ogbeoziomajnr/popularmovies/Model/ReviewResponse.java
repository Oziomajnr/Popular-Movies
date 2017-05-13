package com.example.ogbeoziomajnr.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SQ-OGBE PC on 12/05/2017.
 */

public class ReviewResponse {

    @SerializedName("results")
    private List<Review> results;


    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
