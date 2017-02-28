package com.example.rafae.promoz_001_alfa.util;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by vallux on 28/02/17.
 */

public class PlayAudio {
    private MediaPlayer mMediaPlayer;

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }
}
