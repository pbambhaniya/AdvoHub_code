package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.Adapter.CasesAdapter;
import com.multipz.AdvoHub.Model.CasesModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCases extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    TextView txt_assigned, txt_accepted, txt_pending;
    ImageView img_back;
    RecyclerView listviewCases;
    private String param = "";
    private ArrayList<CasesModel> caseList;
    private CasesAdapter adapter;
    private Context context;
    private int reqStatus = 1;
    ArrayList<CasesModel> tempList = new ArrayList<>();
    private RelativeLayout rel_root;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);
        context = this;
        shared = new Shared(context);
        caseList = new ArrayList<>();

        reference();
        init();


    }

    private void init() {
        txt_pending.setTextColor(getResources().getColor(R.color.delete));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getLawyerCases();

        txt_pending.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                reqStatus = 1;
                getCaseList(caseList, reqStatus);
                txt_pending.setTextColor(getResources().getColor(R.color.delete));
                txt_accepted.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_assigned.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
        txt_accepted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                reqStatus = 2;
                getCaseList(caseList, reqStatus);
                txt_accepted.setTextColor(getResources().getColor(R.color.colorGreen));
                txt_pending.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_assigned.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
        txt_assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqStatus = 4;
                getCaseList(caseList, reqStatus);
                txt_assigned.setTextColor(getResources().getColor(R.color.yellow));
                txt_pending.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_accepted.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });


    }

    private void reference() {

        listviewCases = (RecyclerView) findViewById(R.id.listviewCases);
        txt_assigned = (TextView) findViewById(R.id.txt_assigned);
        txt_accepted = (TextView) findViewById(R.id.txt_accepted);
        txt_pending = (TextView) findViewById(R.id.txt_pending);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


    }

    private void getLawyerCases() {
        param = "{\"action\":\"" + Config.GetAllPostByLawyerType + "\",\"body\":{\"ah_users_id\":" + shared.getUserId() + "}}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ActivityCases.this, param, Config.API_GetAllPostByLawyerType);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    class itemInClickListener implements CasesAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            CasesModel model = tempList.get(position);
            Intent intent = new Intent(ActivityCases.this, AssignCaseActivity.class);
            intent.putExtra("fullname", model.getFull_name());
            intent.putExtra("desc", model.getDescription());
            intent.putExtra("ah_case_post_user_id", model.getAh_case_post_user_id());
            intent.putExtra("ah_users_id", model.getAh_users_id());
            intent.putExtra("idstatus", model.getIdstatus());
            intent.putExtra("img", model.getProfile_img());
            startActivity(intent);
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_GetAllPostByLawyerType) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray aa = o.getJSONArray("data");
                    caseList.clear();
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject objects = aa.getJSONObject(i);
                        CasesModel model = new CasesModel();
                        model.setTitle(objects.getString("title"));
                        model.setAh_lawyer_specialist_id(objects.getInt("ah_lawyer_specialist_id"));
                        model.setAh_lawyer_type_id(objects.getString("ah_lawyer_type_id"));
                        model.setAh_users_id(objects.getString("ah_users_id"));
                        model.setFull_name(objects.getString("full_name"));
                        model.setEmail(objects.getString("email"));
                        model.setProfile_img(objects.getString("profile_img"));
                        model.setDescription(objects.getString("description"));
                        model.setCreate_date(objects.getString("create_date"));
                        model.setAh_case_post_user_id(objects.getInt("ah_case_post_user_id"));
                        model.setIdstatus(objects.getInt("idstatus"));
                        model.setStatus(objects.getString("status"));
                        caseList.add(model);
                    }

                    getCaseList(caseList, reqStatus);
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void getCaseList(ArrayList<CasesModel> caseList, int status) {
        tempList.clear();
        if (status == 1) {
            for (CasesModel m : caseList) {
                if (m.getIdstatus() == 1) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
            adapter.setClickListener(new itemInClickListener());
        } else if (status == 2) {
            for (CasesModel m : caseList) {
                if (m.getIdstatus() == 2) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
        } else if (status == 4) {
            for (CasesModel m : caseList) {
                if (m.getIdstatus() == 4) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
        }
    }

    private void setAdapter() {
        adapter = new CasesAdapter(context, tempList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        listviewCases.setLayoutManager(mLayoutManager);
        listviewCases.setItemAnimator(new DefaultItemAnimator());
        listviewCases.setAdapter(adapter);
    }

}
