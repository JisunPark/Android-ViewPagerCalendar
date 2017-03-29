package com.suminjin.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * `
 * Created by parkjisun on 2017. 3. 28..
 */

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private static final int MAX_VALUE = 10000;
    public static final int START_POSITION = MAX_VALUE / 2;

    public CalendarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return MAX_VALUE;
    }
}
