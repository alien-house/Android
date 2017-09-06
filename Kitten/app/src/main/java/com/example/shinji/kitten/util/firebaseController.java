package com.example.shinji.kitten.util;

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
    private CallBackTask callbacktask;
    public boolean isFistLoad = true;

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
                    firebaseController.userData = new User(
                            user.getUid(),
                            user.getDisplayName(),
                            "",
                            user.getEmail(),
                            "",
                            "",
                            "",
                            user.getPhotoUrl().toString(),
                            ""
                    );
                }
                System.out.println("^^setUserToData:User is signed in~~~~");
                firebaseController.userData.userID = user.getUid();
                firebaseController.userData.username = user.getDisplayName();
                firebaseController.userData.email = user.getEmail();
                firebaseController.userData.photourl = user.getPhotoUrl().toString();
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

    public static void getUserDataEventListener(DatabaseReference usersRef) {
        if(firebaseController.userListener == null){
            //変更があれば都度取得する
    //        usersRef.addValueEventListener(new ValueEventListener() {
            firebaseController.userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("======getUserDataEventListener:onDataChange========");
                    if(firebaseController.userData == null){
                        System.out.println("======nullなんじゃ:onDataChange========");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseController.userData = new User(
                                user.getUid(),
                                user.getDisplayName(),
                                "",
                                user.getEmail(),
                                "",
                                "",
                                "",
                                user.getPhotoUrl().toString(),
                                ""
                        );
                    }

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
                    System.out.println(firebaseController.userData.country);
    //                userData.role = usersValue.getProperty('devStatus');
//                    Log.d("usersValue:", "usersValue is: " + usersValue);
                    if(firebaseController.isFistLoad){
                        firebaseController.callbacktask.CallBack();
                        firebaseController.isFistLoad = false;
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Value:", "Failed to read value.", error.toException());
                }
            };
            usersRef.addValueEventListener(firebaseController.userListener);
        }
    }


    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
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
