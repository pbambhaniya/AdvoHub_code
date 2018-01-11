package com.multipz.AdvoHub.User;

import android.Manifest;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.AppController;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.GetPathFromURI;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;
import com.multipz.AdvoHub.util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AskQuestionActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    private ImageView img_back;
    private RelativeLayout img_attachment, lnr_For, lnr_Problem_type, rel_root;
    private LinearLayout lnr_range;
    private EditText edt_title, edt_ask_description;
    private TextView btnSubmit;
    private String param = "", title = "", desc = "", ah_ask_question_id = "";
    private ProgressDialog dialog;
    private Context context;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
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

        img_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ah_ask_question_id.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Add your Ask Question then file attached");
                } else {
                    selectImage();
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_title.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Title");
                } else if (edt_ask_description.getText().toString().trim().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), "Please Enter Description");
                } else {
                    title = edt_title.getText().toString().trim();
                    desc = edt_ask_description.getText().toString().trim();
                    AskQuestion(title, desc);

                }

            }
        });
    }

    private void AskQuestion(String title, String desc) {

        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.CreateQuestion);
            JSONObject user = new JSONObject();
            user.put("ah_ask_question_user_id", "1");//ah_ask_question_id
            user.put("title", title);
            user.put("description", desc);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, AskQuestionActivity.this, param, Config.API_ADD_ASK_QUESTION);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_attachment = (RelativeLayout) findViewById(R.id.img_attachment);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_ask_description = (EditText) findViewById(R.id.edt_ask_description);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_ADD_ASK_QUESTION) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    edt_title.setText("");
                    edt_ask_description.setText("");
                    ah_ask_question_id = o.getString("ah_ask_question_id");
                    Toaster.getToast(getApplicationContext(), Message);
                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    /********************************************Start Attachment***********************************/
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AskQuestionActivity.this);
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
                            ActivityCompat.requestPermissions(AskQuestionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                        }
                    } else {
                        ActivityCompat.requestPermissions(AskQuestionActivity.this, new String[]{Manifest.permission.CAMERA}, 145);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (checkPermission()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 2);
                    } else {
                        ActivityCompat.requestPermissions(AskQuestionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission_camera() {
        int result = ContextCompat.checkSelfPermission(AskQuestionActivity.this, Manifest.permission.CAMERA);
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
                    ActivityCompat.requestPermissions(AskQuestionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 144);

                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                // Uri contentURI = data.getData();
               /* if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    ArrayList<String> pathArray = new ArrayList<>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        //mArrayUri.add(uri);
                        String path = GetPathFromURI.getPath(getApplicationContext(), uri);
                        //pathArray.add(path);
                        imageUpload(path);
                    }
                }*/
               /* Uri contentURI = data.getData();
                if (null != contentURI) {
                    String path = GetPathFromURI.getPath(getApplicationContext(), contentURI);
                    imageUpload(path);
                }*/
            }
        }
    }


    private void imageUpload(final String imagePath) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Attachment File");
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        // dialog.show();
        String paramURL = Config.BASE_URL + "/UploadAttachment";
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
                // Toaster.getToast(getApplicationContext(), "" + error);
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




        smr.addFile(ah_ask_question_id, imagePath);

        smr.setFixedStreamingMode(true);
        smr.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(smr);
    }
    /********************************************End Attachment***********************************/

}
