package com.example.dylan.appwithserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddStringsActivity extends AppCompatActivity {
    private int current = 0; // where to place the next string
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strings);

    }

    public void addString(String s) {

        //set button unclickable
        findViewById(R.id.addStringsButton).setClickable(false);

        //send request for new list to server
        String url = "192.222.217.226:3000/";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //check for bad response, else display***

                    TextView t = (TextView)findViewById(R.id.textView);
                    t.setText(response);
                    //set button clickable
                    findViewById(R.id.addStringsButton).setClickable(true);
                }
            }, new onErrorResponse(VolleyError error) {
                Log.d("", error.toString());
            }
    })
                @Override
                    protected Map<String String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("","");
                        return params;
                    }
                };
                //

            //
        });
        queue.add(stringRequest);
    }

}-