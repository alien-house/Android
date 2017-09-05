package com.example.shinji.kitten.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.shinji.kitten.BaseActivity;
import com.example.shinji.kitten.R;
import com.example.shinji.kitten.StartActivity;
import com.example.shinji.kitten.login.RegisterActivity;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.User;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    private Button btnUpdate,btnSignOut;
    private Spinner roleSpinner;
    private String userID = "";
//    private String spinnerItems[] = {};
    private ProgressDialog pd;

    private User userData;
    private List<String> devStatusArray = new ArrayList<String>();
    private FirebaseController firebaseController;

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
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        profileImg = view.findViewById(R.id.profileImg);

        storageRef = storage.getReference();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("saving");
        firebaseController = FirebaseController.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        firebaseController = FirebaseController.getInstance();
        userData = firebaseController.getUserData();
        if(userData != null){
            System.out.println("SettingInfoFragment:User aru" + userData.username);
            changeUI();
        }

        System.out.println("SettingInfoFragment userData@@:" + userData.username);

        return view;
    }
    private void changeUI(){
        nameEdit.setText(userData.username);
        emailEdit.setText(userData.email);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //for iamge
        StorageReference userImagesRef = storageRef.child("images/" + userData.userID + "/profile.jpg");
        userImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                if(uri != null){
                    GetImageTask myTask = new GetImageTask(profileImg);
                    myTask.execute(uri.toString());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("userImagesRef :: onFailure");
            }
        });

        System.out.println("======DatabaseReference========");
        usersRef = database.getReference("users/" + userData.userID);
        firebaseController.getUserDataEventListener(usersRef);

        //for devdata
        DatabaseReference devStatusRef = database.getReference("devStatus");

        //just once
        devStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                String devStatusValue = dataSnapshot.getValue(String.class);
                String[] array = {};
                array = devStatusValue.split(",", 0);
                devStatusArray = Arrays.asList(array);
                Log.d("Value:", "Value is: " + devStatusValue);
                int pos = changeUIRole();
                //setting spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, devStatusArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roleSpinner.setAdapter(adapter);
                roleSpinner.setSelection(pos, false);
                roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    //　アイテムが選択された時
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner spinner = (Spinner) parent;
                        String item = (String) spinner.getSelectedItem();
                        userData.devStatus = parent.getItemAtPosition(position).toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
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

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent nextItent;
                nextItent = new Intent(getActivity(), StartActivity.class);
                startActivity(nextItent);
            }
        });


    }

    public int changeUIRole(){
        int pos = 0,
            maxnum = devStatusArray.size();
        if(devStatusArray != null){
            System.out.println("changeUIRole::");
            for(int i = 0; i < maxnum; i++){
                if(devStatusArray.get(i).matches(userData.devStatus)){
                    pos = i;
                }
            }
        }
        return pos;
    }

    public void writeUser(){
        userData.bio = bioEdit.getText().toString();
        userData.username = nameEdit.getText().toString();
        Map<String, Object> postValues = userData.toMap();
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
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userData.username)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("userData.username", "User profile updated.");
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SettingInfoFragment", "onResume ーーーーーーーー");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("SettingInfoFragment", "onStop settingがonStop終わりましたヨーーーーーーーー");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SettingInfoFragment", "onDestroy settingが終わりましたヨーーーーーーーー");
    }
}
