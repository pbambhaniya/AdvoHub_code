package com.multipz.AdvoHub.User;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditUserProfileActivity extends AppCompatActivity {
    private EditText et_fullname, et_Email, edt_emergency_Contact_no, edt_emergency_Contact_name, edt_Profession;
    private Button btn_user_update_profile;
    private Spinner sp_my_profile_city, sp_state;
    private CircleImageView img_my_profile;
    private ArrayList<SpinnerModel> City, State;
    private Shared shared;
    private Context context;
    private String ah_city_id = "", ah_state_id = "";
    private ProgressDialog dialog;
    private String EditProfile = "", param = "", fullname = "", email = "", eme_no = "", eme_name = "", profession = "", SelectImage = "", img = "";
    private ImageView img_back;
    private RelativeLayout rel_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        context = this;
        shared = new Shared(context);
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
            sp_my_profile_city.setAdapter(new SpinnerAdapter(EditUserProfileActivity.this, City));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        State.add(new SpinnerModel("", "Select State"));
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(object.getString("ah_state_id"));
                spinnerModel.setName(object.getString("state_name"));
                State.add(spinnerModel);

            }
            sp_state.setAdapter(new SpinnerAdapter(EditUserProfileActivity.this, State));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_state.getSelectedItem().toString().contentEquals("Select State")) {
                    ah_state_id = State.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_my_profile_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!sp_my_profile_city.getSelectedItem().toString().contentEquals("Select City")) {
                    ah_city_id = City.get(i).getid();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (getIntent().getBooleanExtra("EditProfile", false)) {
            fullname = getIntent().getStringExtra("fullname");
            email = getIntent().getStringExtra("email");
            eme_no = getIntent().getStringExtra("eme_no");
            eme_name = getIntent().getStringExtra("eme_name");
            profession = getIntent().getStringExtra("profession");
            ah_city_id = getIntent().getStringExtra("city");
            ah_state_id = getIntent().getStringExtra("state");
            img = getIntent().getStringExtra("img");
            et_fullname.setText(fullname);
            et_Email.setText(email);
            edt_emergency_Contact_no.setText(eme_no);
            edt_emergency_Contact_name.setText(eme_name);
            edt_Profession.setText(profession);
            setSpinner(sp_my_profile_city, City, ah_city_id);
            setSpinner(sp_state, State, ah_state_id);
            Picasso.with(context).load(Config.ProfileImage + "" + img).placeholder(R.drawable.wedding).into(img_my_profile);
        }


        img_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage = "UserProfile";
                selectImage();
            }
        });

        btn_user_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_fullname.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Full Name");
                } else if (et_Email.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Email ID");
                } else if (!isValidEmail(et_Email.getText().toString().trim())) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Email ID");
                } else if (ah_state_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter State");
                } else if (ah_city_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter City");
                } else if (edt_emergency_Contact_no.getText().toString().contentEquals("") && edt_emergency_Contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Valid Emergency Contact No");
                } else if (edt_emergency_Contact_name.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Emergency Name");
                } else if (edt_Profession.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Profession");
                } else {
                    fullname = et_fullname.getText().toString().trim();
                    email = et_Email.getText().toString().trim();
                    eme_no = edt_emergency_Contact_no.getText().toString().trim();
                    eme_name = edt_emergency_Contact_name.getText().toString().trim();
                    profession = edt_Profession.getText().toString().trim();
                    UpdateUserProfile();
                }


            }
        });

    }


    private void setSpinner(Spinner asset_id, ArrayList<SpinnerModel> object_city, String assetsName) {
        for (int i = 0; i < object_city.size(); i++) {
            if (assetsName.contentEquals(object_city.get(i).getName())) {
                asset_id.setSelection(i);
                break;
            }
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void reference() {
        et_fullname = (EditText) findViewById(R.id.et_fullname);
        et_Email = (EditText) findViewById(R.id.et_Email);
        edt_emergency_Contact_no = (EditText) findViewById(R.id.edt_emergency_Contact_no);
        edt_emergency_Contact_name = (EditText) findViewById(R.id.edt_emergency_Contact_name);
        edt_Profession = (EditText) findViewById(R.id.edt_Profession);
        btn_user_update_profile = (Button) findViewById(R.id.btn_user_update_profile);
        sp_my_profile_city = (Spinner) findViewById(R.id.sp_my_profile_city);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        img_my_profile = (CircleImageView) findViewById(R.id.img_my_profile);
        img_back = (ImageView) findViewById(R.id.img_back);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void UpdateUserProfile() {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
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
                        Intent i = new Intent(EditUserProfileActivity.this, UserDrawerActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
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
                Toaster.getToast(getApplicationContext(), "TimeOut");


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.UpdateProfilebyType);
                    JSONObject user = new JSONObject();
                    user.put("ah_users_id", shared.getUserId());//30
                    user.put("type", shared.getUsertype());
                    user.put("full_name", fullname);
                    user.put("email", email);
                    user.put("ah_city_id", ah_city_id);
                    user.put("emergency_contact_number", eme_no);
                    user.put("emergency_contact_name", eme_name);
                    user.put("profession", profession);
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

    /************************************************************************************************************/

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this);
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
                            ActivityCompat.requestPermissions(EditUserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(EditUserProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(EditUserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
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
        int result = ContextCompat.checkSelfPermission(EditUserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }


    }


    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(EditUserProfileActivity.this, Manifest.permission.CAMERA);
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
                    ActivityCompat.requestPermissions(EditUserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

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
                    img_my_profile.setImageBitmap(BitmapFactory.decodeFile(img));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri contentURI = data.getData();
                if (null != contentURI) {
                    String path = GetPathFromURI.getPath(getApplicationContext(), contentURI);
                    imageUpload(path);
                    Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                    img_my_profile.setImageBitmap(thumbnail);


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
               /* String param = "{\"action\":\"imageUpload\",\"body\":{\"type\":\"profile\",\"ah_users_id\":\"1\"}}";
                params.put("json", param);*/
                return params;
            }
        };

        smr.addFile("30", imagePath);
        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);
    }
}
