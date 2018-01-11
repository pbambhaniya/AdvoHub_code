package com.multipz.AdvoHub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multipz.AdvoHub.Model.SelectAvailabilityModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;
import com.multipz.AdvoHub.util.Constant_method;
import com.multipz.AdvoHub.util.ItemClickListener;

import java.util.ArrayList;


/**
 * Created by Admin on 13-12-2017.
 */

public class SelectAvailabilityAdapter extends RecyclerView.Adapter<SelectAvailabilityAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SelectAvailabilityModel> cartList;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        public TextView txtday, txtstarttime, txtendtime;
        public RelativeLayout view_foreground, view_background;
        private Button btnDelete;
        private FrameLayout rel_root;

        public MyViewHolder(View view) {
            super(view);
            txtday = (TextView) view.findViewById(R.id.txtday);
            txtstarttime = (TextView) view.findViewById(R.id.txtstarttime);
            txtendtime = (TextView) view.findViewById(R.id.txtendtime);
            view_foreground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            view_background = (RelativeLayout) view.findViewById(R.id.view_background);
            rel_root = (FrameLayout) view.findViewById(R.id.rel_root);

            Application.setFontDefault((FrameLayout) view.findViewById(R.id.rel_root));
            // btnDelete = (Button) view.findViewById(R.id.btnDelete);
            //btnDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.itemClicked(view, getAdapterPosition());

            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public SelectAvailabilityAdapter(Context context, ArrayList<SelectAvailabilityModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_availabilty, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SelectAvailabilityModel item = cartList.get(position);
        holder.txtday.setText(item.getDay_name());
      /*  holder.txtstarttime.setText(item.getStart_time());
        holder.txtendtime.setText(item.getEnd_time());*/
        holder.txtstarttime.setText(Constant_method.current_time(item.getStart_time()));
        holder.txtendtime.setText(Constant_method.current_time(item.getEnd_time()));

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(SelectAvailabilityModel item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}