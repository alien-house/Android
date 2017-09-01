package com.example.shinji.kitten.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by shinji on 2017/08/28.
 */

public class JobSearchFragment extends Fragment {
    private Button btnSearch;
    private TextView txtSearchLocation;
    private String searchLocation = "";
    private PlaceAutocompleteFragment autocompleteFragment;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private ArrayAdapter<String> acAdapter;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.main_job_search_fragment, container, false);

        btnSearch = view.findViewById(R.id.btnSearch);
//        searchLocation = "van";

//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        multiAutoCompleteTextView = view.findViewById(R.id.searchWordMultiAuto);
        String[] devAutoArray = {"web designer","front end developer", "android developer"};
        acAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devAutoArray);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        autocompleteFragment  = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        //filter
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .setCountry("CA")
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.
                Log.i("onPlaceSelected", "Place: " + place.getName());
                searchLocation = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO:Handle the error.
                Log.i("onError", "An error occurred: " + status);
            }
        });

        //multiAutoCompleteTextView
        multiAutoCompleteTextView.setAdapter(acAdapter);
        multiAutoCompleteTextView.setThreshold(1);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "multiAutoCompleteTextView" + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchLocation.matches("")){
                    Toast.makeText(getActivity(), "You did not enter a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(multiAutoCompleteTextView.getText().toString().matches("")){
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
                argument.putString(BaseActivity.SEARCH_LOC, searchLocation);
                argument.putString(BaseActivity.SEARCH_WORD, multiAutoCompleteTextView.getText().toString());
                jobResultFragment.setArguments(argument);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.job_fragment, jobResultFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }


//    @Override
//    public void onResume() {
//        autocompleteFragment.onResume();
//        super.onResume();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        autocompleteFragment.onDestroy();
////        getFragmentManager().beginTransaction().remove(autocompleteFragment).commit();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        autocompleteFragment.onActivityResult(requestCode, resultCode, data);
//    }

}
