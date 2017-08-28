package com.example.shinji.kitten.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;

import java.util.ArrayList;

/**
 * Created by shinji on 2017/08/27.
 */

public class JobFragment extends Fragment {

    Button btnSearch;
    final String URL_INDEED = "";
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_job_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final TextView txtSearchLocation = (TextView) view.findViewById(R.id.searchLocation);
        final TextView txtSearchWord = (TextView) view.findViewById(R.id.searchWord);

//        Toast.makeText(getActivity(), "TEST BTN CLICK1", Toast.LENGTH_SHORT).show();
//        btnTEST = (Button) view.findViewById(R.id.btnTEST1);
//        btnTEST.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TEST BTN CLICK1", Toast.LENGTH_SHORT).show();
//            }
//        });
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Log:",txtSearchLocation.getText().toString());

                if(txtSearchLocation.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtSearchWord.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "You did not enter any words", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Intent preferencesIntent = new Intent(getActivity(), ResultActivity.class);
//                System.out.println( "=========================" );
//                preferencesIntent.putExtra("SEARCH_LOC", txtSearchLocation.getText().toString());
//                preferencesIntent.putExtra("SEARCH_WORD", txtSearchWord.getText().toString());
//                startActivity(preferencesIntent);

                JobResultFragment jobResultFragment = new JobResultFragment();
                Bundle argument = new Bundle();
                argument.putString(BaseActivity.SEARCH_LOC, txtSearchLocation.getText().toString());
                argument.putString(BaseActivity.SEARCH_WORD, txtSearchWord.getText().toString());
                jobResultFragment.setArguments(argument);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.containers, jobResultFragment);
                fragmentTransaction.commit();

            }
        });
    }
}
