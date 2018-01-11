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
import com.multipz.AdvoHub.User.AskQuestionViewActivity;
import com.multipz.AdvoHub.User.BookMarkActivity;
import com.multipz.AdvoHub.User.CaseListActivity;
import com.multipz.AdvoHub.User.PostCaseActivity;
import com.multipz.AdvoHub.activity.ChatWindowActivity;
import com.multipz.AdvoHub.activity.HighCourtActivity;
import com.multipz.AdvoHub.activity.SearchActivity;
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
public class UserDeshboardFragment extends Fragment {

    RelativeLayout rel_search_advocate, rel_chat_advocate, rel_ask, rel_bookmark, rel_newsfeed, rel_postcase, rel_case_list, rel_root;
    private ProgressDialog dialog;
    private Shared shared;
    private Context context;

    public UserDeshboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_deshboard, container, false);

        context = this.getActivity();
        shared = new Shared(context);

        rel_search_advocate = (RelativeLayout) view.findViewById(R.id.rel_search_advocate);
        rel_chat_advocate = (RelativeLayout) view.findViewById(R.id.rel_chat_advocate);
        rel_ask = (RelativeLayout) view.findViewById(R.id.rel_ask);
        rel_bookmark = (RelativeLayout) view.findViewById(R.id.rel_bookmark);
        rel_newsfeed = (RelativeLayout) view.findViewById(R.id.rel_newsfeed);
        rel_case_list = (RelativeLayout) view.findViewById(R.id.rel_case_list);
        rel_postcase = (RelativeLayout) view.findViewById(R.id.rel_postcase);

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
        rel_search_advocate.setLayoutParams(param);
        rel_chat_advocate.setLayoutParams(param);
        rel_bookmark.setLayoutParams(param);
        rel_case_list.setLayoutParams(param);
        rel_postcase.setLayoutParams(param);
        rel_ask.setLayoutParams(param);


        rel_search_advocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        rel_chat_advocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatWindowActivity.class);
                startActivity(intent);
            }
        });
        rel_newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HighCourtActivity.class);
                startActivity(intent);
            }
        });

        rel_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AskQuestionViewActivity.class);
                startActivity(intent);
            }
        });
        rel_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookMarkActivity.class);
                startActivity(intent);
            }
        });

        rel_case_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaseListActivity.class);
                startActivity(intent);
            }
        });
        rel_postcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostCaseActivity.class);
                startActivity(intent);
            }
        });
        getSpecialication();
        getCityByState();
        getStateByCountry();
        getSpecialicationByID();
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
                String param = "{\"action\":\"getSpecialication\",\"body\":{}}";
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
                String param = "{\"action\":\"getstatebycountry\",\"body\":{\"ah_country_id\":\"1\"}}";
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
        //dialog.show();

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

    private void getSpecialicationByID() {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        // dialog.show();

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
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.getSpacializationByID, jsonArray.toString());
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String param = "{\"action\": \"getSpecialicationById\",\"body\": {\"ah_users_id\": \"1\"}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
