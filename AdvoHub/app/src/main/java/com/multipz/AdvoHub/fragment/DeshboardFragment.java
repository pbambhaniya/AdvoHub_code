package com.multipz.AdvoHub.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.activity.ActivityCases;
import com.multipz.AdvoHub.activity.AddAdvocareDiaryActivity;
import com.multipz.AdvoHub.activity.ChatAdvocateActivity;
import com.multipz.AdvoHub.activity.DictionaryActivity;
import com.multipz.AdvoHub.activity.FeedBackActivity;
import com.multipz.AdvoHub.activity.HighCourtActivity;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeshboardFragment extends Fragment {
    RelativeLayout rel_newsfeed, rel_chat, rel_diary, rel_dictionary, rel_legislation, rel_drafting, rel_cases, rel_feedback;
    private ProgressDialog dialog;
    private Context context;
    private Shared shared;
    private RelativeLayout rel_root;

    public DeshboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deshboard, container, false);
        context = this.getActivity();
        shared = new Shared(context);

        rel_newsfeed = (RelativeLayout) view.findViewById(R.id.rel_newsfeed);
        rel_chat = (RelativeLayout) view.findViewById(R.id.rel_chat);
        rel_diary = (RelativeLayout) view.findViewById(R.id.rel_diary);
        rel_dictionary = (RelativeLayout) view.findViewById(R.id.rel_dictionary);
        rel_legislation = (RelativeLayout) view.findViewById(R.id.rel_legislation);
        rel_drafting = (RelativeLayout) view.findViewById(R.id.rel_drafting);
        rel_cases = (RelativeLayout) view.findViewById(R.id.rel_cases);
        rel_feedback = (RelativeLayout) view.findViewById(R.id.rel_feedback);

        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        final int VGap = 20;//(VGap * 8)
        final int HGap = 20;

        int heighView = (deviceHeight - 320) / 4;
        int widthView = (deviceWidth - (HGap * 4)) / 2;

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(widthView, heighView);
        param.setMargins(HGap, VGap, HGap, VGap);

        rel_newsfeed.setLayoutParams(param);
        rel_chat.setLayoutParams(param);
        rel_diary.setLayoutParams(param);
        rel_dictionary.setLayoutParams(param);
        rel_legislation.setLayoutParams(param);
        rel_drafting.setLayoutParams(param);
        rel_cases.setLayoutParams(param);
        rel_feedback.setLayoutParams(param);

        rel_newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HighCourtActivity.class);
                startActivity(intent);
            }
        });

        rel_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAdvocareDiaryActivity.class);
                startActivity(intent);
            }
        });

        rel_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DictionaryActivity.class);
                startActivity(intent);
            }
        });

        rel_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatAdvocateActivity.class);
                startActivity(intent);
            }
        });

        rel_cases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityCases.class);
                startActivity(intent);
            }
        });
        rel_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });

      /*  getSpecialication();
        getCityByState();
        getStateByCountry();
*/

        return view;
    }

    private void getSpecialication() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        // dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.Specilization, jsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"" + Config.getSpecialication + "\",\"body\":{}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getCityByState() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        //  dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.State, jsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"getstatebycountry\",\"body\":{\"ah_country_id\":\"1\"}}\n";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getStateByCountry() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        //   dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.CityByState, jsonArray.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\":\"getcitybystate\",\"body\":{\"ah_state_id\":\"1\"}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
