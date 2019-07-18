package com.github.malib.simplevideoview;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4";
    private static final String PLAYBACK_TIME = "play_time";
    MediaController controller;
    private VideoView mVideoView;
    private TextView mBufferingTextView;
    private int mCurrentPosition = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
        checkPlayPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.videoView);
        mBufferingTextView = findViewById(R.id.buffering_textview);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }


        controller = new MediaController(this);
        // connect the controller
        controller.setMediaPlayer(mVideoView);

        // tell the video view that mediacontroller object will control you
        mVideoView.setMediaController(controller);
        checkPlayPause();

        controller.hide();
    }


    public void checkPlayPause() {





    }


    private Uri getMedia(String mediaName) {

        if (URLUtil.isValidUrl(mediaName)) {
            //media is an externa URL
            return Uri.parse(mediaName);
        } else {// media name is a raw resource embedded in the app
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }


    }

    private void initializePlayer() {
        mBufferingTextView.setVisibility(VideoView.VISIBLE);
        Uri videoUri = getMedia(VIDEO_SAMPLE);//getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // mBufferingTextView.setVisibility(VideoView.INVISIBLE);


                controller.show();
                if (mCurrentPosition > 0) {
                    mVideoView.seekTo(mCurrentPosition);

                } else {
                    mVideoView.seekTo(1);

                }

// as the media is loaded it will start playing
                //     mVideoView.start();
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                Toast.makeText(MainActivity.this, "Playback completed", Toast.LENGTH_SHORT).show();

                mVideoView.seekTo(1);
            }
        });
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();


// if sdk is lower than Noughat OS
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

// then pause the video
            mVideoView.pause();


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    checkPlayPause();

    }

    @Override
    protected void onStart() {
        super.onStart();


        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
