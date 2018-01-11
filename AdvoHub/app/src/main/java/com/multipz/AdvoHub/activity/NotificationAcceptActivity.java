package com.multipz.AdvoHub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

public class NotificationAcceptActivity extends AppCompatActivity {
    ImageView img_back;
    RelativeLayout rel_root;
    private String name, img, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_accept);

        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        name = getIntent().getStringExtra("name");
        img = getIntent().getStringExtra("img");
        message = getIntent().getStringExtra("message");


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
