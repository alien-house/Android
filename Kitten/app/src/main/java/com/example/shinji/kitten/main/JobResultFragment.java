package com.example.shinji.kitten.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shinji on 2017/08/28.
 */

public class JobResultFragment extends Fragment {

    private ArrayList<Job> joblist = new ArrayList();
    public static final String LOG_TAG = "volley_test";
    private RequestQueue mQueue;
    private final String URL_BASE = "http://api.indeed.com/ads/apisearch";
    private final String URL_API = "?publisher=2612264898312897&latlong=1&co=ca&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0(Firefox)&v=2&format=json";
    private String url;
    private int _totalItemCount = 0;
    private int resultTotalItem = 0;
    private JobAdapter myAdapter;
    private ListView listView;
    private boolean listenerLock = false;
    private Button btnSearch;
    private TextView txtSearchWord;
    ProgressDialog progressDialog;
    private String url_query;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_job_result_fragment, container, false);
        txtSearchWord = view.findViewById(R.id.searchWord);
        txtSearchWord.setNextFocusDownId(R.id.btnSearch);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        listView = view.findViewById(R.id.view_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle argument = getArguments();
        String search_loc = "";
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
            myAdapter = new JobAdapter(getActivity());
            String url_location = "&l=" + search_loc;
            url_query = "&q=" + search_word;
            url = URL_BASE + URL_API + url_location + url_query;
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
                String url_location = "&l=" + "Vancouver";
                url_query = "&q=" + txtSearchWord.getText().toString();
                url = URL_BASE + URL_API + url_location + url_query;
                Log.e("onScroll::", url);
                makeJsonArrayRequest(url, true);
                myAdapter.notifyDataSetChanged();
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
                            // if no result
                            if(response.getInt("totalResults") == 0){
                                txtSearchWord.setText("");
                                String txt = "No results were found for \" " + response.getString("query") + "\"";
                                Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT).show();
                            }else{
//                                getActivity().setTitle("Results:" + response.getInt("totalResults"));
                                resultTotalItem = response.getInt("totalResults");
                                JSONArray itemArray = response.getJSONArray("results");
                                makeDataToListview(itemArray);

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

    /* 一度でいい処理 */
    void settingListView() {
        myAdapter.setJobList(joblist);
        System.out.println(joblist);
        listView.setAdapter(myAdapter);
        Log.e("settingListView", "settingListView======");
//        myAdapter.notifyDataSetChanged();

//        //リスト項目が選択された時のイベントを追加
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("setOnItemClickListener", String.valueOf(position));
//                Job job_tmp = joblist.get(position);
//                Uri uri = Uri.parse(job_tmp.getUrl());
//                Intent i = new Intent(Intent.ACTION_VIEW, uri);
////                i.putExtra("JOB_DETAIL_URL", job_tmp.getUrl());
//
//                startActivity(i);
//            }
//        });
//
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
//                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
//                            Log.e("onScroll::", new_url);
//                            progressDialog.show();
//                            makeJsonArrayRequest(new_url, false);
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
        joblist.add(new Job(
                obj.getString("jobtitle"),
                obj.getString("url"),
                obj.getString("company"),
                obj.getString("snippet"),
                obj.getString("formattedRelativeTime"),
                obj.getString("formattedLocation")
        ));
        myAdapter.notifyDataSetChanged();
    }




}
