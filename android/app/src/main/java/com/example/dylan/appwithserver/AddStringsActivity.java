package com.example.dylan.appwithserver;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.lang.String;

public class AddStringsActivity extends AppCompatActivity {

    protected static String EVENT = "login";
    protected static String JSON_TEXT = "strings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strings);

    }

    public void addString(View view) {
        //Get text from the EditText and store it.
        EditText editText = (EditText)findViewById(R.id.stringToAdd);
        final String string = editText.getText().toString();
        //If the EditText is empty, do nothing.
        if (string.isEmpty()) {
            return;
        }else
        //if text has illegal characters, spawn dialog box
        if (!string.matches("^[a-zA-Z0-9_ ]+$")) {
            //DialogBox dialogBox = new DialogBox();
            //dialogBox.setMessage(R.string.illegal_character);
        }
        final String token = ""; //TODO get info from intent
        //disable button to prevent additional server queries
        findViewById(R.id.addStringsButton).setClickable(false);
        //remove text from EditText
        editText.setText("", TextView.BufferType.EDITABLE);

        //setup request to server
        String url = getResources().getString(R.string.url)+ EVENT;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response){
                    //TODO get token -> authenticate, check for success/uniqueness, dialog box if not
                    //TODO
                    TextView t = (TextView) findViewById(R.id.textView);
                    try {
                        t.setText(response.getString(JSON_TEXT));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //enable button
                    findViewById(R.id.addStringsButton).setClickable(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError ", error.toString());
                DialogBox dialogBox = new DialogBox();
                dialogBox.setMessage(R.string.bad_password);
                dialogBox.show(getFragmentManager(), "bad_password");
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
        queue.add(stringRequest);
    }

}