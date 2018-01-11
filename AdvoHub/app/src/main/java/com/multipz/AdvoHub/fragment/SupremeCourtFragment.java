package com.multipz.AdvoHub.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multipz.AdvoHub.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupremeCourtFragment extends Fragment {


    public SupremeCourtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supreme_court, container, false);

        getActivity().setTitle("Court");
        return view;
    }

}
