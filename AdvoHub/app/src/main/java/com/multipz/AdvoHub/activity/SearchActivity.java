package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.AdvoHub.Adapter.SearchAdvocateAdapter;
import com.multipz.AdvoHub.Adapter.SpinnerAdapter;
import com.multipz.AdvoHub.Model.SearchAdvocateModel;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.User.SearchAdvocateDetailActivity;
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

public class SearchActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {

    private Spinner sp_search_state, sp_search_city, sp_category;
    private ImageView img_back, img_search_advocate;
    private TextView btn_search;
    private String param = "";
    private Shared shared;
    private Context context;
    private LinearLayout show_sp_date, lnr_show_popup_sp, lnr_show_search, lnr_search_btn;
    private ArrayList<SpinnerModel> CityByStateList, StateByCountry, objects_specilization;
    private RelativeLayout root_layout, rel_search_list;
    private String ah_city_id = "", ah_lawyer_type_id = "", state = "";
    private ImageView search_open_value, search_user_center;
    SearchAdvocateAdapter adapter;
    private List<SearchAdvocateModel> list = new ArrayList<>();
    private ProgressDialog dialog;
    RecyclerView rv_search_lawyer;

    private boolean isopen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        shared = new Shared(context);
        CityByStateList = new ArrayList<>();
        StateByCountry = new ArrayList<>();
        objects_specilization = new ArrayList<>();

        reference();
        init();

    }

    private void init() {
        /******************************************************************************/
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        CityByStateList.add(new SpinnerModel("", "Select City"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.CityByState, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_city_id"));
                spinnerModel.setName(object.getString("city_name"));
                CityByStateList.add(spinnerModel);

            }
            sp_search_city.setAdapter(new SpinnerAdapter(this, CityByStateList));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        StateByCountry.add(new SpinnerModel("", "Select State"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_state_id"));
                spinnerModel.setName(object.getString("state_name"));
                StateByCountry.add(spinnerModel);

            }
            sp_search_state.setAdapter(new SpinnerAdapter(this, StateByCountry));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        objects_specilization.add(new SpinnerModel("", "Select Lawyer"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.Specilization, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_lawyer_type_id"));
                spinnerModel.setName(object.getString("title"));
                objects_specilization.add(spinnerModel);

            }
            sp_category.setAdapter(new SpinnerAdapter(this, objects_specilization));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_search_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_search_state.getSelectedItem().toString().contentEquals("Select State")) {
                    state = StateByCountry.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_search_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_search_city.getSelectedItem().toString().contentEquals("Select City")) {
                    ah_city_id = StateByCountry.get(i).getid();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_category.getSelectedItem().toString().contentEquals("Select Specialisation")) {
                    ah_lawyer_type_id = objects_specilization.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select State");
                } else if (ah_city_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select City");
                } else if (ah_lawyer_type_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Lawyer");
                } else {
                  /*  Intent i = new Intent(SearchActivity.this, SearchAdvocateActivity.class);
                    i.putExtra("ah_city_id", ah_city_id);
                    i.putExtra("ah_lawyer_type_id", ah_lawyer_type_id);
                    startActivity(i);*/
                    getSearchAdvocate();
                }
            }
        });
        img_search_advocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lnr_show_popup_sp.setVisibility(View.VISIBLE);
                img_search_advocate.setVisibility(View.GONE);
                search_user_center.setVisibility(View.VISIBLE);
                lnr_search_btn.setVisibility(View.VISIBLE);
                rv_search_lawyer.setVisibility(View.GONE);
                lnr_show_search.setVisibility(View.VISIBLE);
            }
        });


        show_sp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isopen) {
                    lnr_show_popup_sp.setVisibility(View.VISIBLE);
                    // search_user_center.setVisibility(View.GONE);
                    lnr_show_popup_sp.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    search_open_value.setImageDrawable(getResources().getDrawable(R.mipmap.ic_dropdown_up));
                    isopen = true;
                } else {
                    lnr_show_popup_sp.setVisibility(View.INVISIBLE);
                    //  search_user_center.setVisibility(View.VISIBLE);
                    lnr_show_popup_sp.setBackgroundColor(getResources().getColor(R.color.colorBgPage));
                    search_open_value.setImageDrawable(getResources().getDrawable(R.mipmap.ic_dropdown));
                    isopen = false;
                }
            }
        });
    }

    private void reference() {
        search_user_center = (ImageView) findViewById(R.id.search_user_center);
        search_open_value = (ImageView) findViewById(R.id.search_open_value);
        rv_search_lawyer = (RecyclerView) findViewById(R.id.rv_search_lawyer);
        img_search_advocate = (ImageView) findViewById(R.id.img_search_advocate);
        show_sp_date = (LinearLayout) findViewById(R.id.show_sp_date);
        lnr_show_popup_sp = (LinearLayout) findViewById(R.id.lnr_show_popup_sp);
        lnr_show_search = (LinearLayout) findViewById(R.id.lnr_show_search);
        sp_search_state = (Spinner) findViewById(R.id.sp_search_state);
        sp_search_city = (Spinner) findViewById(R.id.sp_search_city);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_search = (TextView) findViewById(R.id.btn_search);
        lnr_search_btn = (LinearLayout) findViewById(R.id.lnr_search_btn);
        rel_search_list = (RelativeLayout) findViewById(R.id.rel_search_list);
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));

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
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SearchActivity.this, param, Config.API_SEARCH_ADVOCATE);
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
        adapter.setClickListener(new itemInClickListener());

        //prepareExpertIn();
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
                    search_user_center.setVisibility(View.GONE);
                    rv_search_lawyer.setVisibility(View.VISIBLE);
                    img_search_advocate.setVisibility(View.VISIBLE);
                    lnr_show_search.setVisibility(View.GONE);
                    lnr_search_btn.setVisibility(View.GONE);

                    JSONArray aa = o.getJSONArray("data");
                    list.clear();
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
                        model.setAvg_rating(ob.getString("avg_rating"));
                        model.setTotal_rating_count(ob.getString("total_rating_count"));
                        model.setTotal_case_assign(ob.getString("total_case_assign"));
                        model.setIs_available(ob.getString("is_available"));
                        model.setIs_bookmark(ob.getString("is_bookmark"));

                      /*  adapter.notifyDataSetChanged();
                        adapter.filter("");
*/
                        list.add(model);
                    }
                    LawyerList();
                    adapter.notifyDataSetChanged();
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
            String userId = model.getAh_users_id();
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
                Intent intent = new Intent(SearchActivity.this, SearchAdvocateDetailActivity.class);
                intent.putExtra("userId", userId);
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
