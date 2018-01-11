package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.AskQyeModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

import java.util.List;

/**
 * Created by Admin on 20-12-2017.
 */

public class AskQueViewAdapter extends RecyclerView.Adapter<AskQueViewAdapter.MyViewHolder> {
    private List<AskQyeModel> list;
    private ClickListener clickListener;
    Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_que, txt_answer;
        private LinearLayout rel_root;


        public MyViewHolder(View view) {
            super(view);
            txt_que = (TextView) view.findViewById(R.id.txt_que);
            txt_answer = (TextView) view.findViewById(R.id.txt_answer);
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


    public AskQueViewAdapter(Context context, List<AskQyeModel> list) {
        this.list = list;
        this.context = context;
    }
/*
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ask_que, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AskQyeModel model = list.get(position);
        holder.txt_que.setText(model.getTitle());
        String answer = model.getAnswer();
        if (answer.contentEquals("")) {
            holder.txt_answer.setText("Answer not yet");
        } else {
            holder.txt_answer.setText(model.getAnswer());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
