package com.multipz.AdvoHub.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.multipz.AdvoHub.Adapter.HighCourtAdapter;
import com.multipz.AdvoHub.Model.HighcourtModel;
import com.multipz.AdvoHub.R;
import com.multipz.AdvoHub.util.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighcourtFragment extends Fragment {
    RecyclerView recyclerview;
    HighCourtAdapter adapter;
    Context context;
    private RelativeLayout rel_root;
    private List<HighcourtModel> list = new ArrayList<>();

    public HighcourtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highcourt, container, false);
        context = getActivity();
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));

        Court();

        return view;
    }

    private void Court() {
        adapter = new HighCourtAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        recyclerview.setNestedScrollingEnabled(false);
        adapter.setClickListener(new itemInClickListener());
        prepareExpertIn();
    }

    class itemInClickListener implements HighCourtAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            //  Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareExpertIn() {
        list.add(new HighcourtModel("hallo", "21-5-17", "12:PM", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        list.add(new HighcourtModel("hallo", "21-5-17", "12", ""));
        adapter.notifyDataSetChanged();
    }

}
