package com.multipz.AdvoHub.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.activity.ChangePasswordActivity;
import com.multipz.AdvoHub.util.Application;

public class UserSettingActivity extends AppCompatActivity {

    private LinearLayout lnr_change_password, lnr_change_contact;
    private ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

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

        lnr_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserSettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        lnr_change_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserSettingActivity.this, ChangeContactNoActivity.class);
                startActivity(i);
            }
        });
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        lnr_change_password = (LinearLayout) findViewById(R.id.lnr_change_password);
        lnr_change_contact = (LinearLayout) findViewById(R.id.lnr_change_contact);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }
}
