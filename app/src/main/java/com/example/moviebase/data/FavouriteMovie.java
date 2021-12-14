package com.example.moviebase.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie{
    public FavouriteMovie(int uniqueId,int id, int vote_count, String title, String original_title, String overview, String poasterPath, String bigpoasterPath, String backdropPath, double voteAverage, String releaseDate) {
        super(uniqueId,id, vote_count, title, original_title, overview, poasterPath, bigpoasterPath, backdropPath, voteAverage, releaseDate);
    }
    @Ignore
    public FavouriteMovie(Movie movie){
        super(movie.getUniqueId() ,movie.getId(), movie.getVote_count(), movie.getTitle(), movie.getOriginal_title(), movie.getOverview(), movie.getPoasterPath(), movie.getBigpoasterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseDate());
    }
}
