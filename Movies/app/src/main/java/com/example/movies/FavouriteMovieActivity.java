package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class FavouriteMovieActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavouriteMovies;
    private MoviesAdapter moviesAdapter;
    private FavouriteMovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie);
        initViews();
        viewModel = new ViewModelProvider(this).get(FavouriteMovieViewModel.class);
        moviesAdapter = new MoviesAdapter();
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFavouriteMovies.setAdapter(moviesAdapter);
        viewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesAdapter.setMovies(movies);
            }
        });
        moviesAdapter.setOnMovieClickListener(new MoviesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieCLick(Movie movie) {
                Intent intent = MovieDetailActivity.getIntent(
                        FavouriteMovieActivity.this,
                        movie
                );
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FavouriteMovieActivity.class);
    }
}