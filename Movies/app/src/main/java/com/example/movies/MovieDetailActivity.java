package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";
    private static final String TAG = "MovieDetailActivity";
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private MovieDetailViewModel viewModel;

    private ImageView imageViewMoviePoster;
    private ImageView imageViewStar;
    private TextView textViewMovieTitle;
    private TextView textViewMovieYear;
    private TextView textViewMovieDescription;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        trailerAdapter = new TrailerAdapter();
        recyclerViewTrailers.setAdapter(trailerAdapter);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        Glide.with(this)
                .load(movie.getPoster().getURL())
                .into(imageViewMoviePoster);
        textViewMovieTitle.setText(movie.getName());
        textViewMovieYear.setText(String.valueOf(movie.getYear()));
        textViewMovieDescription.setText(movie.getDescription());


        viewModel.loadTrailers(movie.getId());
        viewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                trailerAdapter.setTrailers(trailers);
            }
        });
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailer trailer) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setAdapter(reviewAdapter);
        viewModel.getReviews().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });
        viewModel.loadReviews(movie.getId());
        Drawable starOff = ContextCompat.getDrawable(
                MovieDetailActivity.this,
                android.R.drawable.star_big_off
        );
        Drawable starOn = ContextCompat.getDrawable(
                MovieDetailActivity.this,
                android.R.drawable.star_big_on
        );
        viewModel.getFavouriteMovie(movie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDb) {
                if (movieFromDb == null) {
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.insertMovie(movie);
                        }
                    });
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.removeMovie(movie.getId());
                        }
                    });
                }
            }
        });
    }

    private void initViews(){
        imageViewMoviePoster = findViewById(R.id.imageViewMoviePoster);
        imageViewStar = findViewById(R.id.imageViewStar);
        textViewMovieTitle = findViewById(R.id.textViewMovieTitle);
        textViewMovieYear = findViewById(R.id.textViewYear);
        textViewMovieDescription = findViewById(R.id.textViewMovieDescription);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
    }

    public static Intent getIntent(Context context, Movie movie){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}