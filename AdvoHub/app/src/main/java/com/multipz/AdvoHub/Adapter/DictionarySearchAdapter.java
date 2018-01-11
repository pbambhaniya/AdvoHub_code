package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.DictionaryModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11-12-2017.
 */

public class DictionarySearchAdapter extends RecyclerView.Adapter<DictionarySearchAdapter.MyViewHolder> {
    private List<DictionaryModel> expertsListMain;
    private List<DictionaryModel> expertsList;
    private DictionarySearchAdapter.ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_word;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txt_word = (TextView) view.findViewById(R.id.txt_word);
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


    public DictionarySearchAdapter(List<DictionaryModel> expertsList, Context context) {
        this.expertsList = new ArrayList<>();
        this.expertsListMain = expertsList;
        this.expertsList.addAll(expertsListMain);
    }

    public void setClickListener(DictionarySearchAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public DictionarySearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dictionary, parent, false);
        return new DictionarySearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DictionarySearchAdapter.MyViewHolder holder, int position) {
        DictionaryModel model = expertsList.get(position);
        holder.txt_word.setText(model.getTitle());
    }

    public void filter(String text) {
        //new array list that will hold the filtered data
        expertsList.clear();
        //looping through existing elements
        for (DictionaryModel s : expertsListMain) {
            if (s.getTitle().toLowerCase().contains(text.toLowerCase())) {
                expertsList.add(s);
            }
        }
//        if (text.length() <= 0) {
//            expertsList.addAll(expertsListMain);
//        }

        //calling a method of the adapter class and passing the filtered list
        filterList(expertsList);
    }

    public void filterList(List<DictionaryModel> filterdNames) {
        this.expertsList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return expertsList.size();
    }
}
