package com.example.shinji.kitten.dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.example.shinji.kitten.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by shinji on 2017/08/27.
 */

public class SettingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView texthello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_setting);
        texthello = (TextView) findViewById(R.id.texthello);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("^^:User is signed in~~~~");

            if (user != null) {
                String name = user.getDisplayName();
                String email = user.getEmail();
                System.out.println(name);
                System.out.println(email);
                if(name != null){
                    texthello.setText("Hello! " + name);
                }
            }

        } else {
            // No user is signed in
            System.out.println("^0^:No user is ");
        }
    }

}
