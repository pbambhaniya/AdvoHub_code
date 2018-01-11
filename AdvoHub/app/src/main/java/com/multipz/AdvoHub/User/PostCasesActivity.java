package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.Adapter.PostCaseAdapter;
import com.multipz.AdvoHub.Model.PostCaseModel;
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

public class PostCasesActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    ImageView img_back;
    RecyclerView recyclerview;
    PostCaseAdapter adapter;
    private TextView txt_description, txt_date;
    List<PostCaseModel> list = new ArrayList<>();
    Context context;
    private String title = "", description = "", param = "", txDate = "";
    private int caseid = 0;
    private Shared shared;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cases);
        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_date = (TextView) findViewById(R.id.txt_date);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        caseid = getIntent().getIntExtra("caseid", 0);
        description = getIntent().getStringExtra("description");
        txDate = getIntent().getStringExtra("txDate");
        txt_description.setText(description);
        txt_date.setText(Constant_method.cu_datecase(txDate));

        GetLawyerListbyAcceptUserPost();
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_GetLawyerListbyAcceptUserPost) {
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
                        PostCaseModel model = new PostCaseModel();
                        model.setLawyerid(obj.getString("lawyerid"));
                        model.setFull_name(obj.getString("full_name"));
                        model.setProfile_img(obj.getString("profile_img"));
                        model.setYear_of_experience(obj.getString("year_of_experience"));
                        model.setLaywertype(obj.getString("laywertype"));
                        model.setIdstatus(obj.getInt("idstatus"));
                        model.setAh_case_post_user_id(obj.getInt("ah_case_post_user_id"));
                        list.add(model);

                    }
                    adapter = new PostCaseAdapter(list, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerview.setLayoutManager(mLayoutManager);
                    recyclerview.setItemAnimator(new DefaultItemAnimator());
                    recyclerview.setAdapter(adapter);
                    recyclerview.setNestedScrollingEnabled(false);
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

    private void GetLawyerListbyAcceptUserPost() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.GetLawyerListbyAcceptUserPost);
            JSONObject user = new JSONObject();
            user.put("caseid", caseid);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, PostCasesActivity.this, param, Config.API_GetLawyerListbyAcceptUserPost);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }

    class itemInClickListener implements PostCaseAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            PostCaseModel model = list.get(position);
            String fullname = model.getFull_name();
            String img = model.getProfile_img();
            String lawyer_type = model.getLaywertype();
            int ah_users_lawyer_id = model.getAh_case_post_user_id();
            if (model.getIdstatus() == 4) {
                NodataPopup(fullname, img, lawyer_type, ah_users_lawyer_id);
            } else {
                Intent intent = new Intent(PostCasesActivity.this, AsignCaseLawyerActivity.class);
                intent.putExtra("lawyerid", model.getLawyerid());
                intent.putExtra("fullname", model.getFull_name());
                intent.putExtra("img", model.getProfile_img());
                intent.putExtra("ah_case_post_user_id", model.getAh_case_post_user_id());
                startActivity(intent);
            }

        }
    }

    private void NodataPopup(final String name, final String img, final String lawyer_type, final int ah_users_lawyer_id) {
        LayoutInflater inflater = PostCasesActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_already_asign_case, null);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        TextView head_dialogue = (TextView) c.findViewById(R.id.head_dialogue);
        LinearLayout givefeedback = (LinearLayout) c.findViewById(R.id.givefeedback);

        AlertDialog.Builder builder = new AlertDialog.Builder(PostCasesActivity.this);

        head_dialogue.setText("You already assigned your \n case to " + name);
        builder.setView(c);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        givefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostCasesActivity.this, UserFeedbackActivity.class);
                i.putExtra("img", img);
                i.putExtra("fullname", name);
                i.putExtra("lawyer_type", lawyer_type);
                i.putExtra("ah_users_lawyer_id", ah_users_lawyer_id);
                startActivity(i);
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
