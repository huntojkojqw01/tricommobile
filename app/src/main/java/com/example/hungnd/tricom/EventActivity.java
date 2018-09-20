package com.example.hungnd.tricom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    final HashMap<String, String> joutais = new HashMap<>();
    final HashMap<String, String> bashos = new HashMap<>();
    Button buttonSearchJoutai, buttonSearchBasho;
    EditText editTextJoutai, editTextBasho;
    TextView textView;
    private String auth_token = "";
    AlertDialog dialog;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_new);

        Intent intent = getIntent();
        auth_token = intent.getStringExtra("auth_token"); //if it's a string you stored.

        buttonSearchJoutai = (Button) findViewById(R.id.buttonSearchJoutai);
        buttonSearchBasho = (Button) findViewById(R.id.buttonSearchBasho);

        editTextJoutai = (EditText) findViewById(R.id.joutai);
        editTextBasho = (EditText) findViewById(R.id.basho);

        textView = (TextView) findViewById(R.id.textView2);

        editTextJoutai.requestFocus();
        buttonSearchJoutai.setOnClickListener(new OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/joutaimasters", "joutais", "状態コード", "状態名", joutais, "状態選択", editTextJoutai);
            }
        });
        buttonSearchBasho.setOnClickListener(new OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/bashomasters", "bashos", "場所コード", "場所名", bashos, "場所選択", editTextBasho);
            }
        });
    }

    private boolean get(String url, final String model, final String code, final String name, final HashMap<String, String> list, final String title, final EditText editText){
        if(list.isEmpty()){
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
                        JSONObject mainObject = new JSONObject(response);
                        array = mainObject.getJSONArray(model);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            String code_ = row.getString(code);
                            String name_ = row.getString(name);
                            list.put(name_, code_);
                        }
                        adapter = new ArrayAdapter<String>(EventActivity.this, android.R.layout.select_dialog_item);
                        for(String name_ : list.keySet()){
                            adapter.add(name_);
                        }
                        AlertDialog.Builder alert = new AlertDialog.Builder(EventActivity.this);
                        alert.setTitle(title);
                        alert.setCancelable(true);
                        alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                editText.setText(list.get(adapter.getItem(item)));
                            }
                        });
                        dialog = alert.create();
                        dialog.show();
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
        }
        else{
            adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
            for(String name_ : list.keySet()){
                adapter.add(name_);
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(EventActivity.this);
            alert.setTitle(title);
            alert.setCancelable(true);
            alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    editText.setText(list.get(adapter.getItem(item)));
                }
            });
            dialog = alert.create();
            dialog.show();
        }
        return true;
    }

    private void alert(String message){
        Toast.makeText(getBaseContext(),  message, Toast.LENGTH_LONG).show();
    }

}
