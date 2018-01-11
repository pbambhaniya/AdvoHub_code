package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.FeedBackAdapter;
import com.multipz.AdvoHub.Model.FeedBackModel;
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
import java.util.List;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {
    RecyclerView listview_getfeedback;
    FeedBackAdapter adapter;
    Context context;
    private List<FeedBackModel> list = new ArrayList<>();
    ImageView img_back;
    String param;
    private ProgressDialog dialog;
    Shared shared;
    private RelativeLayout rel_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        context = this;
        shared = new Shared(context);

        reference();
        init();


    }

    private void reference() {
        listview_getfeedback = (RecyclerView) findViewById(R.id.listview_getfeedback);
        img_back = (ImageView) findViewById(R.id.img_back);
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
        getFeedback();
    }

    class itemInClickListener implements FeedBackAdapter.ClickListener {
        @Override
        public void onItemClick(View view, int position) {
            FeedBackModel model = list.get(position);

        }
    }


    public void popUpAlert() {
        LayoutInflater inflater = FeedBackActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_feedback, null);

//        LinearLayout okay = (LinearLayout) c.findViewById(R.id.okay);
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedBackActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//        okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }
    }

    private void getFeedback() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

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
                        JSONArray array = object.getJSONArray("data");
                        String msg = object.getString("msg");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            FeedBackModel model = new FeedBackModel();
                            model.setFull_name(obj.getString("full_name"));
                            model.setProfile_img(obj.getString("profile_img"));
                            model.setEmail(obj.getString("email"));
                            model.setMessage(obj.getString("message"));
                            model.setRate(obj.getString("rate"));
                            model.setFeedbackdate(obj.getString("feedbackdate"));
                            list.add(model);
                        }
                        adapter = new FeedBackAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        listview_getfeedback.setLayoutManager(mLayoutManager);
                        listview_getfeedback.setItemAnimator(new DefaultItemAnimator());
                        listview_getfeedback.setAdapter(adapter);

                        //Toaster.getToast(getApplicationContext(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.GetFeedbackByLawyerId);
                    JSONObject body = new JSONObject();
                    body.put("lawyer_id", shared.getUserId());//
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
