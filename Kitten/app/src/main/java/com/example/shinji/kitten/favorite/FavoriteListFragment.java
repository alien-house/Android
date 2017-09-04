package com.example.shinji.kitten.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;
import com.example.shinji.kitten.main.JobResultFragment;
import com.example.shinji.kitten.util.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shinji on 2017/08/28.
 */

public class FavoriteListFragment extends Fragment {
    private Button btnSearch;
    private EditText txtSearchLocation;
    private String searchLocation = "";
    private ArrayAdapter<String> acAdapter;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
//        LottieAnimationView animationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
//        animationView.setAnimation("beatingheart.json");
//        animationView.loop(true);
//        animationView.playAnimation();

//        btnSearch = view.findViewById(R.id.btnSearch);
//        txtSearchLocation = view.findViewById(R.id.searchLocation);
//        multiAutoCompleteTextView = view.findViewById(R.id.searchWordMultiAuto);
//        String[] devAutoArray = {"web designer","front end developer", "android developer"};
//        acAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devAutoArray);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        txtSearchLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showAutoCompPlace();
//                }
//            }
//        });
//
//        txtSearchLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAutoCompPlace();
//            }
//        });
//
//        multiAutoCompleteTextView.setAdapter(acAdapter);
//        multiAutoCompleteTextView.setThreshold(1);
//        multiAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
//
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(searchLocation.matches("")){
//                    Toast.makeText(getActivity(), "You did not enter a location", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(multiAutoCompleteTextView.getText().toString().matches("")){
//                    Toast.makeText(getActivity(), "You did not enter any words", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                JobResultFragment jobResultFragment = new JobResultFragment();
//                Bundle argument = new Bundle();
//                jobResultFragment.setArguments(argument);
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.job_fragment, jobResultFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
//            }
//        });


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


}
