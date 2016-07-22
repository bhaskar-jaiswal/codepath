package com.example.bjaiswal.movieviewer;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bjaiswal.movieviewer.adapters.MovieAdapter;
import com.example.bjaiswal.movieviewer.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> moviesList = new ArrayList<Movie>();
    MovieAdapter movieAdapter = null;
    RecyclerView lvItems;

    private SwipeRefreshLayout swipeContainer;
//    private AsyncHttpClient client ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        moviesList = new ArrayList<Movie>();
        final AsyncHttpClient client = new AsyncHttpClient();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        lvItems = (RecyclerView)findViewById(R.id.lvMovies);


        lvItems.setHasFixedSize(true);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.setItemAnimator(new DefaultItemAnimator());

        fetchUpdatedMovies(client, url);
        movieAdapter = new MovieAdapter(this, moviesList);
        lvItems.setAdapter(movieAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                fetchUpdatedMovies(client, url);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void fetchUpdatedMovies(AsyncHttpClient client, String url){
        Log.d("isnull",(client==null)+"");
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", "Fetch timeline error: " + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieResults = null;
                try {
                    movieResults = response.getJSONArray("results");
                    clearMovieList();
                    updateMovieList(movieResults);
                    swipeContainer.setRefreshing(false);
                    Log.d("DEBUG", moviesList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearMovieList(){
        moviesList.clear();
        movieAdapter.notifyDataSetChanged();
    }

    private void updateMovieList(JSONArray movieResults){
        moviesList.addAll(Movie.fromJSONArray(movieResults));
        movieAdapter.notifyDataSetChanged();
    }
}
