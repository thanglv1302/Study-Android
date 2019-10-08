package com.example.levietthang.studyandroid;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Field;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;
import static android.media.AudioManager.STREAM_MUSIC;

public class MainActivity extends Activity {
    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 0;
    private Button mode, ring, vibrate, silent, mute, maxVolume, volUp, volDown;
    private ProgressBar volumeChange;
    private AudioManager myAudioManager;
    private int volumeMax, volumeMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMutePermissions();

        mode = (Button) findViewById(R.id.btnMode);
        ring = (Button) findViewById(R.id.btnRing);
        vibrate = (Button) findViewById(R.id.btnVib);
        silent = (Button) findViewById(R.id.btnSilent);
        mute = (Button) findViewById(R.id.btnMute);
        maxVolume = (Button) findViewById(R.id.btnMaxVolume);
        volumeChange = (ProgressBar) findViewById(R.id.volumeBar);
        volUp = (Button) findViewById(R.id.btnVolUp);
        volDown = (Button) findViewById(R.id.btnVolDown);

        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeMax = myAudioManager.getStreamMaxVolume(STREAM_MUSIC);
        volumeMin = myAudioManager.getStreamMinVolume(STREAM_MUSIC);
        volumeChange.setMax(volumeMax);
        volumeChange.setProgress(myAudioManager.getStreamVolume(STREAM_MUSIC));
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_NORMAL);
                Toast.makeText(MainActivity.this, "Now in normal mode", Toast.LENGTH_LONG).show();
            }
        });

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_VIBRATE);
                Toast.makeText(MainActivity.this, "Now in vibrate mode", Toast.LENGTH_LONG).show();
            }
        });

        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_SILENT);
                Toast.makeText(MainActivity.this, "Now in silent mode", Toast.LENGTH_LONG).show();
            }
        });

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = myAudioManager.getRingerMode();
                if (mode == RINGER_MODE_NORMAL) {
                    Toast.makeText(MainActivity.this, "Now in Normal Mode",
                            Toast.LENGTH_LONG).show();
                } else if (mode == RINGER_MODE_VIBRATE) {
                    Toast.makeText(MainActivity.this, "Now in Vibrate Mode",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Now in Silent Mode",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMuteSound();
            }
        });

        maxVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMaxVolume();
            }
        });

        volUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currVol = volumeChange.getProgress();
                if (currVol < volumeMax) {
                    currVol += volumeMax/(volumeChange.getMax());
                    volumeChange.setProgress(currVol);
                    myAudioManager.setStreamVolume(STREAM_MUSIC, currVol, 0);
                }
            }
        });

        volDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currVol = volumeChange.getProgress();
                if (currVol > volumeMin) {
                    currVol -= volumeMax/(volumeChange.getMax());
                    volumeChange.setProgress(currVol);
                    myAudioManager.setStreamVolume(STREAM_MUSIC,currVol,0);
                }
            }
        });
    }

    private void requestMuteSound(){
        volumeChange.setProgress(volumeMin);
        myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumeMin,0);
    }

    private void requestMaxVolume(){
        volumeChange.setProgress(volumeMax);
        myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumeMax, 0);
    }

    private void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch (SecurityException e) {

        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, ON_DO_NOT_DISTURB_CALLBACK_CODE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ON_DO_NOT_DISTURB_CALLBACK_CODE) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }
}
