package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdvocateDetailActivity extends AppCompatActivity {
    TextView txt_message, txt_lawyer_name, txt_lawyer_type, txt_experiense, txt_total_case, txt_total_language, txt_court, btn_avalibility, btn_review;
    ImageView img_back;
    CircleImageView img_dp;
    Shared shared;
    String param, Name, img, exp, Type = "", courtname = "", lang_name = "", Day, start_date, end_date, userId;
    private ProgressDialog dialog;
    Context context;
    private RelativeLayout rel_root;
    private ImageView img_boookmark;
    private RatingBar rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advocate_detail);

        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_lawyer_name = (TextView) findViewById(R.id.txt_lawyer_name);

//        txt_total_case = (TextView) findViewById(R.id.txt_total_case);
        txt_total_language = (TextView) findViewById(R.id.txt_total_language);
        txt_court = (TextView) findViewById(R.id.txt_court);
        txt_experiense = (TextView) findViewById(R.id.txt_experiense);
        txt_lawyer_type = (TextView) findViewById(R.id.txt_lawyer_type);
        txt_message = (TextView) findViewById(R.id.txt_message);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        btn_avalibility = (TextView) findViewById(R.id.btn_avalibility);
        img_boookmark = (ImageView) findViewById(R.id.img_boookmark);
        rating = (RatingBar) findViewById(R.id.rating);


        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        btn_avalibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchAdvocateDetailActivity.this, AvalabilityViewActivity.class);
                startActivity(intent);
            }
        });
        txt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchAdvocateDetailActivity.this, SendLawyerRequesrtActivity.class);
                startActivity(intent);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userId = getIntent().getStringExtra("userId");

        getDetail();


    }

    private void getDetail() {
        {

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
                            String msg = object.getString("msg");
                            JSONObject objects = object.getJSONObject("data");
                            JSONArray jsonArray = objects.getJSONArray("lawyer_user_info");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                c.getString("ah_users_id");
                                Name = c.getString("full_name");
                                String imgg = c.getString("profile_img");
                                exp = c.getString("year_of_experience");
                                c.getString("call_service");
                                c.getString("message_service");
                                c.getString("consult_service");
                                String avg = c.getString("average_rate");
                                rating.setRating(Float.parseFloat(avg));
                                String is_bookmark = c.getString("is_bookmark");
                                if (is_bookmark.contentEquals("true")) {
                                    img_boookmark.setImageResource(R.mipmap.ic_bookmark_select);
                                } else {
                                    img_boookmark.setImageResource(R.mipmap.ic_bookmark);
                                }
                                txt_lawyer_name.setText(Name);
                                txt_experiense.setText(exp);
                                //  Picasso.with(context).load(Config.ProfileImage + "" + imgg).into(img_dp);
                            }
                            JSONArray type = objects.getJSONArray("type");
                            for (int d = 0; d < type.length(); d++) {
                                JSONObject e = type.getJSONObject(d);
                                e.getString("ah_lawyer_type_id");
                                //e.getString("title");

                                if ((type.length() - 1) == d)
                                    Type = Type + e.getString("title");
                                else
                                    Type = Type + e.getString("title") + ", ";

                                txt_lawyer_type.setText(Type);
                            }
                            JSONArray court = objects.getJSONArray("court");
                            for (int c = 0; c < court.length(); c++) {
                                JSONObject e = court.getJSONObject(c);
                                e.getString("ah_court_id");
                                e.getString("ah_lawyer_court_id");

                                if (court.length() - 1 == c) {
                                    courtname = courtname + e.getString("court_name");
                                } else {
                                    courtname = courtname + e.getString("court_name") + ", ";
                                }
                                txt_court.setText(courtname);
                            }

                            JSONArray availability = objects.getJSONArray("availability");
                            shared.putString(Config.Avability, availability.toString());
                            for (int f = 0; f < availability.length(); f++) {
                                JSONObject e = availability.getJSONObject(f);
                                e.getString("ah_day_id");
                                Day = e.getString("day_name");
                                e.getString("ah_lawyer_availability_id");
                                start_date = e.getString("start_time");
                                end_date = e.getString("end_time");
                            }
                            JSONArray language = objects.getJSONArray("language");
                            for (int g = 0; g < language.length(); g++) {
                                JSONObject l = language.getJSONObject(g);
                                l.getString("ah_language_id");
                                l.getString("ah_lawyer_language_id");

                                if ((language.length() - 1) == g)
                                    lang_name = lang_name + l.getString("language_name");
                                else
                                    lang_name = lang_name + l.getString("language_name") + ", ";
                            }
                            txt_total_language.setText(lang_name);
                        } else {
                            Toaster.getToast(getApplicationContext(), "Data Not found");
                            dialog.dismiss();
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
                        main.put("action", Config.getLawyerprofiledeatils);
                        JSONObject body = new JSONObject();
                        body.put("ah_users_id", userId);//shared.getUserId()
                        main.put("body", body);
                        param = main.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                String param = "{\"action\":\"getCity\",\"body\":{}}";
                    params.put("json", param);
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        }
    }


}
