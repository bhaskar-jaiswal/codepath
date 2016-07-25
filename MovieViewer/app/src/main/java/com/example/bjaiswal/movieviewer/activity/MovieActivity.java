package com.example.bjaiswal.movieviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.bjaiswal.movieviewer.R;
import com.example.bjaiswal.movieviewer.adapters.MovieAdapter;
import com.example.bjaiswal.movieviewer.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> moviesList = new ArrayList<Movie>();
    HashMap<String, String> hmTrailerKey = new HashMap<String, String>();
    MovieAdapter movieAdapter = null;
    RecyclerView lvItems;
    String movieId = "movieId";
    final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    final String sTrailerUrl = "http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private SwipeRefreshLayout swipeContainer;
    private AsyncHttpClient client ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        moviesList = new ArrayList<Movie>();
        client = new AsyncHttpClient();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        lvItems = (RecyclerView)findViewById(R.id.lvMovies);


        lvItems.setHasFixedSize(true);
        lvItems.setLayoutManager(new LinearLayoutManager(this));
        lvItems.setItemAnimator(new DefaultItemAnimator());

        if(moviesList.size()==0)
            fetchUpdatedMovies(url);

        movieAdapter = new MovieAdapter(this, moviesList);
        lvItems.setAdapter(movieAdapter);

        movieAdapter.setOnChooseOptionsActionListener(new MovieAdapter.OnItemClickListener(){

            @Override
            public void onPlayVideo(View itemView, int position) {
                Intent activity = new Intent(MovieActivity.this, QuickPlayActivity.class);
                activity.putExtra("trailerKey",hmTrailerKey.get(moviesList.get(position).getMovieId()));
                activity.putExtra("playOrSynopsis","play");
                startActivity(activity);
            }

            @Override
            public void onViewSynopsis(View itemView, int position) {
                Intent activity = new Intent(MovieActivity.this, QuickPlayActivity.class);
                Movie movie = moviesList.get(position);
                activity.putExtra("trailerKey",hmTrailerKey.get(movie.getMovieId()));
                activity.putExtra("playOrSynopsis","synopsis");
                activity.putExtra("title",movie.getOriginalTitle());
                activity.putExtra("synopsis",movie.getOverview());
                activity.putExtra("ratingBar",movie.getRating());
                activity.putExtra("releaseDate",movie.getReleaseDate());
                startActivity(activity);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(true);
                fetchUpdatedMovies(url);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void fetchUpdatedMovies(String url){
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
                    populateMovieTrailerKey();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchMovieTrailerKey(final String movieId, String url){
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", "Movie Trailer Key Fetch timeline error: " + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieTrailerResults = null;
                try {
                    movieTrailerResults = response.getJSONArray("results");
                    hmTrailerKey.put(movieId,Movie.getTrailerKey(movieTrailerResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateMovieTrailerKey(){
        StringBuffer sbTrailerUrl = new StringBuffer(sTrailerUrl);
        int start = sbTrailerUrl.indexOf(movieId);
        int length = movieId.length();
        for(Movie movie : moviesList){
            String id = movie.getMovieId();

            sbTrailerUrl.delete(0,sbTrailerUrl.length());
            sbTrailerUrl.append(sTrailerUrl);
            sbTrailerUrl.replace(start,start+movieId.length(),id+"");
            fetchMovieTrailerKey(id, sbTrailerUrl.toString());
        }
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
