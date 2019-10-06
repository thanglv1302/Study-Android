package com.example.levietthang.studyandroid;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

public class MainActivity extends Activity {
    Button mode, ring, vibrate, silent;
    private AudioManager myAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = (Button) findViewById(R.id.btnMode);
        ring = (Button) findViewById(R.id.btnRing);
        vibrate = (Button) findViewById(R.id.btnVib);
        silent = (Button) findViewById(R.id.btnSilent);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_NORMAL);
                Toast.makeText(MainActivity.this,"Now in normal mode",Toast.LENGTH_LONG).show();
            }
        });

        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_VIBRATE);
                Toast.makeText(MainActivity.this,"Now in vibrate mode",Toast.LENGTH_LONG).show();
            }
        });

        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioManager.setRingerMode(RINGER_MODE_SILENT);
                Toast.makeText(MainActivity.this,"Now in silent mode",Toast.LENGTH_LONG).show();
            }
        });

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = myAudioManager.getRingerMode();
                if( mode == RINGER_MODE_NORMAL){
                    Toast.makeText(MainActivity.this,"Now in Normal Mode",
                            Toast.LENGTH_LONG).show();
                }
                else if(mode == RINGER_MODE_VIBRATE){
                    Toast.makeText(MainActivity.this,"Now in Vibrate Mode",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Now in Silent Mode",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
