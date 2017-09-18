package com.alienhouse.kitten;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.alienhouse.kitten.dashboard.SettingFragment;
import com.alienhouse.kitten.event.EventFragment;
import com.alienhouse.kitten.favorite.FavoriteFragment;
import com.alienhouse.kitten.main.JobFragment;
import com.alienhouse.kitten.onlinecourse.OnlinecourseFragment;

/**
 * Created by shinji on 2017/09/11.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new JobFragment();
        } else if (position == 1) {
            return new FavoriteFragment();
        } else if (position == 2) {
            return new EventFragment();
        } else if (position == 3) {
            return new OnlinecourseFragment();
        } else {
            return new SettingFragment();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
    @Override
    public int getCount() {
        return 5;
    }
}