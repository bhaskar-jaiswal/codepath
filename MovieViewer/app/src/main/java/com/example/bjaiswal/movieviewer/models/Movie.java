package com.example.bjaiswal.movieviewer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bjaiswal on 7/19/2016.
 */
public class Movie {
    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private String rating;
    private String movieId;
    private String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.rating = jsonObject.getString("vote_average");
        this.movieId = jsonObject.getString("id");
        this.releaseDate = jsonObject.getString("release_date");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array){
        ArrayList<Movie> list = new ArrayList<Movie>();

        for(int i=0;i<array.length();i++)
            try {
                list.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return list;
    }

    public static String getTrailerKey(JSONArray array){

        String trailerKey="";

        try {
            if(array.length() > 0)
                trailerKey = array.getJSONObject(0).getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerKey;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getRating() {
        return rating;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
