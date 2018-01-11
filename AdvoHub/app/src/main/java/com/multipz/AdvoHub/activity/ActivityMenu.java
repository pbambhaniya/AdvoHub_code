package com.multipz.AdvoHub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.UserRegistrationActivity;
import com.multipz.AdvoHub.fragment.DeshboardFragment;
import com.multipz.AdvoHub.fragment.MyProfileFragment;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMenu extends AppCompatActivity implements MyAsyncTask.AsyncInterface
//        implements NavigationView.OnNavigationItemSelectedListener
{
    FragmentTransaction transaction;
    ImageView img_menu;
    TextView txt_name;
    RelativeLayout img_notification;
    LinearLayout lnr_home, lnr_my_profile, lnr_privacy_policy, lnr_terms_condition, lnr_faq, lnr_support, lnr_logout;
    private Shared shared;
    private Context context;
    private ImageView img_setting;
    private String param = "";
    TextView txt_full_name;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        context = this;
        shared = new Shared(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Application.setFontDefault((DrawerLayout) findViewById(R.id.drawer_layout));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        img_setting = (ImageView) findViewById(R.id.img_setting);
        txt_full_name = (TextView) findViewById(R.id.txt_full_name);

        img_menu = (ImageView) findViewById(R.id.img_menu);
        img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        txt_name = (TextView) findViewById(R.id.txt_name);
        lnr_home = (LinearLayout) findViewById(R.id.lnr_home);
        lnr_my_profile = (LinearLayout) findViewById(R.id.lnr_my_profile);
        lnr_privacy_policy = (LinearLayout) findViewById(R.id.lnr_privacy_policy);
        lnr_terms_condition = (LinearLayout) findViewById(R.id.lnr_my_profile);
        lnr_faq = (LinearLayout) findViewById(R.id.lnr_terms_condition);
        lnr_support = (LinearLayout) findViewById(R.id.lnr_faq);
        lnr_logout = (LinearLayout) findViewById(R.id.lnr_logout);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMenu.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        lnr_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName("My Profile");
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new MyProfileFragment()).commit();
            }
        });
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ATMIYA LOW LAB
                setToolbarName("Atmiya Law Lab");
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new DeshboardFragment()).commit();
            }
        });

        lnr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                logout();
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityMenu.this, SettingActivity.class);
                startActivity(i);

            }
        });

        setToolbarName("Atmiya Law Lab");
        drawer.closeDrawer(GravityCompat.START);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new DeshboardFragment()).commit();


    }

    private void logout() {
        param = "{\"action\":\"" + Config.logout + "\",\"body\":{\"ah_users_id\":" + shared.getUserId() + "}}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ActivityMenu.this, param, Config.API_logout);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void setToolbarName(String name) {
        txt_name.setText(name);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        MenuItem itemCart = menu.findItem(R.id.about);

        return true;

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
                    Intent intent = new Intent(ActivityMenu.this, SelectUserActivity.class);
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
