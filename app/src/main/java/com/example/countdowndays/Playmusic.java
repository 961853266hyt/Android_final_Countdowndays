package com.example.countdowndays;

import android.content.Context;
import android.media.MediaPlayer;

public class Playmusic {

    private MediaPlayer mediaPlayer;
    public Playmusic(Context context, int id) {
        this.mediaPlayer = MediaPlayer.create(context,id);
    }

    public void play(){
        mediaPlayer.start();
    }

    public void stop(){
        mediaPlayer.stop();
    }
}
