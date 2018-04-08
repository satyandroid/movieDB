package in.mobileappdev.moviedb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import in.mobileappdev.moviedb.model.MovieResponse;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MovieAdapter>{

    List<MovieResponse> movieResponses;
    Context context;

    public PopularMoviesAdapter(Context context, List<MovieResponse> movieResponses) {
        this.movieResponses = movieResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter holder, int position) {
        MovieResponse movieResponse = movieResponses.get(position);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+movieResponse.getPosterPath()).into(holder.thumbnial);
    }

    @Override
    public int getItemCount() {
        return movieResponses.size();
    }

    public class MovieAdapter extends RecyclerView.ViewHolder{
        AppCompatImageView thumbnial;
        public MovieAdapter(View itemView) {
            super(itemView);
            thumbnial = itemView.findViewById(R.id.thumbnail);
        }
    }
}
