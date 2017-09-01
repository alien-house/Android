package com.example.shinji.kitten;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shinji.kitten.dashboard.User;
import com.example.shinji.kitten.login.LoginActivity;
import com.example.shinji.kitten.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/*
1. check whether login or not
2. get the location of user
3. search the user's country

 */

public class StartActivity extends Activity implements View.OnClickListener, LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String LOG_TAG = "volley_test";
    private RequestQueue mQueue;
    private LocationManager locationManager;
    private final String TAG = "onAuthStateChanged";
    private Button btnLoginTo;
    private Button btnRegisterTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // -----------------------------
        System.out.println("==========geocoderisPresent==========");
        System.out.println("==========geocoder==========");


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

        locationStart();
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");

                locationStart();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void locationStart(){

        Log.d("debug","locationStart()");

        // LocationManager インスタンス生成
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // GPSを設定するように促す
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        } else {
            Log.d("debug", "gpsEnabled");
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
//            textView1.setText("Latirsetsetude:"+location.getLatitude());
//            textView2.setText("Longrrrrrrrrrit:"+location.getLongitude());
        }else{
            Log.d("location", "location nullllllllllll");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        mQueue = Volley.newRequestQueue(this);

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
        locationManager.removeUpdates(this);
        Log.d("onLocation--======--", "onLocationChanged");
        // 緯度の表示
//        textView1.setText("Latitude:"+location.getLatitude());

        // 経度の表示
//        textView2.setText("Longit:"+location.getLongitude());
        Log.d("onLocationChanged:", "onLocationChanged==============");


        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                location.getLatitude() + "," + location.getLongitude() +
                "&key=AIzaSyCOvdiOD-i_OlyLIjrN8fJQp96P3gFYX6Q";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse::","====================");
//                        Log.d(LOG_TAG, response.toString());
                        try {
//                            Log.d(LOG_TAG, "Value: " + response.getString("query"));
                            JSONArray itemArray = response.getJSONArray("results");

//                            JSONObject rootObject = response.getJSONObject("results");
//                            JSONObject rootObject = response.getJSONObject("results");
                            JSONObject rootObject = itemArray.getJSONObject(0);

                            JSONArray location_array = rootObject.getJSONArray("address_components");

                            Log.d(LOG_TAG, "results****: " + location_array);

//                            rootObject["address_components"];
                            makeDataToListview(location_array);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error::","~~~~~~~~~~~~~~~~~~~~~~");
                        Log.d(LOG_TAG, error.toString());
//                        progressDialog.dismiss();
                    }
                }
        );
        mQueue.add(jsonObjectReq);

//        if (!Geocoder.isPresent()) {
//            Geocoder coder = new Geocoder(this, Locale.getDefault());
//            try {
//                List<Address> addresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                Log.d("location", addresses.get(0).toString());
//                Log.d("location", addresses.get(0).getCountryCode().toString());
//            } catch (IOException e) {
//                Log.d("onLocationChanged:", "IOException==============");
//                e.printStackTrace();
//            }
//        };
        Log.d("onLocationChanged:", "@@==============");

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


    public void makeDataToListview(JSONArray rootObject){
        System.out.println("=========makeDataToListview=========");
        ArrayList<LocationData> locationDatas = new ArrayList();

        String test = null;
        try {
            for (int i = 0 ; i < rootObject.length(); i++) {
                test = rootObject.getString(i);
                LocationData ld = new Gson().fromJson(test, LocationData.class);
                locationDatas.add(ld);
                Log.d(LOG_TAG, "ldmaktview****: " + ld.types[0] );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] string = {"country"};
        LocationData l = new LocationData("", "", string);
        Collections.sort(locationDatas, LocationData.getLocationDataComparable());
        int index = Collections.binarySearch(locationDatas, l);
        User.USER_COUNTRY = locationDatas.get(index).short_name;
        Log.d(LOG_TAG, "indexindex: " + locationDatas.get(index).short_name);

        System.out.println("obj^^^^^^^-----------");

    }


    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
