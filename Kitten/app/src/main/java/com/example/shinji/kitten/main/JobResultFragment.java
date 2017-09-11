package com.example.shinji.kitten.main;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.HidingScrollListener;
import com.example.shinji.kitten.util.User;
import com.example.shinji.kitten.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by shinji on 2017/08/28.
 */

public class JobResultFragment extends Fragment implements JobRecyclerAdapter.ListItemClickListener {

    private ArrayList<Job> joblist = new ArrayList<Job>();
    public static final String LOG_TAG = "volley_test";
    private RequestQueue mQueue;
    private final String URL_BASE = "http://api.indeed.com/ads/apisearch";
    private final String URL_API = "?publisher=2612264898312897&latlong=1&userip=1.2.3.4&useragent=Mozilla/%2F4.0(Firefox)&v=2&format=json";
    private String search_loc = "";
    private int _totalItemCount = 0;
    private int resultTotalItem = 0;
    private JobRecyclerAdapter myAdapter;
    private ListView listView;
    private RecyclerView listViewRecycle;
    private boolean listenerLock = false;
    private Button btnSearch;
    private TextView txtSearchWord;
    private TextView filterTitle;
    private TextView resultsTxt;
    private LinearLayout sortWrap;
    private LinearLayout mToolbarContainer;
    ProgressDialog progressDialog;
    private String url_location;
    private String url_query;
    private String url_co;
    private String url_sort;
    private SlidingUpPanelLayout slidingLayout;
    private Button btnDate;
    private Switch switchSort;
    private RadioGroup radioJobtypeGroup;
    private RadioButton radioJobtypeButton;
    private String JobTypeString = "";
    private DatabaseReference favoriteRef;
    private FirebaseController firebaseController;
    private User userData;
    private int mToolbarHeight;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_job_result_fragment, container, false);
        txtSearchWord = view.findViewById(R.id.searchWord);
        txtSearchWord.setNextFocusDownId(R.id.btnSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnDate = view.findViewById(R.id.btnDate);
        switchSort = view.findViewById(R.id.sort_switch);
        sortWrap = view.findViewById(R.id.sortWrap);
        filterTitle = view.findViewById(R.id.filterTitle);
        resultsTxt = view.findViewById(R.id.resultsTxt);
        mToolbarHeight = Utils.getToolbarHeight(getContext());
//        mToolbarContainer = view.findViewById(R.id.rlistWrap);

        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//        listView = view.findViewById(R.id.view_list);
        /**/
        listViewRecycle = view.findViewById(R.id.view_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewRecycle.setLayoutManager(layoutManager);
        listViewRecycle.setHasFixedSize(true);



        /**/
        slidingLayout = view.findViewById(R.id.sliding_layout);
        radioJobtypeGroup = view.findViewById(R.id.radioJobType);

        firebaseController = firebaseController.getInstance();
        userData = firebaseController.getUserData();
        favoriteRef = FirebaseDatabase.getInstance().getReference("favorite/" + userData.userID);
        setCountry();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle argument = getArguments();
        String search_word = "";
        System.out.println(argument);
        if(argument != null){
            search_loc = argument.getString(BaseActivity.SEARCH_LOC);
            search_word = argument.getString(BaseActivity.SEARCH_WORD);
        }
        Log.e("onCreateView::", search_loc);
        Log.e("onCreateView::", search_word);
        if((search_loc.length() > 0) && (search_word.length() > 0)){
            mQueue = Volley.newRequestQueue(getActivity());
            myAdapter = new JobRecyclerAdapter(joblist, this);

            String url = getJobSearchURL(search_loc,search_word);
            txtSearchWord.setText(search_word);
            makeJsonArrayRequest(url, true);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSearchWord.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "You did not enter any words", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                joblist.clear();
                String url = getJobSearchURL(search_loc, txtSearchWord.getText().toString());
                Log.e("onScroll::", url);
                makeJsonArrayRequest(url, true);
                myAdapter.notifyDataSetChanged();
            }
        });

        //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED); //to close
        //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED); //to open

        final int colorFrom = ContextCompat.getColor(getContext(), R.color.colorGray);
        final int colorBlack = ContextCompat.getColor(getContext(), R.color.colorText);
        final int colorWhite = Color.WHITE;
        final Drawable[] mDrawable = filterTitle.getCompoundDrawables();

        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e("addPanelSlideListener",String.valueOf(slideOffset));
                sortWrap.setBackgroundColor((int) new ArgbEvaluator().evaluate(slideOffset, colorFrom, colorWhite));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e("onPanelStateChanged",String.valueOf(previousState));

                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    Log.e("onPanelStateChanged","しまった");
                    filterTitle.setTextColor(colorWhite);
                    mDrawable[0].setColorFilter(new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN));
