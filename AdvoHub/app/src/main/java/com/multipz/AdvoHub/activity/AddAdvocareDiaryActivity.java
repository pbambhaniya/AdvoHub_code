package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.multipz.AdvoHub.Adapter.DiaryAdapter;
import com.multipz.AdvoHub.Model.DiaryModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;


public class AddAdvocareDiaryActivity extends AppCompatActivity {

    private ImageView img_back, img_setting;
    private RelativeLayout img_notification, rel_root;
    private HorizontalCalendar horizontalCalendar;
    private RecyclerView advodairylist;
    private FloatingActionButton fabAddAdvoDairy;
    private ProgressDialog dialog;
    private CompactCalendarView compactCalendarView;
    private TextView txtMonthTitle;
    String param;
    DiaryAdapter adapter;
    ArrayList<DiaryModel> list;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Context context;
    private Shared shared;
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advocare_diary);
        context = this;
        shared = new Shared(context);
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
        fabAddAdvoDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddAdvocareDiaryActivity.this, AdvocateDiaryActivity.class);
                startActivity(i);

            }
        });
    }

    private void reference() {
        list = new ArrayList<>();
        img_back = (ImageView) findViewById(R.id.img_back);
        advodairylist = (RecyclerView) findViewById(R.id.advodairylist);
        fabAddAdvoDairy = (FloatingActionButton) findViewById(R.id.fabAddAdvoDairy);
        //   img_setting = (ImageView) findViewById(R.id.img_setting);
        img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        txtMonthTitle = (TextView) findViewById(R.id.txtMonthTitle);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.getWeekNumberForCurrentMonth();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        fabAddAdvoDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAdvocareDiaryActivity.this, AdvocateDiaryActivity.class));
            }
        });
/*
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAdvocareDiaryActivity.this, SettingActivity.class));

            }
        });*/

        gotoToday();
        txtMonthTitle.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAdvocareDiaryActivity.this, NotificationActivity.class));

            }
        });
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Log.d("date", "" + dateClicked);
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                String dd = fmt.format(dateClicked);
                getDiaryData(dd);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMonthTitle.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }

        });


    }


    public void gotoToday() {
        // Set any date to navigate to particular date
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = fmt.format(date);
        getDiaryData(today);
    }

    private void getDiaryData(final String date) {

        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //   dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = "", status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    list.clear();
                    if (status.contentEquals("1")) {
                        dialog.dismiss();
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            DiaryModel model = new DiaryModel();
                            model.setAh_advocate_dairy_id(c.getString("ah_advocate_dairy_id"));
                            model.setName(c.getString("name"));
                            model.setDescription(c.getString("description"));
                            model.setDate(c.getString("date"));
                            model.setTime(c.getString("time"));
                            list.add(model);
                        }
                        adapter = new DiaryAdapter(list, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        advodairylist.setLayoutManager(mLayoutManager);
                        advodairylist.setItemAnimator(new DefaultItemAnimator());
                        advodairylist.setAdapter(adapter);
                        advodairylist.setNestedScrollingEnabled(false);
                        adapter.setClickListener(new itemInClickListener());

                    } else if (status.contentEquals("0")) {
                        dialog.dismiss();
                        Toaster.getToast(getApplicationContext(), msg);
                        adapter = new DiaryAdapter(list, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        advodairylist.setLayoutManager(mLayoutManager);
                        advodairylist.setItemAnimator(new DefaultItemAnimator());
                        advodairylist.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Toaster.getToast(getApplicationContext(), error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getDairyData);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("date", date);
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

    class itemInClickListener implements DiaryAdapter.ClickListener {
        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
