package com.alienhouse.kitten.util;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by shinji on 2017/09/01.
 */

public class FirebaseController {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private DatabaseReference devStatusRef;
    private List<String> devStatusArray = new ArrayList<String>();
    private User userData;
    private ValueEventListener userListener = null;
    private ValueEventListener favDataListener = null;
    private ValueEventListener devStatusDataListener = null;
    private CallBackTask callbacktask;
    private CallBackTaskNormal callbacktaskNormal;
    public boolean isFistLoad = true;
    private ValueEventListener getUserOnceListener;

    private static FirebaseController firebaseController;

    private FirebaseController() {
        firebaseController = null;
    }

    public static FirebaseController getInstance() {
        if (firebaseController == null) {
            firebaseController = new FirebaseController();
        }
        return firebaseController;
    }

    public static void deleteUserData(){
        firebaseController.userData = null;
    }

    public static User getUserData() {
        System.out.println("^getUserData"+firebaseController.userData);
        if(firebaseController.userData == null){
            FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
            firebaseController.setUserToData(userFirebase);
        }
        return firebaseController.userData;
    }

    public static void setUserToData(FirebaseUser user) {
        System.out.println("^^setUserToData:FirebaseUser");
            if (user != null) {
                if(firebaseController.userData == null){
                    createUserToData(user);
                }
                firebaseController.userData.userID = user.getUid();
                if(user.getDisplayName() != null) {
                    firebaseController.userData.username = user.getDisplayName();
                }
                firebaseController.userData.email = user.getEmail();
                if(user.getPhotoUrl() != null) {
                    firebaseController.userData.photourl = user.getPhotoUrl().toString();
                }
            }

    }

//    public static void writeFavDatabase(DatabaseReference usersFavRef){
//
//        userData.username = nameEdit.getText().toString();
//        String key = usersRef.push().getKey();
//        Map<String, Object> postValues = userData.toMap();
//        usersFavRef.updateChildren(postValues, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError != null) {
//                    System.out.println("Data could not be saved " + databaseError.getMessage());
//                } else {
//                    System.out.println("Data saved successfully.");
//                }
//                pd.hide();
//            }
//        });
//    }

    public static void createUserToData(FirebaseUser user) {
        String imgurl = null;
        if(user.getPhotoUrl() != null){
            imgurl = user.getPhotoUrl().toString();
        }
        firebaseController.userData = new User(
                user.getUid(),
                user.getDisplayName(),
                "",
                user.getEmail(),
                "",
                "",
                "",
                imgurl,
                ""
        );
    }

