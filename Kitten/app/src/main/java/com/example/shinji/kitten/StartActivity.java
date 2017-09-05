package com.example.shinji.kitten;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.GetImageTaskUpload;
import com.example.shinji.kitten.util.User;
import com.example.shinji.kitten.login.LoginActivity;
import com.example.shinji.kitten.login.RegisterActivity;
import com.example.shinji.kitten.util.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/*
1. check whether login or not
2. get the location of user
3. search the user's country

 */

public class StartActivity extends Activity implements View.OnClickListener, LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    private SecureRandom random = new SecureRandom();
    public static final String LOG_TAG = "volley_test";
    private RequestQueue mQueue;
    private LocationManager locationManager;
    private final String TAG = "StartActivity";
    private Button btnLoginTo;
    private Button btnRegisterTo;
    private Button btnGithub;
    private Button btnSignOut;
    private WebView webView;
    private FirebaseController firebaseController;
//    private GithubApp mApp;
    private ProgressDialog pd;

//    private static final String REDIRECT_URL_CALLBACK = "melardev://git.oauth2token";
    private static final String REDIRECT_URL_CALLBACK = "https://programming-473ea.firebaseapp.com/__/auth/handler";
    public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseAuth.getInstance().signOut();

        pd = new ProgressDialog(this);
        firebaseController = FirebaseController.getInstance();
        storageRef = storage.getReference();

        btnLoginTo = (Button) findViewById(R.id.btnLoginTo);
        btnLoginTo.setOnClickListener(this);
        btnRegisterTo = (Button) findViewById(R.id.btnRegisterTo);
        btnGithub = (Button) findViewById(R.id.btnGithub);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnRegisterTo.setOnClickListener(this);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getQueryParameter("code") != null
                        && uri.getScheme() != null
                        && uri.getScheme().equalsIgnoreCase("https")) {

                    String code = uri.getQueryParameter("code");
                    String state = uri.getQueryParameter("state");
                    sendPost(code, state);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

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
            System.out.println("^^onCreate:User is signed in");
            User userdata = firebaseController.getUserData();
            if(userdata != null){
                System.out.println("^^userdata:User aru");
                gotoNextIntent();
            }
        } else {
            System.out.println("^0^:No user is ");
        }

        //Called after the github server redirect us to REDIRECT_URL_CALLBACK
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URL_CALLBACK)) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            if (code != null && state != null)
                sendPost(code, state);
        }


        btnGithub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pd.show();
                // Github auth
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme("http")
                        .host("github.com")
                        .addPathSegment("login")
                        .addPathSegment("oauth")
                        .addPathSegment("authorize")
                        .addQueryParameter("client_id", Config.GITHUB_ID)
                        .addQueryParameter("redirect_uri", REDIRECT_URL_CALLBACK)
                        .addQueryParameter("state", getRandomString())
                        .addQueryParameter("scope", "user,user:email")
                        .build();
                Log.d(TAG, httpUrl.toString());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()));
//                startActivity(intent);

                webView.loadUrl(httpUrl.toString());

            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        locationStart();
    }



    private void sendPost(String code, String state) {
        //POST https://github.com/login/oauth/access_token
        System.out.println("sendPostきtア？");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody form = new FormBody.Builder()
                .add("client_id", Config.GITHUB_ID)
                .add("client_secret", Config.GITHUB_SECRET)
                .add("code", code)
                .add("redirect_uri", REDIRECT_URL_CALLBACK)
                .add("state", state)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(form)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(StartActivity.this, "onFailure: " + e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseBody = response.body().string();
                String[] splitted = responseBody.split("=|&");
                if (splitted[0].equalsIgnoreCase("access_token")) {
                    Log.d(TAG, "onResponse:" + splitted[1]);
                    signInWithToken(splitted[1]);
                }else
                    Toast.makeText(StartActivity.this, "splitted[0] =>" + splitted[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signInWithToken(String token) {
        Log.d(TAG, "signInWithToken:" + token);
        AuthCredential credential = GithubAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();
                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(StartActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }else{
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userData = firebaseController.getUserData();
                            final String userIDRes = userData.userID;
                            if(userData != null){
                                Log.d(TAG, "isSuccessful:" + userData.userID);
                                usersRef = database.getReference("users/" + userData.userID);
                                firebaseController.writeUserToData(usersRef);

                                // save images
                                GetImageTaskUpload myTask = new GetImageTaskUpload();
                                myTask.setOnCallBack(new GetImageTaskUpload.CallBackTask(){

                                    @Override
                                    public void CallBack(Bitmap bitmap) {
                                        super.CallBack(bitmap);
                                        // ※１
                                        // resultにはdoInBackgroundの返り値が入ります。
                                        // ここからAsyncTask処理後の処理を記述します。
                                        Log.i("AsyncTaskCallback", "非同期処理が終了しました。");

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] data = baos.toByteArray();


                                        StorageReference userImagesRef = storageRef.child("images/" + userIDRes + "/profile.jpg");
                                        UploadTask uploadTask = userImagesRef.putBytes(data);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                System.out.println("onFailure:Handle unsuccessful uploads");
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                System.out.println("onSuccess:UploadTask");
                                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            }
                                        });

                                    }

                                });
                                pd.hide();
                                myTask.execute(userData.photourl);
                                gotoNextIntent();
                            }
                        }
                    }
                });
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

    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
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

    private void gotoNextIntent(){
        Intent nextItent;
        nextItent = new Intent(StartActivity.this, BaseActivity.class);
        startActivity(nextItent);
    }

    private String getRandomString() {
        return new BigInteger(130, random).toString(32);
    }
}
