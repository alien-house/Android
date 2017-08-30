package com.example.shinji.kitten.dashboard;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinji.kitten.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shinji on 2017/08/28.
 */

public class SettingInfoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private TextView nameEdit,emailEdit,bioEdit,locationEdit,urlEdit;
    private ImageView profileImg;
    private Button btnUpdate;
    private Spinner roleSpinner;
    private String userID = "";
//    private String spinnerItems[] = {};
    private ProgressDialog pd;

    private User userData;
    private List<String> devStatusArray = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_setting, container, false);
        nameEdit = view.findViewById(R.id.nameEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        bioEdit = view.findViewById(R.id.bioEdit);
        locationEdit = view.findViewById(R.id.locationEdit);
        urlEdit = view.findViewById(R.id.urlEdit);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        profileImg = view.findViewById(R.id.profileImg);

        storageRef = storage.getReference();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("saving");

//        Toast.makeText(getActivity(), "TEST BTN CLICK2", Toast.LENGTH_SHORT).show();
//        btnTEST = (Button) view.findViewById(R.id.btnTEST1);
//        btnTEST.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TEST BTN CLICK1", Toast.LENGTH_SHORT).show();
//            }
//        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("^^:User is signed in~~~~");
            if (user != null) {
                nameEdit.setText(user.getDisplayName());
                emailEdit.setText(user.getEmail());
                userData = new User(
                        user.getUid(),
                        user.getDisplayName(),
                        "rde",
                        user.getEmail(),
                        "biowoireru",
                        "vancouver",
                        "url"
                );
                usersRef = database.getReference("users/" + userData.userID);
                System.out.println(userData);
                System.out.println(userData.userID);
//                if(name != null){
//                    texthello.setText("Hello! " + name);
//                }
            }

        } else {
            // No user is signed in
            System.out.println("^0^:No user is ");
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //for iamge
        StorageReference userImagesRef = storageRef.child("images/" + userData.userID + "/profile.jpg");
        userImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                System.out.println("userImagesRef :: ");
                System.out.println(uri);
                if(uri != null){
                    GetImageTask myTask = new GetImageTask(profileImg);
                    myTask.execute(uri.toString());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("userImagesRef :: onFailure");
            }
        });


        //for devdata
        DatabaseReference devStatusRef = database.getReference("devStatus");
        devStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                String devStatusValue = dataSnapshot.getValue(String.class);
                String[] array = {};
                array = devStatusValue.split(",", 0);
                devStatusArray = Arrays.asList(array);
                Log.d("Value:", "Value is: " + devStatusValue);

                //setting spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, devStatusArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roleSpinner.setAdapter(adapter);
                // リスナーを登録
                roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    //　アイテムが選択された時
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        String item = (String) spinner.getSelectedItem();

                        if (item.equals("Android")) {
//                    textView.setText("Android");
                            Toast.makeText(getActivity(), "Android", Toast.LENGTH_SHORT).show();
                        } else if (item.equals("Apple")) {
//                    textView.setText("Apple");
                            Toast.makeText(getActivity(), "Apple", Toast.LENGTH_SHORT).show();
                        } else if (item.equals("Windows")) {
//                    textView.setText("Windows");
                            Toast.makeText(getActivity(), "Windows", Toast.LENGTH_SHORT).show();
                        } else {
//                    textView.setText("Spinner");
                            Toast.makeText(getActivity(), "Spinner", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //　アイテムが選択されなかった
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
            }
        });


        System.out.println("======DatabaseReference========");
        System.out.println(userData.userID);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("======onDataChange========");
                // This method is called once with the initial value and again whenever data at this location is updated.
                User usersValue = dataSnapshot.getValue(User.class);
//                String[] array = {};
//                array = devStatusValue.split(",", 0);
//                devStatusArray = Arrays.asList(array);
                System.out.println(usersValue);
                //元のを最新差分文を更新する
                userData.devStatus = usersValue.devStatus;
                userData.bio = usersValue.bio;

//                System.out.println(usersValue.devStatus);
                bioEdit.setText(userData.bio);

//                userData.role = usersValue.getProperty('devStatus');
                Log.d("usersValue:", "usersValue is: " + usersValue);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Value:", "Failed to read value.", error.toException());
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeUser();
                pd.show();
            }
        });


    }

    public void writeUser(){
        userData.bio = bioEdit.getText().toString();
        String key = usersRef.push().getKey();
        Map<String, Object> postValues = userData.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put(key, postValues);
        usersRef.updateChildren(postValues, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
                pd.hide();
            }
        });
    }






}
