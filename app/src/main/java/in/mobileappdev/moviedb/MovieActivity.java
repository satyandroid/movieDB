package in.mobileappdev.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.mobileappdev.moviedb.model.MovieResponse;
import in.mobileappdev.moviedb.rest.MovieRestClient;
import in.mobileappdev.moviedb.rest.MovieRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private AppCompatEditText movieId;
    private AppCompatImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        movieId = findViewById(R.id.edt_movie_id);
        imageView = findViewById(R.id.imageView);
    }

    public void searchMovie(View view) {
        MovieRestService restService = new MovieRestClient().getService();
        restService.getMovieByIdUsingConverter(Integer.parseInt(movieId.getText().toString())).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                String bp = "https://image.tmdb.org/t/p/w500/"+movieResponse.getBackdropPath();
                Glide.with(MovieActivity.this).load(bp).into(imageView);

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
}
