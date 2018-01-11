package com.multipz.AdvoHub.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Model.ProfileModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.activity.UpdateProfileActivity;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    TextView txt_name, txt_email, txt_city, txt_state, txt_experiense, txt_qualification, txt_reg_no, txt_edit;
    Context context;
    ProgressDialog dialog;
    String param, CityName, title, StateName, Email, Exp, R_no, Qua, Call = "N", Img = "";
    CheckBox cb_call, cb_message, cb_consult;
    CircleImageView img_dp;
    ArrayList<ProfileModel> list;
    String Imgg;
    private RelativeLayout rel_root;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = getActivity();
        txt_name = (TextView) view.findViewById(R.id.txt_name);
        txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_city = (TextView) view.findViewById(R.id.txt_city);
        txt_state = (TextView) view.findViewById(R.id.txt_state);
        txt_experiense = (TextView) view.findViewById(R.id.txt_experiense);
        txt_qualification = (TextView) view.findViewById(R.id.txt_qualification);
        txt_reg_no = (TextView) view.findViewById(R.id.txt_reg_no);
        txt_edit = (TextView) view.findViewById(R.id.txt_edit);
        cb_call = (CheckBox) view.findViewById(R.id.cb_call);
        img_dp = (CircleImageView) view.findViewById(R.id.img_dp);
        cb_message = (CheckBox) view.findViewById(R.id.cb_message);
        cb_consult = (CheckBox) view.findViewById(R.id.cb_consult);

        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

        getProfileData();

        cb_call.setButtonDrawable(R.drawable.check_box);
        cb_consult.setButtonDrawable(R.drawable.check_box);
        cb_message.setButtonDrawable(R.drawable.check_box);

        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                intent.putExtra("name", txt_name.getText().toString());
                intent.putExtra("email", txt_email.getText().toString());
                intent.putExtra("city", txt_city.getText().toString());
                intent.putExtra("state", txt_state.getText().toString());
                intent.putExtra("img", Config.ProfileImage + "" + Imgg);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getProfileData() {
        String tag_string_req = "string_req";
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.BASE_URL, new Response.Listener<String>() {
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
                        JSONObject data = object.getJSONObject("data");

                        JSONArray lawyer_info = data.getJSONArray("lawyer_user_info");
                        for (int i = 0; i < lawyer_info.length(); i++) {
                            JSONObject info = lawyer_info.getJSONObject(i);
                            ProfileModel model = new ProfileModel();
                            model.setAh_users_id(info.getString("ah_users_id"));
                            String Name = model.setFull_name(info.getString("full_name"));
                            Imgg = model.setProfile_img(info.getString("profile_img"));
                            Email = model.setEmail(info.getString("email"));
                            Exp = model.setYear_of_experience(info.getString("year_of_experience"));
                            //  Call = model.setCall_service(info.getString("call_service"));
                            model.setMessage_service(info.getString("message_service"));
                            model.setConsult_service(info.getString("consult_service"));
                            R_no = model.setAdvocate_registration_number(info.getString("advocate_registration_number"));
                            Qua = model.setEducation_qualification(info.getString("education_qualification"));


                            txt_name.setText(Name);
                            txt_email.setText(Email);
                            txt_experiense.setText(Exp);
                            txt_reg_no.setText(R_no);
                            txt_qualification.setText(Qua);

                            if (Call.contentEquals("Y")) {
                                cb_call.setChecked(true);
                            } else if (Call.contentEquals("N")) {
                                cb_call.setChecked(false);
                            }
                            Picasso.with(context).load(Config.ProfileImage + "" + Imgg).placeholder(R.drawable.wedding).into(img_dp);
                        }
                        JSONArray type = data.getJSONArray("type");
                        for (int j = 0; j < type.length(); j++) {
                            JSONObject types = type.getJSONObject(j);
                            ProfileModel model = new ProfileModel();
                            model.setAh_lawyer_type_id(types.getString("ah_lawyer_type_id"));
                            model.setParent_id(types.getString("parent_id"));
                            title = model.setTitle(types.getString("title"));
//                            if (type.length() - 1 == j) {
//                                title = title + types.getString("title");
//                            } else {
//                                title = title + types.getString("title") + ", ";
//
//                            }
//                            txt_name.setText(title);
                        }
                        JSONArray courts = data.getJSONArray("court");
                        for (int k = 0; k < courts.length(); k++) {
                            JSONObject c = courts.getJSONObject(k);
                            ProfileModel model = new ProfileModel();
                            String courtname = model.setCourt_name(c.getString("court_name"));
                            model.setAh_court_id(c.getString("ah_court_id"));
                            model.setAh_lawyer_court_id(c.getString("ah_lawyer_court_id"));
                        }
                        JSONArray city = data.getJSONArray("location");
                        for (int k = 0; k < city.length(); k++) {
                            JSONObject s = city.getJSONObject(k);
                            ProfileModel model = new ProfileModel();
                            model.setAh_city_id(s.getString("ah_city_id"));
                            CityName = model.setCity_name(s.getString("city_name"));
                            StateName = model.setState_name(s.getString("state_name"));
                            model.setAh_state_id(s.getString("ah_state_id"));

                            txt_state.setText(StateName);
                            txt_city.setText(CityName);
                        }
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
                    body.put("ah_users_id", "31");
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
