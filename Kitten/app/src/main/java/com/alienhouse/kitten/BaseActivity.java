package com.alienhouse.kitten;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.alienhouse.kitten.dashboard.GetImageTask;
import com.alienhouse.kitten.dashboard.SettingFragment;
import com.alienhouse.kitten.dashboard.SettingInfoFragment;
import com.alienhouse.kitten.event.EventFragment;
import com.alienhouse.kitten.favorite.FavoriteFragment;
import com.alienhouse.kitten.main.JobFragment;
import com.alienhouse.kitten.main.JobSearchFragment;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by shinji on 2017/08/27.
 */
//https://github.com/delaroy/MaterialTabs

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener,
        SettingInfoFragment.SettingInfoFragmentInterface{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef;
    private FirebaseController firebaseController;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private User userData;
    private ProgressDialog progressDialog;
//    private boolean isFirst = true;

    private static final String TAG = "MainActivity";
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private ViewPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    public static final String SEARCH_LOC = "SEARCH_LOC";
    public static final String SEARCH_WORD = "SEARCH_WORD";
//    private MenuItem menuItem;
    private DrawerLayout drawer;
//    private GoogleApiClient mGoogleApiClient;
    // 選択中のタブ番号を保持
    private int selected;
    private ImageView profileSideImg;
    private TextView nhName;
    private TextView nhEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_base_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        menuItem = findViewById(R.id.menuItem);

        storageRef = storage.getReference();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nh = navigationView.getHeaderView(0);
        profileSideImg = nh.findViewById(R.id.imageView);
        nhName = nh.findViewById(R.id.headName);
        nhEmail = nh.findViewById(R.id.headEmail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("User Data Loading");
//        progressDialog.show();
        setTitle("Job Search");
//        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        firebaseController = FirebaseController.getInstance();
        userData = firebaseController.getUserData();
        usersRef = database.getReference("users/" + userData.userID);

        //ユーザーデータセット後にやったほうがいい。取得できなくても帰って来るはずなのんで。
        firstPage();

        firebaseController.getUserDataEventListener(usersRef);//終わった後に木灰する？
        System.out.println("userImagesRef:userData.photourl "+userData.photourl);
        System.out.println("userImagesRef:userData.userID "+userData.userID);
        System.out.println("userImagesRef:userData.userID "+userData.userID);

        setSideImage(true);

    }


    public void setSideImage(boolean isImgChanged){
        if(isImgChanged){

            System.out.println("setSideImageUserID:"+userData.userID);
            StorageReference userImagesRef = storageRef.child("images/" + userData.userID + "/profile.jpg");
            userImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    System.out.println(uri);
                    if(uri != null){
                        System.out.println("userImagesRef:userData.uri " + uri.toString());
                        userData.photourl = uri.toString();
                        GetImageTask myTask = new GetImageTask(profileSideImg);
                        myTask.execute(uri.toString());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("userImagesRef :: onFailure");
                }
            });
        }

        nhName.setText(userData.username);
        nhEmail.setText(userData.email);


    }

    @Override
    public void onSavedImage(boolean isImgChanged) {
        System.out.println("onSavedImage--------:" + isImgChanged);
        setSideImage(isImgChanged);
//
//        EventFragment eventFragment = (EventFragment)pagerAdapter.getItem(mViewPager.getCurrentItem());
//        eventFragment.update();
        Fragment fragment = pagerAdapter.getRegisteredFragment(2);
        EventFragment eventFragment = (EventFragment)fragment;
        eventFragment.update();


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart :: onStart");
    }

    private void firstPage(){
        mViewPager = findViewById(R.id.containers);
        mViewPager.setOffscreenPageLimit(2);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(pagerAdapter);

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onBackPressed() {
        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();

        System.out.println("onBackPressed::backStackCnt"+backStackCnt);

        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_job) {
            // Handle the camera action
            System.out.println("nav_job");
            mViewPager.setCurrentItem(0);
//            JobSearchFragment jobSearchFragment = (JobSearchFragment)pagerAdapter.getItem(mViewPager.getCurrentItem());
//            jobSearchFragment.update();
        } else if (id == R.id.nav_favorite) {
            System.out.println("nav_favorite");
            mViewPager.setCurrentItem(1);
        }else if (id == R.id.nav_event) {
            System.out.println("nav_event");
            mViewPager.setCurrentItem(2);
        }else if (id == R.id.nav_onlinecourse) {
            System.out.println("nav_onlinecourse");
            mViewPager.setCurrentItem(3);
        }else if (id == R.id.nav_setting) {
            System.out.println("nav_setting");
            mViewPager.setCurrentItem(4);
        }

//        Fragment fragment = null;
//        Class fragmentClass = null;
//
//        if (id == R.id.nav_job) {
//            // Handle the camera action
//            System.out.println("nav_job");
//            fragmentClass = JobSearchFragment.class;
//        } else if (id == R.id.nav_favorite) {
//            System.out.println("nav_favorite");
//            fragmentClass = FavoriteFragment.class;
//
//        } else if (id == R.id.nav_event) {
//            System.out.println("nav_event");
//
//        } else if (id == R.id.nav_setting) {
//            System.out.println("nav_setting");
//            fragmentClass = SettingFragment.class;
//
//        }else{
//            fragmentClass = JobFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.containers, fragment).commit();
//
        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
