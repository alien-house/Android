package com.example.shinji.kitten;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest.permission;

import com.example.shinji.kitten.login.LoginActivity;
import com.example.shinji.kitten.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class StartActivity extends Activity implements View.OnClickListener, LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "onAuthStateChanged";
    private Button btnLoginTo;
    private Button btnRegisterTo;

    private LocationManager locationManager;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 位置測定を始めるコードへ跳ぶ
                locationStart();
                return;
            }
        }
    }

    private void locationStart(){

        Log.d("debug","locationStart()");

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // GPSを設定するように促す
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        } else {
            Log.d("debug", "gpsEnabled");
        }

        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // -----------------------------
        System.out.println("==========geocoderisPresent==========");
            System.out.println("==========geocoder==========");

//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_PERMISSION);
//
//            return;
//        }


//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                return;
//            }
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            System.out.println(longitude);
//            System.out.println(latitude);
//// ネットワークアクセスが発生するので、UIスレッド以外で動かすこと
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(
//                        location.getLatitude(), location.getLongitude(), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//// リバースジオコーディングに失敗したときは、 addresses がnullもしくは空リストになる
//
//            Log.d("location", addresses.get(0).toString()); //Address[addressLines=[0:"日本",1:"〒100-8111 東京都千代田区千代田１ー１"...
//
//            System.out.println(geocoder);

        if (!Geocoder.isPresent()) {
        }

        // -----------------------------





        btnLoginTo = (Button) findViewById(R.id.btnLoginTo);
        btnLoginTo.setOnClickListener(this);
        btnRegisterTo = (Button) findViewById(R.id.btnRegisterTo);
        btnRegisterTo.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        // If login, go to tab contents
/*
*/
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("^^:User is signed in");
            Intent nextItent;
            nextItent = new Intent(StartActivity.this, BaseActivity.class);
            startActivity(nextItent);
        } else {
            // No user is signed in
            System.out.println("^0^:No user is ");
        }
    }
//
//    public void signIn(String email, String password){
//
//        if (!validateForm()) {
//            return;
//        }
//
//        System.out.println("アッレレレ");
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
//                            Toast.makeText(StartActivity.this, "auth_failed",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent nextItent;
        if (i == R.id.btnLoginTo) {
            nextItent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(nextItent);
        } else if (i == R.id.btnRegisterTo) {
            nextItent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(nextItent);
        }
//        if (i == R.id.email_create_account_button) {
//            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
//        } else if (i == R.id.btnLogin) {
//            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
//        } else if (i == R.id.sign_out_button) {
//            signOut();
//        } else if (i == R.id.verify_email_button) {
//            sendEmailVerification();
//        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }





    @Override
    public void onLocationChanged(Location location) {

        // 緯度
        Log.d("debug","Latitude:"+location.getLatitude());

        // 経度
        Log.d("debug","Latitude:"+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
