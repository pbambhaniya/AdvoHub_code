package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.AvalibilityModel;
import com.multipz.AdvoHub.R;

import java.util.List;

/**
 * Created by Admin on 18-12-2017.
 */

public class AvalibilityAdapter extends RecyclerView.Adapter<AvalibilityAdapter.MyViewHolder> {
    private List<AvalibilityModel> list;
    private ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_day, txt_start_date, txt_end_date;


        public MyViewHolder(View view) {
            super(view);
            txt_day = (TextView) view.findViewById(R.id.txt_day);
            txt_start_date = (TextView) view.findViewById(R.id.txt_start_date);
            txt_end_date = (TextView) view.findViewById(R.id.txt_end_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public AvalibilityAdapter(Context context, List<AvalibilityModel> list) {
        this.list = list;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avability, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AvalibilityModel model = list.get(position);
        holder.txt_day.setText(model.getDay_name());
        holder.txt_start_date.setText(model.getStart_time());
        holder.txt_end_date.setText(model.getEnd_time());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
