package com.multipz.AdvoHub.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.Adapter.ReportAdapter;
import com.multipz.AdvoHub.Model.ReportModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    Context context;
    private List<ReportModel> list = new ArrayList<>();
    public ReportAdapter adapter;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        context = this;
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        Report();

    }

    private void Report() {
        adapter = new ReportAdapter(context, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);
        adapter.setClickListener(new itemInClickListener());

        DummyData();
    }

    class itemInClickListener implements ReportAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            //  Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void DummyData() {
        list.add(new ReportModel("", "Kets", "3 Hour Ago", "Paid"));
        list.add(new ReportModel("", "Raju", "2 Hour Ago", "Paid"));
        list.add(new ReportModel("", "jack", "4 Hour Ago", "Paid"));
        list.add(new ReportModel("", "Vicky", "1 Hour Ago", "Paid"));
        adapter.notifyDataSetChanged();
    }
}