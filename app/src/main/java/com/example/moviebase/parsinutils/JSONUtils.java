package com.example.moviebase.parsinutils;

import com.example.moviebase.data.Movie;
import com.example.moviebase.data.Review;
import com.example.moviebase.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    // Значение ключей, по которым мы выбираем определенные значения
    private static final String KEY_RESULTS = "results";
    // Для получения отзывов
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    // Для получения видео
    private static final String KEY_KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";


    // Для информации о фильме
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    // Ссылка базовая парсинга постеров и их размеры
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    public static ArrayList<Trailer> getTrailersFromJSON (JSONObject jsonObject){
        ArrayList<Trailer> result = new ArrayList<>();
        if(jsonObject == null)
            return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0 ; i < jsonArray.length(); i++){
                JSONObject jsonObjectReview = jsonArray.getJSONObject(i);
                String keyofvideo = BASE_YOUTUBE_URL + jsonObjectReview.getString(KEY_KEY_OF_VIDEO);
                String name = jsonObjectReview.getString(KEY_NAME);
                Trailer trailer = new Trailer(keyofvideo,name);
                result.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Review> getReviewFromJSON (JSONObject jsonObject){
        ArrayList<Review> result = new ArrayList<>();
        if(jsonObject == null)
            return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0 ; i < jsonArray.length(); i++){
                JSONObject jsonObjectReview = jsonArray.getJSONObject(i);
                String author = jsonObjectReview.getString(KEY_AUTHOR);
                String content = jsonObjectReview.getString(KEY_CONTENT);
                Review review = new Review(author,content);
                result.add(review);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Преобразование полученного ранее JSON и запись информации о фильмах в ArrayList
    public static ArrayList<Movie> getMoviesFromJSON (JSONObject jsonObject){
        ArrayList<Movie> result = new ArrayList<>();
        if(jsonObject == null)
            return result;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt(KEY_ID);
                int voteCount = object.getInt(KEY_VOTE_COUNT);
                String title = object.getString(KEY_TITLE);
                String original_title = object.getString(KEY_ORIGINAL_TITLE);
                String overview = object.getString(KEY_OVERVIEW);
                String poster_path = BASE_POSTER_URL + SMALL_POSTER_SIZE + object.getString(KEY_POSTER_PATH);
                String Big_poster_path = BASE_POSTER_URL + BIG_POSTER_SIZE + object.getString(KEY_POSTER_PATH);
                String backdrop_path = object.getString(KEY_BACKDROP_PATH);
                double vote_average = object.getDouble(KEY_VOTE_AVERAGE);
                String release_date = object.getString(KEY_RELEASE_DATE);
                Movie movie = new Movie(id, voteCount, title, original_title, overview, poster_path, Big_poster_path,backdrop_path, vote_average, release_date);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
