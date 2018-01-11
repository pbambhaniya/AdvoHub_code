package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.multipz.AdvoHub.Adapter.SelectAvailabilityAdapter;
import com.multipz.AdvoHub.Adapter.SpinnerDaysAdapter;
import com.multipz.AdvoHub.Model.SelectAvailabilityModel;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.ItemClickListener;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.RecyclerItemTouchHelper;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SelectAvalibilty extends AppCompatActivity implements ItemClickListener, MyAsyncTask.AsyncInterface, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private Spinner spinner2;
    private TextView txt_save;
    private ArrayList<SelectAvailabilityModel> list;
    private EditText edtStartTimeMon, edtEndTimeMon;
    private String format = "";
    private SelectAvailabilityAdapter adapterlist;
    private RecyclerView listavailability;
    private Shared shared;
    private Context context;
    ArrayList<SpinnerModel> ObjectDays;
    private String param = "", daysID = "", startTime = "", endTime = "";
    private int removePos = 0;
    private ImageView img_back;
    private RelativeLayout rel_root;
    private Paint p = new Paint();
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avalibilty);
        context = this;
        shared = new Shared(context);
        reference();
        init();


    }

    private void init() {
        getDays();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.textDays);
                if (!ObjectDays.get(i).getName().contentEquals("Days")) {
                    daysID = ObjectDays.get(i).getid();
                    textView.setTextColor(Color.WHITE);
                } else {
                    textView.setTextColor(Color.WHITE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        edtStartTimeMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SelectAvalibilty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //startTime = hour + ":" + minute /*+ ":" + "00"*/;
                        getTimeFormate(selectedHour, selectedMinute, view);
                        startTime = edtStartTimeMon.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        edtEndTimeMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SelectAvalibilty.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
                        //endTime = hourOfDay + ":" + selectedMinute /*+ ":" + "00"*/;
                        getTimeFormate(hourOfDay, selectedMinute, view);
                        endTime = edtEndTimeMon.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (daysID.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Day");
                } else if (edtStartTimeMon.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Start Time");
                } else if (edtEndTimeMon.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select End Time");
                } else {
                    createlawyeravailability(daysID, startTime, endTime);
                 /*   SelectAvailabilityModel model = new SelectAvailabilityModel();
                    model.setDay_name("");
                    model.setStart_time(edtStartTimeMon.getText().toString());
                    model.setEnd_time(edtEndTimeMon.getText().toString());
                    list.add(model);

                    adapterlist = new SelectAvailabilityAdapter(getApplicationContext(), list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    listavailability.setLayoutManager(mLayoutManager);
                    listavailability.setAdapter(adapterlist);
                    adapterlist.setClickListener(SelectAvalibilty.this);*/

                }

            }
        });

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void reference() {
        list = new ArrayList<>();
        ObjectDays = new ArrayList<>();
        getLawyerAvailabilty();

        ObjectDays.add(new SpinnerModel("", "Days"));
        txt_save = (TextView) findViewById(R.id.txt_save);
        spinner2 = (Spinner) findViewById(R.id.sp_day);
        img_back = (ImageView) findViewById(R.id.img_back);
        edtStartTimeMon = (EditText) findViewById(R.id.edtStartTimeMonnew);
        edtEndTimeMon = (EditText) findViewById(R.id.edtEndTimeMonnew);
        listavailability = (RecyclerView) findViewById(R.id.listavailability);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void createlawyeravailability(String daysID, String startTime, String endTime) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.createlawyeravailability);
            JSONObject user = new JSONObject();
            user.put("Ah_lawyer_availability_id", "");
            user.put("lawyer_id", "1");
            user.put("ah_day_id", daysID);
            user.put("start_time", startTime);
            user.put("end_time", endTime);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvalibilty.this, param, Config.API_ADD_CREATE_LAYER_AVAILABILTY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void getTimeFormate(int hourOfDay, int selectedMinute, View view) {
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        if (view.getId() == R.id.edtStartTimeMonnew) {
            edtStartTimeMon.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        } else if (view.getId() == R.id.edtEndTimeMonnew) {
            edtEndTimeMon.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        }

    }

    private void getDays() {
        String param = "{\"action\":\"" + Config.Action_getdays + "\"}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvalibilty.this, param, Config.API_GETDAYS);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletelawyercourt(String lawyer_availabilty_id) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.deletelawyeravailability);
            JSONObject user = new JSONObject();
            user.put("ah_lawyer_availability_id", lawyer_availabilty_id);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvalibilty.this, param, Config.API_DELETE_LAWYER_AVAILABILITY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLawyerAvailabilty() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.getlawyeravailabilitybyId);
            JSONObject user = new JSONObject();
            user.put("lawyer_id", "1");
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, SelectAvalibilty.this, param, Config.API_GET_LAWYER_AVAILABILTY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        removePos = position;
        SelectAvailabilityModel model = list.get(position);
       /* if (view.getId() == R.id.btnDelete) {
            String ah_lawyer_availability_id = model.getAh_lawyer_availability_id();
            deletelawyercourt(ah_lawyer_availability_id);
        }*/
    }


    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_GETDAYS) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray aa = o.getJSONArray("data");
                    shared.putString(Config.getDays, aa.toString());
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject objects = aa.getJSONObject(i);
                        SpinnerModel spinnerModel = new SpinnerModel();
                        spinnerModel.setid(objects.getString("ah_day_id"));
                        spinnerModel.setName(objects.getString("day_name"));
                        ObjectDays.add(spinnerModel);

                    }
                    spinner2.setAdapter(new SpinnerDaysAdapter(this, ObjectDays));
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_ADD_CREATE_LAYER_AVAILABILTY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    edtStartTimeMon.setText("");
                    edtEndTimeMon.setText("");
                    spinner2.setSelection(0);
                    Toaster.getToast(getApplicationContext(), Message);
                    getLawyerAvailabilty();
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_DELETE_LAWYER_AVAILABILITY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                    adapterlist.removeItem(removePos);
                    adapterlist.notifyDataSetChanged();

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (flag == Config.API_GET_LAWYER_AVAILABILTY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray aa = o.getJSONArray("data");
                    list.clear();
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject obj = aa.getJSONObject(i);
                        SelectAvailabilityModel model = new SelectAvailabilityModel();
                        model.setAh_day_id(obj.getString("ah_day_id"));
                        model.setDay_name(obj.getString("day_name"));
                        model.setAh_lawyer_availability_id(obj.getString("ah_lawyer_availability_id"));
                        model.setStart_time(obj.getString("start_time"));
                        model.setEnd_time(obj.getString("end_time"));
                        list.add(model);
                    }

                    adapterlist = new SelectAvailabilityAdapter(getApplicationContext(), list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    listavailability.setLayoutManager(mLayoutManager);
                    listavailability.setAdapter(adapterlist);
                    adapterlist.setClickListener(SelectAvalibilty.this);
                    adapterlist.notifyDataSetChanged();
                    //Toaster.getToast(getApplicationContext(), Message);
                    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listavailability);

                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SelectAvailabilityAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = list.get(viewHolder.getAdapterPosition()).getDay_name();
            // backup of removed item for undo purpose
            final SelectAvailabilityModel deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            String ah_lawyer_availability_id = deletedItem.getAh_lawyer_availability_id();

            deletelawyercourt(ah_lawyer_availability_id);
            // remove the item from recycler view
            //adapterlist.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
          /*  Snackbar snackbar = Snackbar
                    .make(rel_root, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    adapterlist.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }
}
