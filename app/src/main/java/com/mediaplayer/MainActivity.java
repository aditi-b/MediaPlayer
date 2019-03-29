package com.mediaplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPlay, btnPause, btnStop, btnNext, btnCancel;  // buttons for controlling the music events
    BoundService mService;
    boolean mBound = false;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = findViewById(R.id.play);
        btnPause = findViewById(R.id.pause);
        btnStop = findViewById(R.id.stop);
        btnNext = findViewById(R.id.next);
        btnCancel = findViewById(R.id.cancel);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /**
     * Binding to BoundService
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        ContextCompat.startForegroundService(this, intent);
        intent.putExtra("Message", message);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        if (mBound) {
            switch (v.getId()) {
                case R.id.play:                           // play the audio
                    mService.startAudio();
                    message = "Audio started";
                    break;

                case R.id.pause:                         // pause the audio
                    mService.pauseAudio();
                    message = "Audio paused";
                    break;

                case R.id.stop:                         // stop the audio
                    mService.stopAudio();
                    message = "Audio Stopped";
                    break;

                case R.id.next:                         // moving to NextActivity
                    Intent i = new Intent(this, NextActivity.class);
                    startActivity(i);
                    break;

                case R.id.cancel:                       // stopping the service
                    if (mBound) {
                        unbindService(connection);
                        mBound = false;
                    }
                    break;
            }
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

}
