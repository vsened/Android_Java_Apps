package com.example.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class FavouriteMovieViewModel extends AndroidViewModel {
    private final MovieDao movieDao;

    public LiveData<List<Movie>> getFavouriteMovies() {
        return movieDao.getAllFavouriteMovies();
    }

    public FavouriteMovieViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(application).movieDao();
    }
}