//                    sortWrap.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGray));
                }else{
//                    sortWrap.setBackgroundColor(Color.WHITE);
                    filterTitle.setTextColor(colorBlack);
//                    Drawable mDrawable = getContext().getResources().getDrawable(R.drawable.ic_adjust_24dp);
                    int imgResource = R.drawable.ic_adjust_24dp;
                    mDrawable[0].setColorFilter(new PorterDuffColorFilter(colorBlack, PorterDuff.Mode.SRC_IN));

                    Log.e("onPanelStateChanged","空いた");
                }
            }
        });

        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Log.e("setOnCedChange:", String.valueOf(isChecked));
                if(isChecked){
                    url_sort = "date";
                }else{
                    url_sort = "";
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
//                int selectedId = radioJobtypeGroup.getCheckedRadioButtonId();
////                View radioButton = radioJobtypeGroup.findViewById(selectedId);
////                int idx = radioJobtypeGroup.indexOfChild(radioButton);
////                radioJobtypeButton = (RadioButton) radioJobtypeGroup.getChildAt(idx);
//
//                Toast.makeText(getActivity(),
//                        radioJobtypeButton.getText().toString(), Toast.LENGTH_SHORT).show();
////

                progressDialog.show();
                joblist.clear();
                String url = getJobSearchURL(search_loc, txtSearchWord.getText().toString());
                makeJsonArrayRequest(url, false);
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        radioJobtypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioFulltime:
                        JobTypeString = "fulltime";
                        break;
                    case R.id.radioParttime:
                        JobTypeString = "parttime";
                        break;
                    case R.id.radioContract:
                        JobTypeString = "contract";
                        break;
                    case R.id.radioInternship:
                        JobTypeString = "internship";
                        break;
                    case R.id.radioTemporary:
                        JobTypeString = "temporary";
                        break;
                    case R.id.radioNone:
                        JobTypeString = "";
                        break;
                }
            }
        });

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
                                txtSearchWord.setText("");
                                String txt = "No results were found for \" " + response.getString("query") + "\"";
                                Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getActivity(), String.valueOf(response.getInt("totalResults")), Toast.LENGTH_SHORT).show();

                                resultsTxt.setText("Results:" + response.getInt("totalResults"));
                                resultTotalItem = response.getInt("totalResults");
                                JSONArray itemArray = response.getJSONArray("results");
                                makeDataToListview(itemArray);
                                myAdapter.notifyDataSetChanged();

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
        Job job_tmp = joblist.get(index);
//        Job job_tmp = (Job)myAdapter.getItemId(index)
        Uri uri = Uri.parse(job_tmp.getUrl());
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                i.putExtra("JOB_DETAIL_URL", job_tmp.getUrl());

        startActivity(i);

    }


    /* 一度でいい処理 */
    void settingListView() {
//        myAdapter.setJobList(joblist);
//        listView.setAdapter(myAdapter);
        listViewRecycle.setAdapter(myAdapter);
        Log.e("settingListView", "settingListView======");
//        myAdapter.notifyDataSetChanged();



//        listView.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Job job_tmp = (Job)myAdapter.getItem(position);
//                Log.e("setOnItemClickListener", String.valueOf(position));
//                        Uri uri = Uri.parse(job_tmp.getUrl());
//                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
////                i.putExtra("JOB_DETAIL_URL", job_tmp.getUrl());
//
//                        startActivity(i);
////
////                switch (view.getId()) {
////                    case R.id.animationView:
////                        Toast.makeText(getActivity(), "tap!!", Toast.LENGTH_LONG).show();
////                        LottieAnimationView animationView = view.findViewById(view.getId());
//////                        if(job_tmp.clickon) {
//////                            animationView.setProgress(0f);
//////                            job_tmp.clickon = false;
//////                        } else {
//////                            animationView.playAnimation();
//////                            job_tmp.clickon = true;
//////                        }
//////                            view.animationView.setProgress(0f);
//////                            holder.clickon = false;
//////                            Map<String, Object> postValues = job.toMap();
//////                            Map<String, Object> childUpdates = new HashMap<>();
//////                            childUpdates.put("/" + job.getJobkey() + "/", postValues);
//////                            favoriteRef.child(job.getJobkey()).removeValue();
//////                        } else {
//////                            holder.animationView.playAnimation();
//////                            holder.clickon = true;
//////                            Map<String, Object> postValues = job.toMap();
//////                            Map<String, Object> childUpdates = new HashMap<>();
//////                            childUpdates.put("/" + job.getJobkey() + "/", postValues);
//////                            favoriteRef.updateChildren(childUpdates);
//////                        }
////                        break;
////                    default:
////                        Uri uri = Uri.parse(job_tmp.getUrl());
////                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//////                i.putExtra("JOB_DETAIL_URL", job_tmp.getUrl());
////
////                        startActivity(i);
////                        break;
////
////                }
//            }
//        });

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
                            String url = getJobSearchURL(url_location,url_query,String.valueOf(totalItemCount));
//                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
                            Log.e("onScroll::", url);
                            progressDialog.show();
                            makeJsonArrayRequest(url, false);
                        }
                    }
                }

            }
        });

