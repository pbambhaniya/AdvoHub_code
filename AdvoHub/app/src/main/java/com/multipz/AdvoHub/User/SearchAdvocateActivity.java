package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.SearchAdvocateAdapter;
import com.multipz.AdvoHub.Model.SearchAdvocateModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdvocateActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    ImageView img_back, img_search_advocate;
    RecyclerView rv_search_lawyer;
    Context context;
    EditText edit_search_item;
    private String ah_lawyer_type_id = "", ah_city_id = "", param = "";
    SearchAdvocateAdapter adapter;
    private List<SearchAdvocateModel> list = new ArrayList<>();
    private ProgressDialog dialog;
    private RelativeLayout rel_root, rel_search_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advocate);
        context = this;
        img_back = (ImageView) findViewById(R.id.img_back);
        edit_search_item = (EditText) findViewById(R.id.edit_search_item);

        rv_search_lawyer = (RecyclerView) findViewById(R.id.rv_search_lawyer);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_search_list = (RelativeLayout) findViewById(R.id.rel_search_list);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_search_advocate = (ImageView) findViewById(R.id.img_search_advocate);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        ah_lawyer_type_id = getIntent().getStringExtra("ah_lawyer_type_id");
        ah_city_id = getIntent().getStringExtra("ah_city_id");
        //  getSearchAdvocate();
    }

    private void getSearchAdvocate() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.searchAdvocate);
            JSONObject user = new JSONObject();
            user.put("ah_city_id", "2");
            user.put("ah_lawyer_type_id", "4");
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SearchAdvocateActivity.this, param, Config.API_SEARCH_ADVOCATE);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LawyerList() {
        adapter = new SearchAdvocateAdapter(list, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_search_lawyer.setLayoutManager(mLayoutManager);
        rv_search_lawyer.setItemAnimator(new DefaultItemAnimator());
        rv_search_lawyer.setAdapter(adapter);
        rv_search_lawyer.setNestedScrollingEnabled(false);
        //adapter.setClickListener(new itemInClickListener());
        prepareExpertIn();
    }

    private void prepareExpertIn() {
       /* list.add(new SearchAdvocateModel("KK", "criminal", "", false));
        list.add(new SearchAdvocateModel("vraj", "Estate Planning Lawyer.", "", false));
        list.add(new SearchAdvocateModel("dn", "criminal", "", false));
        list.add(new SearchAdvocateModel("dhaval", "criminal", "", false));
        list.add(new SearchAdvocateModel("rajesh", "Intellectual Property Lawyer", "", false));
        list.add(new SearchAdvocateModel("KK", "criminal", "", false));
        list.add(new SearchAdvocateModel("KK", "criminal", "", false));
        list.add(new SearchAdvocateModel("kartik", "Corporate Lawyer", "", false));
        list.add(new SearchAdvocateModel("KK", "criminal", "", false));
        list.add(new SearchAdvocateModel("KK", "criminal", "", false));
        list.add(new SearchAdvocateModel("kamlesh", "Immigration Lawyer", "", false));
        list.add(new SearchAdvocateModel("KK", "criminal", "", false));*/
        adapter.notifyDataSetChanged();
        adapter.filter("");
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_SEARCH_ADVOCATE) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    rel_search_list.setVisibility(View.VISIBLE);
                    img_search_advocate.setVisibility(View.VISIBLE);
                    JSONArray aa = o.getJSONArray("data");
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject ob = aa.getJSONObject(i);
                        SearchAdvocateModel model = new SearchAdvocateModel();
                        model.setAh_users_id(ob.getString("ah_users_id"));
                        model.setFull_name(ob.getString("full_name"));
                        model.setYear_of_experience(ob.getString("year_of_experience"));
                        model.setProfile_img(ob.getString("profile_img"));
                        model.setAh_lawyer_specialist_id(ob.getString("ah_lawyer_specialist_id"));
                        model.setCity_name(ob.getString("city_name"));
                        model.setTitle(ob.getString("title"));
                      /*  adapter.notifyDataSetChanged();
                        adapter.filter("");
*/
                        list.add(model);
                        LawyerList();
                    }

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    class itemInClickListener implements SearchAdvocateAdapter.ClickListener {
        @Override
        public void onItemClick(View view, int position) {
            SearchAdvocateModel model = list.get(position);
            String ah_users_lawyer_id = model.getAh_lawyer_bookmark_id();
            if (view.getId() == R.id.img_favourite) {
                ImageView imageView = (ImageView) view.findViewById(R.id.img_favourite);
                if (!model.isIscheck()) {
                    model.setIscheck(true);
                    imageView.setImageResource(R.mipmap.ic_bookmark_select);
                    AddOrREmoveBookmark(model);
                } else {
                    model.setIscheck(false);
                    imageView.setImageResource(R.mipmap.ic_bookmark);
                    AddOrREmoveBookmark(model);
                }

            } else {
                Intent intent = new Intent(SearchAdvocateActivity.this, SearchAdvocateDetailActivity.class);
                startActivity(intent);
            }
        }
    }

    private void AddOrREmoveBookmark(final SearchAdvocateModel model) {
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
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(getApplicationContext(), msg);
                    }
                    dialog.hide();
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
                String param;
                if (model.isIscheck()) {
                    param = "{\"action\":\"" + Config.addBookmark + "\",\"body\":{\"ah_users_id\":\"21\",\"ah_users_lawyer_id\":\"1\"}}";
                } else {
                    param = "{\"action\":\"" + Config.removeBookmark + "\",\"body\":{\"ah_users_id\":\"21\",\"ah_users_lawyer_id\":\"1\"}}";
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
