package com.multipz.AdvoHub.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.CourtAdapter;
import com.multipz.AdvoHub.Model.CourtsModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivitySettingCourts extends AppCompatActivity {

    RecyclerView rv_court;
    ImageView img_back;
    ProgressDialog dialog;
    String param;
    ArrayList<CourtsModel> list;
    CourtAdapter adapter;
    Context context;
    Shared shared;
    RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_courts);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        reference();
        init();
    }

    private void reference() {
        rv_court = (RecyclerView) findViewById(R.id.rv_court);
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

    private void init() {
        getCourt();
    }

    private void getCourt() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        dialog.dismiss();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = array.getJSONObject(i);
                            CourtsModel model = new CourtsModel();
                            model.setCourt_name(c.getString("court_name"));
                            model.setAh_court_id(c.getString("ah_court_id"));
                            model.setAh_city_id(c.getString("ah_city_id"));
                            list.add(model);
                        }

                        adapter = new CourtAdapter(list, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_court.setLayoutManager(mLayoutManager);
                        rv_court.setItemAnimator(new DefaultItemAnimator());
                        rv_court.setAdapter(adapter);
                        rv_court.setNestedScrollingEnabled(false);
                        adapter.notifyDataSetChanged();

                    } else if (status.contentEquals("0")) {
                        NodataPopup();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.getcourt + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void NodataPopup() {

        LayoutInflater inflater = ActivitySettingCourts.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_no_data, null);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettingCourts.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}


