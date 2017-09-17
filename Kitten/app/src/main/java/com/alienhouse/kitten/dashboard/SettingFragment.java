package com.alienhouse.kitten.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alienhouse.kitten.R;
import com.alienhouse.kitten.main.JobSearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by shinji on 2017/08/27.
 */

public class SettingFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView texthello;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tab_base_setting_fragment, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    private void initFragment(){
        FragmentManager fm = getChildFragmentManager();
        if(fm.findFragmentById(R.id.setting_fragment) != null){
            return;
        }
        SettingInfoFragment settingInfoFragment = new SettingInfoFragment();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.setting_fragment, settingInfoFragment);
        fragmentTransaction.commit();

    }
}
