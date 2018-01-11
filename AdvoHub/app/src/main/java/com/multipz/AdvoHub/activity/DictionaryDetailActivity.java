package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DictionaryDetailActivity extends AppCompatActivity {
    TextView txt_word_name, txtdescrption;
    String name;
    ImageView img_back;
    private ProgressDialog dialog;
    private String param = "", ah_dictionary_id = "";
    private RelativeLayout rel_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_detail);


        reference();
        init();

    }

    private void reference() {

        txt_word_name = (TextView) findViewById(R.id.txt_word_name);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        txtdescrption = (TextView) findViewById(R.id.txtdescrption);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        ah_dictionary_id = getIntent().getStringExtra("ah_dictionary_id");
        //   txt_word_name.setText(name);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getDictionaryDetailsById();

    }

    private void getDictionaryDetailsById() {
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
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status == 1) {
                        dialog.dismiss();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String ah_dictionary_id = o.getString("ah_dictionary_id");
                            String title = o.getString("title");
                            String description = o.getString("description");
                            txt_word_name.setText(title);
                            txtdescrption.setText(description);
                        }
                        //  Toaster.getToast(getApplicationContext(), msg);
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
                    main.put("action", Config.getDictionaryDetailsById);
                    JSONObject body = new JSONObject();
                    body.put("ah_dictionary_id", ah_dictionary_id);
                    main.put("body", body);
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


}
