package com.multipz.AdvoHub.activity;

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
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private RelativeLayout rel_root;
    private LinearLayout layout_edit;
    private EditText edt_current_password, edt_new_password, edt_confirm_password;
    private TextView btnchangePassword;
    private ImageView imgClose;
    private String password = "", current_password = "", param = "";
    private Shared shared;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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
        btnchangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_current_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Current Password");
                } else if (edt_new_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Confirm Password");
                } else if (edt_confirm_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Confirm Password");
                } else if (edt_new_password.getText().toString().contentEquals(edt_confirm_password.getText().toString())) {
                    current_password = edt_current_password.getText().toString();
                    password = edt_confirm_password.getText().toString();
                    ChangePassword(current_password, password);
                } else {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Password");
                }
            }
        });


    }

    private void ChangePassword(String current_password, String password) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.changepassword);
            JSONObject user = new JSONObject();
            user.put("ah_users_id", "1");//shared.getUserId()
            user.put("oldpassword", current_password);
            user.put("password", password);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ChangePasswordActivity.this, param, Config.API_CHANGE_PASSWORD);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        layout_edit = (LinearLayout) findViewById(R.id.layout_edit);
        edt_current_password = (EditText) findViewById(R.id.edt_current_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        btnchangePassword = (TextView) findViewById(R.id.btnchangePassword);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_CHANGE_PASSWORD) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + Message);

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
