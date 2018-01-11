package com.multipz.AdvoHub.User;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.multipz.AdvoHub.Adapter.AvalibilityAdapter;
import com.multipz.AdvoHub.Model.AvalibilityModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AvalabilityViewActivity extends AppCompatActivity {
    RecyclerView rv_avability;
    Shared shared;
    AvalibilityAdapter adapter;
    ArrayList<AvalibilityModel> list;
    Context context;
    private LinearLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avalability_view);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        rv_avability = (RecyclerView) findViewById(R.id.rv_avability);

        rel_root = (LinearLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((LinearLayout) findViewById(R.id.rel_root));
        getData();

    }

    private void getData() {
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.Avability, "[{}]"));
            for (int f = 0; f < jsonArray.length(); f++) {
                JSONObject e = jsonArray.getJSONObject(f);
                AvalibilityModel avalibilityModel = new AvalibilityModel();
                avalibilityModel.setDay_name(e.getString("day_name"));
                avalibilityModel.setAh_day_id(e.getString("ah_day_id"));
                avalibilityModel.setStart_time(Constant_method.getTime(e.getString("start_time")));
                avalibilityModel.setEnd_time(Constant_method.getTime(e.getString("end_time")));
                avalibilityModel.setAh_lawyer_availability_id(e.getString("ah_lawyer_availability_id"));
                list.add(avalibilityModel);

            }


            adapter = new AvalibilityAdapter(context, list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rv_avability.setLayoutManager(mLayoutManager);
            rv_avability.setItemAnimator(new DefaultItemAnimator());
            rv_avability.setAdapter(adapter);
            rv_avability.setNestedScrollingEnabled(false);
            adapter.setClickListener(new itemInClickListener());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class itemInClickListener implements AvalibilityAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            Toaster.getToast(getApplicationContext(), "" + position);

        }
    }
}
