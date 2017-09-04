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
    private ValueEventListener userListener;
    private ValueEventListener devDataListener;


    private static FirebaseController firebaseController = new FirebaseController( );

    private FirebaseController() {
    }

    public static FirebaseController getInstance( ) {
        return firebaseController;
    }


    public static User getUserData(FirebaseUser user) {
        if(firebaseController.userData == null){
            firebaseController.setUserToData(user);
        }
        return firebaseController.userData;
    }

    public static void setUserToData(FirebaseUser user) {
        if(firebaseController.userData == null){
            if (user != null) {
                System.out.println("^^setUserToData:User is signed in~~~~");
                if (user != null) {
                    firebaseController.userData = new User(
                            user.getUid(),
                            user.getDisplayName(),
                            "",
                            user.getEmail(),
                            "",
                            "",
                            "",
                            user.getPhotoUrl().toString()
                    );
                }
            } else {
                System.out.println("^0^:No user is");
            }
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
                    System.out.println("======onDataChange========");
                    // This method is called once with the initial value and again whenever data at this location is updated.
                    User usersValue = dataSnapshot.getValue(User.class);
    //                String[] array = {};
    //                array = devStatusValue.split(",", 0);
    //                devStatusArray = Arrays.asList(array);
                    System.out.println(usersValue);

    //                if(!usersValue.devStatus.isEmpty()){
    //                    userData.devStatus = usersValue.devStatus;
    //                }
    //                if(!usersValue.bio.isEmpty()){
    //                    userData.bio = usersValue.bio;
    //                    bioEdit.setText(userData.bio);
    //                }
                    firebaseController.userData.userID = usersValue.userID;
                    firebaseController.userData.username = usersValue.username;
                    firebaseController.userData.devStatus = usersValue.devStatus;
                    firebaseController.userData.email = usersValue.email;
                    firebaseController.userData.bio = usersValue.bio;
                    firebaseController.userData.location = usersValue.location;
                    firebaseController.userData.url = usersValue.url;
    //                userData.role = usersValue.getProperty('devStatus');
                    Log.d("usersValue:", "usersValue is: " + usersValue);
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


    public static void getDataBaseEventListener(DatabaseReference devStatusRef) {
        if(firebaseController.devDataListener == null) {
            //for devdata e.g) "devStatus"
            firebaseController.devDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again whenever data at this location is updated.
                    String devStatusValue = dataSnapshot.getValue(String.class);
                    String[] array = {};
                    array = devStatusValue.split(",", 0);
                    firebaseController.devStatusArray = Arrays.asList(array);
                    Log.d("Value:", "Value is: " + devStatusValue);

//                return dataSnapshot;
                    //setting spinner
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, devStatusArray);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                roleSpinner.setAdapter(adapter);
//                // リスナーを登録
//                roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    //　アイテムが選択された時
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Spinner spinner = (Spinner) parent;
//                        String item = (String) spinner.getSelectedItem();
//
//                        if (item.equals("Android")) {
//    //                    textView.setText("Android");
//                            Toast.makeText(getActivity(), "Android", Toast.LENGTH_SHORT).show();
//                        } else if (item.equals("Apple")) {
//    //                    textView.setText("Apple");
//                            Toast.makeText(getActivity(), "Apple", Toast.LENGTH_SHORT).show();
//                        } else if (item.equals("Windows")) {
//    //                    textView.setText("Windows");
//                            Toast.makeText(getActivity(), "Windows", Toast.LENGTH_SHORT).show();
//                        } else {
//    //                    textView.setText("Spinner");
//                            Toast.makeText(getActivity(), "Spinner", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    //　アイテムが選択されなかった
//                    public void onNothingSelected(AdapterView<?> parent) {
//                        //
//                    }
//                });

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Value:", "Failed to read value.", error.toException());
                }
            };

            devStatusRef.addListenerForSingleValueEvent(firebaseController.devDataListener);
        }
    }
}
