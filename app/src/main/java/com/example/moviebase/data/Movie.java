package com.example.moviebase.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.security.PrivateKey;
@Entity(tableName = "movies")

public class Movie {
    // Поля которые парсятся для каждого фильма
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id;
    private int vote_count;
    private String title;
    private String original_title;
    private String overview;
    private String poasterPath;
    private String bigpoasterPath;
    private String backdropPath;
    private double voteAverage;
    private String releaseDate;

    // Конструктор класса
    public Movie(int uniqueId,int id,int vote_count,String title,String original_title,String overview,String poasterPath,String bigpoasterPath,String backdropPath,double voteAverage,String releaseDate) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.poasterPath = poasterPath;
        this.bigpoasterPath = bigpoasterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    @Ignore
    public Movie(int id,int vote_count,String title,String original_title,String overview,String poasterPath,String bigpoasterPath,String backdropPath,double voteAverage,String releaseDate) {
        this.id = id;
        this.vote_count = vote_count;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.poasterPath = poasterPath;
        this.bigpoasterPath = bigpoasterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    // Геттеры и сеттеры класса
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getTitle() {
        return title;
    }

    public String getBigpoasterPath() {
        return bigpoasterPath;
    }

    public void setBigpoasterPath(String bigpoasterPath) {
        this.bigpoasterPath = bigpoasterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoasterPath() {
        return poasterPath;
    }

    public void setPoasterPath(String poasterPath) {
        this.poasterPath = poasterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
