package com.example.moviebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebase.adapters.MovieAdapter;
import com.example.moviebase.data.MainViewModel;
import com.example.moviebase.data.Movie;
import com.example.moviebase.parsinutils.JSONUtils;
import com.example.moviebase.parsinutils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    // Переменные используемых классов
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;

    private MainViewModel viewModel;
    private static final int LOADER_ID = 100;
    private LoaderManager loaderManager;
    private static int methodOfSort;
    private static int page = 1;
    private static boolean isLoading = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavorite:
                Intent intent1 = new Intent(this, FavoriteActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderManager = LoaderManager.getInstance(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Получаем id полей, связанных с переменными
        switchSort = findViewById(R.id.switch_sort);
        textViewPopularity = findViewById(R.id.text_popularity);
        textViewTopRated = findViewById(R.id.text_top_rated);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);

        // Добавляем в Recycle layout две колонки постеров с фильмами
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this,getColumnCount()));
        movieAdapter = new MovieAdapter();

        // Парсим JSON и получаем список фильмов и передаем его в recyclerView
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY,1);
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        movieAdapter.setMovies(movies);
        recyclerViewPosters.setAdapter(movieAdapter);
        recyclerViewPosters.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1) && !isLoading){
                    downloadData(methodOfSort, page);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // Устанавливаем значение для свитчера
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
                setMethodOfSort(isChecked);
            }
        });
        switchSort.setChecked(false);
        // Вызов метода обработчика клика на постер
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailInfoActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });
        // Вызов метода отслеживающего позицию пользователя
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if(!isLoading){
                //downloadData(methodOfSort,page);
            }
            }
        });
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {

            }
        });
    }

    // Методы для обработки положения свитчера
    public void onClickSetPopularity (View view){
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickSetPTopRated (View view){
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    private void setMethodOfSort (boolean isTopRated){
        if(isTopRated){
            methodOfSort = NetworkUtils.TOP_RATED;
            textViewTopRated.setTextColor(getResources().getColor(R.color.teal_200));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        }else{
            methodOfSort = NetworkUtils.POPULARITY;
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.teal_200));
        }
        downloadData(methodOfSort,1);
    }

    // Загрузка данных в БД
    private void downloadData(int methodOfSort, int page){
        URL url = NetworkUtils.buildURL(methodOfSort,page);
        Bundle bundle = new Bundle();
        bundle.putString("url",url.toString());
        loaderManager.restartLoader(LOADER_ID,bundle,this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if(movies != null && !movies.isEmpty()){
            viewModel.deleteAllMovies();
            for(Movie movie : movies){
                viewModel.InsertMovie(movie);
            }
            movieAdapter.AddMovies(movies);
        }
        isLoading = false;
        loaderManager.destroyLoader(LOADER_ID);
        page++;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {
    }
}
