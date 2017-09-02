package com.example.shinji.kitten.util;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shinji on 2017/09/01.
 */

public class firebaseController {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private DatabaseReference devStatusRef;
    private List<String> devStatusArray = new ArrayList<String>();


    private static firebaseController firebaseController = new firebaseController( );

    private firebaseController() {

    }

    public static firebaseController getInstance( ) {
        return firebaseController;
    }
    protected static void getDataFromFirebase(){
        //for devdata
        firebaseController.devStatusRef = firebaseController.database.getReference("devStatus");
        firebaseController.devStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                String devStatusValue = dataSnapshot.getValue(String.class);
                String[] array = {};
                array = devStatusValue.split(",", 0);
                firebaseController.devStatusArray = Arrays.asList(array);
                Log.d("Value:", "Value is: " + devStatusValue);

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
        });
    }
}
