package com.example.shinji.kitten;

import android.app.ActionBar;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.shinji.kitten.dashboard.SettingFragment;
import com.example.shinji.kitten.main.JobFragment;

/**
 * Created by shinji on 2017/08/27.
 */
//https://github.com/delaroy/MaterialTabs

public class BaseActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    public static final String SEARCH_LOC = "SEARCH_LOC";
    public static final String SEARCH_WORD = "SEARCH_WORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getActionBar().isShowing()){
//            getActionBar().hide();
//        }

        setContentView(R.layout.main_base_activity);
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.containers);
        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobFragment(), "Job");
        adapter.addFragment(new SettingFragment(), "Setting");
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_business_center_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_settings_24dp);
    }


}
