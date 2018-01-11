package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.SearchAdvocateModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 08-12-2017.
 */

public class SearchAdvocateAdapter extends RecyclerView.Adapter<SearchAdvocateAdapter.MyViewHolder> {
    private List<SearchAdvocateModel> expertsListMain;
    private List<SearchAdvocateModel> expertsList;
    private SearchAdvocateAdapter.ClickListener clickListener;
    private Context context;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_lawyer_name, txt_lawyer_type, txt_city, txt_experiense, txt_availabilty, txt_ratting, txt_total_case;
        ImageView img_lawyer, img_favourite, imgrating_star;
        private RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txt_lawyer_name = (TextView) view.findViewById(R.id.txt_lawyer_name);
            txt_city = (TextView) view.findViewById(R.id.txt_city);
            txt_experiense = (TextView) view.findViewById(R.id.txt_experiense);
            img_lawyer = (CircleImageView) view.findViewById(R.id.img_lawyer);
            txt_availabilty = (TextView) view.findViewById(R.id.txt_availabilty);
            txt_ratting = (TextView) view.findViewById(R.id.txt_ratting);
            imgrating_star = (ImageView) view.findViewById(R.id.imgrating_star);
            txt_total_case = (TextView) view.findViewById(R.id.txt_total_case);
            img_favourite = (ImageView) view.findViewById(R.id.img_favourite);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
            Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

            img_favourite.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public SearchAdvocateAdapter(List<SearchAdvocateModel> expertsList, Context context) {

        this.expertsList = new ArrayList<>();
        this.expertsListMain = expertsList;
        this.expertsList.addAll(expertsListMain);
        this.context = context;
    }

    public void setClickListener(SearchAdvocateAdapter.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public SearchAdvocateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lawyer_list, parent, false);
        return new SearchAdvocateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchAdvocateAdapter.MyViewHolder holder, int position) {
        SearchAdvocateModel model = expertsList.get(position);
        holder.txt_lawyer_name.setText(model.getFull_name());
        holder.txt_city.setText(model.getCity_name());
        holder.txt_experiense.setText("Experience - " + model.getYear_of_experience() + " years");
        holder.txt_ratting.setText(model.getAvg_rating());
        String availability = model.getIs_available();
        if (availability.contentEquals("Not Available Today")) {
            holder.txt_availabilty.setText("Not Available Today");
            holder.txt_availabilty.setTextColor(Color.parseColor("#af1b25"));
        } else {
            holder.txt_availabilty.setText("Available Today");
            holder.txt_availabilty.setTextColor(Color.parseColor("#25AF1B"));
        }
        String avg_rating = model.getAvg_rating();
        if (avg_rating.contentEquals("0")) {
            holder.imgrating_star.setImageResource(R.mipmap.ic_unstar);
        } else {
            holder.imgrating_star.setImageResource(R.mipmap.ic_star);
        }
        holder.txt_total_case.setText("Total Case - " + model.getTotal_case_assign());

        String is_bookmark = model.getIs_bookmark();
        if (is_bookmark.contentEquals("true")) {
            model.setIscheck(true);
            holder.img_favourite.setImageResource(R.mipmap.ic_bookmark_select);
        } else {
            model.setIscheck(false);
            holder.img_favourite.setImageResource(R.mipmap.ic_bookmark);
        }
        //holder.txt_lawyer_type.setText(model.getTitle());
        String img = model.getProfile_img();
        Picasso.with(context).load(Config.ProfileImage + "" + img).placeholder(R.drawable.wedding).into(holder.img_lawyer);
    }

    public void filter(String text) {
        //new array list that will hold the filtered data
        expertsList.clear();
        //looping through existing elements
      /*  for (SearchAdvocateModel s : expertsListMain) {
            if (s.getLawyer_name().toLowerCase().contains(text.toLowerCase())) {
                expertsList.add(s);
            }
        }*/
        if (text.length() <= 0) {
            expertsList.addAll(expertsListMain);
        }

        //calling a method of the adapter class and passing the filtered list
        filterList(expertsList);
    }

    public void filterList(List<SearchAdvocateModel> filterdNames) {
        this.expertsList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return expertsList.size();
    }
}
