package com.example.shinji.kitten.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shinji.kitten.R;
import com.example.shinji.kitten.main.JobSearchFragment;

/**
 * Created by shinji on 2017/08/27.
 */

public class FavoriteFragment extends Fragment {

    Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.tab_base_favorite_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    private void initFragment(){
        FragmentManager fm = getChildFragmentManager();
        if(fm.findFragmentById(R.id.favorite_fragment) != null){
            return;
        }
        FavoriteListFragment favoriteListFragment = new FavoriteListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.favorite_fragment, favoriteListFragment);
        fragmentTransaction.addToBackStack(null);
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