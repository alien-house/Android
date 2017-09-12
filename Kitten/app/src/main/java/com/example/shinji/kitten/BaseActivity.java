package com.example.shinji.kitten;

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

import com.example.shinji.kitten.dashboard.GetImageTask;
import com.example.shinji.kitten.dashboard.SettingFragment;
import com.example.shinji.kitten.favorite.FavoriteFragment;
import com.example.shinji.kitten.main.JobFragment;
import com.example.shinji.kitten.main.JobSearchFragment;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.User;
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

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener  {

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
        progressDialog.show();
//        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());


        firebaseController = FirebaseController.getInstance();
        userData = firebaseController.getUserData();

        if(userData != null) {
            nhName.setText(userData.username);
            nhEmail.setText(userData.email);
        }
        usersRef = database.getReference("users/" + userData.userID);
        firebaseController.getUserDataEventListener(usersRef);
        firebaseController.setOnCallBackNormal(new FirebaseController.CallBackTaskNormal(){
            @Override
            public void CallBack() {
                super.CallBack();
                Log.d("BaseActivity:", "CallBack: " + "多分終わらん＝＝＝＝＝＝＝＝");
                System.out.println("BaseActivity:" + userData.username);
                System.out.println("BaseActivity:" + userData.country);
                System.out.println("BaseActivity:" + userData.bio);
                System.out.println("BaseActivity:" + userData.userID);
//                mViewPager.setAdapter(mSectionPageAdapter);
//                setupViewPager(mViewPager);
                progressDialog.dismiss();
                //ユーザーデータセット後にやったほうがいい。取得できなくても帰って来るはずなのんで。
                firstPage();

                StorageReference userImagesRef = storageRef.child("images/" + userData.userID + "/profile.jpg");
                userImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("BaseActivity**"+uri);
                        if(uri != null){
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
        });


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
//        setupViewPager(mViewPager);
//
//        JobSearchFragment fragment = new JobSearchFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.containers, fragment).commit();
    }


    private void setupViewPager(ViewPager viewPager){
        String[] nameNames = {"Job","Favorite",/*"Event","Learning",*/"Setting"};
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobFragment(), nameNames[0]);
        adapter.addFragment(new FavoriteFragment(), nameNames[1]);
        adapter.addFragment(new SettingFragment(), nameNames[2]);
//        adapter.addFragment(new SettingFragment(), nameNames[3]);
//        adapter.addFragment(new SettingFragment(), nameNames[4]);
        viewPager.setAdapter(adapter);

//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        TabLayout.Tab tab = tabLayout.newTab();
//        tab.setCustomView(R.layout.custom_tab);
//        tab.setText("Tab 1");
//        tab.setIcon(R.drawable.ic_adjust_24dp);
//        tabLayout.addTab(tab);

//        tabLayout.setupWithViewPager(mViewPager);
//        setTabs(nameNames);
//        tabLayout.getTabAt(0).setCustomView(R.layout.custom_tab);
//        View tabviews = tabLayout.getTabAt(0).getCustomView();
//        TextView tabTxtView = tabviews.findViewById(R.id.tabContent);
//        ImageView tabImageView = tabviews.findViewById(R.id.tabIcon);
//        tabTxtView.setText("Job");
//        tabImageView.setImageResource(R.drawable.ic_business_center_24dp);

//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_business_center_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_favorite_24dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings_24dp);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selected = position;
            }
            @Override
            public void onPageSelected(int position) {
                selected = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    public void setTabs(String[] tabName){
//        int[] iconsName = {
//                R.drawable.ic_business_center_24dp,
//                R.drawable.ic_favorite_24dp,
//                R.drawable.ic_settings_24dp
//        };
//        int maxNum = tabName.length;
//        for (int i = 0; i < maxNum; i++){
//            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
//            View tabview2 = tabLayout.getTabAt(i).getCustomView();
//            TextView tabTxtView = tabview2.findViewById(R.id.tabContent);
//            ImageView tabImageView = tabview2.findViewById(R.id.tabIcon);
//            tabTxtView.setText(tabName[i]);
//            tabImageView.setImageResource(iconsName[i]);
//        }
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    // 戻るボタン
//    @Override
//    public void onBackPressed() {
//        System.out.println("selected:"+selected);
//        // 選択中のタブのRootFragmentにバックスタックがあれば戻る処理
//        FragmentManager fm = getSupportFragmentManager();
//        List<Fragment> fragments = fm.getFragments();
//        Fragment fragment = fragments.get(selected);
//        FragmentManager fragmentManager = fragment.getChildFragmentManager();
//        if (0 < fragmentManager.getBackStackEntryCount()) {
//            fragmentManager.popBackStack();
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_job) {
            // Handle the camera action
            System.out.println("nav_job");
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.nav_favorite) {
            System.out.println("nav_favorite");
            mViewPager.setCurrentItem(1);

        }
//        else if (id == R.id.nav_event) {
//            System.out.println("nav_event");
//            mViewPager.setCurrentItem(2);
//
//        }
        else if (id == R.id.nav_setting) {
            System.out.println("nav_setting");
            mViewPager.setCurrentItem(2);

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
