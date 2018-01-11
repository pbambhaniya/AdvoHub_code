package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class UserFeedbackActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    private ImageView img_back;
    private TextView txtassign_lawyer_name, txtDivorceLawyer, btn_give_feedback, btn_no_thanks;
    private RatingBar feedbackRating;
    private EditText edt_feedback_description;
    private String param = "";
    private CircleImageView img_dp;
    private String meassageFeedback = "", img = "", fullname = "", lawyer_type = "";
    private float rating = 1;
    private Context context;
    private RelativeLayout rel_root;
    private int ah_users_lawyer_id = 0;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        context = this;
        shared = new Shared(context);

        reference();
        init();


    }

    private void init() {
        img = getIntent().getStringExtra("img");
        fullname = getIntent().getStringExtra("fullname");
        lawyer_type = getIntent().getStringExtra("lawyer_type");
        ah_users_lawyer_id = getIntent().getIntExtra("ah_users_lawyer_id", 0);


        txtassign_lawyer_name.setText(fullname);
        txtDivorceLawyer.setText(lawyer_type);
        Picasso.with(context).load(Config.ProfileImage + img).into(img_dp);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        feedbackRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;

            }
        });

        btn_give_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_feedback_description.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Message");
                } else {
                    meassageFeedback = edt_feedback_description.getText().toString();
                    AddFeedBack(meassageFeedback);
                }

            }
        });

        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void AddFeedBack(String msg) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.addFeedback);
            JSONObject user = new JSONObject();
            user.put("ah_users_lawyer_id", "31");
            user.put("ah_users_id", shared.getUserId());
            user.put("rate", rating);
            user.put("msg", msg);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, UserFeedbackActivity.this, param, Config.API_ADD_FEEDBACK);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txtassign_lawyer_name = (TextView) findViewById(R.id.txtassign_lawyer_name);
        txtDivorceLawyer = (TextView) findViewById(R.id.txtDivorceLawyer);
        btn_give_feedback = (TextView) findViewById(R.id.btn_give_feedback);
        feedbackRating = (RatingBar) findViewById(R.id.feedbackRating);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        edt_feedback_description = (EditText) findViewById(R.id.edt_feedback_description);
        btn_no_thanks = (TextView) findViewById(R.id.btn_no_thanks);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == Config.API_ADD_FEEDBACK) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    edt_feedback_description.setText("");
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
