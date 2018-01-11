package com.multipz.AdvoHub.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.activity.ActivityMenu;
import com.multipz.AdvoHub.activity.NotificationActivity;
import com.multipz.AdvoHub.activity.SelectUserActivity;
import com.multipz.AdvoHub.fragment.UserDeshboardFragment;
import com.multipz.AdvoHub.fragment.UserMyProfileFragment;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDrawerActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface
//        implements NavigationView.OnNavigationItemSelectedListener
{
    FragmentTransaction transaction;
    LinearLayout lnr_home, lnr_log_out, lnr_user_my_profle;
    TextView txt_name;
    private Shared shared;
    private Context context;
    private ImageView img_setting, btn_bag;
    private String param = "";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_drawer);
        context = this;
        shared = new Shared(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Application.setFontDefault((DrawerLayout) findViewById(R.id.drawer_layout));


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.menu_open);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        lnr_home = (LinearLayout) findViewById(R.id.lnr_home);
        lnr_log_out = (LinearLayout) findViewById(R.id.lnr_logout);
        txt_name = (TextView) findViewById(R.id.txt_name);
        lnr_user_my_profle = (LinearLayout) findViewById(R.id.lnr_user_my_profle);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        btn_bag = (ImageView) findViewById(R.id.btn_bag);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        setToolbarName(getResources().getString(R.string.app_name));
        drawer.closeDrawer(GravityCompat.START);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containers, new UserDeshboardFragment()).commit();

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserDrawerActivity.this, UserSettingActivity.class);
                startActivity(i);

            }
        });
        btn_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDrawerActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName(getResources().getString(R.string.app_name));
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containers, new UserDeshboardFragment()).commit();
            }
        });

        lnr_user_my_profle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName("My Profile");
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containers, new UserMyProfileFragment()).commit();
            }
        });


        lnr_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                logout();
            }
        });

    }

    private void logout() {
        param = "{\"action\":\"" + Config.logout + "\",\"body\":{\"ah_users_id\":" + shared.getUserId() + "}}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, UserDrawerActivity.this, param, Config.API_logout);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setToolbarName(String name) {
        txt_name.setText(name);
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_logout) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONObject o = object.getJSONObject("body");
                    String msg = o.getString("msg");
                    shared.setlogin(false);
                    shared.logout();
                    Intent intent = new Intent(UserDrawerActivity.this, SelectUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
