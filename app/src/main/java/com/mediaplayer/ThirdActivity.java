package com.mediaplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPlay, btnPause, btnStop, btnCancel;     // buttons for controlling the music events
    BoundService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btnPlay = findViewById(R.id.play1);
        btnPause = findViewById(R.id.pause1);
        btnStop = findViewById(R.id.stop1);
        btnCancel = findViewById(R.id.cancel);

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    /*
    Binding to BoundService
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        if (mBound) {
            switch (v.getId()) {
                case R.id.play1:                  // start the audio
                    mService.startAudio();
                    break;

                case R.id.pause1:                // pause the audio
                    mService.pauseAudio();
                    break;

                case R.id.stop1:                 // stop the audio
                    mService.stopAudio();
                    break;

                case R.id.cancel:                // stop the service
                    if(mBound) {
                        unbindService(connection);
                        mBound=false;
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
