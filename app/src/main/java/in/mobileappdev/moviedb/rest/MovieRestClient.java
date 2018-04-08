package in.mobileappdev.moviedb.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRestClient {

    public Retrofit createClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }


    public MovieRestService getService(){
        return createClient().create(MovieRestService.class);
    }



}