    public static void getUserDataEventListener(DatabaseReference usersRef) {

        if(firebaseController.userListener != null){
            usersRef.removeEventListener(firebaseController.userListener);
        }
            //変更があれば都度取得する
    //        usersRef.addValueEventListener(new ValueEventListener() {
            firebaseController.userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("======getUserDataEventListener:onDataChange========");
                    if(firebaseController.userData == null){
                        System.out.println("======nullなんじゃ:onDataChange========");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        createUserToData(user);
                    }

                    if(dataSnapshot != null){
                        // This method is called once with the initial value and again whenever data at this location is updated.
                        User usersValue = dataSnapshot.getValue(User.class);
                        //                String[] array = {};
                        //                array = devStatusValue.split(",", 0);
                        //                devStatusArray = Arrays.asList(array);
                        System.out.println(usersValue);

//                    firebaseController.userData.userID = usersValue.userID;
//                    firebaseController.userData.username = usersValue.username;
                        firebaseController.userData.devStatus = usersValue.devStatus;
//                    firebaseController.userData.email = usersValue.email;
                        firebaseController.userData.bio = usersValue.bio;
                        firebaseController.userData.location = usersValue.location;
                        firebaseController.userData.url = usersValue.url;
                        firebaseController.userData.country = usersValue.country;
                        firebaseController.userData.lat = usersValue.lat;
                        firebaseController.userData.lon = usersValue.lon;
                        System.out.println(firebaseController.userData.country);
                        //                userData.role = usersValue.getProperty('devStatus');
//                        firebaseController.callbacktask.CallBack(usersValue.location, usersValue.devStatus);
                    }
                    //nullでもなくっても

                    System.out.println("=====firebaseController.callbacktask====");
//                    if(firebaseController.isFistLoad) {
//                        firebaseController.callbacktaskNormal.CallBack();
//                        firebaseController.isFistLoad = false;
//                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Value:", "Failed to read value.", error.toException());
                    //ミスってもコールバックする
//                    if(firebaseController.isFistLoad) {
//                        firebaseController.callbacktaskNormal.CallBack();
//                        firebaseController.isFistLoad = false;
//                    }
                }
            };
            usersRef.addValueEventListener(firebaseController.userListener);
    }


    public static void getUserDataOnceEventListener(DatabaseReference usersRef) {

        if(firebaseController.getUserOnceListener != null){
            usersRef.removeEventListener(firebaseController.getUserOnceListener);
        }
        firebaseController.getUserOnceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("======getUserDataEventListener:onDataChange========");
                if(firebaseController.userData == null){
                    System.out.println("======nullなんじゃ:onDataChange========");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    createUserToData(user);
                }

                if(dataSnapshot != null){
                    // This method is called once with the initial value and again whenever data at this location is updated.
                    User usersValue = dataSnapshot.getValue(User.class);
                    System.out.println(usersValue);

                    firebaseController.userData.devStatus = usersValue.devStatus;
//                    firebaseController.userData.email = usersValue.email;
                    firebaseController.userData.bio = usersValue.bio;
                    firebaseController.userData.location = usersValue.location;
                    firebaseController.userData.url = usersValue.url;
                    firebaseController.userData.country = usersValue.country;
                    firebaseController.userData.lat = usersValue.lat;
                    firebaseController.userData.lon = usersValue.lon;
                    System.out.println(firebaseController.userData.country);
                    //                userData.role = usersValue.getProperty('devStatus');
//                    if(firebaseController.isFistLoad){
//                        firebaseController.callbacktask.CallBack();
//                        firebaseController.isFistLoad = false;
//                    }
                }

                System.out.println("=====firebaseController.callbacktask====");
                firebaseController.callbacktaskNormal.CallBack();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
                firebaseController.callbacktaskNormal.CallBack();
            }
        };
        usersRef.addListenerForSingleValueEvent(firebaseController.getUserOnceListener);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }
    public static class CallBackTask {
        public void CallBack() {
        }
        public void CallBack(String[] devStatusArray) {
        }
    }
    public void setOnCallBackNormal(CallBackTaskNormal _cbj) {
        callbacktaskNormal = _cbj;
    }
    public static class CallBackTaskNormal {
        public void CallBack() {
        }
    }



    public static void writeUserToData(DatabaseReference usersRef) {
        Map<String, Object> postValues = firebaseController.userData.toMap();
        usersRef.updateChildren(postValues, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });
    }


    public static void getDevStatusDataEventListener(DatabaseReference devStatusRef){
//        DatabaseReference devStatusRef = database.getReference("devStatus");
        firebaseController.devStatusDataListener = new ValueEventListener() {
            //        devStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                String devStatusValue = dataSnapshot.getValue(String.class);
                String[] array = {};
                array = devStatusValue.split(",", 0);
//                    List devStatusArray = Arrays.asList(array);
//                    Log.d("Value:", "Value is: " + devStatusValue);
                firebaseController.callbacktask.CallBack(array);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
            }
        };
        devStatusRef.addListenerForSingleValueEvent(firebaseController.devStatusDataListener);
    }


    public static void getFavDataEventListener(DatabaseReference favRef) {
        if(firebaseController.favDataListener == null) {
            //for devdata e.g) "devStatus"
            firebaseController.favDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again whenever data at this location is updated.
                    String devStatusValue = dataSnapshot.getValue(String.class);
                    String[] array = {};
                    array = devStatusValue.split(",", 0);
                    firebaseController.devStatusArray = Arrays.asList(array);
                    Log.d("Value:", "Value is: " + devStatusValue);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Value:", "Failed to read value.", error.toException());
                }
            };

            favRef.addValueEventListener(firebaseController.favDataListener);
        }
    }




}
