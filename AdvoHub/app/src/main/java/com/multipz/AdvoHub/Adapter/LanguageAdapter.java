package com.multipz.AdvoHub.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Model.LanguageModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 18-12-2017.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    private List<LanguageModel> list;
    private ClickListener clickListener;
    Context context;
    String id, name, param;
    private ProgressDialog dialog;


    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox cb_language;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            cb_language = (CheckBox) view.findViewById(R.id.cb_language);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public LanguageAdapter(List<LanguageModel> expertsList, Context context) {
        this.context = context;
        this.list = expertsList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LanguageModel model = list.get(position);
        holder.cb_language.setText(model.getLanguage_name());
        holder.cb_language.setButtonDrawable(R.drawable.check_box);
//        holder.cb_language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (b) {
//                    id = model.getAh_language_id();
//                    name = model.getLanguage_name();
//                    addLanguage(id, name);
//                } else {
//                    id = model.getAh_language_id();
//                    deleteLanguage(id);
//
//                }
//
//            }
//        });
    }

    private void deleteLanguage(final String id) {
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
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);
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
                    main.put("action", Config.deletelanguage);
                    JSONObject body = new JSONObject();
                    body.put("ah_language_id", id);
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

    private void addLanguage(final String id, final String name) {


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
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);
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
                    main.put("action", Config.createlanguage);
                    JSONObject body = new JSONObject();
                    body.put("ah_language_id", id);
                    body.put("lawyer_id", "1");
                    body.put("ah_lawyer_language_id", "");
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


    @Override
    public int getItemCount() {
        return list.size();
    }


}
