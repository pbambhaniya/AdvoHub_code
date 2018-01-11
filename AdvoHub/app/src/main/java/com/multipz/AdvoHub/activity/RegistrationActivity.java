package com.multipz.AdvoHub.activity;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.multipz.AdvoHub.util.MultiSelectSpinner;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegistrationActivity extends AppCompatActivity implements MultiSelectSpinner.OnMultipleItemsSelectedListener, CompoundButton.OnCheckedChangeListener {
    String[] array = {"None", "Apple", "Google", "Facebook", "Tesla", "IBM", "Twitter"};
    Button btn_create_profile;
    RadioButton radioMale, radioFemale, rb_criminal, rb_family, rb_hindi, rb_english, rb_high_court, rb_supreme_court;
    MultiSelectSpinner spinner;
    CheckBox cb_message, cb_call, cb_consulte;
    CircleImageView img_registration;
    Context context;
    private ProgressDialog dialog;
    String images = "", Select_Profile_img = "", Select_Reg_Proof = "", Select_Id_proof = "", Select_qulification_proof = "", param;
    Shared shared;
    ImageView img_id_proof, img_qualification_proof, img_reg_proof;
    Spinner sp_specialization, sp_city;
    ArrayList<SpinnerModel> objects_specilization, object_city;
    EditText et_lawyer_qualification_no, et_experience, et_reg_no;
    String Profile = "", Id_Proof = "", Qua_proof = "", Reg_proof = "", City = "", specilization = "", experience = "", reg_no = "", call = "N", message = "Y", cunsult = "N", Gender = "M", lawyer_qua = "";
    RadioGroup radioSex;
    LinearLayout contact_through;
    private RelativeLayout rel_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        shared = new Shared(context);
        objects_specilization = new ArrayList<>();
        object_city = new ArrayList<>();

        reference();
        init();


    }

    private void init() {
        radioMale.setChecked(true);
        radioMale.setButtonDrawable(R.drawable.check_box);
        radioFemale.setButtonDrawable(R.drawable.check_box);

        cb_message.setButtonDrawable(R.drawable.check_box);
        cb_call.setButtonDrawable(R.drawable.check_box);
        cb_consulte.setButtonDrawable(R.drawable.check_box);


        rb_criminal.setButtonDrawable(R.drawable.check_box);
        rb_family.setButtonDrawable(R.drawable.check_box);

        rb_hindi.setButtonDrawable(R.drawable.check_box);
        rb_english.setButtonDrawable(R.drawable.check_box);

        cb_call.setOnCheckedChangeListener(this);
        cb_message.setOnCheckedChangeListener(this);
        cb_consulte.setOnCheckedChangeListener(this);

        radioMale.setOnCheckedChangeListener(this);
        radioFemale.setOnCheckedChangeListener(this);


        spinner.setItems(array);
        spinner.hasNoneOption(true);
        spinner.setSelection(new int[]{0});
        spinner.setListener(this);


        img_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Profile_img = "profile";
                Select_Reg_Proof = "";
                Select_Id_proof = "";
                Select_qulification_proof = "";
                selectImage();
            }
        });

        img_reg_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Reg_Proof = "reg_proof";
                Select_Profile_img = "";
                Select_Id_proof = "";
                Select_qulification_proof = "";
                selectImage();
            }
        });
        img_id_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Id_proof = "id_proof";
                Select_Reg_Proof = "";
                Select_Profile_img = "";
                Select_qulification_proof = "";
                selectImage();
            }
        });

        img_qualification_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_qulification_proof = "qualification_proof";
                Select_Id_proof = "";
                Select_Reg_Proof = "";
                Select_Profile_img = "";
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

        objects_specilization.add(new SpinnerModel("", "Select Specialization"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.Specilization, "[{}]"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_lawyer_type_id"));
                spinnerModel.setName(object.getString("title"));
                objects_specilization.add(spinnerModel);

            }
            sp_specialization.setAdapter(new SpinnerAdapter(this, objects_specilization));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sp_specialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_specialization.getSelectedItem().toString().contentEquals("Select Specialization")) {
                    specilization = objects_specilization.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


        btn_create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lawyer_qua = et_lawyer_qualification_no.getText().toString().trim();
                experience = et_experience.getText().toString().trim();
                reg_no = et_reg_no.getText().toString().trim();

                /*City = object_city.get(sp_city.getSelectedItemPosition()).getid();
                specilization = objects_specilization.get(sp_specialization.getSelectedItemPosition()).getid();*/

                if (Gender.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Gender");
                } else if (City.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select City");
                } else if (specilization.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Select Specialization");
                } else if (experience.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Lawyer Experiense");
                } else if (lawyer_qua.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Lawyer Qualification");
                } else if (reg_no.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Lawyer Registration Number");
                }/* else if (Select_Reg_Proof.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Select Registration Proof");
                } else if (Select_qulification_proof.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Select Qualification Proof");
                } else if (Select_Id_proof.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Select Id Proof");
                }*/ else if (Constant_method.checkConn(context)) {
                    getCreateProfile();
                }

//                Intent intent = new Intent(RegistrationActivity.this, ActivityMenu.class);
//                startActivity(intent);
            }
        });
    }

    private void reference() {

        btn_create_profile = (Button) findViewById(R.id.btn_create_profile);
        radioSex = (RadioGroup) findViewById(R.id.radioSex);

        spinner = (MultiSelectSpinner) findViewById(R.id.spinner);
        sp_specialization = (Spinner) findViewById(R.id.sp_specialization);
        sp_city = (Spinner) findViewById(R.id.sp_city);

        cb_message = (CheckBox) findViewById(R.id.cb_message);
        cb_call = (CheckBox) findViewById(R.id.cb_call);
        cb_consulte = (CheckBox) findViewById(R.id.cb_consulte);

        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        rb_criminal = (RadioButton) findViewById(R.id.rb_criminal);
        rb_family = (RadioButton) findViewById(R.id.rb_family);
        rb_hindi = (RadioButton) findViewById(R.id.rb_hindi);
        rb_english = (RadioButton) findViewById(R.id.rb_english);

        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        img_id_proof = (ImageView) findViewById(R.id.img_id_proof);
        img_qualification_proof = (ImageView) findViewById(R.id.img_qualification_proof);
        img_reg_proof = (ImageView) findViewById(R.id.img_reg_proof);

        et_experience = (EditText) findViewById(R.id.et_experience);
        et_reg_no = (EditText) findViewById(R.id.et_reg_no);
        et_lawyer_qualification_no = (EditText) findViewById(R.id.et_lawyer_qualification_no);
        contact_through = (LinearLayout) findViewById(R.id.contact_through);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }


    private void getCreateProfile() {
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
                        Toaster.getToast(getApplicationContext(), "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
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
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.setLongProfile);
                    JSONObject user = new JSONObject();
                    user.put("ah_users_id", "27");//shared.getUserId()
                    user.put("type", shared.getUsertype());
                    user.put("gender", Gender);
                    user.put("title", "xyz");
                    user.put("ah_city_id", City);
                    user.put("call_service", call);
                    user.put("message_service", message);
                    user.put("consult_service", cunsult);
                    user.put("ah_specialication_id", specilization);
                    user.put("year_of_experience", experience);
                    user.put("education_qualification", lawyer_qua);
                    user.put("advocate_registration_number", reg_no);
                    user.put("breif_about", "sdsdsd");
                    user.put("notification_token", FirebaseInstanceId.getInstance().getToken());
                    main.put("body", user);
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


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Toast.makeText(this.getApplicationContext(), "Selected Companies" + strings, Toast.LENGTH_LONG).show();
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
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
                            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


    }


    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA);
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
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

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

                    if (Select_Profile_img.contentEquals("profile")) {
                        img_registration.setImageBitmap(BitmapFactory.decodeFile(img));
                    } else if (Select_Reg_Proof.contentEquals("reg_proof")) {
                        img_reg_proof.setImageBitmap(BitmapFactory.decodeFile(img));
                    } else if (Select_Id_proof.contentEquals("id_proof")) {
                        img_id_proof.setImageBitmap(BitmapFactory.decodeFile(img));
                    } else if (Select_qulification_proof.contentEquals("qualification_proof")) {
                        img_qualification_proof.setImageBitmap(BitmapFactory.decodeFile(img));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri contentURI = data.getData();
                if (null != contentURI) {
                    String path = GetPathFromURI.getPath(getApplicationContext(), contentURI);
                    imageUpload(path);
                    Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                    if (Select_Profile_img.contentEquals("profile")) {
                        img_registration.setImageBitmap(thumbnail);
                    } else if (Select_Reg_Proof.contentEquals("reg_proof")) {
                        img_reg_proof.setImageBitmap(thumbnail);
                    } else if (Select_Id_proof.contentEquals("id_proof")) {
                        img_id_proof.setImageBitmap(thumbnail);
                    } else if (Select_qulification_proof.contentEquals("qualification_proof")) {
                        img_qualification_proof.setImageBitmap(thumbnail);
                    }

                }
            }
        }
    }

    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(RegistrationActivity.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        String paramURL = "";
        if (Select_Profile_img.contentEquals("profile")) {
            paramURL = Config.BASE_URL + "/UploadProfileImage";
        } else if (Select_Reg_Proof.contentEquals("reg_proof")) {
            paramURL = Config.BASE_URL + "/UploadRegistrationProof";
        } else if (Select_Id_proof.contentEquals("id_proof")) {
            paramURL = Config.BASE_URL + "/UploadIdProof";
        } else if (Select_qulification_proof.contentEquals("qualification_proof")) {
            paramURL = Config.BASE_URL + "/UploadQualificationProof";
        }

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                paramURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        String msg;
                        Log.d("Response", response);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cb_call) {
            if (b) {
                call = "Y";
            } else {
                call = "N";
            }

        } else if (compoundButton.getId() == R.id.cb_message) {
            if (b) {
                message = "Y";
            } else {
                message = "N";
            }

        } else if (compoundButton.getId() == R.id.cb_consulte) {
            if (b) {
                cunsult = "Y";
            } else {
                cunsult = "N";
            }
        }

    }


}