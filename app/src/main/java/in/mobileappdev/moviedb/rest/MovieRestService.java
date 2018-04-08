package in.mobileappdev.moviedb.rest;

import android.graphics.Movie;

import java.util.List;

import in.mobileappdev.moviedb.model.MovieResponse;
import in.mobileappdev.moviedb.model.SearchMovieResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieRestService {

    @GET("3/movie/{movieId}?api_key=58a20c02f33a3222e8cffae1092f3453")
    public Call<ResponseBody> getMovieById(@Path("movieId") int id);

    @GET("3/movie/{movieId}?api_key=58a20c02f33a3222e8cffae1092f3453")
    public Call<MovieResponse> getMovieByIdUsingConverter(@Path("movieId") int id);


    @GET("3/movie/popular?api_key=58a20c02f33a3222e8cffae1092f3453&language=en-US&page=1")
    public Call<SearchMovieResponse> getPopularMovies();

}
