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
public class ViewHolderItem extends RecyclerView.ViewHolder{

    @BindView   (R.id.ivMovieImage)
    ImageView ivImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvOverview)
    TextView tvOverview;

    private MovieAdapter.OnItemClickListener itemClicked;

    public ViewHolderItem (final View view , final MovieAdapter.OnItemClickListener mitemClicked){
        super(view);
        ButterKnife.bind(this, view);

        itemClicked = mitemClicked;

        ivImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (itemClicked != null){
                    itemClicked.onViewSynopsis(view, getLayoutPosition());
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