//        listViewRecycle.setOnScrollListener(new HidingScrollListener(getActivity()) {
//            @Override
//            public void onMoved(int distance) {
//                slidingLayout.setTranslationY(-distance);
//            }
//
//            @Override
//            public void onShow() {
//                slidingLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//
//            }
//
//            @Override
//            public void onHide() {
//                slidingLayout.animate().translationY(-mToolbarHeight).setInterpolator(new AccelerateInterpolator(2)).start();
//
//            }
//        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//            }
//            @Override
//            public void onScroll(AbsListView absListView,
//                                 int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
//                    // 最後尾までスクロールしたので、何かデータ取得する処理
//                    if(!listenerLock){
//                        listenerLock = true;
//                        Log.e("firstVisibleItem", String.valueOf(firstVisibleItem));
//                        Log.e("visibleItemCount", String.valueOf(visibleItemCount));
//                        Log.e("totalItemCount", String.valueOf(totalItemCount));
//                        _totalItemCount = totalItemCount;
//                        if(resultTotalItem != totalItemCount){
//                            String url = getJobSearchURL(url_location,url_query,String.valueOf(totalItemCount));
////                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
//                            Log.e("onScroll::", url);
//                            progressDialog.show();
//                            makeJsonArrayRequest(url, false);
//                        }
//                    }
//                }
//            }
//        });

    }

    void restoreListPosition() {
        Log.e("restoreListPosition @_", String.valueOf(_totalItemCount));
        System.out.println(listView);
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

        favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getKey().matches(newJob.getJobkey())){
                        newJob.setFav(true);
                        Log.d("favoriteRefあったよ:", postSnapshot.getKey());
                    }
                }
                joblist.add(newJob);
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
            }
        });

    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////        switch (view.getId()) {
////            case R.id.animationView:
////                Toast.makeText(getActivity(), "tap!!", Toast.LENGTH_LONG).show();
////                break;
////        }
//    }

    /**
     * get the indeed url
     * @param location city name (e.g:l=vancouver )
     * @param word query you want to search (e.g:q=webdesigner )
     */
    public String getJobSearchURL(String location, String word){
        String url = "";
        url_location = location;
        String url_locationWithQuery = "&l=" + url_location;
        url_query = word;
        String url_queryWithQuery = "&q=" + url_query;
        url = URL_BASE + URL_API + url_co + url_locationWithQuery + url_queryWithQuery;
        if(url_sort != null){
            String url_sortWithQuery = "&sort=" + url_sort;
            url = url + url_sortWithQuery;
        }
        if(JobTypeString != null){
            String url_jtWithQuery = "&jt=" + JobTypeString;
            url = url + url_jtWithQuery;
        }
        System.out.println(url);
        return url;
    }
    /**
     * overload
     * ...
     * @param start e.g start=10
     */
    public String getJobSearchURL(String location, String word, String start){
        String url = getJobSearchURL(location, word);
        String url_start = "&start=" + start;
        url = url + url_start;
        return url;
    }

    public void setCountry(){
        if(userData.country != null){
            url_co = "&co=" + userData.country;
        }else{
            userData.country = "us";
            url_co = "&co=us";
        }
    }
}
