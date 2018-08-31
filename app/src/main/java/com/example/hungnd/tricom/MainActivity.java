package com.example.hungnd.tricom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

public class MainActivity extends AppCompatActivity {
    public static final String HOST = "http://103.63.109.157";
    public static final String LOGIN_GET = HOST + "/login.json";
    public static final String LOGIN_POST = HOST + "/login";
    JSONArray gArray = new JSONArray();
    TextView textView;
    TextView tokenTv;
    Button btn, get;
    EditText username, password, url;
    private String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        btn = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.setText("");
        password.setText("");
        btn.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
//                username.setText(token);
                token = "35ErJ5wbntWxUGPfRuxHDe9aSM/inD3Us7DKDJSThmkmuaR2hEE+lkcutw+TBnGvPSoQ2JdVvv/7AuFl/GXjCyw==";
                login(username.getText().toString(),password.getText().toString());
//                username.setText(token);

            }
        });

        textView = (TextView) findViewById(R.id.textView);
        tokenTv = (TextView) findViewById(R.id.tokenTV);

        ConnectionManager.volleyJSONRequest(this, true, null, LOGIN_GET, new VolleyResponse() {
            //ConnectionManager.volleyJSONRequest(this, true, null, get_url, headers,  new VolleyResponse() {
//        ConnectionManager.volleyJSONRequest(this, true, null, url, Request.Method.POST, jsonParams, headers, new VolleyResponse() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    token =  mainObject.getString("token");
                    tokenTv.setText(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                JSONObject universityObject = mainObject.getJSONObject("response");
//                JSONString name = universityObject.getJsonString("name");
//                JSONString url = universityObject.getJsonString("url");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,  error.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void isNetwork(boolean connected) {
                /**
                 * True if internet is connected otherwise false
                 */
            }

        });

        url = (EditText) findViewById(R.id.editText);
        get = (Button) findViewById(R.id.button2);

        get.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + url.getText().toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getToken(String url){
//        final HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("X-CSRF-Token", "fetch");
//        headers.put("Accept","application/html");
//        headers.put("Content-Type","application/json");

        ConnectionManager.volleyJSONRequest(this, true, null, url, new VolleyResponse() {
            //ConnectionManager.volleyJSONRequest(this, true, null, get_url, headers,  new VolleyResponse() {
//        ConnectionManager.volleyJSONRequest(this, true, null, url, Request.Method.POST, jsonParams, headers, new VolleyResponse() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    token =  mainObject.getString("token");
                    textView.setText(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                JSONObject universityObject = mainObject.getJSONObject("response");
//                JSONString name = universityObject.getJsonString("name");
//                JSONString url = universityObject.getJsonString("url");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,  error.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void isNetwork(boolean connected) {
                /**
                 * True if internet is connected otherwise false
                 */
            }
        });

    }

    private boolean login(String username, String password){
        Toast.makeText(MainActivity.this,  tokenTv.getText(), Toast.LENGTH_LONG).show();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Connect-Type","api");

//        JSONObject jsonUserParams = new JSONObject();
//        try {
//            jsonUserParams.put("担当者コード", username);
//            jsonUserParams.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("担当者コード", username);
            jsonParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectionManager.volleyJSONRequest(this, true, null, LOGIN_POST, Request.Method.POST, jsonParams, headers, new VolleyResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    tokenTv.setText(mainObject.getString("auth_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                JSONObject universityObject = mainObject.getJSONObject("response");
//                JSONString name = universityObject.getJsonString("name");
//                JSONString url = universityObject.getJsonString("url");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                alert(error.toString());
            }

            @Override
            public void isNetwork(boolean connected) {
                /**
                 * True if internet is connected otherwise false
                 */
            }
        });
        return true;
    }

    private boolean get(String url){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Connect-Type","api");
        String auth_token = tokenTv.getText().toString();
        headers.put("Authorization", auth_token);

//        JSONObject jsonUserParams = new JSONObject();
//        try {
//            jsonUserParams.put("担当者コード", username);
//            jsonUserParams.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("担当者コード", username);
//            jsonParams.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        ConnectionManager.volleyJSONRequest(this, true, null, url, Request.Method.GET, jsonParams, headers, new VolleyResponse() {
            @Override
            public void onResponse(String response) {
                JSONArray array = new JSONArray();
                try {

                    JSONObject mainObject = new JSONObject(response);
                    array = mainObject.getJSONArray("bashos");
//                    textView.append(String.valueOf(array.length()) + "\n");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);

                        String code = row.getString("場所コード");
                        String name = row.getString("場所名");
                        textView.append(code + name + "\n");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gArray = array;
//                JSONObject universityObject = mainObject.getJSONObject("response");
//                JSONString name = universityObject.getJsonString("name");
//                JSONString url = universityObject.getJsonString("url");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                alert(error.toString());
            }

            @Override
            public void isNetwork(boolean connected) {
                /**
                 * True if internet is connected otherwise false
                 */
            }
        });
        return true;
    }

    private void alert(String message){
        Toast.makeText(getBaseContext(),  message, Toast.LENGTH_LONG).show();
    }

}
