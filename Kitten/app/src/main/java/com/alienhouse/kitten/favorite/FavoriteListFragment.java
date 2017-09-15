package com.alienhouse.kitten.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.airbnb.lottie.LottieAnimationView;
import com.alienhouse.kitten.BaseActivity;
import com.alienhouse.kitten.R;
import com.alienhouse.kitten.main.Job;
import com.alienhouse.kitten.main.JobResultFragment;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shinji on 2017/08/28.
 */

public class FavoriteListFragment extends Fragment implements FavoriteRecyclerAdapter.ListItemClickListener  {
    private ArrayList<Job> favlist = new ArrayList<Job>();
    private Button btnSearch;
    private EditText txtSearchLocation;
    private String searchLocation = "";
    private FavoriteRecyclerAdapter frAdapter;
    private RecyclerView listViewRecycle;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseController firebaseController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);

        listViewRecycle = view.findViewById(R.id.view_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewRecycle.setLayoutManager(layoutManager);
        listViewRecycle.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        User userData = firebaseController.getUserData();
        //for devdata
        if(userData.userID != null){

            DatabaseReference favRef = database.getReference("favorite").child(userData.userID);
            ValueEventListener favDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                String devStatusValue = dataSnapshot.getValue(Job.class);
//                firebaseController.devStatusArray = Arrays.asList(array);

                    Log.d("favoriteValue:", "Value is: " + dataSnapshot);
                    Log.d("favoritegetKey:", dataSnapshot.getKey());

                    favlist.clear();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Log.d("postSnapshot:", postSnapshot.getKey());
//                        Map<String, String> favMap = (Map<String, String>) postSnapshot.getValue();
                        addList(postSnapshot);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Value:", "Failed to read value.", error.toException());
                }
            };
            favRef.addValueEventListener(favDataListener);
            frAdapter = new FavoriteRecyclerAdapter(favlist, this);
            listViewRecycle.setAdapter(frAdapter);
        }

//        btnSearch = view.findViewById(R.id.btnSearch);
//        txtSearchLocation = view.findViewById(R.id.searchLocation);
//        multiAutoCompleteTextView = view.findViewById(R.id.searchWordMultiAuto);
//        String[] devAutoArray = {"web designer","front end developer", "android developer"};
//        acAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devAutoArray);

        return view;
    }

    @Override
    public void onListItemClick(int index) {
        Log.w("onListItemClick:", String.valueOf(index));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    void addList(DataSnapshot child)  {
//        String title = (String) child.child("title").getValue();
//        System.out.println("addList val : " + title);
        Job newJob = new Job(
                (String)child.child("title").getValue(),
                (String)child.child("url").getValue(),
                (String)child.child("company").getValue(),
                (String)child.child("description").getValue(),
                "",
                (String)child.child("area").getValue(),
                (String)child.child("jobkey").getValue()
        );
        newJob.setFav(true);
        favlist.add(newJob);
        frAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
