package com.example.moviebase.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebase.R;
import com.example.moviebase.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Адаптер для контейнера RecyclerView
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // Список фильмов
    private List<Movie> movies;
    // Класс для обработки кликов на постеры
    private OnPosterClickListener onPosterClickListener;
    // Класс для отслеживания позиции пользователя, чтобы вовремя подгружать данные
    private OnReachEndListener onReachEndListener;

    public MovieAdapter(){
        movies = new ArrayList<>();
    }

    public interface OnPosterClickListener {
        // Метод для обработки клика на постер
        void onPosterClick(int position);
    }
    // Сеттер класса для обработки клика на постер
    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public interface OnReachEndListener {
        // Метод для отслеживания позиции пользователя
        void onReachEnd();
    }

    // Сеттер класса для отслеживания позиции на пользователя
    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    // Обработчик создания метода MovieViewHolder
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    // Обработка и подгрузка изображений с помощью библиотеки Picasso
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movies.size() >= 20 &&  position == movies.size() - 4 && onReachEndListener != null){
            onReachEndListener.onReachEnd();
        }
        Movie movie = movies.get(position);
        Picasso.get().load(movie.getPoasterPath()).into(holder.SmallPoster);
    }

    @Override
    // Подсчет количества необходимых к подгрузке фильмов
    public int getItemCount() {
        return movies.size();
    }

    // Класс в котором мы подгружаем обложки фильмов
    class MovieViewHolder extends RecyclerView.ViewHolder{

        private ImageView SmallPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            SmallPoster = itemView.findViewById(R.id.SmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    // Получение списка фильмов ранее спаршенных в JSON
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    // Добавление новых фильмов по мере парсинга(без удаления уже имеющихся в контейнере фильмов)
    public void AddMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}
