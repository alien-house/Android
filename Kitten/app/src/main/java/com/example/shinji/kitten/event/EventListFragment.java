package com.example.shinji.kitten.event;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shinji.kitten.R;
import com.example.shinji.kitten.StartActivity;
import com.example.shinji.kitten.dashboard.GetImageTask;
import com.example.shinji.kitten.favorite.FavoriteRecyclerAdapter;
import com.example.shinji.kitten.main.Job;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.User;
import com.example.shinji.kitten.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by shinji on 2017/08/28.
 */

public class EventListFragment extends Fragment implements EventRecyclerAdapter.ListItemClickListener  {
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final String LOG_TAG = "EventListFragment";
    private final String EVENTBRITE_URL_BASE = "https://www.eventbriteapi.com";
    private final String URL_API = "/v3/events/search/?";
    private ArrayList<Event> eventlist = new ArrayList<Event>();
    private RequestQueue mQueue;
    private EventRecyclerAdapter eventAdapter;
    private RecyclerView listViewRecycle;
    private int _totalItemCount = 0;
    private int resultTotalItem = 0;
    private String url_location;
    private String url_query;
    private String url_co;
    private String url_sort;
    private ProgressDialog progressDialog;
    private boolean listenerLock = false;
    private User userData;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseController firebaseController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        listViewRecycle = view.findViewById(R.id.view_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewRecycle.setLayoutManager(layoutManager);
        listViewRecycle.setHasFixedSize(true);


        firebaseController = firebaseController.getInstance();
        userData = firebaseController.getUserData();

        return view;
    }

    /**
     * Method to make json array request where response starts
     */
    private void makeJsonArrayRequest(String url, final boolean begin) {

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse::","====================");
                        Log.d(LOG_TAG, response.toString());
                        try {
                            progressDialog.dismiss();
                            Log.d(LOG_TAG, "Value: " + response.getString("query"));
                            Log.d(LOG_TAG, "results: " + response.getJSONArray("results"));
                            Log.d(LOG_TAG, "totalResults: " + response.getInt("totalResults"));
                            // if no result
                            if(response.getInt("totalResults") == 0){
                                String txt = "No results were found for \" " + response.getString("query") + "\"";
                                Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getActivity(), String.valueOf(response.getInt("totalResults")), Toast.LENGTH_SHORT).show();

//                                resultsTxt.setText("Results:" + response.getInt("totalResults"));
//                                resultTotalItem = response.getInt("totalResults");
                                JSONArray itemArray = response.getJSONArray("results");
                                makeDataToListview(itemArray);
                                eventAdapter.notifyDataSetChanged();

                                if(begin){
                                    settingListView();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error::","~~~~~~~~~~~~~~~~~~~~~~");
                        Log.d(LOG_TAG, error.toString());
                        progressDialog.dismiss();
                    }
                }
        );
        mQueue.add(jsonObjectReq);
    }

    public void makeDataToListview(JSONArray itemArray){
        try {
            for (int i = 0 ; i < itemArray.length(); i++) {
                JSONObject obj = itemArray.getJSONObject(i);
                Log.d(LOG_TAG, obj.getString("company"));
                addList(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listenerLock = false;

    }

    @Override
    public void onListItemClick(int index) {
        Log.e("onListItemClick", String.valueOf(index));
        Event event_tmp = eventlist.get(index);
        Uri uri = Uri.parse(event_tmp.getUrl());
        Intent i = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(i);

    }


    /* 一度でいい処理 */
    void settingListView() {
        listViewRecycle.setAdapter(eventAdapter);
        Log.e("EventListFragment", "EventListFragment======");



        listViewRecycle.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount(); //合計のアイテム数
                int visibleItemCount = recyclerView.getChildCount(); // RecyclerViewに表示されてるアイテム数
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                    // 最後尾までスクロールしたので、何かデータ取得する処理
                    if(!listenerLock){
                        listenerLock = true;
                        Log.e("firstVisibleItem", String.valueOf(firstVisibleItem));
                        Log.e("visibleItemCount", String.valueOf(visibleItemCount));
                        Log.e("totalItemCount", String.valueOf(totalItemCount));
                        _totalItemCount = totalItemCount;
                        if(resultTotalItem != totalItemCount){
                            String url = getEventSearchURL(url_location,url_query,String.valueOf(totalItemCount));
//                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
                            Log.e("onScroll::", url);
                            progressDialog.show();
                            makeJsonArrayRequest(url, false);
                        }
                    }
                }

            }
        });

    }



    void addList(JSONObject obj) throws JSONException {

        final Job newJob = new Job(
                obj.getString("jobtitle"),
                obj.getString("url"),
                obj.getString("company"),
                obj.getString("snippet"),
                obj.getString("formattedRelativeTime"),
                obj.getString("formattedLocation"),
                obj.getString("jobkey")
        );


    }


    /**
     * get the indeed url
     * @param location city name (e.g:l=vancouver )
     * @param word query you want to search (e.g:q=webdesigner )
     */
    public String getEventSearchURL(String location, String word){
        String url = "";
        url_location = location;
        String url_locationWithQuery = "&l=" + url_location;
        url_query = word;
        String url_queryWithQuery = "&q=" + url_query;
        url = URL_BASE + URL_API + url_co + url_locationWithQuery + url_queryWithQuery;
//        if(url_sort != null){
//            String url_sortWithQuery = "&sort=" + url_sort;
//            url = url + url_sortWithQuery;
//        }
//        if(JobTypeString != null){
//            String url_jtWithQuery = "&jt=" + JobTypeString;
//            url = url + url_jtWithQuery;
//        }
        System.out.println(url);
        return url;
    }
    /**
     * overload
     * ...
     * @param start e.g start=10
     */
    public String getEventSearchURL(String location, String word, String start){
        String url = getEventSearchURL(location, word);
        String url_start = "&start=" + start;
        url = url + url_start;
        return url;
    }


}
