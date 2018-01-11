package com.multipz.AdvoHub.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

public class SendLawyerRequesrtActivity extends AppCompatActivity {
    ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_lawyer_requesrt);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
