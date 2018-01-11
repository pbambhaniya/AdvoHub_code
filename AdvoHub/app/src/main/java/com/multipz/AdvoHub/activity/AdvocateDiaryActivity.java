package com.multipz.AdvoHub.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdvocateDiaryActivity extends AppCompatActivity {

    private ImageView img_back;
    private TextView txt_start_date, txt_time;
    private EditText edt_name, edt_des;
    private Button btnAddTssk;
    private int hour, minutes;
    private int mYear, mMonth, mDay;
    String name, des, date = "", time = "", param;
    Context context;
    Shared shared;
    private ProgressDialog dialog;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advocate_diary);

        reference();
        init();

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAddTssk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_name.getText().toString().trim();
                des = edt_des.getText().toString().trim();
                date = txt_start_date.getText().toString();
                time = txt_time.getText().toString();

                if (name.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Name");
                } else if (des.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Description");
                } else if (date.contentEquals("Enter Date")) {
                    Toaster.getToast(getApplicationContext(), "Enter Date");
                } else if (time.contentEquals("Enter Time")) {
                    Toaster.getToast(getApplicationContext(), "Enter Time");
                } else if (Constant_method.checkConn(context)) {
                    getAddDiary();
                }


            }
        });


        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdvocateDiaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
//                                txt_start_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                txt_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minutes = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AdvocateDiaryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txt_time.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minutes, false);
                timePickerDialog.show();
            }
        });
    }

    private void getAddDiary() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(getApplicationContext(), msg);
                        Intent i = new Intent(AdvocateDiaryActivity.this, AddAdvocareDiaryActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.addDairyNote);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("name", name);
                    body.put("description", des);
                    body.put("date", date);
                    body.put("time", time);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getCity\",\"body\":{}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void reference() {
        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_start_date = (TextView) findViewById(R.id.txt_start_date);
        txt_time = (TextView) findViewById(R.id.txt_time);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_des = (EditText) findViewById(R.id.edt_des);
        btnAddTssk = (Button) findViewById(R.id.btnAddTssk);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }
}
