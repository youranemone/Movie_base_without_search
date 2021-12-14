package com.example.moviebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviebase.adapters.ReviewAdapter;
import com.example.moviebase.adapters.TrailerAdapter;
import com.example.moviebase.data.FavouriteMovie;
import com.example.moviebase.data.MainViewModel;
import com.example.moviebase.data.Movie;
import com.example.moviebase.data.Review;
import com.example.moviebase.data.Trailer;
import com.example.moviebase.parsinutils.JSONUtils;
import com.example.moviebase.parsinutils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailInfoActivity extends AppCompatActivity {

    private ImageView BigPoster;
    private TextView Title;
    private TextView OriginalTitle;
    private TextView Rating;
    private TextView ReleaseDate;
    private TextView Discription;
    private ImageView ToFavourite;

    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private ScrollView scrollViewInfo;

    private int Movie_ID;
    private Movie movie ;
    private FavouriteMovie favouriteMovie;
    private MainViewModel viewModel;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        BigPoster = findViewById(R.id.imageViewBigPoster);
        Title = findViewById(R.id.textViewTitle);
        OriginalTitle = findViewById(R.id.textViewOriginal);
        Rating = findViewById(R.id.textViewRatingText);
        ReleaseDate = findViewById(R.id.textViewReleaseDateText);
        Discription = findViewById(R.id.textViewDiscriptionText);
        ToFavourite = findViewById(R.id.imageViewAddToFavorite);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("id")){
            Movie_ID = intent.getIntExtra("id", -1);
        } else{
            finish();
        }
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(Movie_ID);
        Picasso.get().load(movie.getBigpoasterPath()).into(BigPoster);
        Title.setText(movie.getTitle());
        OriginalTitle.setText(movie.getOriginal_title());
        Rating.setText(Double.toString(movie.getVoteAverage()));
        ReleaseDate.setText(movie.getReleaseDate());
        Discription.setText(movie.getOverview());
        setFavourite();
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String Url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                startActivity(intentToTrailer);
            }
        });
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(movie.getId());
        JSONObject jsonObjectReviews = NetworkUtils.getJSONForReviews(movie.getId());
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JSONUtils.getReviewFromJSON(jsonObjectReviews);
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);
        scrollViewInfo.smoothScrollTo(0,0);
    }

    public void onClickChangeFavourite(View view) {
        FavouriteMovie favouriteMovie = viewModel.getFavouriteMovieById(Movie_ID);
        if(favouriteMovie == null){
            viewModel.InsertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
        }else {
            viewModel.DeleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.remove_from_favourite, Toast.LENGTH_SHORT).show();
        }
        setFavourite();
    }

    private void setFavourite(){
        favouriteMovie = viewModel.getFavouriteMovieById(Movie_ID);
        if(favouriteMovie == null){
            ToFavourite.setImageResource(R.drawable.favourite_add_to);
        } else{
            ToFavourite.setImageResource(R.drawable.favourite_remove);
        }
    }
}