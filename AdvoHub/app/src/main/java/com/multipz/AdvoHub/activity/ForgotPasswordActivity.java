package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private RelativeLayout root_layout;
    private EditText edt_mobile_no;
    private TextView btnSubmit;
    private String param, mobileNo;
    private ImageView imgClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_mobile_no.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Mobile No");
                } else if (edt_mobile_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Mobile No");
                } else {
                    mobileNo = edt_mobile_no.getText().toString();
                    ForgotPassword(mobileNo);
                }
            }
        });


    }

    private void reference() {
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));

    }

    private void ForgotPassword(String mNO) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.forgotpassword);
            JSONObject user = new JSONObject();
            user.put("mobile_number", mNO);
            main.put("body", user);
            //objFinal.put("json", main);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ForgotPasswordActivity.this, param, Config.API_FORGOT_PASSWORD);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_FORGOT_PASSWORD) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    String msg = o.getString("msg");
                    String ah_users_id = o.getString("ah_users_id");
                    String OTP = o.getString("OTP");
                    Intent i = new Intent(ForgotPasswordActivity.this, VerifyOTP.class);
                    i.putExtra("ah_users_id", ah_users_id);
                    i.putExtra("OTP", OTP);
                    edt_mobile_no.setText("");
                    startActivity(i);
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    String msg = o.getString("msg");
                    edt_mobile_no.setText("");
                    Toaster.getToast(getApplicationContext(), "" + msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
