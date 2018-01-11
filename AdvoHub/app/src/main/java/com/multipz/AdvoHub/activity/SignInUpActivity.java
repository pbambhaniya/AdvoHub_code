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
import android.view.View;
import android.widget.Button;
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

/**
 * Created by Admin on 30-11-2017.
 */

public class SignInUpActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    Context context;
    TextView btn_user, btn_done, txt_lawyer, txt_user, btnForgotPassword;
    private ImageView img_back;
    EditText et_password, et_mobile_no, edt_signUpMobile, edtSignUpOTP;
    private String SelectUser, mobileNo, password;
    private LinearLayout layoutsetOtp;
    private String param = "";
    private Shared shared;
    private Button btnsendotp;
    private RelativeLayout rel_root;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinup);
        context = this;
        shared = new Shared(context);


        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        initTab();

    }


    private void initTab() {

        layoutsetOtp = (LinearLayout) findViewById(R.id.layoutsetOtp);
        edt_signUpMobile = (EditText) findViewById(R.id.edt_signUpMobile);
        btnsendotp = (Button) findViewById(R.id.btnsendotp);
        edtSignUpOTP = (EditText) findViewById(R.id.edtSignUpOTP);

        final LinearLayout lnrSignup = (LinearLayout) findViewById(R.id.lnr_signup);
        final LinearLayout lnrLogin = (LinearLayout) findViewById(R.id.lnr_login);
        LinearLayout lnrSignupTab = (LinearLayout) findViewById(R.id.lnr_signup_tab);
        LinearLayout lnrLoginTab = (LinearLayout) findViewById(R.id.lnr_login_tab);
        final TextView txtSignup = (TextView) findViewById(R.id.txt_signup_tab);
        final TextView txtLogin = (TextView) findViewById(R.id.txt_login_tab);
        final ImageView imgSignup = (ImageView) findViewById(R.id.img_signup_tab);
        final ImageView imgLogin = (ImageView) findViewById(R.id.img_login_tab);
        txt_lawyer = (TextView) findViewById(R.id.txt_lawyer);
        txt_user = (TextView) findViewById(R.id.txt_user);

        img_back = (ImageView) findViewById(R.id.img_back);
        btn_user = (TextView) findViewById(R.id.btn_user);
        btn_done = (TextView) findViewById(R.id.btn_done);

        et_password = (EditText) findViewById(R.id.et_password);
        et_mobile_no = (EditText) findViewById(R.id.et_mobile_no);
        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SelectUser = getIntent().getStringExtra("SelectUser");
        if (SelectUser != null) {
            if (SelectUser.contentEquals("Lawyer")) {
                txt_lawyer.setBackgroundResource(R.drawable.bg_btn_app);
                txt_lawyer.setTextColor(Color.parseColor("#FFFFFF"));
                txt_user.setBackgroundResource(R.drawable.bg_btn_white);
                txt_user.setTextColor(Color.parseColor("#303F52"));
            } else if (SelectUser.contentEquals("User")) {
                txt_user.setBackgroundResource(R.drawable.bg_btn_app);
                txt_user.setTextColor(Color.parseColor("#FFFFFF"));
                txt_lawyer.setBackgroundResource(R.drawable.bg_btn_white);
                txt_lawyer.setTextColor(Color.parseColor("#303F52"));
            }
        }

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInUpActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });


        lnrLoginTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnrLogin.setVisibility(View.VISIBLE);
                txtLogin.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtLogin.setBackgroundColor(getResources().getColor(R.color.colorBgPage));
                imgLogin.setVisibility(View.VISIBLE);

                lnrSignup.setVisibility(View.GONE);
                lnrLogin.setVisibility(View.VISIBLE);

                imgSignup.setVisibility(View.INVISIBLE);
                txtSignup.setTextColor(getResources().getColor(R.color.colorWhite));
                txtSignup.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        lnrSignupTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSignup.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtSignup.setBackgroundColor(getResources().getColor(R.color.colorBgPage));
                imgSignup.setVisibility(View.VISIBLE);

                lnrSignup.setVisibility(View.VISIBLE);
                lnrLogin.setVisibility(View.GONE);

                imgLogin.setVisibility(View.INVISIBLE);
                txtLogin.setTextColor(getResources().getColor(R.color.colorWhite));
                txtLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_mobile_no.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Mobile No");
                } else if (et_mobile_no.getText().toString().trim().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Mobile No");
                } else if (et_password.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Password");
                } else {
                    mobileNo = et_mobile_no.getText().toString().trim();
                    password = et_password.getText().toString().trim();
                    if (!shared.getUsertype().contentEquals("")) {
                        if (shared.getUsertype().contentEquals("L")) {
                            param = "{\"action\":\"" + Config.Action_login + "\",\"body\":{\"mobile_number\":\"" + mobileNo + "\",\"password\":\"" + password + "\",\"type\":\"L\"}}";
                            if (Constant_method.checkConn(getApplicationContext())) {
                                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_LOGIN);
                                myAsyncTask.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
                            }
                        } else if (shared.getUsertype().contentEquals("U")) {
                            param = "{\"action\":\"" + Config.Action_login + "\",\"body\":{\"mobile_number\":\"" + mobileNo + "\",\"password\":\"" + password + "\",\"type\":\"U\"}}";
                            if (Constant_method.checkConn(getApplicationContext())) {
                                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_LOGIN);
                                myAsyncTask.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    /*if (et_mobile_no.getText().toString().contentEquals("1234567890") && et_password.getText().toString().contentEquals("123")) {
                        Intent intent = new Intent(SignInUpActivity.this, ActivityMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        shared.setlogin(true);
                    } else if (et_mobile_no.getText().toString().contentEquals("1234567899") && et_password.getText().toString().contentEquals("123")) {
                        Intent intent = new Intent(SignInUpActivity.this, UserDrawerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        shared.setlogin(true);
                    } else {
                        Toast.makeText(context, "Please Enter UserName and Password", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        });
        btnsendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_signUpMobile.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Mobile No");
                } else if (edt_signUpMobile.getText().toString().trim().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Mobile No");
                } else if (btnsendotp.getText().toString().trim().contentEquals("resend Otp")) {
                    mobileNo = edt_signUpMobile.getText().toString();
                    if (!shared.getUsertype().contentEquals("")) {
                        resendOTP(mobileNo);
                    }
                } else {
                    layoutsetOtp.setVisibility(View.VISIBLE);
                    btnsendotp.setText("resend Otp");
                    mobileNo = edt_signUpMobile.getText().toString();
                    if (!shared.getUsertype().contentEquals("")) {
                        registerOTP(mobileNo);
                    }
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInUpActivity.this, AddDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerOTP(String moblileNo) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.Action_registerOTP);
            JSONObject user = new JSONObject();
            user.put("mobile_number", moblileNo);
            user.put("type", shared.getUsertype());
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (shared.getUsertype().contentEquals("L")) {
            if (Constant_method.checkConn(getApplicationContext())) {
                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_SIGN_UP);
                myAsyncTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
            }
        } else if (shared.getUsertype().contentEquals("U")) {
            if (Constant_method.checkConn(getApplicationContext())) {
                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_SIGN_UP);
                myAsyncTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void resendOTP(String moblileNo) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.Action_resetOTP);
            JSONObject user = new JSONObject();
            user.put("mobile_number", moblileNo);
            user.put("ah_users_id", shared.getUserId());
            main.put("body", user);
            //objFinal.put("json", main);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (shared.getUsertype().contentEquals("L")) {

            if (Constant_method.checkConn(getApplicationContext())) {
                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_RESEND_OTP);
                myAsyncTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
            }
        } else if (shared.getUsertype().contentEquals("U")) {
            if (Constant_method.checkConn(getApplicationContext())) {
                MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SignInUpActivity.this, param, Config.API_RESEND_OTP);
                myAsyncTask.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {

        String Message = "", status;
        if (flag == Config.API_LOGIN) {
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
                        String username = oo.getString("full_name");
                        shared.setUserId(ah_users_id);
                        String userType = oo.getString("type");
                        shared.setUsertype(userType);
                        if (shared.getUsertype().contentEquals("L")) {
                            Intent i1 = new Intent(SignInUpActivity.this, ActivityMenu.class);
                            i1.putExtra("name", username);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        } else if (shared.getUsertype().contentEquals("U")) {
                            Intent i1 = new Intent(SignInUpActivity.this, UserDrawerActivity.class);
                            i1.putExtra("name", username);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        }
                    }
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_SIGN_UP) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                String msg = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    String ah_users_id = o.getString("ah_users_id");
                    String type = o.getString("type");
                    String OTP = o.getString("OTP");

                    shared.setUserId(ah_users_id);
                    shared.setUsertype(type);
                    layoutsetOtp.setVisibility(View.VISIBLE);
                    edtSignUpOTP.setText(OTP);
                    btnsendotp.setText("resendOtp");

                    Toaster.getToast(getApplicationContext(), msg);
                } else {
                    pd.dismiss();
                    layoutsetOtp.setVisibility(View.GONE);
                    Toaster.getToast(getApplicationContext(), "" + msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
