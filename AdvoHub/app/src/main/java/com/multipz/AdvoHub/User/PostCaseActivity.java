package com.multipz.AdvoHub.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.AdvoHub.Adapter.SpinnerAdapter;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.activity.NotificationActivity;
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

public class PostCaseActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    ImageView img_back, img_setting;
    RelativeLayout img_notification, rel_root;
    private Spinner spn_category, spn_city, spn_state;
    private EditText edt_description, edt_title;
    private TextView btnSave;
    private ArrayList<SpinnerModel> objects_specilization, City, State;
    private Shared shared;
    private Context context;
    private String ah_city_id = "", ah_lawyer_type_id = "", ah_state_id = "";
    private String param = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_case);
        context = this;
        shared = new Shared(context);

        objects_specilization = new ArrayList<>();
        City = new ArrayList<>();
        State = new ArrayList<>();

        reference();
        init();


    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostCaseActivity.this, NotificationActivity.class));

            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostCaseActivity.this, UserSettingActivity.class));

            }
        });

        objects_specilization = new ArrayList<>();
        objects_specilization.add(new SpinnerModel("", "Select Specialisation"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.Specilization, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_lawyer_type_id"));
                spinnerModel.setName(object.getString("title"));
                objects_specilization.add(spinnerModel);

            }
            spn_category.setAdapter(new SpinnerAdapter(this, objects_specilization));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        City = new ArrayList<>();
        City.add(new SpinnerModel("", "Select City"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.CityByState, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_city_id"));
                spinnerModel.setName(object.getString("city_name"));
                City.add(spinnerModel);

            }
            spn_city.setAdapter(new SpinnerAdapter(this, City));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        State.add(new SpinnerModel("", "Select State"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_country_id"));
                spinnerModel.setName(object.getString("state_name"));
                State.add(spinnerModel);
            }
            spn_state.setAdapter(new SpinnerAdapter(this, State));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_city.getSelectedItem().toString().contentEquals("Select City")) {
                    ah_city_id = City.get(i).getid();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_state.getSelectedItem().toString().contentEquals("Select City")) {
                    ah_state_id = State.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_category.getSelectedItem().toString().contentEquals("Select Specialisation")) {
                    ah_lawyer_type_id = objects_specilization.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ah_city_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select City");
                } else if (ah_lawyer_type_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Category");
                } else if (edt_title.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Title");
                } else if (edt_description.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Description");
                } else {
                    String des = edt_description.getText().toString();
                    String title = edt_title.getText().toString();
                    getPost(des, title);
                }
            }
        });
    }

    private void getPost(String desc, String title) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.createPost);
            JSONObject user = new JSONObject();
            user.put("id", "");
            user.put("iduser", shared.getUserId());//30
            user.put("idlawyerspecialist", ah_lawyer_type_id);
            user.put("idcity", ah_city_id);
            user.put("title", title);
            user.put("description", desc);
            main.put("body", user);
            /*user.put("ah_lawyer_type_id", ah_lawyer_type_id);
            user.put("idcity", ah_city_id);
            user.put("title", title);
            user.put("description", desc);
            user.put("ah_post_user_id", "1");
            main.put("body", user);*/
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, PostCaseActivity.this, param, Config.API_CREATE_POST);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        spn_category = (Spinner) findViewById(R.id.spn_category);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_state = (Spinner) findViewById(R.id.spn_state);
        edt_description = (EditText) findViewById(R.id.edt_description);
        btnSave = (TextView) findViewById(R.id.btnSave);
        img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        edt_title = (EditText) findViewById(R.id.edt_title);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_CREATE_POST) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    Toaster.getToast(getApplicationContext(), Message);
                    Intent i = new Intent(PostCaseActivity.this, UserDrawerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
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
}
