package com.alienhouse.kitten.onlinecourse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alienhouse.kitten.R;
import com.alienhouse.kitten.WebviewActivity;
import com.alienhouse.kitten.util.Config;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.alienhouse.kitten.util.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.annotations.NonNull;

/**
 * Created by shinji on 2017/08/28.
 */

public class OnlinecourseListFragment extends Fragment implements OnlinecourseRecyclerAdapter.ListItemClickListener  {
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int LOAD_DATA_NUM = 20;
    public static final String LOG_TAG = "OLListFragment";
    private final int LOADAPI_NUMBER = 2;
    private int current_load_num = 0;
    private ArrayList<Onlinecourse> ollist = new ArrayList<Onlinecourse>();
    private RequestQueue mQueue;
    private OnlinecourseRecyclerAdapter onlineAdapter;
    private RecyclerView listViewRecycle;
    private int _totalItemCount = 0;
    private int resultTotalItem = 0;
    private int meetupResultTotalItemNum = 0;
    private int meetupCurrentItemNum = 0;
    private ProgressDialog progressDialog;
    private boolean listenerLock = false;
    private User userData;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private FirebaseController firebaseController;
    private DatabaseReference onlineRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        listViewRecycle = view.findViewById(R.id.view_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewRecycle.setLayoutManager(layoutManager);
        listViewRecycle.setHasFixedSize(true);

        firebaseController = firebaseController.getInstance();
        userData = firebaseController.getUserData();
        storageRef = storage.getReference();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mQueue = Volley.newRequestQueue(getActivity());
        onlineAdapter = new OnlinecourseRecyclerAdapter(ollist, this);

        listViewRecycle.setAdapter(onlineAdapter);
        getOnlineInfo();
    }


    public void getOnlineInfo(){
        onlineRef = database.getReference("onlinecourses");
        ValueEventListener onlineDataListener;
        onlineDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                System.out.println("======OnlineInfo:onlineValue==========");
                ollist.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("postSnapshot:", postSnapshot.getKey());
                    final Onlinecourse onlineValue = postSnapshot.getValue(Onlinecourse.class);
                    System.out.println(onlineValue.getName());
                    System.out.println(onlineValue.getDescription());
                    System.out.println(onlineValue.getUrl());
                    System.out.println(onlineValue.getImgUrl());

                    storageRef.child("images/onlinecourses/" + onlineValue.getImgUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            System.out.println("======uri==========");
                            System.out.println(uri);
                            onlineValue.image = uri.toString();
                            ollist.add(onlineValue);
                            onlineAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            onlineValue.image = "";
                            ollist.add(onlineValue);
                            onlineAdapter.notifyDataSetChanged();
                        }
                    });
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
            }
        };
        onlineRef.addListenerForSingleValueEvent(onlineDataListener);
    }


    @Override
    public void onListItemClick(int index) {
        Log.e("onListItemClick", String.valueOf(index));
        Onlinecourse onlinecourse_tmp = ollist.get(index);
        Intent i = new Intent(getActivity(), WebviewActivity.class);
        i.putExtra(Utils.SEND_URL, onlinecourse_tmp.getUrl());
        i.putExtra(Utils.SEND_PAGE_TITLE, onlinecourse_tmp.getName());
        startActivity(i);
    }


}
