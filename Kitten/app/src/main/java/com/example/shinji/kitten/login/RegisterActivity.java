package com.example.shinji.kitten.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shinji.kitten.R;
import com.example.shinji.kitten.dashboard.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by shinji on 2017/08/27.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth mAuth;
    private final String TAG = "onAuthStateChanged";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUsernameField;
    private Button btnRegister;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        mEmailField = (EditText) findViewById(R.id.editTextUser);
        mPasswordField = (EditText) findViewById(R.id.editTextPass);
        mUsernameField = (EditText) findViewById(R.id.editTextName);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage("loading");

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnRegister) {
//            System.out.println("dddddddddddss");
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(), mUsernameField.getText().toString());
        }
    }


    private void createAccount(String email, String password, final String username) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        pd.show();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");

                                                Intent nextItent;
                                                nextItent = new Intent(RegisterActivity.this, SettingActivity.class);
                                                startActivity(nextItent);
                                            }
                                        }
                                    });

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        pd.hide();
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String username = mUsernameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
}
