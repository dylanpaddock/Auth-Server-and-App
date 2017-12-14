package com.example.dylan.appwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void sendLoginStart(View view) {
        if (view.getId() == R.id.loginButton) {
            //start login activity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }else {
            throw new RuntimeException("Unknown button id");
        }

    }

    public void sendSignupStart(View view) {
        if (view.getId() == R.id.signupButton) {
            //start login activity
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }else {
            throw new RuntimeException("Unknown button id");
        }

    }
}
