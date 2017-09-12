package com.example.shinji.kitten;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shinji.kitten.dashboard.SettingFragment;
import com.example.shinji.kitten.event.EventFragment;
import com.example.shinji.kitten.favorite.FavoriteFragment;
import com.example.shinji.kitten.main.JobFragment;

/**
 * Created by shinji on 2017/09/11.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

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
        } else {
            return new SettingFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}