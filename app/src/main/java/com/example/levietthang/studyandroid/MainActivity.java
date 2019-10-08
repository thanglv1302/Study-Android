package com.example.levietthang.studyandroid;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

public class MainActivity extends Activity {
    private static final int ON_DO_NOT_DISTURB_CALLBACK_CODE = 0;
    Button mode, ring, vibrate, silent;
    private AudioManager myAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestMutePermissions();

        mode = (Button) findViewById(R.id.btnMode);
        ring = (Button) findViewById(R.id.btnRing);
        vibrate = (Button) findViewById(R.id.btnVib);
        silent = (Button) findViewById(R.id.btnSilent);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
