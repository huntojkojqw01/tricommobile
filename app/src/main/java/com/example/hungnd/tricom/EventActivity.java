package com.example.hungnd.tricom;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.gloxey.gnm.interfaces.VolleyResponse;
import io.gloxey.gnm.managers.ConnectionManager;

public class EventActivity extends AppCompatActivity {
    public static final String HOST = MainActivity.HOST;
    public static final String EVENT_CREATE = HOST + "/events";
    final HashMap<String, String> shains = new HashMap<>();
    final HashMap<String, String> joutais = new HashMap<>();
    final HashMap<String, String> bashos = new HashMap<>();
    final HashMap<String, String> jobs = new HashMap<>();
    final HashMap<String, String> kouteis = new HashMap<>();
    Button buttonSearchShain, buttonSearchJoutai, buttonSearchBasho, buttonSearchJob, buttonSearchKoutei, buttonSubmit, bStartDate, bEndDate, bStartTime, bEndTime;
    EditText editTextShain, editTextJoutai, editTextBasho, editTextJob, editTextKoutei, editTextComment, editTextStartDate, editTextEndDate, editTextStartTime, editTextEndTime;
    TextView textView;
    private String auth_token = "";
    AlertDialog dialog;
    ArrayAdapter<String> adapter;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    // Get Current Date
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_new);

        Intent intent = getIntent();
        auth_token = intent.getStringExtra("auth_token"); //if it's a string you stored.

        buttonSearchShain = (Button) findViewById(R.id.buttonSearchShain);
        buttonSearchJoutai = (Button) findViewById(R.id.buttonSearchJoutai);
        buttonSearchBasho = (Button) findViewById(R.id.buttonSearchBasho);
        buttonSearchJob = (Button) findViewById(R.id.buttonSearchJob);
        buttonSearchKoutei = (Button) findViewById(R.id.buttonSearchKoutei);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        bStartDate = (Button) findViewById(R.id.bStartDate);
        bStartTime = (Button) findViewById(R.id.bStartTime);
        bEndDate = (Button) findViewById(R.id.bEndDate);
        bEndTime = (Button) findViewById(R.id.bEndTime);

        editTextShain = (EditText) findViewById(R.id.shain);
        editTextJoutai = (EditText) findViewById(R.id.joutai);
        editTextBasho = (EditText) findViewById(R.id.basho);
        editTextJob = (EditText) findViewById(R.id.job);
        editTextKoutei = (EditText) findViewById(R.id.koutei);
        editTextStartDate = (EditText) findViewById(R.id.start_date);
        editTextStartTime = (EditText) findViewById(R.id.start_time);
        editTextEndDate = (EditText) findViewById(R.id.end_date);
        editTextEndTime = (EditText) findViewById(R.id.end_time);

        editTextShain.requestFocus();

        buttonSearchShain.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/shainmasters", "shains", "社員番号", "氏名", shains, "社員選択", editTextShain);
            }
        });
        buttonSearchJoutai.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/joutaimasters", "joutais", "状態コード", "状態名", joutais, "状態選択", editTextJoutai);
            }
        });
        buttonSearchBasho.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/bashomasters", "bashos", "場所コード", "場所名", bashos, "場所選択", editTextBasho);
            }
        });
        buttonSearchJob.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/jobmasters", "jobs", "job番号", "job名", jobs, "JOB選択", editTextJob);
            }
        });
        buttonSearchKoutei.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                get(HOST + "/kouteimasters", "kouteis", "工程コード", "工程名", kouteis, "工程選択", editTextKoutei);
            }
        });

        bStartDate.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dateTimePicker(true, editTextStartDate);
            }
        });
        bStartTime.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dateTimePicker(false, editTextStartTime);
            }
        });
        bEndDate.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dateTimePicker(true, editTextEndDate);
            }
        });
        bEndTime.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dateTimePicker(false, editTextEndTime);
            }
        });
        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
    }

    private boolean get(String url, final String model, final String code, final String name, final HashMap<String, String> list, final String title, final EditText editText) {
        if (list.isEmpty()) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Connect-Type", "api");
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
                        for (String name_ : list.keySet()) {
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
        } else {
            adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
            for (String name_ : list.keySet()) {
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

    private boolean create() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Connect-Type", "api");
        headers.put("Authorization", auth_token);

        JSONObject eventParams = new JSONObject();
        try {
            eventParams.put("社員番号", editTextShain.getText().toString());
            eventParams.put("状態コード", editTextJoutai.getText().toString());
            eventParams.put("場所コード", editTextBasho.getText().toString());
            eventParams.put("JOB", editTextJob.getText().toString());
            eventParams.put("工程コード", editTextKoutei.getText().toString());
            eventParams.put("開始", editTextStartDate.getText().toString() + " " + editTextStartTime.getText().toString());
            eventParams.put("終了", editTextEndDate.getText().toString() + " " + editTextEndTime.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("event", eventParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ConnectionManager.volleyJSONRequest(this, true, null, EVENT_CREATE, Request.Method.POST, jsonParams, headers, new VolleyResponse() {
            @Override
            public void onResponse(String response) {
                alert("SUCCESS!");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(new String(error.networkResponse.data));
                    JSONArray errors = mainObject.getJSONArray("errors");
                    String str = "";
                    for(int i = 0; i < errors.length(); i++)
                        str += errors.getString(i) + "\n";
                    alert(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    private void alert(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void dateTimePicker(boolean isDate, final EditText editText) {

        if (isDate) {

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(EventActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                            c.set(year, monthOfYear, dayOfMonth);
                            editText.setText(format.format(c.getTime()));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else {

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(EventActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                            c.set(mYear, mMonth, mDay, hourOfDay, minute);
                            editText.setText(format.format(c.getTime()));
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}