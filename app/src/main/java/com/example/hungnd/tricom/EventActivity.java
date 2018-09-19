package com.example.hungnd.tricom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

public class EventActivity extends AppCompatActivity {
    public static final String HOST = "http://103.63.109.157";
    Button buttonSearchBasho;
    EditText editTextBasho;
    TextView textView;
    private String auth_token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        Intent intent = getIntent();
        auth_token = intent.getStringExtra("auth_token"); //if it's a string you stored.
        editTextBasho = (EditText) findViewById(R.id.editTextBasho);
        buttonSearchBasho = (Button) findViewById(R.id.buttonSearchBasho);
        textView = (TextView) findViewById(R.id.textView2);

        buttonSearchBasho.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + editTextBasho.getText().toString());
            }
        });
    }

    private boolean get(String url){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept","application/json");
        headers.put("Content-Type","application/json");
        headers.put("Connect-Type","api");
        headers.put("Authorization", auth_token);

        JSONObject jsonParams = new JSONObject();

        ConnectionManager.volleyJSONRequest(this, true, null, url, Request.Method.GET, jsonParams, headers, new VolleyResponse() {
            @Override
            public void onResponse(String response) {
                JSONArray array = new JSONArray();
                try {
                    final HashMap<String, String> bashos = new HashMap<>();
                    JSONObject mainObject = new JSONObject(response);
                    array = mainObject.getJSONArray("bashos");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);

                        String code = row.getString("場所コード");
                        String name = row.getString("場所名");
                        bashos.put(name, code);
                    }
                    for(HashMap.Entry<String, String> basho : bashos.entrySet()){
                        textView.append(basho.getKey() + "," + basho.getValue() + "\n");
                    }
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
