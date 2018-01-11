package com.multipz.AdvoHub.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    ImageView img_back;
    CircleImageView img_dp;
    Context context;
    ProgressDialog dialog;
    String param, SelectProfile = "", Name, Email;
    Button btn_update;
    EditText et_full_name, et_email;
    private ArrayList<SpinnerModel> object_city, StateByCountry;
    Spinner sp_city, sp_state;
    Shared shared;
    private RelativeLayout rel_root;
    private String ah_state_id = "", ah_city_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        btn_update = (Button) findViewById(R.id.btn_update);
        et_email = (EditText) findViewById(R.id.et_email);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        et_full_name = (EditText) findViewById(R.id.et_full_name);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectProfile = "Profile";
                selectImage();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = et_full_name.getText().toString();
                Email = et_email.getText().toString();
                if (Name.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Enter Full Name");
                } else if (Email.trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Email ID");
                } else if (!isValidEmail(Email)) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Email ID");
                } else if (Constant_method.checkConn(context)) {
                    getUpdateProfile();

                }

            }
        });


        getCity();
        getState();
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_city.getSelectedItem().toString().contentEquals("Select City")) {
                    ah_city_id = object_city.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_state.getSelectedItem().toString().contentEquals("Select State")) {
                    ah_state_id = StateByCountry.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String Fullname = getIntent().getStringExtra("name");
        String Email = getIntent().getStringExtra("email");
        String city = getIntent().getStringExtra("city");
        String state = getIntent().getStringExtra("state");
        et_email.setText(Email);
        et_full_name.setText(Fullname);
        setSpinner(sp_city, object_city, city);
        setSpinner(sp_state, StateByCountry, state);

        String img = getIntent().getStringExtra("img");
        Picasso.with(context).load(img).placeholder(R.drawable.wedding).into(img_dp);
    }

    private void getState() {
        StateByCountry = new ArrayList<>();
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
            sp_state.setAdapter(new SpinnerAdapter(this, StateByCountry));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCity() {

        object_city = new ArrayList<>();
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
    }

    private void getUpdateProfile() {


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
                        dialog.dismiss();
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(getApplicationContext(), "" + msg);

                        Intent intent = new Intent(UpdateProfileActivity.this, ActivityMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        NodataPopup();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.UpdateProfilebyType);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
                    body.put("full_name", Name);
                    body.put("email", Email);
                    body.put("ah_city_id", ah_city_id);
                    main.put("body", body);
                    param = main.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
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
                            ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
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
        int result = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


    }


    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.CAMERA);
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
                    ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

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
        dialog = new ProgressDialog(UpdateProfileActivity.this);
        dialog.setTitle("Upload Image");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();

        String paramURL = "";
        paramURL = Config.BASE_URL + "/UploadProfileImage";

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST,
                paramURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status;
                        Log.d("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            status = jsonObject.getInt("status");
//                            msg = jsonObject.getString("msg");
                            dialog.dismiss();
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
                Toast.makeText(context, "Timeout Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        smr.addFile("31", imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);
    }

    private void setSpinner(Spinner asset_id, ArrayList<SpinnerModel> object_city, String assetsName) {
        for (int i = 0; i < object_city.size(); i++) {
            if (assetsName.contentEquals(object_city.get(i).getName())) {
                asset_id.setSelection(i);
                break;
            }
        }
    }

    private void NodataPopup() {

        LayoutInflater inflater = UpdateProfileActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_no_data, null);

        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
