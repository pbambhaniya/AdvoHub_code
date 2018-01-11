package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeContactNoActivity extends AppCompatActivity {

    private ImageView imgClose;
    private EditText edt_old_contact_no, edt_new_Contact_no, edt_otp;
    private TextView btnSendOtp, btnChange;
    private LinearLayout layout_hide_show_otp;
    private ProgressDialog dialog;
    private String param = "", oldCno = "", newCno = "";
    private boolean IsOTPVerify = false;
    private RelativeLayout rel_root;
    private Shared shared;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact_no);
        context = this;
        shared = new Shared(context);

        reference();
        init();
    }

    private void init() {

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_old_contact_no.getText().toString().contentEquals("") || edt_old_contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Old Contact No");
                } else if (edt_new_Contact_no.getText().toString().contentEquals("") || edt_new_Contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid New Contact No");
                } else {
                    oldCno = edt_old_contact_no.getText().toString().trim();
                    newCno = edt_new_Contact_no.getText().toString().trim();
                    ChangeContactNo();
                }
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_otp.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter OTP");
                } else {
                    ChangeContactNo();
                }

            }
        });
    }

    private void reference() {
        imgClose = (ImageView) findViewById(R.id.imgClose);
        edt_old_contact_no = (EditText) findViewById(R.id.edt_old_contact_no);
        edt_new_Contact_no = (EditText) findViewById(R.id.edt_new_Contact_no);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        btnSendOtp = (TextView) findViewById(R.id.btnSendOtp);
        btnChange = (TextView) findViewById(R.id.btnChange);
        layout_hide_show_otp = (LinearLayout) findViewById(R.id.layout_hide_show_otp);

        rel_root = (RelativeLayout) findViewById(R.id.root_layout);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));
    }

    private void ChangeContactNo() {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        dialog.dismiss();
                        String ah_users_id = object.getString("ah_users_id");
                        String OTP = object.getString("OTP");
                        IsOTPVerify = true;
                        layout_hide_show_otp.setVisibility(View.VISIBLE);
                        edt_otp.setText(OTP);
                        Toaster.getToast(getApplicationContext(), msg);
                    } else {
                        dialog.dismiss();
                        Toaster.getToast(getApplicationContext(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Toaster.getToast(getApplicationContext(), "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.ChangeContactNo);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());//1
                    body.put("old_mobile_number", oldCno);
                    body.put("new_mobile_number", newCno);
                    body.put("IsOTPVerify", IsOTPVerify);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
