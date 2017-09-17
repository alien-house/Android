package com.alienhouse.kitten.event;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
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

import com.alienhouse.kitten.WebviewActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.alienhouse.kitten.BaseActivity;
import com.alienhouse.kitten.R;
import com.alienhouse.kitten.StartActivity;
import com.alienhouse.kitten.dashboard.GetImageTask;
import com.alienhouse.kitten.favorite.FavoriteRecyclerAdapter;
import com.alienhouse.kitten.main.Job;
import com.alienhouse.kitten.main.JobRecyclerAdapter;
import com.alienhouse.kitten.util.Config;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.alienhouse.kitten.util.Utils;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by shinji on 2017/08/28.
 */

public class EventListFragment extends Fragment implements EventRecyclerAdapter.ListItemClickListener  {
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int LOAD_DATA_NUM = 20;
    public static final String LOG_TAG = "EventListFragment";
    private final int LOADAPI_NUMBER = 2;
    private int current_load_num = 0;
    private ArrayList<Event> eventlist = new ArrayList<Event>();
    private RequestQueue mQueue;
    private EventRecyclerAdapter eventAdapter;
    private RecyclerView listViewRecycle;
    private int _totalItemCount = 0;
    private int resultTotalItem = 0;
    private int meetupResultTotalItemNum = 0;
    private int meetupCurrentItemNum = 0;
//    private String url_location;
//    private String url_query;
//    private String url_co;
//    private String url_sort;
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
//        progressDialog.setMessage("....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        listViewRecycle = view.findViewById(R.id.view_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listViewRecycle.setLayoutManager(layoutManager);
        listViewRecycle.setHasFixedSize(true);

        firebaseController = firebaseController.getInstance();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mQueue = Volley.newRequestQueue(getActivity());
        eventAdapter = new EventRecyclerAdapter(eventlist, this);

