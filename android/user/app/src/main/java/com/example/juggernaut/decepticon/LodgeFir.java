package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by juggernaut on 17/3/16.
 */
public class LodgeFir extends Activity {

    String[] items = new String[]{"Theft", "Rape", "Accident", "illegal"};
    Spinner bTypeOfCrime;
    GPSTracker gps;
    Toolbar toolbar;
    private static int RESULT_LOAD_IMG = 1;
    Button attach_img;
    String encodedImage = null,token,bal;
    EditText desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lodge_fir);
        gps = new GPSTracker(LodgeFir.this);
        bTypeOfCrime = (Spinner) findViewById(R.id.bTypeCrime);
        desc = (EditText) findViewById(R.id.et_desc);
        try{
            token = getIntent().getExtras().getString("value");
        }catch (Exception e){
            Log.d("exception", String.valueOf(e));
        }
        attach_img = (Button) findViewById(R.id.btn_attach_image);
        drawer();
        dropDownMenu();
    }

    private void dropDownMenu(){
        bTypeOfCrime = (Spinner) findViewById(R.id.bTypeCrime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        bTypeOfCrime.setAdapter(adapter);

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



        Drawer result = new DrawerBuilder().withToolbar(toolbar).withAccountHeader(headerResult).withSelectedItemByPosition(2).withActivity(LodgeFir.this).addDrawerItems(item1, item2, item3, item4).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                String path = getRealPathFromURI(selectedImage);

                Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte [] ba = bao.toByteArray();
                bal=Base64.encodeToString(ba, Base64.DEFAULT);
                Log.d("juggernaut",bal);
                attach_img.setText("Image Attached");

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void submit(View view){

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
        json.addProperty("aadhar", token);
        json.addProperty("sub",bTypeOfCrime.getSelectedItem().toString());
        json.addProperty("msg",desc.getText().toString());
        json.addProperty("img",bal);

        //json.addProperty("qrCode", QR);
        //json.addProperty("imei", getIMEI());
        json.addProperty("req", "fir");
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
                        Toast.makeText(getBaseContext(),"Help is on the way",Toast.LENGTH_LONG).show();
                        Log.d("juggernaut", String.valueOf(result));
                    }

                });
    }


}
