package com.multipz.AdvoHub.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.multipz.AdvoHub.R;

public class SubscriptionActivity extends AppCompatActivity {

    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
