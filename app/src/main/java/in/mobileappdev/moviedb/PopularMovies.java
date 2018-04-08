package in.mobileappdev.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import in.mobileappdev.moviedb.model.SearchMovieResponse;
import in.mobileappdev.moviedb.rest.MovieRestClient;
import in.mobileappdev.moviedb.rest.MovieRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMovies extends AppCompatActivity {

    private static final String TAG = PopularMovies.class.getSimpleName();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        recyclerView = findViewById(R.id.popularMoviesList);

        MovieRestService service = new MovieRestClient().getService();
        service.getPopularMovies().enqueue(new Callback<SearchMovieResponse>() {
            @Override
            public void onResponse(Call<SearchMovieResponse> call, Response<SearchMovieResponse> response) {
                Log.d(TAG, "Onresponse");
                PopularMoviesAdapter adapter = new PopularMoviesAdapter(PopularMovies.this, response.body().results);
                recyclerView.setLayoutManager(new GridLayoutManager(PopularMovies.this, 2));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<SearchMovieResponse> call, Throwable t) {
                Log.d(TAG, "ONFailure");
            }
        });

    }
}
