package com.example.shinji.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    TextView status;
    LoginButton loginBtn;

    private Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //before setContentView!
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.txt_status);
        loginBtn = (LoginButton) findViewById(R.id.btn_login);
        loginBtn.setReadPermissions("public_profile");
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                status.setText("Login Successfull\n" + loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                status.setText("Login cancel!!");
            }

            @Override
            public void onError(FacebookException error) {
                status.setText("Login Error!!" + error.getMessage());
            }
        });


/*
        b1 = (Button) findViewById(R.id.btn_share);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri img = Uri.parse("android.resource://com.example.shinji.facebook/*");
                try {
                    InputStream is = getContentResolver().openInputStream(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, img);
                startActivity(Intent.createChooser(shareIntent, "SHARING USING"));

            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
