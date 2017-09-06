package com.example.shinji.kitten.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shinji.kitten.R;

/**
 * Created by shinji on 2017/08/27.
 */

public class JobFragment extends Fragment {

    Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tab_base_job_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    private void initFragment(){
        FragmentManager fm = getChildFragmentManager();
        if(fm.findFragmentById(R.id.job_fragment) != null){
            return;
        }
        JobSearchFragment jobSearchFragment = new JobSearchFragment();
//        FragmentManager fragMan = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.job_fragment, jobSearchFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //    @Override
//    public void onBackPressed() {
//        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
//        if (backStackCnt != 0) {
//            getSupportFragmentManager().popBackStack();
//        }
//    }
}
