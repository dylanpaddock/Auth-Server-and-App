package com.example.dylan.appwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    protected static String EVENT = "signup";
    protected static String JSON_SUCCESS = "success";
    protected static String AUTH_TOKEN = "token";
    protected static String JSON_TEXT = "strings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void verifySignup(View view) {
        //get information from text fields
        final String username = ((EditText) findViewById(R.id.signupUsernameText)).getText().toString();
        final String password = ((EditText) findViewById(R.id.signupPasswordText)).getText().toString();

        //Verify username/password restrictions.
        //username: length 4+, characters: alphanumeric+_@.
        //password: length 7+, characters: no spaces
        if (!username.matches("^[A-Za-z0-9+@_]{4,}$") && !password.matches("^\\S{7,}$")) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.setMessage(R.string.bad_username_and_password);
            dialogBox.show(getFragmentManager(), "bad_username_and_password");
            return;
        }else if (!username.matches("^[A-Za-z0-9+@_]{4,}$")) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.setMessage(R.string.bad_username);
            dialogBox.show(getFragmentManager(), "bad_username");
            return;
        }else if (!password.matches("^\\S{7,}$")) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.setMessage(R.string.bad_password);
            dialogBox.show(getFragmentManager(), "bad_password");
            return;
        }


        //disable button to prevent additional server queries
        findViewById(R.id.signupButton2).setClickable(false);
        //setup request to server
        String url = getResources().getString(R.string.url) + EVENT;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("server response: ", response);
                    JSONObject j = new JSONObject(response);
                    if (j.getBoolean(JSON_SUCCESS)) {
                        //Get authentication information and list of strings for next activity
                        //If server comes back with true, start next activity
                        Intent intent = new Intent(SignUpActivity.this, AddStringsActivity.class);
                        intent.putExtra("token", j.getString(AUTH_TOKEN));
                        intent.putExtra("strings", j.getString(JSON_TEXT));
                        startActivity(intent);
                    }else{
                        DialogBox dialogBox = new DialogBox();
                        dialogBox.setMessage(R.string.username_exists);
                        dialogBox.show(getFragmentManager(), "username_exists");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //enable button
                findViewById(R.id.signupButton2).setClickable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogBox dialogBox = new DialogBox();
                dialogBox.setMessage(R.string.volley_error);
                dialogBox.show(getFragmentManager(), "volley_error");
                findViewById(R.id.signupButton2).setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Authenticate and send input string
                params.put("event", EVENT);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        //queue up the request to the server
        queue.add(stringRequest);
    }
}
