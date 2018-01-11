package com.multipz.AdvoHub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.multipz.AdvoHub.Adapter.DictionarySearchAdapter;
import com.multipz.AdvoHub.Model.DictionaryModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.MyAsyncTask;
import com.multipz.AdvoHub.util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    ImageView img_search, img_back;
    RelativeLayout rel_search, rel_root;
    private String param = "";
    private ArrayList<DictionaryModel> list;
    private Shared shared;
    private Context context;
    private DictionarySearchAdapter adapter;
    private RecyclerView rv_dictonary;
    private EditText edit_search_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        reference();
        init();

    }

    private void init() {
        getDictionary();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getDictionary() {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.getDictionary);
            JSONObject user = new JSONObject();
            user.put("title", "");
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, DictionaryActivity.this, param, Config.API_GET_DICTIONARY);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
      /*  img_search = (ImageView) findViewById(R.id.img_search);
        edit_search_item = (EditText) findViewById(R.id.edit_search_item);*/
        rv_dictonary = (RecyclerView) findViewById(R.id.rv_serach);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == Config.API_GET_DICTIONARY) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    JSONArray aa = o.getJSONArray("data");
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject obj = aa.getJSONObject(i);
                        String ah_dictionary_id = obj.getString("ah_dictionary_id");
                        String title = obj.getString("title");
                        String description = obj.getString("description");

                        DictionaryModel model = new DictionaryModel();
                        model.setAh_dictionary_id(ah_dictionary_id);
                        model.setTitle(title);
                        model.setDescription(description);
                        list.add(model);
                    }
                    adapter = new DictionarySearchAdapter(list, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rv_dictonary.setLayoutManager(mLayoutManager);
                    rv_dictonary.setItemAnimator(new DefaultItemAnimator());
                    rv_dictonary.setAdapter(adapter);
                    adapter.setClickListener(new itemInClickListener());
                    //Toaster.getToast(getApplicationContext(), Message);


                } else if (status.contentEquals("0")) {
                    pd.dismiss();
                    Message = o.getString("msg");
                    NodataPopup(Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class itemInClickListener implements DictionarySearchAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            DictionaryModel model = list.get(position);
            Intent intent = new Intent(DictionaryActivity.this, DictionaryDetailActivity.class);
            intent.putExtra("Name", model.getTitle());
            intent.putExtra("ah_dictionary_id", model.getAh_dictionary_id());
            startActivity(intent);
        }
    }


    private void NodataPopup(String msg) {
        LayoutInflater inflater = DictionaryActivity.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_no_data, null);
        LinearLayout cancel = (LinearLayout) c.findViewById(R.id.cancel);
        TextView head_desc = (TextView) c.findViewById(R.id.head_desc);
        AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
//        RelativeLayout rel_root = (RelativeLayout) findViewById(R.id.rel_root);
//        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        head_desc.setText(msg);
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
}
