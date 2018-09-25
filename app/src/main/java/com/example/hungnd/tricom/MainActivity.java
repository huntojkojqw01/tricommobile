package com.example.hungnd.tricom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

public class MainActivity extends AppCompatActivity {
    public static final String HOST = "http://103.63.109.157";
    public static final String LOGIN_POST = HOST + "/login";
    Button btn;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.setText("");
        password.setText("");
        btn.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                login(username.getText().toString(),password.getText().toString());
            }
        });
    }

    private boolean login(String username, String password){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Connect-Type","api");

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
                    Intent myIntent = new Intent(MainActivity.this, EventActivity.class);
                    myIntent.putExtra("auth_token", mainObject.getString("auth_token")); //Optional parameters
                    MainActivity.this.startActivity(myIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
