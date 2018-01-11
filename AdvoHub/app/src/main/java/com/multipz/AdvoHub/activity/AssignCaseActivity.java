package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssignCaseActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private ImageView img_back;
    private CircleImageView imglawyerAccept;
    private TextView txtAcceptRequest, txtdecline, txtFullname, txtDescription;
    private String ah_users_id;
    private int idstatus = 0, ah_case_post_user_id = 0;
    private String param = "", fullname = "", desc = "", img = "";
    private Context context;
    private Shared shared;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_case);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        imglawyerAccept = (CircleImageView) findViewById(R.id.imglawyerAccept);
        txtAcceptRequest = (TextView) findViewById(R.id.txtAcceptRequest);
        txtdecline = (TextView) findViewById(R.id.txtdecline);
        txtFullname = (TextView) findViewById(R.id.txtFullname);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {

        fullname = getIntent().getStringExtra("fullname");
        desc = getIntent().getStringExtra("desc");
        ah_case_post_user_id = getIntent().getIntExtra("ah_case_post_user_id", 0);
        ah_users_id = getIntent().getStringExtra("ah_users_id");
        img = getIntent().getStringExtra("img");
        Picasso.with(context).load(Config.ProfileImage + img).into(imglawyerAccept);
        txtFullname.setText(fullname);
        txtDescription.setText(desc);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LawyerAcceptPostCase(2);
            }
        });

        txtdecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LawyerAcceptPostCase(3);
            }
        });
    }

    private void LawyerAcceptPostCase(int idstatus) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.LawyerAcceptPostCase);
            JSONObject user = new JSONObject();
            user.put("ah_case_post_user_id", ah_case_post_user_id);
            user.put("ah_users_id", shared.getUserId());//shared.getUserId()
            user.put("idstatus", idstatus);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, AssignCaseActivity.this, param, Config.API_LawyerAcceptPostCase);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_LawyerAcceptPostCase) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                } else {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
