package com.example.shinji.kitten.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.shinji.kitten.R;

/**
 * Created by shinji on 2017/09/01.
 */

public class UserActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String TAG = UserActivity.class.getSimpleName();
    public static final String PREFERENCE = "github_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        System.out.println("UserActivity------------------");
        sharedPreferences = getSharedPreferences(PREFERENCE, 0);
        String oauthToken = sharedPreferences.getString("oauth_token", null);
        Log.d(TAG, "oauth token for github loged in user is :" + oauthToken);
    }

}
