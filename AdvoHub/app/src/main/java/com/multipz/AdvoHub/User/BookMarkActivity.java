package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.BookMarkAdapter;
import com.multipz.AdvoHub.Model.SearchAdvocateModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMarkActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    ImageView img_back;
    RecyclerView rv_bookmark;
    Context context;
    BookMarkAdapter adapter;
    private List<SearchAdvocateModel> list = new ArrayList<>();
    private String param = "";
    private ProgressDialog dialog;
    private RelativeLayout rel_root;
    private Shared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);

        rv_bookmark = (RecyclerView) findViewById(R.id.rv_bookmark);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        getBookMarkList();
        //BookMarkData();

    }

    private void getBookMarkList() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.bookmarkList);
            JSONObject user = new JSONObject();
            user.put("ah_users_id", shared.getUserId());//30
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, BookMarkActivity.this, param, Config.API_GET_BOOKMARK_LIST);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void LawyerList() {
        adapter = new BookMarkAdapter(context, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_bookmark.setLayoutManager(mLayoutManager);
        rv_bookmark.setItemAnimator(new DefaultItemAnimator());
        rv_bookmark.setAdapter(adapter);
//        rv_bookmark.setNestedScrollingEnabled(false);
        adapter.setClickListener(new itemInClickListener());

    }

    private void BookMarkData() {
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
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {

        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_GET_BOOKMARK_LIST) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");

                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray aa = o.getJSONArray("data");
                    if (aa.length() > 0) {
                        for (int i = 0; i < aa.length(); i++) {
                            JSONObject objBookMarkList = aa.getJSONObject(i);
                            SearchAdvocateModel model = new SearchAdvocateModel();
                            model.setAh_users_id(objBookMarkList.getString("ah_users_id"));
                            model.setFull_name(objBookMarkList.getString("full_name"));
                            model.setYear_of_experience(objBookMarkList.getString("year_of_experience"));
                            model.setProfile_img(objBookMarkList.getString("profile_img"));
                            model.setAh_lawyer_bookmark_id(objBookMarkList.getString("ah_lawyer_bookmark_id"));
                            model.setCity_name(objBookMarkList.getString("city_name"));
                            model.setTitle(objBookMarkList.getString("types"));
                            model.setAvg_rating(objBookMarkList.getString("avg_rating"));
                            model.setTotal_rating_count(objBookMarkList.getString("total_rating_count"));
                            model.setTotal_case_assign(objBookMarkList.getString("total_case_assign"));
                            model.setIs_available(objBookMarkList.getString("is_available"));
                            model.setIs_bookmark(objBookMarkList.getString("is_bookmark"));

                            list.add(model);
                        }
                        LawyerList();
                    } else {
                        Toaster.getToast(getApplicationContext(), "No Record Found");
                    }


                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), Message);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class itemInClickListener implements BookMarkAdapter.ClickListener {
        @Override
        public void onItemClick(View view, int position) {
            SearchAdvocateModel model = list.get(position);
            String ah_users_id = model.getAh_users_id();
            String ah_users_lawyer_id = model.getAh_lawyer_bookmark_id();
            if (view.getId() == R.id.img_favourite_bookmark) {
                RemoveBookMarkList(position);

            }

        }
    }

    private void RemoveBookMarkList(final int pos) {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(BookMarkActivity.this);
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
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        adapter.remove(pos);
                        Toaster.getToast(getApplicationContext(), msg);
                    } else {
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
                param = "{\"action\":\"" + Config.removeBookmark + "\",\"body\":{\"ah_users_id\":\"" + "21" + "\",\"ah_users_lawyer_id\":\"1\"}}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
