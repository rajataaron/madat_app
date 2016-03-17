package com.example.juggernaut.decepticon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by juggernaut on 17/3/16.
 */
public class Sign_up extends Activity{

    String QR=null,name="",email="",phone="";
    EditText inputName, inputEmail, inputPassword, inputPasswordAgain, inputPhone;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutPasswordAgain, inputLayoutPhone;
    Button btnCreateOwner, btnAadhar;
    ProgressDialog prgDialog;
    String qrText = "Scan Aadhar QR code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        try {
            QR = getIntent().getExtras().getString("QR",null);
            name = getIntent().getExtras().getString("name","");
            email = getIntent().getExtras().getString("email","");
            phone = getIntent().getExtras().getString("phone","");
        } catch (Exception e) {
            e.printStackTrace();
        }

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputName = (EditText) findViewById(R.id.input_name);
        inputPhone = (EditText) findViewById(R.id.input_phone);

        inputEmail.setText(email);
        inputName.setText(name);
        inputPhone.setText(phone);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        btnAadhar = (Button) findViewById(R.id.btn_aadhar);
        if (QR != null){
            btnAadhar.setText("aadhar scaned");
        }
        btnAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),Aadhar.class);
                intent.putExtra("name",inputName.getText().toString());
                intent.putExtra("email",inputEmail.getText().toString());
                intent.putExtra("phone",inputPhone.getText().toString());
                startActivity(intent);
            }
        });

        btnCreateOwner = (Button) findViewById(R.id.btn_signup);
        btnCreateOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    submitForm();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() throws UnsupportedEncodingException, JSONException {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }


        if (!validatePhone()) {
            return;
        }


        if (btnAadhar.getText().toString().equals(qrText)){
            Utilities.message(this,btnAadhar.getText().toString());
        }else {
            Log.d("juggernaut", btnAadhar.getText().toString().trim());
            Log.d("juggernaut", QR);
/*
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("juggernaut", sharedpreferences.getString("Token", null));
        String token = "Token "+sharedpreferences.getString("Token", null);
        Log.d("juggernaut",token);
*/
            Ion.with(getBaseContext())
                    .load("http://192.168.0.3:8888/hack/form.php")
//                .addHeader("Authorization", token)
                    .setMultipartParameter("email", inputEmail.getText().toString().trim())
                    .setMultipartParameter("name", inputName.getText().toString().trim())
                    .setMultipartParameter("phone", inputPhone.getText().toString().trim())
                    .setMultipartParameter("qr",QR)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            Log.d("juggernaut", String.valueOf(result));
                            Toast.makeText(getBaseContext(), String.valueOf(result), Toast.LENGTH_LONG).show();

                        }
                    });


        }

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateQR() {
        if (btnAadhar.getText().toString().trim()=="Scan Aadhar QR code") {
            Utilities.message(this,"No Aadhar detail added");
            return false;
        }


        return true;
    }

    private boolean validatePhone() {
        String phone = inputPhone.getText().toString().trim();
        if (phone.isEmpty() || phone.length()!=10 ) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
            }
        }
    }

}
