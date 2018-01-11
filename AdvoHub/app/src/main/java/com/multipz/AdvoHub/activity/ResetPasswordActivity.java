package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.UserDrawerActivity;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    private RelativeLayout root_layout;
    private LinearLayout layout_edit;
    private EditText edt_reset_new_password, edt_reset_confirm_password;
    private TextView btnresetPassword;
    private String param, password = "", ah_users_id;
    private ImageView imgClose;
    private Context context;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void init() {
        //  ah_users_id = getIntent().getStringExtra("ah_users_id");

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_reset_new_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter New Password");
                } else if (edt_reset_confirm_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Confirm Password");
                } else if (edt_reset_new_password.getText().toString().contentEquals(edt_reset_confirm_password.getText().toString())) {
                    password = edt_reset_confirm_password.getText().toString();
                    ResetPassword(password, ah_users_id);
                }
              /*  if (shared.getUsertype().contentEquals("L")) {
                    Intent i1 = new Intent(ResetPasswordActivity.this, ActivityMenu.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    finish();
                } else if (shared.getUsertype().contentEquals("U")) {
                    Intent i1 = new Intent(ResetPasswordActivity.this, UserDrawerActivity.class);
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i1);
                    finish();
                }*/


            }
        });
    }

    private void reference() {
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        layout_edit = (LinearLayout) findViewById(R.id.layout_edit);
        edt_reset_new_password = (EditText) findViewById(R.id.edt_reset_new_password);
        edt_reset_confirm_password = (EditText) findViewById(R.id.edt_reset_confirm_password);
        btnresetPassword = (TextView) findViewById(R.id.btnresetPassword);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));

    }

    private void ResetPassword(String password, String ah_users_id) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.resetpassword_Action);
            JSONObject user = new JSONObject();
            user.put("password", password);
            user.put("ah_users_id", ah_users_id);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ResetPasswordActivity.this, param, Config.API_RESET_PASSWORD);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_RESET_PASSWORD) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray array = o.getJSONArray("data");
                    shared.putString(Config.CurrentUser, array.toString());
                    shared.setlogin(true);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject oo = array.getJSONObject(i);
                        String ah_users_id = oo.getString("ah_users_id");
                        shared.setUserId(ah_users_id);
                        String userType = oo.getString("type");

                        if (userType.contentEquals("L")) {
                            Intent i1 = new Intent(ResetPasswordActivity.this, ActivityMenu.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        } else if (userType.contentEquals("U")) {
                            Intent i1 = new Intent(ResetPasswordActivity.this, UserDrawerActivity.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        }
                    }
                } else {
                    Message = object.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
