package com.example.bjaiswal.movieviewer.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.bjaiswal.movieviewer.R;
import com.example.bjaiswal.movieviewer.models.Movie;
import com.example.bjaiswal.movieviewer.viewholder.ViewHolderImage;
import com.example.bjaiswal.movieviewer.viewholder.ViewHolderItem;
import com.example.bjaiswal.movieviewer.viewholder.ViewHolderSimpleRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by bjaiswal on 7/19/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    private int orientation = -1;
    private OnItemClickListener onItemClick;

    public final int POSTER = 0, BACKDROP = 1;
    public final double EPSILON=0.0000001, RATING_BENCHMARK=5.0;

    public interface OnItemClickListener {
        void onPlayVideo(View itemView, int position);
        void onViewSynopsis(View itemView, int position);
    }

    public void setOnChooseOptionsActionListener(OnItemClickListener itemClicked) {
        onItemClick = itemClicked;
    }

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewholder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType){
            case POSTER:
                View posterView = inflater.inflate(R.layout.item_movie, parent, false);
                viewholder = new ViewHolderItem(posterView, onItemClick);
                break;
            case BACKDROP:
                View backdropView = inflater.inflate(R.layout.image_movie, parent, false);
                viewholder = new ViewHolderImage(backdropView, onItemClick);
                break;
            default:
                Log.d("DEBUG","INITIALIZING DEFAULT VIEWHOLDER");
                View simpleViewHolder = inflater.inflate(R.layout.item_movie, parent, false);
                viewholder = new ViewHolderSimpleRecyclerView(simpleViewHolder);
        }
        return viewholder;

        /*View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()){
            case POSTER:
                configureViewHolderItem((ViewHolderItem)viewHolder, position);
//                ((ViewHolderItem)viewHolder).bindTo(movies.get(position));
                break;
            case BACKDROP:
                configureViewHolderImage((ViewHolderImage)viewHolder, position);
                break;
        }
    }

    private void configureViewHolderItem(ViewHolderItem viewHolderItem, int position) {
        Movie movie = (Movie) movies.get(position);
        if (movie != null) {
            Picasso.with(context).load(movie.getPosterPath())
                    .placeholder(R.drawable.oscars_w170)
                    .resize(500,0)
                    .error(R.drawable.strip_w170)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolderItem.getIvImage());
            viewHolderItem.getTvTitle().setText(Html.fromHtml("<b>"+movie.getOriginalTitle()+"<b>"));
            viewHolderItem.getTvOverview().setText(Html.fromHtml("<i><b>"+movie.getOverview()+"<i><>b"));
        }
    }

    private void configureViewHolderImage(final ViewHolderImage viewHolderImage, int position) {
        Movie movie = (Movie) movies.get(position);
        if (movie != null) {

            RelativeLayout.LayoutParams playOverlayParams = (RelativeLayout.LayoutParams) viewHolderImage.getIvPlayOverlay().getLayoutParams();
            viewHolderImage.getIvPlayOverlay().setVisibility(View.INVISIBLE);
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                viewHolderImage.getTvTitle().setVisibility(View.VISIBLE);
                viewHolderImage.getTvOverview().setVisibility(View.VISIBLE);
                viewHolderImage.getTvTitle().setText(Html.fromHtml("<b>"+movie.getOriginalTitle()+"<>b"));
                viewHolderImage.getTvOverview().setText(Html.fromHtml("<i><b>"+movie.getOverview()+"<i><b>"));
                viewHolderImage.getIvImage().setAdjustViewBounds(false);
                viewHolderImage.getIvImage().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                playOverlayParams.setMargins(425,70,playOverlayParams.rightMargin,playOverlayParams.bottomMargin);

                viewHolderImage.getIvPlayOverlay().setLayoutParams(playOverlayParams);
            }else if(orientation == Configuration.ORIENTATION_PORTRAIT){
                viewHolderImage.getTvTitle().setVisibility(View.INVISIBLE);
                viewHolderImage.getTvOverview().setVisibility(View.INVISIBLE);
                viewHolderImage.getIvImage().setAdjustViewBounds(true);
                viewHolderImage.getIvImage().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                playOverlayParams.setMargins(playOverlayParams.leftMargin,100,playOverlayParams.rightMargin,playOverlayParams.bottomMargin);
                playOverlayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                viewHolderImage.getIvPlayOverlay().setLayoutParams(playOverlayParams);
            }
            Picasso.with(context).load(movie.getBackdropPath())
                    .placeholder(R.drawable.oscars)
                    .resize(850,500)
                    .error(R.drawable.strip)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolderImage.getIvImage(), new ImageLoadedCallback(viewHolderImage.getIvPlayOverlay()){
                        @Override
                        public void onSuccess() {
                            if (viewHolderImage.getIvPlayOverlay() != null) {
                                viewHolderImage.getIvPlayOverlay().setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = movies.get(position);
        double ratings = Double.parseDouble(movie.getRating());

        if (ratings - RATING_BENCHMARK < EPSILON)
            return POSTER;
        else
            return BACKDROP;
    }

    @Override
    public int getItemCount() {
        orientation = context.getResources().getConfiguration().orientation;
        return movies.size();
    }

    private class ImageLoadedCallback implements Callback {
        ImageView imageView;

        public  ImageLoadedCallback(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onError() {
        }
    }
}
