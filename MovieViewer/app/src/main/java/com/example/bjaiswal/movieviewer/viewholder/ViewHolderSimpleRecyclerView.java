package com.example.bjaiswal.movieviewer.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bjaiswal.movieviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjaiswal on 7/21/2016.
 */
public class ViewHolderSimpleRecyclerView extends RecyclerView.ViewHolder{

    @BindView(R.id.ivMovieImage)
    ImageView ivImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvOverview)
    TextView tvOverview;

    public ViewHolderSimpleRecyclerView( View convertView){
        super(convertView);
        ButterKnife.bind(this, convertView);
        ivImage.setImageResource(0);
    }
}