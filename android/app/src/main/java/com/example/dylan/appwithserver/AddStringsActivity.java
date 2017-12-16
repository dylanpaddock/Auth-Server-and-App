package com.example.dylan.appwithserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import java.lang.String;

public class AddStringsActivity extends AppCompatActivity {
    //TODO make these global or something
    protected static final String EVENT = "addstring";
    protected static final String JSON_SUCCESS = "success";
    protected static final String JSON_AUTH = "auth";
    protected static final String JSON_TEXT = "strings";
    protected static final String SAVED_TEXT_KEY = "text_value";
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strings);

        //get information from previous activity: initial strings in TextView.
        Intent intent = getIntent();
        this.token = intent.getStringExtra("token");
        TextView t = (TextView)findViewById(R.id.textView);
        if (savedInstanceState == null) {
            t.setText(intent.getStringExtra("strings"));
        }else {
            t.setText(savedInstanceState.getString(SAVED_TEXT_KEY));
        }

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView t = (TextView)findViewById(R.id.textView);
        outState.putCharSequence(SAVED_TEXT_KEY, t.getText());
    }

    public void addString(View view) {
        //Get text from the EditText and store it.
        EditText editText = (EditText)findViewById(R.id.stringToAdd);
        final String string = editText.getText().toString();
        //If the EditText is empty, do nothing.
        if (string.isEmpty()) {
            return;
        }
        if (!string.matches("^[a-zA-Z0-9_ ]+$")) {//if text has illegal characters, spawn dialog box
            DialogBox dialogBox = new DialogBox();
            dialogBox.setMessage(R.string.short_login);
            dialogBox.show(getFragmentManager(), "short_login");
        }



        //disable button to prevent additional server queries
        findViewById(R.id.addStringsButton).setClickable(false);
        //remove text from EditText
        //editText.setText("", TextView.BufferType.EDITABLE);

        //setup request to server
        String url = getResources().getString(R.string.url)+ EVENT;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest
                (Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    try {
                        Log.d("server response: ", response);
                        JSONObject j = new JSONObject(response);
                        if (j.getBoolean(JSON_SUCCESS)){ //authenticated + string is unique
                            TextView t = (TextView) findViewById(R.id.textView);
                            t.setText(j.getString(JSON_TEXT));
                        }else {
                            int id = j.getBoolean(JSON_AUTH) ?
                                    R.string.duplicate_string : R.string.bad_auth;
                            DialogBox dialogBox = new DialogBox();
                            dialogBox.setMessage(id);
                            dialogBox.show(getFragmentManager(), "bad_add_string");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //enable button
                    findViewById(R.id.addStringsButton).setClickable(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogBox dialogBox = new DialogBox();
                    dialogBox.setMessage(R.string.volley_error);
                    dialogBox.show(getFragmentManager(), "volley_error");
                    findViewById(R.id.addStringsButton).setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Authenticate and send input string
                params.put("event", EVENT);
                params.put("token", token);//authentication information***
                params.put("text", string);
                return params;
            }
        };

        Log.d("test message", "Token: " + token + "\n text: " + string);
        queue.add(stringRequest);
    }

}