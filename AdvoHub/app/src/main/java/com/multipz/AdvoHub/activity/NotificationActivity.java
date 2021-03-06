package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.multipz.AdvoHub.Adapter.NotificationAdapter;
import com.multipz.AdvoHub.Model.NotificationModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    RecyclerView rv_notification;
    ImageView img_back;
    Context context;
    private List<NotificationModel> list = new ArrayList<>();
    public NotificationAdapter adapter;
    private String param = "";
    private RelativeLayout rel_root;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = this;
        shared = new Shared(context);
        reference();
        init();


    }

    private void reference() {
        rv_notification = (RecyclerView) findViewById(R.id.rv_notification);
        img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        Notification();
    }

    private void Notification() {
        getAllNotification();
        //DummyData();

    }

    private void getAllNotification() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.GetActivityById);
            JSONObject user = new JSONObject();
            if (shared.getUsertype().contentEquals("U")) {
                user.put("actionbyuserid", "30");
            } else {
                user.put("actionbyuserid", shared.getUserId());
            }
            user.put("usertype", shared.getUsertype());
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, NotificationActivity.this, param, Config.API_GET_ALL_NOTIFICATION);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_GET_ALL_NOTIFICATION) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray array = o.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        NotificationModel model = new NotificationModel();
                        model.setActivity_id(obj.getInt("activity_id"));
                        model.setAh_case_lawyer_master_id(obj.getInt("ah_case_lawyer_master_id"));
                        model.setAh_requested_by_user_id(obj.getInt("ah_requested_by_user_id"));
                        model.setActivity_type_id(obj.getInt("activity_type_id"));
                        model.setAh_requested_name(obj.getString("ah_requested_name"));
                        model.setAh_requested_profile_img(obj.getString("ah_requested_profile_img"));
                        model.setAh_lawyer_id(obj.getInt("ah_lawyer_id"));
                        model.setAh_status_id(obj.getInt("ah_status_id"));
                        model.setActivity_message(obj.getString("activity_message"));
                        model.setCreate_date(obj.getString("create_date"));
                        list.add(model);
                    }
                    adapter = new NotificationAdapter(context, list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rv_notification.setLayoutManager(mLayoutManager);
                    rv_notification.setItemAnimator(new DefaultItemAnimator());
                    rv_notification.setAdapter(adapter);
                    rv_notification.setNestedScrollingEnabled(false);
                    adapter.setClickListener(new itemInClickListener());

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class itemInClickListener implements NotificationAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            NotificationModel model = list.get(position);
            Intent intent = new Intent(NotificationActivity.this, NotificationAcceptActivity.class);
            intent.putExtra("name", model.getAh_requested_name());
            intent.putExtra("img", model.getAh_requested_profile_img());
            intent.putExtra("message", model.getActivity_message());
            intent.putExtra("activity_type_id", model.getActivity_id());
            startActivity(intent);
        }
    }


   /* private void DummyData() {
        list.add(new NotificationModel("paresh", "2 Hours Ago", ""));
        list.add(new NotificationModel("paresh", "2 Hours Ago", ""));
        list.add(new NotificationModel("paresh", "2 Hours Ago", ""));
        list.add(new NotificationModel("paresh", "2 Hours Ago", ""));
        adapter.notifyDataSetChanged();
    }*/


}
