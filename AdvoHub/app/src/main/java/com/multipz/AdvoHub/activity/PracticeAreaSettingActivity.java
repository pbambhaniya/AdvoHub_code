package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.PractiseAreaAdapter;
import com.multipz.AdvoHub.Model.PractiseAreaModel;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PracticeAreaSettingActivity extends AppCompatActivity {
    ImageView img_back;
    RecyclerView rv_practise_area;
    RelativeLayout rel_root;
    String param;
    ProgressDialog dialog;
    PractiseAreaAdapter adapter;
    ArrayList<PractiseAreaModel> list;
    Context context;
    private Shared shared;
    private ArrayList<SpinnerModel> object_specializebyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_area_setting);
        context = this;
        shared = new Shared(context);
        object_specializebyId = new ArrayList<>();
        ref();
        init();
    }

    private void ref() {

        list = new ArrayList<>();
        img_back = (ImageView) findViewById(R.id.img_back);
        rv_practise_area = (RecyclerView) findViewById(R.id.rv_practise_area);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getSpecialicationByID();
        getPractiseArea();

    }


    private void getSpecialicationByID() {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        // dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objectd = jsonArray.getJSONObject(i);
                            SpinnerModel spinnerModel = new SpinnerModel();
                            spinnerModel.setid(objectd.getString("ah_lawyer_type_id"));
                            spinnerModel.setName(objectd.getString("title"));
                            object_specializebyId.add(spinnerModel);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toaster.getToast(getApplicationContext(), error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\": \"" + Config.getSpecialicationById + "\",\"body\": {\"ah_users_id\": \"1\"}}";//shared.getUserId()
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getPractiseArea() {

        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //  dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = array.getJSONObject(i);
                            PractiseAreaModel model = new PractiseAreaModel();
                            model.setTitle(c.getString("title"));
                            model.setAh_lawyer_type_id(c.getString("ah_lawyer_type_id"));
                            list.add(model);
                        }

                        adapter = new PractiseAreaAdapter(list, context, object_specializebyId);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_practise_area.setLayoutManager(mLayoutManager);
                        rv_practise_area.setItemAnimator(new DefaultItemAnimator());
                        rv_practise_area.setAdapter(adapter);
                        rv_practise_area.setNestedScrollingEnabled(false);
                        adapter.setClickListener(new itemInClickListener());
                        adapter.notifyDataSetChanged();

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
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getSpecialication);
                    JSONObject body = new JSONObject();
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    class itemInClickListener implements PractiseAreaAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {


        }
    }

}
