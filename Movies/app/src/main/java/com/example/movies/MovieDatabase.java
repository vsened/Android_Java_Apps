package com.example.movies;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private final static String NAME = "movie.db";
    private static MovieDatabase instance = null;

    public static MovieDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    MovieDatabase.class,
                    NAME
            ).build();
        }
        return instance;
    }

    abstract MovieDao movieDao();
}
