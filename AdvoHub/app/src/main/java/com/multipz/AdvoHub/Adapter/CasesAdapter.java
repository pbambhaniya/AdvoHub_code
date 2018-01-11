package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.CasesModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Constant_method;

import java.util.List;

/**
 * Created by Admin on 08-12-2017.
 */

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyViewHolder> {
    private List<CasesModel> list;
    private CasesAdapter.ClickListener clickListener;
    private Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtcase_title, txt_case_desc, txt_post_date;
        private LinearLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txtcase_title = (TextView) view.findViewById(R.id.txtcase_title);
            txt_case_desc = (TextView) view.findViewById(R.id.txt_case_desc);
            txt_post_date = (TextView) view.findViewById(R.id.txt_post_date);
            rel_root = (LinearLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((LinearLayout) view.findViewById(R.id.rel_root));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }

        }
    }

    public CasesAdapter(Context context, List<CasesModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(CasesAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void remove(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, list.size());
    }

    @Override
    public CasesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cases, parent, false);

        return new CasesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CasesAdapter.MyViewHolder holder, int position) {
        CasesModel model = list.get(position);
        int idstatus = model.getIdstatus();
        holder.txtcase_title.setText("Title: " + model.getTitle());
        holder.txt_case_desc.setText("Case Description: " + model.getDescription());
        holder.txt_post_date.setText("Posted Date: " + Constant_method.cu_time(model.getCreate_date()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
