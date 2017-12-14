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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    protected static String EVENT = "login";
    protected static String JSON_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void verifyLogin(View view){
        //get information from text fields
        final String username = ((EditText)findViewById(R.id.loginUsernameText)).getText().toString();
        final String password = ((EditText)findViewById(R.id.loginPasswordText)).getText().toString();

        if (username.length() < 3) {
            DialogBox dialogBox = new DialogBox();
            dialogBox.setMessage(R.string.short_login);
            dialogBox.show(getFragmentManager(), "short_login");
            return;
        }
        //send to server for verification (ideally encrypted, salt+hash, etc.

        //disable button to prevent additional server queries
        findViewById(R.id.loginButton2).setClickable(false);

        //setup request to server
        String url = getResources().getString(R.string.url)+ EVENT;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            Log.d("server response: ", response.toString());
                            //authenticate, session id info --> intent***
                            if (response.getBoolean(JSON_SUCCESS)) {
                                //if server comes back with true, start next activity
                                Intent intent = new Intent(LogInActivity.this, AddStringsActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //enable button
                        findViewById(R.id.loginButton2).setClickable(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError ", error.toString());
                        //make dialog box
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
        queue.add(stringRequest);



    }

}
