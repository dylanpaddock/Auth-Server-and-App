package com.example.dylan.appwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void sendSignupToServer(View view) {
        //get information from text fields
        String username = ((EditText)findViewById(R.id.signupUsernameText)).getText().toString();
        String password = ((EditText)findViewById(R.id.signupPasswordText)).getText().toString();
        //send to server for verification (ideally encrypted, salt+hash, etc.

        //if server comes back with true, start next activity
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
