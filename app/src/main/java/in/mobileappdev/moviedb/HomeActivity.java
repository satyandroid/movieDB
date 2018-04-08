package in.mobileappdev.moviedb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.mobileappdev.moviedb.model.MovieResponse;
import in.mobileappdev.moviedb.rest.MovieRestClient;
import in.mobileappdev.moviedb.rest.MovieRestService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    ImageView imageView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        imageView = findViewById(R.id.nwImage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private Bitmap getImage(InputStream in) {
       return BitmapFactory.decodeStream(in);
    }

    private void readStream(InputStream in) {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(total.toString());
            Log.d("Name :: ", jsonObject.getString("original_title"));
            Log.d("popularity :: ",""+jsonObject.getInt("popularity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        MovieRestService service = new MovieRestClient().getService();
        MyTask myTask =new MyTask();
        if (id == R.id.nav_camera) {
            startActivity(new Intent(HomeActivity.this, PopularMovies.class));
        } else if (id == R.id.nav_gallery) {
            myTask.execute("https://wallpapercave.com/wp/6Y1SC6L.jpg");
        } else if (id == R.id.nav_slideshow) {
            myTask.execute("https://www.hdwallpapers.in/walls/spectrum_of_the_sky_hdtv_1080p-HD.jpg");
        } else if (id == R.id.nav_manage) {
            //without converter factory -- >ResponseBody

            service.getMovieById(550).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "OnResponse");
                    response.body().byteStream();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure");
                }
            });

            imageView.setImageBitmap(null);
        } else if (id == R.id.nav_share) {
            progressDialog.setMessage("Retrofit and Glide ");
            progressDialog.show();
            service.getMovieByIdUsingConverter(350312).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    progressDialog.dismiss();
                    MovieResponse movieResponse = response.body();
                    String bp = "https://image.tmdb.org/t/p/w500/"+movieResponse.getBackdropPath();
                    Log.d(TAG, "backdrop : "+bp);
                    Glide.with(HomeActivity.this).load(bp).into(imageView);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
            imageView.setImageBitmap(null);
        } else if (id == R.id.nav_send) {
            imageView.setImageBitmap(null);
            startActivity(new Intent(HomeActivity.this, MovieActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyTask extends AsyncTask<String, String, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return in;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e1){
                e1.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(InputStream is) {
            super.onPostExecute(is);
            progressDialog.dismiss();

            Bitmap bitmap = getImage(is);
            imageView.setImageBitmap(bitmap);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Downloading.....");
            progressDialog.show();
        }
    }
}
