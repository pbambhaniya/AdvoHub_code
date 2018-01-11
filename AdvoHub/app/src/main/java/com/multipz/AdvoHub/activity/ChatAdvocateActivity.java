package com.multipz.AdvoHub.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Adapter.Adapter;
import com.multipz.AdvoHub.Model.Model;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Shared;

import java.util.ArrayList;
import java.util.List;

public class ChatAdvocateActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    Adapter adapter;
    Context context;
    private List<Model> list = new ArrayList<>();
    ImageView img_back, img_setting;
    RelativeLayout img_notification, rel_root;
    private Shared shared;
    private TextView txt_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_advocate);
        context = this;
        shared = new Shared(context);


        reference();
        init();


    }

    private void init() {
        if (shared.getUsertype().contentEquals("U")) {
            txt_name.setText("Chat with Lawyer");
        } else if (shared.getUsertype().contentEquals("L")) {
            txt_name.setText("Chat with User");
        }
      /*  img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatAdvocateActivity.this, SettingActivity.class));

            }
        });*/
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Chat();
    }

    private void reference() {

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        img_back = (ImageView) findViewById(R.id.img_back);
        // img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        txt_name = (TextView) findViewById(R.id.txt_name);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }


    private void Chat() {
        adapter = new Adapter(list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);
        adapter.setClickListener(new itemInClickListener());
        Data();
    }

    private void Data() {
        list.add(new Model("", "Doctore"));
        list.add(new Model("", "Nurse"));
        list.add(new Model("", "Ditesion"));
        list.add(new Model("", "Doctore"));
        list.add(new Model("", "Nurse"));
        list.add(new Model("", "Ditesion"));
        list.add(new Model("", "Doctore"));
        list.add(new Model("", "Nurse"));
        list.add(new Model("", "Ditesion"));
        list.add(new Model("", "Doctore"));
        list.add(new Model("", "Nurse"));
        list.add(new Model("", "Ditesion"));
        adapter.notifyDataSetChanged();
    }

    class itemInClickListener implements Adapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            //  Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ChatAdvocateActivity.this, ChatActivity.class);
            startActivity(i);
        }
    }
}
