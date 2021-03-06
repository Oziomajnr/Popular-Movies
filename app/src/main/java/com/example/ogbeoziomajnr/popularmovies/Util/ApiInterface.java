package com.example.ogbeoziomajnr.popularmovies.Util;

import com.example.ogbeoziomajnr.popularmovies.Model.MovieResponse;
import com.example.ogbeoziomajnr.popularmovies.Model.MovieTrailerResponse;
import com.example.ogbeoziomajnr.popularmovies.Model.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SQ-OGBE PC on 15/04/2017.
 */

public interface ApiInterface  {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pages);

    @GET("movie/popular")
    Call<MovieResponse> getPopularRatedMovies(@Query("api_key") String apiKey,@Query("page") int pages);

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
