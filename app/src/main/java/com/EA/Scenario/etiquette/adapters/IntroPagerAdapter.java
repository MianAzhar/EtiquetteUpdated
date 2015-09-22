package com.EA.Scenario.etiquette.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.EA.Scenario.etiquette.fragments.IntroSliderFragment;

/**
 * Created by Mian on 9/17/2015.
 */
public class IntroPagerAdapter extends FragmentPagerAdapter {

    private int pagerCount = 3;

    //private Random random = new Random();

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int i) {
        return IntroSliderFragment.getInstance(i);
    }

    @Override public int getCount() {
        return pagerCount;
    }
}