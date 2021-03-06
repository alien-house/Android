package com.alienhouse.kitten.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.alienhouse.kitten.BaseActivity;
import com.alienhouse.kitten.R;
import com.alienhouse.kitten.dashboard.GetImageTask;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shinji on 2017/08/28.
 */

public class JobSearchFragment extends Fragment {
    private Button btnSearch;
    private EditText txtSearchLocation;
    private String searchLocation = "";
    private PlaceAutocompleteFragment autocompleteFragment;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private ArrayAdapter<String> acAdapter;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private FirebaseController firebaseController;
    private User userData;
//    private List devAutoArray = new ArrayList<String>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String[] devAutoArray;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.main_job_search_fragment, container, false);

        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.requestFocus();
        txtSearchLocation = view.findViewById(R.id.searchLocation);
//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        multiAutoCompleteTextView = view.findViewById(R.id.searchWordMultiAuto);
//        multiAutoCompleteTextView.requestFocus();

        Log.d("Value:", "onCreateView:きてる？毎回 ");
        firebaseController = firebaseController.getInstance();
        userData = firebaseController.getUserData();
        DatabaseReference devStatusRef = database.getReference("devStatus");
        firebaseController.getDevStatusDataEventListener(devStatusRef);
        firebaseController.setOnCallBack(new FirebaseController.CallBackTask(){
            @Override
            public void CallBack(String[] devStatusArray) {
                super.CallBack(devStatusArray);
                Log.d("JobSearchFragment:", "CallBack: " + "おわた＝＝＝＝＝＝＝＝");
                if(devStatusArray != null){
                    devAutoArray = devStatusArray;
//                    String[] stringArray = list.toArray(new String[0]);
//                    String[] stringArray = devStatusArray.toArray(new String[0]);

                }else{
                    String[] repArray = {"web designer","front end developer","android developer"};
                    devAutoArray = repArray;
                }
                Log.d("Value:", "setOnCallBackValue is: " + devAutoArray[0]);
                acAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devAutoArray);

                //multiAutoCompleteTextView
                multiAutoCompleteTextView.setAdapter(acAdapter);
                multiAutoCompleteTextView.setThreshold(0);
                        multiAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
//                multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Toast.makeText(getActivity(), "multiAutoCompleteTextView:" + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                Log.d("Value:", "onActivityCreated:きてる？毎回 ");
//                if(userData.location != null && !userData.location.matches("")){
//                    Log.d("Value:", "odlocationある？？？ "+userData.location.toString());
//                    txtSearchLocation.setText(userData.location.toString());
//                }
//                if(userData.devStatus != null && !userData.devStatus.matches("")){
//                    Log.d("Value:", "odevStatusある？？？ "+userData.devStatus.toString());
//                    multiAutoCompleteTextView.setText(userData.devStatus.toString());
//                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    public void update(){
        Log.d("Value:", "onActivityCreated:きてる？毎回 ");
        userData = firebaseController.getUserData();
        if(userData.location != null && !userData.location.matches("")){
            Log.d("Value:", "odlocationある？？？ "+userData.location.toString());
            txtSearchLocation.setText(userData.location.toString());
        }
        if(userData.devStatus != null && !userData.devStatus.matches("")){
            Log.d("Value:", "odevStatusある？？？ "+userData.devStatus.toString());
            multiAutoCompleteTextView.setText(userData.devStatus.toString());
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//
//        PlaceAutocompleteFragment test = new PlaceAutocompleteFragment();
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.place_autocomplete_liner, test).commit();

//        autocompleteFragment  = (PlaceAutocompleteFragment)
//                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        autocompleteFragment.setFilter(typeFilter);
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO:Get info about the selected place.
//                Log.i("onPlaceSelected", "Place: " + place.getName());
//                searchLocation = place.getName().toString();
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO:Handle the error.
//                Log.i("onError", "An error occurred: " + status);
//            }
//        });

        txtSearchLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showAutoCompPlace();
                }
            }
        });

        txtSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAutoCompPlace();
            }
        });

//        btnAuto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .build(getActivity());
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO:Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO:Handle the error.
//                }
//            }
//        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSearchLocation.getText().toString().matches("")){
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
                argument.putString(BaseActivity.SEARCH_LOC, txtSearchLocation.getText().toString());
                argument.putString(BaseActivity.SEARCH_WORD, multiAutoCompleteTextView.getText().toString());
                jobResultFragment.setArguments(argument);

//                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//                FragmentTransaction fragmentTransaction = getParentFragment().getChildFragmentManager().beginTransaction();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.job_fragment, jobResultFragment);
                fragmentTransaction.commit();

            }
        });

//        firebaseController.setOnCallBack(new FirebaseController.CallBackTask(){
//            @Override
//            public void CallBack(String location, String dev) {
//                super.CallBack(location, dev);
//                Log.d("JobSearchFragment:", "CallBack: " + "JOBのやつが更新されるはず！＝＝＝＝＝＝＝＝");
//
//                System.out.println(location);
//                System.out.println(dev);
//                txtSearchLocation.setText(location);
//                multiAutoCompleteTextView.setText(dev);
//
//            }
//        });
    }

    public void showAutoCompPlace(){
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .setCountry(userData.country)//* should be changed! later *_*
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO:Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO:Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i("TAG", "Place: " + place.getName());
                searchLocation = place.getName().toString();
                txtSearchLocation.setText(searchLocation);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO:Handle the error.
                Log.i("TAG", status.getStatusMessage());
            }
        }
    }

    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }
            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }
            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }

}
