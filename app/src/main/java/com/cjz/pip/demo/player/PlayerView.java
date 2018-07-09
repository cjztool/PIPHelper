package com.cjz.pip.demo.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by cjz on 2018/7/5.
 */
public class PlayerView extends SurfaceView {


    private MediaPlayer mediaPlayer;


    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(mediaPlayer!=null){
                    mediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if(mediaPlayer!=null){
                    mediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public void play() {
        new MediaPlayerThread().start();
    }

    class MediaPlayerThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                mediaPlayer = new MediaPlayer();
                AssetFileDescriptor assetFileDescriptor = getContext().getAssets().openFd("Batman vs Dracula.mp4");
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mediaPlayer.setDisplay(PlayerView.this.getHolder());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });

                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
