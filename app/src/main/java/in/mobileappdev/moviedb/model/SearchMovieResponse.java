package in.mobileappdev.moviedb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchMovieResponse {

    @SerializedName("results")
    @Expose
    public List<MovieResponse> results;

    public SearchMovieResponse(List<MovieResponse> results) {
        this.results = results;
    }
}
