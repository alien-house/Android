package com.example.shinji.gps;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements LocationListener{

    public static final String LOG_TAG = "volley_test";
    private RequestQueue mQueue;
    private LocationManager locationManager;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.text_view1);
        textView2 = (TextView) findViewById(R.id.text_view2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else{
            locationStart();
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            textView1.setText("Latirsetsetude:"+location.getLatitude());
            textView2.setText("Longrrrrrrrrrit:"+location.getLongitude());
        }else{
            Log.d("location", "location nullllllllllll");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        mQueue = Volley.newRequestQueue(this);

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
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        Log.d("onLocation--======--", "onLocationChanged");
        // 緯度の表示
        textView1.setText("Latitude:"+location.getLatitude());

        // 経度の表示
        textView2.setText("Longit:"+location.getLongitude());
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


    public void makeDataToListview(JSONArray rootObject){
        System.out.println("=========makeDataToListview=========");
        ArrayList<LocationData> locationDatas = new ArrayList();


        String test = rootObject.toString();
        LocationData ld = new Gson().fromJson(test, LocationData.class);
        Log.d(LOG_TAG, "ldmakeDataToListview****: " + ld);

//        try {
//            for (int i = 0 ; i < rootObject.length(); i++) {
//
//
////                    rootObject.get(i).getString("long_name");
////                    JsonReader jsonReader = new JsonReader( rootObject );
////                if(Arrays.asList(rootObject.get(i).toString()).contains("long_name")){
////                    System.out.println("long_name - Found!");
////                }
////                else{
////                    System.out.println("long_name - Not Found!");
////                    Log.d(LOG_TAG, "makeDataToListview****: " + rootObject.get(i));
////                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



//        int maxNum = rootObject.length();

//        JSONObject[] obj = new JSONObject[maxNum];
//        JSONArray objAC = new JSONArray();
//        try {
//            JSONObject bodyObject = rootObject.getJSONObject("address_components");
//
//            System.out.println(bodyObject);
//
//            for (int i = 0 ; i < maxNum; i++) {
////                obj[i] = rootObject.getJSONObject(i);
////                obj = rootObject.getJSONObject(i);
////                Log.d(LOG_TAG, obj.getString("address_components"));
////                addList(obj);
//            }
////            objAC = obj.getJSONArray("address_components");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        System.out.println("obj^^^^^^^-----------");

//        for (int i = 0; i < obj.length; i++) {
//            System.out.println(obj[i]);
//        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocationData{
        String long_name;
        String short_name;
        String[] types;
        LocationData(String long_name, String short_name, String[] types){
            long_name = long_name;
            short_name = short_name;
            types = types;
        }
    }
}
