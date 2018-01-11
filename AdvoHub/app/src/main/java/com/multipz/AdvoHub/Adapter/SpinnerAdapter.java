package com.multipz.AdvoHub.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.SpinnerModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;


import java.util.ArrayList;

/**
 * Created by Admin on 13-12-2017.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context c;

    ArrayList<SpinnerModel> objects;

    public SpinnerAdapter(Context context, ArrayList<SpinnerModel> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        SpinnerModel cur_obj = objects.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.company);
        RelativeLayout rel_root = (RelativeLayout) row.findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) row.findViewById(R.id.rel_root));
        label.setText(cur_obj.getName());

        return row;
    }
}
