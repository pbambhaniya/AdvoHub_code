package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.AskQueViewAdapter;
import com.multipz.AdvoHub.Model.AskQyeModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AskQuestionViewActivity extends AppCompatActivity {

    RecyclerView rv_ask_que;
    Context context;
    ProgressDialog dialog;
    String param;
    ArrayList<AskQyeModel> list;
    ImageView img_back;
    RelativeLayout rel_plus;
    AskQueViewAdapter adapter;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question_view);
        context = this;

        reference();
        init();

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AskQuestionViewActivity.this, AskQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();
        getAsqViewApi();

        // getData();


    }

    private void getData() {
        AskQyeModel model = new AskQyeModel();
        model.setTitle("Ask Question");
        model.setAnswer("answer");
        model.setTitle("Ask Question1");
        model.setAnswer("answer");
        list.add(model);

        adapter = new AskQueViewAdapter(context, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_ask_que.setLayoutManager(mLayoutManager);
        rv_ask_que.setItemAnimator(new DefaultItemAnimator());
        rv_ask_que.setAdapter(adapter);
        rv_ask_que.setNestedScrollingEnabled(false);
    }

    private void reference() {
        rv_ask_que = (RecyclerView) findViewById(R.id.rv_ask_que);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_plus = (RelativeLayout) findViewById(R.id.rel_plus);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void getAsqViewApi() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        String tag_string_req = "string_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                String Message = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    Message = object.getString("msg");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            AskQyeModel model = new AskQyeModel();
                            model.setAh_ask_question_id(c.getString("ah_ask_question_id"));
                            model.setAh_ask_question_user_id(c.getString("ah_ask_question_user_id"));
                            model.setTitle(c.getString("title"));
                            model.setDescription(c.getString("description"));
                            model.setAnswer(c.getString("answer"));
                            list.add(model);
                        }

                        adapter = new AskQueViewAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_ask_que.setLayoutManager(mLayoutManager);
                        rv_ask_que.setItemAnimator(new DefaultItemAnimator());
                        rv_ask_que.setAdapter(adapter);
                        rv_ask_que.setNestedScrollingEnabled(false);
                        // adapter.setClickListener(new itemInClickListener());
                    } else if (status == 0) {
                        dialog.dismiss();
                        Toaster.getToast(getApplicationContext(), Message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.d("error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                param = "{\"action\":\"" + Config.getQuestionAnswerbyId + "\",\"body\":{\"ah_ask_question_user_id\":\"1\"}}";
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
   /* class itemInClickListener implements AskQueViewAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {


        }
    }*/

}
