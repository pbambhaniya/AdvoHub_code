package com.multipz.AdvoHub.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.EditUserProfileActivity;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserMyProfileFragment extends Fragment {


    private Shared shared;
    private Context context;
    private CircleImageView img_my_profile;
    private TextView txtfullnameprofile, txtemail, txtem_con_no, txtem_con_name, txtem_advocate, txtem_state, txtem_city, txt_edit_user_profile;
    private ProgressDialog dialog;
    private String param = "", ah_city_id = "", ah_state_id = "", img = "";
    private ScrollView rel_root;

    public UserMyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_my_profile, container, false);
        context = this.getActivity();
        shared = new Shared(context);

        reference(view);
        init();

        return view;
    }

    private void init() {

        GetUserProfileDetails();

        txt_edit_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditUserProfileActivity.class);
                intent.putExtra("EditProfile", true);
                intent.putExtra("fullname", txtfullnameprofile.getText().toString());
                intent.putExtra("email", txtemail.getText().toString());
                intent.putExtra("eme_no", txtem_con_no.getText().toString());
                intent.putExtra("eme_name", txtem_con_name.getText().toString());
                intent.putExtra("profession", txtem_advocate.getText().toString());
                intent.putExtra("city", txtem_city.getText().toString());
                intent.putExtra("state", txtem_state.getText().toString());
                intent.putExtra("img", img);


                startActivity(intent);

            }
        });

    }

    private void GetUserProfileDetails() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
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
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray aa = object.getJSONArray("data");
                        for (int i = 0; i < aa.length(); i++) {
                            JSONObject o = aa.getJSONObject(i);
                            String full_name = o.getString("full_name");
                            String email = o.getString("email");
                            img = o.getString("profile_img");
                            String emergency_contact_number = o.getString("emergency_contact_number");
                            String emergency_contact_name = o.getString("emergency_contact_name");
                            String profession = o.getString("profession");
                            ah_city_id = o.getString("ah_city_id");
                            String city_name = o.getString("city_name");
                            ah_state_id = o.getString("ah_state_id");
                            String state_name = o.getString("state_name");

                            Picasso.with(context).load(Config.ProfileImage + "" + img).placeholder(R.drawable.wedding).into(img_my_profile);

                            txtfullnameprofile.setText(full_name);
                            txtemail.setText(email);
                            txtem_con_no.setText(emergency_contact_number);
                            txtem_con_name.setText(emergency_contact_name);
                            txtem_advocate.setText(profession);
                            txtem_city.setText(city_name);
                            txtem_state.setText(state_name);

                        }
                        //Toaster.getToast(getActivity(), "" + msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toaster.getToast(getActivity(), "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", "GetUserProfileDetails");
                    JSONObject user = new JSONObject();
                    user.put("ah_users_id", "30");
                    main.put("body", user);
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

    private void reference(View view) {
        txtfullnameprofile = (TextView) view.findViewById(R.id.txtfullnameprofile);
        txtemail = (TextView) view.findViewById(R.id.txtemail);
        txtem_con_no = (TextView) view.findViewById(R.id.txtem_con_no);
        txtem_con_name = (TextView) view.findViewById(R.id.txtem_con_name);
        txtem_advocate = (TextView) view.findViewById(R.id.txtem_advocate);
        txtem_state = (TextView) view.findViewById(R.id.txtem_state);
        txtem_city = (TextView) view.findViewById(R.id.txtem_city);
        img_my_profile = (CircleImageView) view.findViewById(R.id.img_my_profile);
        txt_edit_user_profile = (TextView) view.findViewById(R.id.txt_edit_user_profile);
        rel_root = (ScrollView) view.findViewById(R.id.rel_root);
        Application.setFontDefault((ScrollView) view.findViewById(R.id.rel_root));
    }


}
