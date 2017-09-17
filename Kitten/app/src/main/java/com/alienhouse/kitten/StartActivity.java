package com.alienhouse.kitten;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.alienhouse.kitten.dashboard.SettingInfoFragment;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.GetImageTaskUpload;
import com.alienhouse.kitten.util.User;
import com.alienhouse.kitten.login.LoginActivity;
import com.alienhouse.kitten.login.RegisterActivity;
import com.alienhouse.kitten.util.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;

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
import rx.Observer;

/*
1. check whether login or not
2. get the location of user
3. search the user's country

 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

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
    private WebView webView;
    private FirebaseController firebaseController;
    private User userData;

    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;
    private static HttpUrl httpUrl;
//    private GithubApp mApp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseAuth.getInstance().signOut();

        //
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        boolean bool = pref.getBoolean("activity_executed",false);
        if(!bool){
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean("activity_executed", true);
        ed.apply();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Start");
        firebaseController = FirebaseController.getInstance();
        storageRef = storage.getReference();

        btnLoginTo = (Button) findViewById(R.id.btnLoginTo);
        btnLoginTo.setOnClickListener(this);
        btnRegisterTo = (Button) findViewById(R.id.btnRegisterTo);
        btnGithub = (Button) findViewById(R.id.btnGithub);
        btnRegisterTo.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                User userdata = firebaseController.getUserData();
                if (user != null) {
                    // User is signed in
                    firebaseController.setUserToData(user);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    firebaseController.deleteUserData();
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            System.out.println("^^onCreate:User is signed in");
            User userdata = firebaseController.getUserData();
            if(userdata != null){
                System.out.println("^^userdata:User aru");
                gotoNextIntent();
            }
        } else {
            System.out.println("No user is ");
        }

        //Called after the github server redirect us to REDIRECT_URL_CALLBACK
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(Config.CALLBACK_URL)) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            if (code != null && state != null)
                sendPost(code, state);
        }

        // 1) Github : generating URL
        btnGithub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                // Github auth
                httpUrl = new HttpUrl.Builder()
                        .scheme("http")
                        .host("github.com")
                        .addPathSegment("login")
                        .addPathSegment("oauth")
                        .addPathSegment("authorize")
                        .addQueryParameter("client_id", Config.GITHUB_ID)
                        .addQueryParameter("redirect_uri", Config.CALLBACK_URL)
                        .addQueryParameter("state", getRandomString())
                        .addQueryParameter("scope", "user,user:email")
                        .build();
                Log.d(TAG, httpUrl.toString());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl.toString()));
//                startActivity(intent);
//                webView.loadUrl(httpUrl.toString());

                flagmentManager = getSupportFragmentManager();
                dialogFragment = new AlertDialogFragment();
                dialogFragment.show(flagmentManager, "test alert dialog");

            }
        });

        locationStart();
    }


    // 3) Github : requesting token
    private void sendPost(String code, String state) {
        //POST https://github.com/login/oauth/access_token
        System.out.println("sendPostきtア？");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody form = new FormBody.Builder()
                .add("client_id", Config.GITHUB_ID)
                .add("client_secret", Config.GITHUB_SECRET)
                .add("code", code)
                .add("redirect_uri", Config.CALLBACK_URL)
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
                    requestGitHubUserData(splitted[1]);
                }else
                    Toast.makeText(StartActivity.this, "splitted[0] =>" + splitted[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 5) Github :  auth using token
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
                            Toast.makeText(StartActivity.this, "GitHub Authentication failed.", Toast.LENGTH_SHORT).show();
                        }else{
                            //ここで初めてfirebaseのユーザーが取れる。
                            FirebaseUser user = mAuth.getCurrentUser();

                            //ここでfirebase Auth と databaseの同期する：githubの場合
                            usersRef = database.getReference("users/" + user.getUid());
                            firebaseController.getUserDataOnceEventListener(usersRef);
                            Log.d(TAG, "FirebaseUser写真URL取れてる？:" + user.getPhotoUrl());

                            firebaseController.setOnCallBackNormal(new FirebaseController.CallBackTaskNormal(){
                            @Override
                            public void CallBack() {
                                super.CallBack();
                                Log.d("BaseActivity:", "CallBack: " + "多分終わらん＝＝＝＝＝＝＝＝");

                                userData = firebaseController.getUserData();
                                //ここでコールバックしないと、時差がある。。

                                final String userIDRes = userData.userID;
                                Log.d(TAG, "userData写真URL取れてる？:" + userData.photourl);
                                Log.d(TAG, "userData写真URL取れてる？:" + userData.created);
                                userData.userID = userIDRes;
                                if(userData != null){
                                    Log.d(TAG, "isSuccessful:" + userData.userID);

                                    if(userData.created == 0){
                                        // save images
                                        Log.i("signInWithCredential", "一回だけですよ〜");
                                        GetImageTaskUpload myTask = new GetImageTaskUpload();
                                        myTask.setOnCallBack(new GetImageTaskUpload.CallBackTask(){

                                            @Override
                                            public void CallBack(Bitmap bitmap) {
                                                super.CallBack(bitmap);
                                                if(bitmap == null){
                                                    Log.i("AsyncTaskCallback", "Error happend");
                                                }else{
                                                    Log.i("AsyncTaskCallback", "finished");
                                                }

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
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
                                        myTask.execute(userData.photourl);
                                        locationSave();
                                    }
                                    userData.created = 1;

                                    firebaseController.writeUserToData(usersRef);
                                    progressDialog.dismiss();
                                    gotoNextIntent();
                                }

                            }
                            });

                        }
                    }
                });
    }

    private void locationSave(){

        //とりあえずここで保存：国と市のため　初回のみ
        if(!User.USER_COUNTRY.matches("")) {
            userData.country = User.USER_COUNTRY;
        }
        if(!User.USER_LOCATION.matches("")) {
            userData.location = User.USER_LOCATION;
        }
        if(!Double.isNaN(User.USER_LAT)) {
            userData.lat = User.USER_LAT;
            userData.lon = User.USER_LON;
        }

    }

    // 4) Github :  get username by token
    private void requestGitHubUserData( final String accessToken ){
        System.out.println("requestGitHubUserData=====");
        GetAuthUserClient getAuthUserClient = new GetAuthUserClient( accessToken );
        getAuthUserClient
                .observable()
                .subscribe(new Observer<com.alorma.github.sdk.bean.dto.response.User>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("requestGitH:onCompleted=====");
                    }
                    @Override
                    public void onError(Throwable e) {
                        System.out.println("requestGitH:Throwable=====");
                        FirebaseCrash.report( e );
                    }

                    @Override
                    public void onNext(com.alorma.github.sdk.bean.dto.response.User user) {
                        System.out.println("requestGitH:onNext=====");
                        // todo: ここでユーザーデータ取れてないんじゃないの？
//                        User userData = firebaseController.getUserData();
//                        userData.username = user.name;
                        System.out.println("requestGitH:name====="+user.name);
                        System.out.println("requestGitH:avatar_url====="+user.avatar_url);
                        System.out.println("requestGitH:email====="+user.email);
                        System.out.println("requestGitH:bio====="+user.bio);
                        System.out.println("requestGitH:location====="+user.location);
                        System.out.println("requestGitH:accessToken====="+accessToken);
                        signInWithToken(accessToken);
//                        userData.email = user.email;
//                        usersRef = database.getReference("users/" + userData.userID);
//                        firebaseController.writeUserToData(usersRef);

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
                Toast toast = Toast.makeText(this, "It might not get a data appropriately.", Toast.LENGTH_SHORT);
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        mQueue = Volley.newRequestQueue(this);

    }

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
        User.USER_LAT = location.getLatitude();
        User.USER_LON = location.getLongitude();

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                location.getLatitude() + "," + location.getLongitude() +
                "&key=" + Config.GOOGLEAPIS_KEY;

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse::","====================");
//                        Log.d(LOG_TAG, response.toString());
                        try {
                            JSONArray itemArray = response.getJSONArray("results");
                            JSONObject rootObject = itemArray.getJSONObject(0);
                            JSONArray location_array = rootObject.getJSONArray("address_components");
                            Log.d(LOG_TAG, "results****: " + location_array);
                            makeDataToListview(location_array);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error:googleapis:","~~~~~~~~~~~~~~~~~~~~~~");
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
//                e.printStackTrace();
//            }
//        };

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

    // getting country from location with googleapis : coz GPS doesn't work on emulater
    public void makeDataToListview(JSONArray rootObject){
        ArrayList<LocationData> locationDatas = new ArrayList();
        String rootObjString = null;
        try {
            for (int i = 0 ; i < rootObject.length(); i++) {
                rootObjString = rootObject.getString(i);
                LocationData ld = new Gson().fromJson(rootObjString, LocationData.class);
                locationDatas.add(ld);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set country
        String[] string = {"country"};
        LocationData l = new LocationData("", "", string);
        Collections.sort(locationDatas, LocationData.getLocationDataComparable());
        int index = Collections.binarySearch(locationDatas, l);
        User.USER_COUNTRY = locationDatas.get(index).short_name;
        Log.d(LOG_TAG, "indexindex: " + locationDatas.get(index).short_name);

        //set locality
        String[] stringCity = {"locality"};
        LocationData lstringCity = new LocationData("", "", stringCity);
        int indexCity = Collections.binarySearch(locationDatas, lstringCity);
        User.USER_LOCATION = locationDatas.get(indexCity).short_name;
//        System.out.println(User.USER_LOCATION);
//        System.out.println(locationDatas.get(indexCity).short_name);

    }

    private void gotoNextIntent(){
        Intent nextItent;
        nextItent = new Intent(StartActivity.this, BaseActivity.class);
        startActivity(nextItent);
        finish();
    }

    private String getRandomString() {
        return new BigInteger(130, random).toString(32);
    }

    // 2) Github : show web for connecting with Github
    public static class AlertDialogFragment extends DialogFragment {

        private AlertDialog dialog;
        private AlertDialog.Builder alert;
        private CropImageView cropImageView;
        private Button btnCancel;
        private Button btnUpload;

        public static SettingInfoFragment.AlertDialogFragment newInstance() {
            SettingInfoFragment.AlertDialogFragment frag = new SettingInfoFragment.AlertDialogFragment();
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            alert = new AlertDialog.Builder(getActivity());
//            alert.setTitle("Are you going to upload this image?");

            View alertView = getActivity().getLayoutInflater().inflate(R.layout.dialog_login_web_layout, null);
            WebView webview = alertView.findViewById(R.id.webview);
            webview.loadUrl(httpUrl.toString());

            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getQueryParameter("code") != null
                    && uri.getScheme() != null
                    && uri.getScheme().equalsIgnoreCase("https")) {
                String code = uri.getQueryParameter("code");
                String state = uri.getQueryParameter("state");
//                StartActivity startActivity = getActivity();
                    StartActivity activity = (StartActivity) getActivity();
                    activity.sendPost(code, state);
            return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
            }
            });

            alert.setView(alertView);
            dialog = alert.create();
            dialog.show();
            return dialog;
        }
    }
}
