package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.ReportModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 04-12-2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    private List<ReportModel> list;
    private ClickListener clickListener;
    Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView img;
        public TextView txt_person_name, txt_type, txt_time;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);

            img = (CircleImageView) view.findViewById(R.id.img);
            txt_person_name = (TextView) view.findViewById(R.id.txt_person_name);
            txt_type = (TextView) view.findViewById(R.id.txt_type);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
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


    public ReportAdapter(Context context, List<ReportModel> expertsList) {
        this.context = context;
        this.list = expertsList;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReportModel model = list.get(position);
        holder.txt_person_name.setText(model.getName());
        holder.txt_type.setText(model.getType());
        holder.txt_time.setText(model.getTime());
        Picasso.with(context).load(R.drawable.ic_def_feed).into(holder.img);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
