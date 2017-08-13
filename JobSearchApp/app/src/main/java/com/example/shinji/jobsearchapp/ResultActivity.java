package com.example.shinji.jobsearchapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shinji on 2017/07/31.
 */

public class ResultActivity extends AppCompatActivity {
//    private ProgressBar spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtSearchWord = (TextView) findViewById(R.id.searchWord);
        txtSearchWord.setNextFocusDownId(R.id.btnSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
//        spinner = (ProgressBar)findViewById(R.id.progressBar1);
//        spinner.setVisibility(View.VISIBLE);

//        ProgressDialog.show(Context context, CharSequence title, CharSequence message);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Intent i = getIntent();
        String search_loc = i.getStringExtra("SEARCH_LOC");
        if (search_loc != null) {
            Log.e("search_word::", search_loc);
        }
        String search_word = i.getStringExtra("SEARCH_WORD");
        if((search_loc.length() > 0) && (search_word.length() > 0)){
            mQueue = Volley.newRequestQueue(this);
            myAdapter = new JobAdapter(ResultActivity.this);
            listView = (ListView)findViewById(R.id.view_list);
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
                    Toast.makeText(getApplicationContext(), "You did not enter any words", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
                            }else{
                                setTitle("Results:" + response.getInt("totalResults"));
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

//        if(_totalItemCount >= 10){
//            Log.e("restoreListPosition @_", String.valueOf(_totalItemCount));
//            // 現在のスクリーン上端に表示されているインデックスを取得する
//            int position = listView.getFirstVisiblePosition();
//            int yOffset = 0;
//            Log.e("restoreListPosition @_:", String.valueOf(listView.getChildCount()));
//            if (listView.getChildCount() > 0) {
//                yOffset = listView.getChildAt(0).getTop();
//            }
//            listView.setSelectionFromTop(position, yOffset);
////            restoreListPosition(listView);
//        }
    }

    /* 一度でいい処理 */
    void settingListView() {
        myAdapter.setJobList(joblist);
        listView.setAdapter(myAdapter);
        Log.e("settingListView", "settingListView======");

        //リスト項目が選択された時のイベントを追加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener", String.valueOf(position));
//                String msg = position + "番目のアイテムがクリックされました";
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                Intent i = new Intent(ResultActivity.this, DetailActivity.class);
//                Uri uri = Uri.parse("https://ca.indeed.com/viewjob?jk=9730ffabfb499627&qd=FTKGzx5hxvinx60eE_WyuHHCWdCoTlfVb2Ec6gaAqA2ChJ7WQwdU06mCLrCVgY-qT0cAYljBV3Wi4Ql5I-6rXam1Aj3izkOw87NHJgxznYA&atk=1bmnt98cua4ond8n&utm_source=publisher&utm_medium=organic_listings&utm_campaign=affiliate");
                Job job_tmp = joblist.get(position);
                Uri uri = Uri.parse(job_tmp.getUrl());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
//                i.putExtra("JOB_DETAIL_URL", job_tmp.getUrl());

                startActivity(i);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView,
                                 int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
                    // 最後尾までスクロールしたので、何かデータ取得する処理
                    if(!listenerLock){
                        listenerLock = true;
                        Log.e("firstVisibleItem", String.valueOf(firstVisibleItem));
                        Log.e("visibleItemCount", String.valueOf(visibleItemCount));
                        Log.e("totalItemCount", String.valueOf(totalItemCount));
                        _totalItemCount = totalItemCount;
                        if(resultTotalItem != totalItemCount){
                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
                            Log.e("onScroll::", new_url);
                            progressDialog.show();
                            makeJsonArrayRequest(new_url, false);
                        }
                    }
                }
            }
        });
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

