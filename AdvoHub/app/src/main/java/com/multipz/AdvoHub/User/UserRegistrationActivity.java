package com.multipz.AdvoHub.User;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.AdvoHub.Adapter.SpinnerAdapter;
import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.GetPathFromURI;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegistrationActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    TextView txt_start_date;
    private RelativeLayout root_layout;
    CircleImageView img_dp;
    Context context;
    Shared shared;
    String Select_Profile_img = "", Profile = "", param = "", Gender = "", ContactName = "", Contact_no = "", Profession = "", Date = "", City = "";
    private ProgressDialog dialog;
    RadioButton radioMale, radioFemale;
    RadioGroup radioSex;
    EditText et_contact_name, et_contact_no, et_profesion;
    TextView txt_send_req;
    private Spinner sp_city;
    ArrayList<SpinnerModel> object_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        context = this;
        shared = new Shared(context);
        object_city = new ArrayList<>();
        Ref();
        Init();


    }

    private void Ref() {
        txt_start_date = (TextView) findViewById(R.id.txt_start_date);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioSex = (RadioGroup) findViewById(R.id.radioSex);
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        et_contact_name = (EditText) findViewById(R.id.et_contact_name);
        et_contact_no = (EditText) findViewById(R.id.et_contact_no);
        et_profesion = (EditText) findViewById(R.id.et_profesion);
        txt_send_req = (TextView) findViewById(R.id.txt_send_req);
        sp_city = (Spinner) findViewById(R.id.sp_city);


        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));
    }

    private void Init() {
        radioMale.setButtonDrawable(R.drawable.check_box);
        radioFemale.setButtonDrawable(R.drawable.check_box);
        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserRegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txt_start_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        img_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Profile_img = "profile";
                selectImage();
            }
        });

        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                switch (checked) {
                    case R.id.radioMale:
                        Gender = "M";
                        break;
                    case R.id.radioFemale:
                        Gender = "F";
                        break;
                }

            }
        });

        object_city.add(new SpinnerModel("", "Select City"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.City, "[{}]"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_city_id"));
                spinnerModel.setName(object.getString("city_name"));
                object_city.add(spinnerModel);

            }
            sp_city.setAdapter(new SpinnerAdapter(this, object_city));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_city.getSelectedItem().toString().contentEquals("Select City")) {
                    City = object_city.get(i).getid();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_send_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactName = et_contact_name.getText().toString();
                Contact_no = et_contact_no.getText().toString();
                Profession = et_profesion.getText().toString();
                Date = txt_start_date.getText().toString();
                if (Profile.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.profile));
                } else if (Gender.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.gender));
                } else if (Date.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.DOB));
                } else if (ContactName.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.ContactName));
                } else if (Contact_no.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.ContactNo));
                } else if (et_contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), getResources().getString(R.string.ValidCNo));
                } else if (Profession.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), String.valueOf(R.string.Profession));
                } else if (Constant_method.checkConn(context)) {
                    getUserRegistration();
                }

            }
        });

    }

    private void getUserRegistration() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    dialog.dismiss();
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(getApplicationContext(), "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                //Toaster.getToast(getApplicationContext(), "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.setLongProfile);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", "21");
                    body.put("type", shared.getUsertype());
                    body.put("profile_img", Profile);
                    body.put("gender", Gender);
                    body.put("dob", Date);
                    body.put("ah_city_id", City);
                    body.put("emergency_contact_number", Contact_no);
                    body.put("emergency_contact_name", ContactName);
                    body.put("profession", Profession);
                    body.put("notification_token", FirebaseInstanceId.getInstance().getToken());
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

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
        builder.setTitle("Add Profile Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (checkPermission_camera()) {
                        if (checkPermission()) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            ActivityCompat.requestPermissions(UserRegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(UserRegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(UserRegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
/*

    private void LoadImage(String images) {
        Glide.with(context).load(Config.BASE_URL + images).error(getResources().getDrawable(R.drawable.wedding));
    }
*/

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(UserRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


    }


    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(UserRegistrationActivity.this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 144:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            return;
                        }
                    }
                }
                break;

            case 145:
                if (checkPermission()) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else {
                    ActivityCompat.requestPermissions(UserRegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 8;
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    String path = Environment.getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix";

                    if (!new File(path).exists()) {
                        new File(path).mkdir();
                    }

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    final String img = file.getAbsolutePath();
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //   encodedImage = encodeImage(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    imageUpload(img);
                    img_dp.setImageBitmap(BitmapFactory.decodeFile(img));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri contentURI = data.getData();
                if (null != contentURI) {
                    String path = GetPathFromURI.getPath(getApplicationContext(), contentURI);
                    imageUpload(path);
                    Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                    img_dp.setImageBitmap(thumbnail);


                }
            }
        }
    }

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                Config.BASE_URL + "/UploadProfileImage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg;
                        Log.d("Response", response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
                            dialog.dismiss();
                            if (status == 1) {
                                JSONObject object = jsonObject.getJSONObject("body");
                                msg = object.getString("msg");
                                Toaster.getToast(getApplicationContext(), msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toaster.getToast(getApplicationContext(), error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        smr.addFile("21", imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);

    }
}