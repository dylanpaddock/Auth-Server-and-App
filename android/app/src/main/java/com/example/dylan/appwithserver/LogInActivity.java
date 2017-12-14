package com.example.dylan.appwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void sendLoginToServer(View view){
        //get information from text fields
        String username = ((EditText)findViewById(R.id.loginUsernameText)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginPasswordText)).getText().toString();
        //send to server for verification (ideally encrypted, salt+hash, etc.

        //if server comes back with true, start next activity
        Intent intent = new Intent(this, AddStringsActivity.class);
        startActivity(intent);

    }

}
