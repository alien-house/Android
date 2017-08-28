package com.example.shinji.kitten.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shinji.kitten.R;

/**
 * Created by shinji on 2017/08/27.
 */

public class SettingFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_setting_fragment, container, false);

//        Toast.makeText(getActivity(), "TEST BTN CLICK2", Toast.LENGTH_SHORT).show();
//        btnTEST = (Button) view.findViewById(R.id.btnTEST1);
//        btnTEST.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TEST BTN CLICK1", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }
}
