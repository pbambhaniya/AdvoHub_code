package com.multipz.AdvoHub.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.UserDrawerActivity;
import com.multipz.AdvoHub.util.Shared;

/**
 * Created by Admin on 30-11-2017.
 */

public class SplashActivity extends AppCompatActivity {

    private Shared shared;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        shared = new Shared(context);

        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    /*Intent i = new Intent(SplashActivity.this, SelectUserActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);*/
                    if (!shared.isLoggedIn()) {
                        Intent i = new Intent(SplashActivity.this, SelectUserActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        if (shared.getUsertype().contentEquals("L")) {
                            Intent i1 = new Intent(SplashActivity.this, ActivityMenu.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        } else if (shared.getUsertype().contentEquals("U")) {
                            Intent i1 = new Intent(SplashActivity.this, UserDrawerActivity.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        } else {
                            Intent i1 = new Intent(SplashActivity.this, SelectUserActivity.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        }

                    }
                }
            }
        };
        t.start();
    }
}