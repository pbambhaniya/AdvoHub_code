package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.CaseListModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Constant_method;

import java.util.List;

/**
 * Created by Admin on 11-12-2017.
 */

public class CaseListAdapter extends RecyclerView.Adapter<CaseListAdapter.MyViewHolder> {
    private List<CaseListModel> list;
    private ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_des, txt_date, txt_location, txt_lawyer_type;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txt_des = (TextView) view.findViewById(R.id.txt_des);
            txt_lawyer_type = (TextView) view.findViewById(R.id.txt_lawyer_type);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public CaseListAdapter(List<CaseListModel> expertsList, Context context) {
        this.list = expertsList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_case_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CaseListModel model = list.get(position);
        holder.txt_des.setText(model.getDescription());
        holder.txt_location.setText(model.getState_name() + "," + model.getCity_name());
        holder.txt_lawyer_type.setText(model.getLawyer_type());
        holder.txt_date.setText("Your Posted on " + Constant_method.cu_time(model.getCreate_date()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
