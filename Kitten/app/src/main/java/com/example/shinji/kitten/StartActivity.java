package com.example.shinji.kitten;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shinji.kitten.login.LoginActivity;
import com.example.shinji.kitten.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends Activity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final String TAG = "onAuthStateChanged";
    private Button btnLoginTo;
    private Button btnRegisterTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

}
