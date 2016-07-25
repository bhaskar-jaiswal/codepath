package com.example.bjaiswal.movieviewer.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bjaiswal.movieviewer.R;
import com.example.bjaiswal.movieviewer.videotube.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class QuickPlayActivity extends YouTubeBaseActivity {

    final String Play = "play", Synopsis = "synopsis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);

        final String trailerKey = getIntent().getStringExtra("trailerKey");
        final String playOrSynopsis = getIntent().getStringExtra("playOrSynopsis");

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(Config.YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        switch(playOrSynopsis){
                            case Play:
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                if(trailerKey != null && trailerKey.length() > 0) {
                                    youTubePlayer.loadVideo(trailerKey);
                                }
                                break;
                            case Synopsis:
                                final String title = getIntent().getStringExtra("title");
                                final String synopsis = getIntent().getStringExtra("synopsis");
                                final float ratingBar = Float.parseFloat(getIntent().getStringExtra("ratingBar"));
                                final String releaseDate = "Release Date: "+getIntent().getStringExtra("releaseDate");

                                final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
                                final TextView tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);
                                final RatingBar rbRatingBar = (RatingBar) findViewById(R.id.ratingBar);
                                final TextView tvReleaseDate = (TextView) findViewById(R.id.releaseDate);

                                tvTitle.setText(Html.fromHtml("<b>"+title+"<b>"));
                                tvSynopsis.setText(Html.fromHtml("<i><b>"+synopsis+"<i><b>"));
                                rbRatingBar.setRating(ratingBar);
                                tvReleaseDate.setText(Html.fromHtml("<i><b>"+releaseDate+"<i><b>"));
                                if(trailerKey != null && trailerKey.length() > 0) {
                                    youTubePlayer.cueVideo(trailerKey);
                                }
                                break;
                        }
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(QuickPlayActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
