package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 30-11-2017.
 */

public class SelectUserActivity extends AppCompatActivity {

    Context context;
    Shared shared;
    private TextView txt_select_lawyer, txt_select_user;
    private String SelectUser = "";
    private ProgressDialog dialog;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        txt_select_lawyer = (TextView) findViewById(R.id.txt_select_lawyer);
        txt_select_user = (TextView) findViewById(R.id.txt_select_user);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {


       /* findViewById(R.id.txt_lawyer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_select_lawyer.getText().toString().contentEquals("Lawyer")) {
                    txt_select_lawyer.setBackgroundResource(R.drawable.bg_btn_app);
                    txt_select_lawyer.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_select_user.setBackgroundResource(R.drawable.bg_btn_white);
                    txt_select_user.setTextColor(Color.parseColor("#303F52"));
                }
                shared.setUsertype("L");
                Intent i = new Intent(context, SignInUpActivity.class);
                i.putExtra("SelectUser", SelectUser);
                startActivity(i);
            }
        });
        findViewById(R.id.txt_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_select_lawyer.getText().toString().contentEquals("User")) {
                    txt_select_lawyer.setBackgroundResource(R.drawable.bg_btn_app);
                    txt_select_lawyer.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_select_user.setBackgroundResource(R.drawable.bg_btn_white);
                    txt_select_user.setTextColor(Color.parseColor("#303F52"));
                }
                shared.setUsertype("L");
                Intent i = new Intent(context, SignInUpActivity.class);
                i.putExtra("SelectUser", SelectUser);
                startActivity(i);
            }
        });*/
  /*      if (txt_select_lawyer.getText().toString().contentEquals("Lawyer")) {
            txt_select_lawyer.setBackgroundResource(R.drawable.bg_btn_app);
            txt_select_lawyer.setTextColor(Color.parseColor("#FFFFFF"));
            txt_select_user.setBackgroundResource(R.drawable.bg_btn_white);
            txt_select_user.setTextColor(Color.parseColor("#303F52"));
        }
        // startActivity(new Intent(context, SignInUpActivity.class));

        SelectUser = "Lawyer";
        txt_select_lawyer.setText(SelectUser);*/

        findViewById(R.id.lnr_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SelectUser.contentEquals("")) {
                    if (SelectUser.contentEquals("Lawyer")) {
                        shared.setUsertype("L");
                        Intent i = new Intent(context, SignInUpActivity.class);
                        i.putExtra("SelectUser", SelectUser);
                        startActivity(i);
                    } else if (SelectUser.contentEquals("User")) {
                        shared.setUsertype("U");
                        Intent i = new Intent(context, SignInUpActivity.class);
                        i.putExtra("SelectUser", SelectUser);
                        startActivity(i);
                    }
                } else {
                    Toaster.getToast(getApplicationContext(), "Please Select User");
                }
            }
        });

        txt_select_lawyer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (txt_select_lawyer.getText().toString().contentEquals("Lawyer")) {
                    txt_select_lawyer.setBackgroundResource(R.drawable.bg_btn_app);
                    txt_select_lawyer.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_select_user.setBackgroundResource(R.drawable.bg_btn_white);
                    txt_select_user.setTextColor(Color.parseColor("#303F52"));
                }
                // startActivity(new Intent(context, SignInUpActivity.class));
                SelectUser = "Lawyer";
                txt_select_lawyer.setText(SelectUser);

                shared.setUsertype("L");
                Intent i = new Intent(context, SignInUpActivity.class);
                i.putExtra("SelectUser", SelectUser);
                startActivity(i);

            }
        });

        txt_select_user.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (txt_select_user.getText().toString().contentEquals("User")) {
                    txt_select_user.setBackgroundResource(R.drawable.bg_btn_app);
                    txt_select_user.setTextColor(Color.parseColor("#FFFFFF"));
                    txt_select_lawyer.setBackgroundResource(R.drawable.bg_btn_white);
                    txt_select_lawyer.setTextColor(Color.parseColor("#303F52"));
                }
                SelectUser = "User";
                txt_select_user.setText(SelectUser);

                shared.setUsertype("U");
                Intent i = new Intent(context, SignInUpActivity.class);
                i.putExtra("SelectUser", SelectUser);
                startActivity(i);
            }
        });
        getSpecialication();
        getCityByState();
        getStateByCountry();
        getgetcountry();


    }

    private void getSpecialication() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(SelectUserActivity.this);
        dialog.setMessage("Loading...");
        // dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.Specilization, jsonArray.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.Action_getSpecialication + "\",\"body\":{}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getCityByState() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(SelectUserActivity.this);
        dialog.setMessage("Loading...");
        //  dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.State, jsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.Action_getstatebycountry + "\",\"body\":{\"ah_country_id\":\"1\"}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getStateByCountry() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(SelectUserActivity.this);
        dialog.setMessage("Loading...");
        //   dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.CityByState, jsonArray.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.Action_getcitybystate + "\",\"body\":{\"ah_state_id\":\"1\"}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getgetcountry() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(SelectUserActivity.this);
        dialog.setMessage("Loading...");
        //  dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.country, jsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.getcountry + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
