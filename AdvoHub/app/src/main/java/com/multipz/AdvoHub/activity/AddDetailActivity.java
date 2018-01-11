package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.UserRegistrationActivity;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;


public class AddDetailActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    RelativeLayout rel_done;
    ImageView img_back;
    EditText edt_full_name, edt_email_id, edt_password;
    private String param = "", fullname = "", emailId = "", password = "";
    private Context context;
    private Shared shared;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        context = this;
        shared = new Shared(context);

        reference();
        init();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void init() {
        rel_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edt_full_name.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Name");
                } else if (edt_email_id.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Email ID");

                } else if (!isValidEmail(edt_email_id.getText().toString().trim())) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Email ID");
                } else if (edt_password.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Password");
                } else {
                    fullname = edt_full_name.getText().toString().trim();
                    emailId = edt_email_id.getText().toString().trim();
                    password = edt_password.getText().toString().trim();
                    param = "{\"action\":\"" + Config.setShortProfile + "\",\"body\":{\"ah_users_id\":\"30\",\"password\":\"" + password + "\",\"full_name\":\"" + fullname + "\",\"email\":\"" + emailId + "\"}}";
                    if (Constant_method.checkConn(getApplicationContext())) {
                        MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, AddDetailActivity.this, param, Config.API_SHORT_PROFILE);
                        myAsyncTask.execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
                    }
                }

              /*  Intent intent = new Intent(AddDetailActivity.this, RegistrationActivity.class);
                startActivity(intent);*/
            }
        });

    }

    private void reference() {
        edt_full_name = (EditText) findViewById(R.id.edt_full_name);
        edt_email_id = (EditText) findViewById(R.id.edt_email_id);
        edt_password = (EditText) findViewById(R.id.edt_password);
        rel_done = (RelativeLayout) findViewById(R.id.rel_done);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_SHORT_PROFILE) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONObject o = object.getJSONObject("body");
                    String msg = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + msg);
                    if (shared.getUsertype().contentEquals("L")) {
                        Intent intent = new Intent(AddDetailActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    } else if (shared.getUsertype().contentEquals("U")) {
                        Intent intent = new Intent(AddDetailActivity.this, UserRegistrationActivity.class);
                        startActivity(intent);
                    }

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
