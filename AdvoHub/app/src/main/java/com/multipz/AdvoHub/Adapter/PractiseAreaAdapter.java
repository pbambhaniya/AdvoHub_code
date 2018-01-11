package com.multipz.AdvoHub.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Model.PractiseAreaModel;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19-12-2017.
 */

public class PractiseAreaAdapter extends RecyclerView.Adapter<PractiseAreaAdapter.MyViewHolder> {
    private List<PractiseAreaModel> list;
    private ClickListener clickListener;
    String specialist_id, l_id, param;
    private ProgressDialog dialog;
    Context context;
    private Shared shared;
    ArrayList<SpinnerModel> object_specializebyId;


    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox cb_practise_area;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            cb_practise_area = (CheckBox) view.findViewById(R.id.cb_practise_area);
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


    public PractiseAreaAdapter(List<PractiseAreaModel> expertsList, Context cox, ArrayList<SpinnerModel> listsp_by_id) {
        this.list = expertsList;
        this.context = cox;
        this.shared = new Shared(context);
        object_specializebyId = listsp_by_id;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_practise_area, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PractiseAreaModel model = list.get(position);
        holder.cb_practise_area.setText(model.getTitle());
        holder.cb_practise_area.setButtonDrawable(R.drawable.check_box);


        holder.cb_practise_area.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    specialist_id = model.getAh_lawyer_specialist_id();
                    l_id = model.getAh_lawyer_type_id();
                    // addPractiseArea(specialist_id, l_id);
                }
//                else {
//                    specialist_id = model.getAh_lawyer_specialist_id();
//                    deletePractiseArea(specialist_id);
//
//                }

            }
        });
        //   MarkPracticeArea(holder, position);
    }

 /*   private void MarkPracticeArea(MyViewHolder holder, int position) {
        if (object_specializebyId.size() > 0) {

            if (list.get(position).getAh_lawyer_type_id().contentEquals(object_specializebyId.get(position).getid())) {
                holder.cb_practise_area.setChecked(true);
            }
        }
    }*/


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addPractiseArea(String id, String l_id) {
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
                String msg = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);


                    } else {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        msg = object.getString("msg");
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
                    main.put("action", Config.createlawyerspecialist);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_specialist_id", "");
                    body.put("ah_users_id", "1");
                    body.put("ah_lawyer_type_id", "1");
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deletePractiseArea(final String id) {
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
                String msg = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        msg = object.getString("msg");
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
                Log.d("Error", "" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.delete_record_lawyerspecialist);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_specialist_id", id/* ah_lawyer_specialist_id*/);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
