package com.multipz.AdvoHub.User;

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

import com.multipz.AdvoHub.Adapter.CaseListAdapter;
import com.multipz.AdvoHub.Model.CaseListModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaseListActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    RecyclerView rv_caselist;
    ImageView img_back;
    List<CaseListModel> list = new ArrayList<>();
    Context context;
    private CaseListAdapter adapter;
    private String param = "";
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);


        rv_caselist = (RecyclerView) findViewById(R.id.rv_caselist);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        CaseList();

    }

    private void CaseList() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.GetAllPostListbyUser);
            JSONObject user = new JSONObject();
            user.put("ah_users_id", 30);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, CaseListActivity.this, param, Config.API_GetAllPostListbyUser);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }

    class itemInClickListener implements CaseListAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            CaseListModel model = list.get(position);
            Intent intent = new Intent(CaseListActivity.this, PostCasesActivity.class);
            intent.putExtra("caseid", model.getCaseid());
            intent.putExtra("title", model.getTitle());
            intent.putExtra("description", model.getDescription());
            intent.putExtra("txDate", model.getCreate_date());

            startActivity(intent);
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;

        if (flag == Config.API_GetAllPostListbyUser) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray adata = o.getJSONArray("data");
                    for (int i = 0; i < adata.length(); i++) {
                        JSONObject obj = adata.getJSONObject(i);
                        CaseListModel model = new CaseListModel();
                        model.setCaseid(obj.getInt("caseid"));
                        model.setTitle(obj.getString("title"));
                        model.setCity_name(obj.getString("city_name"));
                        model.setState_name(obj.getString("state_name"));
                        model.setDescription(obj.getString("description"));
                        model.setCreate_date(obj.getString("create_date"));
                        model.setLawyer_type(obj.getString("lawyer_type"));
                        list.add(model);
                    }
                    adapter = new CaseListAdapter(list, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rv_caselist.setLayoutManager(mLayoutManager);
                    rv_caselist.setItemAnimator(new DefaultItemAnimator());
                    rv_caselist.setAdapter(adapter);
                    rv_caselist.setNestedScrollingEnabled(false);
                    adapter.setClickListener(new itemInClickListener());

                } else {
                    pd.dismiss();
                    String msg = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}