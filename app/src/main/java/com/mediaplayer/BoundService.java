package com.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class BoundService extends Service {

    MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.Song);
    }

    class LocalBinder extends Binder {
        BoundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startAudio() {

        mediaPlayer.start();
    }

    public void pauseAudio() {
        mediaPlayer.pause();
    }

    public void stopAudio() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}

