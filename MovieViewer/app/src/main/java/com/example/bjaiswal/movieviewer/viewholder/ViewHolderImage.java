package com.example.bjaiswal.movieviewer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bjaiswal.movieviewer.R;
import com.example.bjaiswal.movieviewer.adapters.MovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjaiswal on 7/21/2016.
 */
public class ViewHolderImage extends RecyclerView.ViewHolder{

    @BindView(R.id.ivImage_backdrop)
    ImageView ivImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvOverview)
    TextView tvOverview;

    @BindView(R.id.ivImage_playOverlay)
    ImageView ivPlayOverlay;

    MovieAdapter.OnItemClickListener itemClicked;

    public ViewHolderImage(final View view,final MovieAdapter.OnItemClickListener itemClicked){
        super(view);
        ButterKnife.bind(this, view);

        this.itemClicked = itemClicked;

        ivImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onPlayVideo(view, getLayoutPosition());
                }
            }
        });
    }

    public ImageView getIvImage() {
        return ivImage;
    }

    public void setIvImage(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    public ImageView getIvPlayOverlay() {
        return ivPlayOverlay;
    }

    public void setIvPlayOverlay(ImageView ivPlayOverlay) {
        this.ivPlayOverlay = ivPlayOverlay;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvOverview() {
        return tvOverview;
    }

    public void setTvOverview(TextView tvOverview) {
        this.tvOverview = tvOverview;
    }
}
