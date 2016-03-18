package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by juggernaut on 17/3/16.
 */
public class Dashboard extends Activity {

    Toolbar toolbar;
    String token = "327811660516";
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawer();
        gps = new GPSTracker(Dashboard.this);
        try{
            token = getIntent().getExtras().getString("value");
        }catch (Exception e){
            Log.d("exception", String.valueOf(e));
        }
        Utilities.message(this,token);
    }

    private void drawer() {
        toolbar = (Toolbar) findViewById(R.id.tb);
        toolbar.setTitle("Madad");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Emergency");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Lodge FIR");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("Check Status");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("Exit");
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerbackground).build();



        Drawer result = new DrawerBuilder().withToolbar(toolbar).withAccountHeader(headerResult).withActivity(Dashboard.this).addDrawerItems(item1, item2, item3, item4).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                switch (i) {
                    case 1:
                        emergency();
                        break;
                    case 3:
                        checkStatus();
                        break;
                    case 4:
                        finish();
                        System.exit(0);
                        break;
                    case 2:
                        lodgeFir();
                        break;
                }
                return false;
            }
        }).build();

    }

    private void checkStatus() {

        Intent intent = new Intent(this,GetStatus.class);
        intent.putExtra("value",token);
        startActivity(intent);
    }

    private void emergency(){
        Intent intent = new Intent(this,Dashboard.class);
        intent.putExtra("value",token);
        startActivity(intent);
    }

    private void lodgeFir(){
        Intent intent = new Intent(this,LodgeFir.class);
        intent.putExtra("value",token);
        startActivity(intent);
    }

    private String getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public void emergency (View view){

        String latitude = null,longitude = null;
        try {
            if (gps.canGetLocation()) {

                latitude = String.valueOf(gps.getLatitude());
                longitude = String.valueOf(gps.getLongitude());

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        JsonObject json = new JsonObject();
        json.addProperty("xCo", latitude);
        json.addProperty("yCo", longitude);
        json.addProperty("aadhar",token);
        //json.addProperty("qrCode", QR);
        //json.addProperty("imei", getIMEI());
        json.addProperty("req", "emergency");
        Log.d("juggernaut", String.valueOf(json));
        Ion.with(getBaseContext())
                .load("http://192.168.0.2:8888/hack/")
                .addHeader("Content-Type", "application/json")
                .setStringBody(String.valueOf(json))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        //Utilities.message(getBaseContext(), String.valueOf(result));
                        Log.d("juggernaut", String.valueOf(result));
                        Toast.makeText(getBaseContext(),"Help is on the way",Toast.LENGTH_LONG).show();
                    }

                });

    }
}
