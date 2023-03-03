package com.example.movies;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie?token={paste_your_token}&field=rating.kp&search=7-10&sortField=votes.kp&sortType=-1&limit=30")
    Single<MovieResponse> loadMovies(@Query("page") int page);

    @GET("movie?token={paste_your_token}&field=id")
    Single<TrailerResponse> loadTrailers(@Query("search") int id);

    @GET("review?field=movieId&token={paste_your_token}")
    Single<ReviewResponse> loadReviews(@Query("search") int id);

}
