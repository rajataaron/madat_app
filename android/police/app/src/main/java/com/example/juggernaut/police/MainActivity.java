package com.example.juggernaut.police;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.os.Vibrator;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    GPSTracker gps;
    Vibrator v;
    AssetFileDescriptor descriptor;
    private final int WAIT_TIME = 2500;

    String latitude=null,longitude=null;
    static String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gps = new GPSTracker(MainActivity.this);
        imei = getIMEI();
        check();
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            descriptor = getAssets().openFd("siren.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // callAsynchronousTask();

        //vibrate();
        // check if GPS enabled
        //song();


        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
              //  check();
            }
        }, 5000,10000);
    }

    public void check(){
        try {
            if (gps.canGetLocation()) {

                latitude = String.valueOf(gps.getLatitude());
                longitude = String.valueOf(gps.getLongitude());

                // \n is for new line
               // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        //String Imei = getIMEI();
        JsonObject json = new JsonObject();
        json.addProperty("xCo", latitude);
        json.addProperty("yCo", longitude);
        json.addProperty("imei",getIMEI());
        json.addProperty("req", "update");
        Log.d("juggernaut",json.toString());
        Ion.with(this)
                .load("http://192.168.0.2:8888/hack/")
                .addHeader("Content-Type", "application/json")
                .setStringBody(String.valueOf(json))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("juggernaut", result);
                      /*  song();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=37.423156,-122.084917 (" + "test" + ")"));
                        startActivity(intent);*/

                    }
/*
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error

                        try {
                            String state = Utilities.json_clean(result.get("state").toString());


                            //Toast.makeText(getBaseContext(), String.valueOf(result), Toast.LENGTH_LONG).show();
                            if (state == "1") {

                            } else {

                                String cordinate = Utilities.json_clean(result.get("cordinate").toString());
                                Intent intent = new Intent(getApplicationContext(), Emergency.class);
                                intent.putExtra("value", cordinate);
                                startActivity(intent);
                            }
                            Log.d("juggernaut", result.toString());
                        } catch (Exception err) {
                            Log.d("juggernaut", String.valueOf(err));
                        }

                    }*/
                });
    }

    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
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








