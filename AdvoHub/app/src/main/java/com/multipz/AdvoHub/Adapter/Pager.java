package com.multipz.AdvoHub.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.multipz.AdvoHub.fragment.HighcourtFragment;

/**
 * Created by Admin on 01-12-2017.
 */


public class Pager extends FragmentStatePagerAdapter {

    // tab titles
    private String[] tabTitles = new String[]{"Tops News", "High court", "Supreme court"};

    public Pager(FragmentManager fm) {
        super(fm);
    }

    // overriding getPageTitle()
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HighcourtFragment();
            case 1:
                return new HighcourtFragment();
            case 2:
                return new HighcourtFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
