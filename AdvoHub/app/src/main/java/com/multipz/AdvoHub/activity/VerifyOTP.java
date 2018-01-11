package com.multipz.AdvoHub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Toaster;

public class VerifyOTP extends AppCompatActivity {
    private RelativeLayout root_layout;
    private EditText edt_otp;
    private TextView btnVerify;
    private String param, OTP, ah_users_id;
    private ImageView imgClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        reference();
        init();

    }

    private void init() {
        OTP = getIntent().getStringExtra("OTP");
        ah_users_id = getIntent().getStringExtra("ah_users_id");
        if (OTP != null) {
            edt_otp.setText(OTP);
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(VerifyOTP.this, ResetPasswordActivity.class);
                i.putExtra("ah_users_id", ah_users_id);
                startActivity(i);*/
                if (edt_otp.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Mobile No");
                } else {
                    OTP = edt_otp.getText().toString();
                    Intent i = new Intent(VerifyOTP.this, ResetPasswordActivity.class);
                    i.putExtra("ah_users_id", ah_users_id);
                    startActivity(i);
                }
            }
        });


    }

    private void reference() {
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        btnVerify = (TextView) findViewById(R.id.btnVerify);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));

    }
}
