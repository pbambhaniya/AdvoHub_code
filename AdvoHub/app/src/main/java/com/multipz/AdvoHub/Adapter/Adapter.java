package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.Model;
import com.multipz.AdvoHub.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 30-11-2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context context;
    private List<Model> list;
    private ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }


    public Adapter(List<Model> expertsList, Context context) {
        this.list = expertsList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName;
        public CircleImageView imguser;
        public RelativeLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            imguser = (CircleImageView) view.findViewById(R.id.imguser);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Model model = list.get(position);
        holder.txtName.setText(model.getText());
        Picasso.with(context).load(R.drawable.wedding).into(holder.imguser);
//        Picasso.with(context).load(R.drawable.unnamed).into(holder.imguser);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
