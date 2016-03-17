package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONObject;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by juggernaut on 17/3/16.
 */
public class Aadhar extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);

        //myTextView = (TextView) findViewById(R.id.exampleTextView);
    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Utilities.message(this,text);
        String name = getIntent().getExtras().getString("name","");
        String email = getIntent().getExtras().getString("email","");
        String phone = getIntent().getExtras().getString("phone","");
        Intent i = new Intent(this, Sign_up.class);
        i.putExtra("QR",text);
        i.putExtra("name",name);
        i.putExtra("email",email);
        i.putExtra("phone",phone);
        startActivity(i);
        //myTextView.setText(text);
    }


    // Called when your device have no camera
    @Override
    public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    {
    }
}
