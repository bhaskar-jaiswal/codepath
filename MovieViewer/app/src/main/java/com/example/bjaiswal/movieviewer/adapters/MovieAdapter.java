package com.example.bjaiswal.movieviewer.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bjaiswal.movieviewer.R;
import com.example.bjaiswal.movieviewer.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bjaiswal on 7/19/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getOriginalTitle());
        holder.tvOverview.setText(movie.getOverview());
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            Picasso.with(context).load(movie.getPosterPath()).into(holder.ivImage);
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            Picasso.with(context).load(movie.getBackdropPath()).into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("number of movies",movies.size()+"");
        return movies.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvOverview;

        public ViewHolder( View convertView){
            super(convertView);
            ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            ivImage.setImageResource(0);
            tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            tvOverview = (TextView)convertView.findViewById(R.id.tvOverview);
        }
    }
}