        listViewRecycle.setAdapter(eventAdapter);
        getEventInfo();
    }


    public void getEventInfo() {

        userData = firebaseController.getUserData();
        progressDialog.setTitle("Events Loading");
        String url = getEventbriteEventSearchURL();
        Log.e("onActivityCreated::", url);
        makeJsonObjectRequest(url, true);

        String url_meetup = getMeetupEventSearchURL();
        makeJsonArrayRequest(url_meetup, true);
    }

    /**
     * Method to make json array request where response starts
     */
    private void makeJsonObjectRequest(String url, final boolean begin) {

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("EventListFragment:onR:","===========");
                        Log.d(LOG_TAG, response.toString());

                        JsonParser parser = new JsonParser();
                        JsonElement jsonTree = parser.parse(response.toString());
                        JsonObject jsonObject = jsonTree.getAsJsonObject();
                        JsonObject pagination = jsonObject.get("pagination").getAsJsonObject();
                        JsonElement object_count = pagination.get("object_count");
                        JsonArray eventsObj = jsonObject.get("events").getAsJsonArray();

                        try {
                            progressDialog.dismiss();
                            Log.d(LOG_TAG, "pagination: " + object_count);
                            Log.d(LOG_TAG, "results: " + response.getJSONArray("events"));
                            Log.d("EventListFragment:each:","------------");

                            for (int i = 0 ; i < eventsObj.size(); i++) {
                                JsonObject obj = eventsObj.get(i).getAsJsonObject();
                                Log.d(LOG_TAG, String.valueOf(obj.get("name").getAsJsonObject().get("text")));

                                String[] timeArray = obj.get("start").getAsJsonObject().get("local").getAsString().split("T");
                                int week = 0;
                                String theDate = "";
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date formatDate = sdf.parse(timeArray[0] + " " + timeArray[1]);
                                    SimpleDateFormat format2 = new SimpleDateFormat("dd,MM,yyyy HH:mm");
                                    theDate = format2.format(formatDate);
//                                    Log.d(LOG_TAG, "yyyyMMdd:" + theDate);

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(formatDate);
                                    week = calendar.get(Calendar.DAY_OF_WEEK);
                                    System.out.println("week(" + week + ")");
                                    String theDates = String.valueOf(Utils.WEEK_NAME[week - 1] + ", " + theDate);
                                    addList(obj,theDates);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            // if no result
//                            if(response.getInt("totalResults") == 0){
//                                String txt = "No results were found for \" " + response.getString("query") + "\"";
//                                Toast.makeText(getActivity(), txt, Toast.LENGTH_SHORT).show();
//                            }else{
////                                Toast.makeText(getActivity(), String.valueOf(response.getInt("totalResults")), Toast.LENGTH_SHORT).show();
//
////                                resultsTxt.setText("Results:" + response.getInt("totalResults"));
////                                resultTotalItem = response.getInt("totalResults");
//                                JSONArray itemArray = response.getJSONArray("results");
//                                makeDataToListview(itemArray);
//                                eventAdapter.notifyDataSetChanged();
//                                current_load_num++;
//                                if(begin){
//                                    settingListView();
//                                }
//                            }
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

    private void makeJsonArrayRequest(String url, final boolean begin) {

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("EventListFragment:onR:","===========");
                        Log.d(LOG_TAG, response.toString());
//                        parseJsonData(response);
                        JsonParser parser = new JsonParser();
                        JsonElement jsonTree = parser.parse(response.toString());
                        JsonArray dataArray = jsonTree.getAsJsonArray();

                        Log.d("EventListFrag:MeetUP:","-----------");
                        progressDialog.dismiss();

                        for (int i = 0 ; i < dataArray.size(); i++) {

                            try {
                                JsonObject obj = dataArray.get(i).getAsJsonObject();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy HH:mm");
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                                System.out.println(sdf.format(new Date(obj.get("time").getAsLong())));
//                                System.out.println(DateFormat.getDateTimeInstance(
//                                        DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(obj.get("time").getAsLong())));
                                String theDates = String.valueOf(DateFormat.getDateTimeInstance(
                                        DateFormat.MEDIUM, DateFormat.SHORT).format(new Date(obj.get("time").getAsLong())));
                                addMeetupList(obj, theDates);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error::","~~~~~makeJsonArrayRequest~~~~~~~~");
                        Log.d(LOG_TAG, error.toString());
                        progressDialog.dismiss();
                    }
                }
        );
        mQueue.add(jsonArrayReq);
    }


    void addMeetupList(JsonObject obj, String theDates) throws JSONException {
        String adrress = "";
        String photo_url = "";
        String venue_name = "";
        String description = "";
        try {
            if(obj.has("venue")){
                if(obj.get("venue").getAsJsonObject().has("address_1")){
                    adrress += obj.get("venue").getAsJsonObject().get("address_1").getAsString();
                }
                if(obj.get("venue").getAsJsonObject().has("city")){
                    adrress += ", " + obj.get("venue").getAsJsonObject().get("city").getAsString();
                }
                if(obj.get("venue").getAsJsonObject().has("state")){
                    adrress += ", " + obj.get("venue").getAsJsonObject().get("state").getAsString();
                }
                if(obj.get("venue").getAsJsonObject().has("name")){
                    venue_name = obj.get("venue").getAsJsonObject().get("name").getAsString();
                }
            }
            if(obj.get("group").getAsJsonObject().has("key_photo")){
                photo_url = obj.get("group").getAsJsonObject().get("key_photo").getAsJsonObject().get("photo_link").getAsString();
            }
            if(obj.has("description")){
                description = obj.get("description").getAsString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // id, title, url, organizer, description, holdDate, holdTime, venues, address

        Event newEvent = new Event(
                obj.get("id").getAsString(),
                obj.get("name").getAsString(),
                obj.get("link").getAsString(),
                obj.get("group").getAsJsonObject().get("name").getAsString(),
                description,
                theDates,
                theDates,
                venue_name,
                adrress,
                photo_url
        );
        eventlist.add(newEvent);
        eventAdapter.notifyDataSetChanged();

    }

    void addList(JsonObject obj, String theDates) throws JSONException {
        String organizer = "";
        try {
            // id, title, url, organizer, description, holdDate, holdTime, venues, address
            Event newEvent = new Event(
                    obj.get("id").getAsString(),
                    obj.get("name").getAsJsonObject().get("text").getAsString(),
                    obj.get("url").getAsString(),
                    obj.get("organizer").getAsJsonObject().get("name").getAsString(),
                    obj.get("description").getAsJsonObject().get("text").getAsString(),
                    theDates,
                    obj.get("start").getAsJsonObject().get("local").getAsString(),
                    obj.get("venue").getAsJsonObject().get("name").getAsString(),
                    obj.get("venue").getAsJsonObject().get("address").getAsJsonObject().get("localized_address_display").getAsString(),
                    obj.get("logo").getAsJsonObject().get("url").getAsString()
            );
            eventlist.add(newEvent);
        }
        catch (Exception e) {
        }
        eventAdapter.notifyDataSetChanged();

    }


    @Override
    public void onListItemClick(int index) {
        Log.e("onListItemClick", String.valueOf(index));
        Event event_tmp = eventlist.get(index);
//        Log.e("onListItemClick", String.valueOf(event_tmp.getTitle()));
//        Uri uri = Uri.parse(event_tmp.getUrl());
//        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(i);
        Intent i = new Intent(getActivity(), WebviewActivity.class);
        i.putExtra(Utils.SEND_URL, event_tmp.getUrl());
        i.putExtra(Utils.SEND_PAGE_TITLE, event_tmp.getTitle());
        startActivity(i);
    }

    /* 一度でいい処理 */
    void settingListView() {
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
                            String url = getEventbriteEventSearchURL();
//                            String new_url = url + "&start=" + String.valueOf(totalItemCount);
                            Log.e("onScroll::", url);
                            progressDialog.show();
                            makeJsonObjectRequest(url, false);
                        }
                    }
                }

            }
        });

    }


    /**
     * get the EVENTBRITE url
     */
    public String getEventbriteEventSearchURL(){
        String url = "";
        String url_locationWithQuery = "&location.address=" + userData.location;
        String url_queryWithQuery = "&q=" + userData.devStatus;
        String url_expandWithQuery = "&expand=organizer,venue";
        String url_tokenWithQuery = "token=" + Config.EVENTBRITE_TOKEN;

        //https://www.eventbriteapi.com/v3/events/search/?q=ios developer&location.address=vancouver,bc&token=CVKT5QQJUJYOJDWX2KNX
        url = Config.EVENTBRITE_URL_BASE + Config.EVENTBRITE_URL_API + url_tokenWithQuery + url_locationWithQuery + url_expandWithQuery + url_queryWithQuery;

        System.out.println("Eventbrite============");
        System.out.println(url);
        return url;
    }

    public String getMeetupEventSearchURL(){
        String url = "";
        String devStatus = "";
        String location = "";
        if(userData.location != null){
            if(userData.location.matches("")){
                location = userData.location;
            }
        }
        if(userData.devStatus != null){
            if(userData.devStatus.matches("")){
                devStatus = "Developer";
            }else{
                devStatus = userData.devStatus;
            }
        }
        String url_locationWithQuery = "&location=" + location;
        String url_queryWithQuery = "&text=" + devStatus;

        //https://www.eventbriteapi.com/v3/events/search/?q=ios developer&location.address=vancouver,bc&token=CVKT5QQJUJYOJDWX2KNX
        url = Config.MEETUP_URL_BASE + Config.MEETUP_URL_API + "upcoming_events=true&photo-host=public" +
                url_locationWithQuery + "&page=20&fields=group_key_photo" + url_queryWithQuery +
                Config.MEETUP_URL_SIGN;

        int diff = Double.compare(userData.lat, 0.0);
        if(diff != 0) {
            url += "&lat=" + userData.lat + "&lon=" + userData.lon;
        }

        System.out.println("MEETUP============");
        System.out.println(url);
        return url;
    }

//https://api.meetup.com/find/events?upcoming_events=true&photo-host=public&location=vancouver&
// page=20&text=iOS+Developer&sign=true&key=1b7e6b545246732d3b4d6c53f3a5563

}
