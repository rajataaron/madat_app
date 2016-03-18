package com.example.juggernaut.police;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import java.io.IOException;

/**
 * Created by juggernaut on 18/3/16.
 */
public class Emergency extends Activity {

    Vibrator v;
    AssetFileDescriptor descriptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            descriptor = getAssets().openFd("siren.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        song();
    }
    private void song(){

        vibrate();
        MediaPlayer player = new MediaPlayer();

        long start = descriptor.getStartOffset();
        long end = descriptor.getLength();

        try {
            player.setDataSource(this.descriptor.getFileDescriptor(), start, end);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        vibrate();
        player.setVolume(1.0f, 1.0f);
        player.start();

    }
    public void vibrate(){

        // Vibrate for 500 milliseconds
        v.vibrate(30000);
    }
}
