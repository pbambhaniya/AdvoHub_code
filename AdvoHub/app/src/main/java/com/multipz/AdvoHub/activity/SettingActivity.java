package com.multipz.AdvoHub.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.AsignCaseLawyerActivity;
import com.multipz.AdvoHub.User.ChangeContactNoActivity;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Toaster;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout lnr_availabilty, lnr_Courts, lnr_Practice_Area, lnr_language, lnr_change_pass, lnr_rate_us, lnr_contact_medium, lnr_change_con_number;
    private ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        reference();
        init();


    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        lnr_availabilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, SelectAvalibilty.class);
                startActivity(i);
            }
        });

        lnr_Courts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, ActivitySettingCourts.class);
                startActivity(i);
            }
        });

        lnr_Practice_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, PracticeAreaSettingActivity.class);
                startActivity(i);
            }
        });
        lnr_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, LanguageActivity.class);
                startActivity(i);
            }
        });

        lnr_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        lnr_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                if (!MyStartActivity(intent)) {
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + getApplicationContext().getPackageName()));
                    if (!MyStartActivity(intent)) {
                        Toaster.getToast(getApplicationContext(), "Could not open Android market, please install the market app.");
                    }
                }
            }
        });
        lnr_contact_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupAssignCase();
            }
        });

        lnr_change_con_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangeContactNoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void PopupAssignCase() {
        LayoutInflater inflater = SettingActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_conact_medium, null);
        ImageView close_popup_medium = (ImageView) c.findViewById(R.id.close_popup_medium);
        RadioButton radioCall = (RadioButton) c.findViewById(R.id.radioCall);
        RadioButton radioVoiceCall = (RadioButton) c.findViewById(R.id.radioVoiceCall);
        RadioButton radioVideoCall = (RadioButton) c.findViewById(R.id.radioVideoCall);

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setView(c);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        close_popup_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        radioCall.setButtonDrawable(R.drawable.check_box);
        radioVoiceCall.setButtonDrawable(R.drawable.check_box);
        radioVideoCall.setButtonDrawable(R.drawable.check_box);

        dialog.show();

    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void reference() {
        lnr_availabilty = (LinearLayout) findViewById(R.id.lnr_availabilty);
        lnr_Courts = (LinearLayout) findViewById(R.id.lnr_Courts);
        lnr_Practice_Area = (LinearLayout) findViewById(R.id.lnr_Practice_Area);
        lnr_language = (LinearLayout) findViewById(R.id.lnr_language);
        lnr_change_pass = (LinearLayout) findViewById(R.id.lnr_change_pass);
        lnr_contact_medium = (LinearLayout) findViewById(R.id.lnr_contact_medium);
        lnr_change_con_number = (LinearLayout) findViewById(R.id.lnr_change_con_number);

        img_back = (ImageView) findViewById(R.id.img_back);
        lnr_rate_us = (LinearLayout) findViewById(R.id.lnr_rate_us);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }
}
