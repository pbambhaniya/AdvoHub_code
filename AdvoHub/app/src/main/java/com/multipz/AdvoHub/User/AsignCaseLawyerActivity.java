package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AsignCaseLawyerActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    private ImageView img_back;
    private TextView txtassign_lawyer_name, btn_assign, btn_cancel;
    private CircleImageView img_dp;
    private String fullname = "", img = "", param = "", lawyerid = "";
    private int ah_case_post_user_id = 0;
    private Context context;
    private Shared shared;
    private RelativeLayout rel_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asign_case_lawyer);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void init() {

        ah_case_post_user_id = getIntent().getIntExtra("ah_case_post_user_id", 0);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        fullname = getIntent().getStringExtra("fullname");
        img = getIntent().getStringExtra("img");
        lawyerid = getIntent().getStringExtra("lawyerid");

        txtassign_lawyer_name.setText(fullname);
        Picasso.with(context).load(Config.ProfileImage + img).into(img_dp);
        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupAssignCase(fullname);

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void LawyerAcceptPostCase(int idstatus) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.LawyerAcceptPostCase);
            JSONObject user = new JSONObject();
            user.put("ah_case_post_user_id", ah_case_post_user_id);
            user.put("ah_users_id", lawyerid);
            user.put("idstatus", idstatus);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, AsignCaseLawyerActivity.this, param, Config.API_LawyerAcceptPostCase);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        txtassign_lawyer_name = (TextView) findViewById(R.id.txtassign_lawyer_name);
        btn_assign = (TextView) findViewById(R.id.btn_assign);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
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
                    Intent i = new Intent(AsignCaseLawyerActivity.this, UserDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void PopupAssignCase(String name) {
        LayoutInflater inflater = AsignCaseLawyerActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case_lawyer, null);
        LinearLayout ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.lnr_cancel);

        TextView head_dialogue = (TextView) c.findViewById(R.id.head_dialogue);
        AlertDialog.Builder builder = new AlertDialog.Builder(AsignCaseLawyerActivity.this);
        head_dialogue.setText("sure you want to assign your \ncase to " + name);
        builder.setView(c);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LawyerAcceptPostCase(4);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
