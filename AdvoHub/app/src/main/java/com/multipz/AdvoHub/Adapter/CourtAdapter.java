package com.multipz.AdvoHub.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Model.CourtsModel;
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

public class CourtAdapter extends RecyclerView.Adapter<CourtAdapter.MyViewHolder> {
    private List<CourtsModel> list;
    private ClickListener clickListener;
    ProgressDialog dialog;
    Context context;
    String id, name, param;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox cb_court, txt_date;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            cb_court = (CheckBox) view.findViewById(R.id.cb_court);
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


    public CourtAdapter(List<CourtsModel> expertsList, Context cox) {
        this.list = expertsList;
        this.context = cox;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_court_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CourtsModel model = list.get(position);
        holder.cb_court.setText(model.getCourt_name());
        holder.cb_court.setButtonDrawable(R.drawable.check_box);
        holder.cb_court.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    name = model.getCourt_name();
                    id = model.getAh_court_id();
                    addCourt(id);
                } else {
                    id = model.getAh_court_id();
                    //  deleteCourt(id);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    private void addCourt(final String id) {
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
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.createlawyercourt);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_court_id", "");
                    body.put("lawyer_id", "10");
                    body.put("ah_court_id", id);
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


    private void deleteCourt(final String id) {

        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    if (status.contentEquals("1")) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);
                    } else if (status.contentEquals("0")) {
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
                    main.put("action", Config.deletelawyercourt);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_court_id", id);
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