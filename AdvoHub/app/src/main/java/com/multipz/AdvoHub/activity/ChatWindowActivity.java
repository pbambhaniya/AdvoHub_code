package com.multipz.AdvoHub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

public class ChatWindowActivity extends AppCompatActivity {
    private LinearLayout lnr_startChat;
    ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        reference();
        init();

    }

    private void init() {
        lnr_startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChatWindowActivity.this, ChatAdvocateActivity.class);
                startActivity(i);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void reference() {
        lnr_startChat = (LinearLayout) findViewById(R.id.lnr_startChat);
        img_back = (ImageView) findViewById(R.id.img_back);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

}
