package com.alienhouse.kitten.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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

import com.alienhouse.kitten.BaseActivity;
import com.alienhouse.kitten.R;
import com.alienhouse.kitten.StartActivity;
import com.alienhouse.kitten.login.RegisterActivity;
import com.alienhouse.kitten.util.Config;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
    private static ImageView profileImg;
    private Button btnUpdate,btnSignOut;
    private Spinner roleSpinner;
    private Spinner countrySpinner;
    private String userID = "";

    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;
//    private String spinnerItems[] = {};
    private ProgressDialog progressDialog;

    private static User userData;
    private List<String> devStatusArray = new ArrayList<String>();
//    private List<String> countryArray = new ArrayList<String>();
    private String[] countryArrayValue;
    private FirebaseController firebaseController;
    private static final int REQUEST_GALLERY = 0;
//    private static final int RESULT_OK = 1;
    private static Bitmap img;
    private static Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private static Uri mSourceUri = null;
    private static RectF mFrameRect = null;
    private RequestQueue mQueue;
    private static boolean isImgChanged = false;
    private Uri photourl;

    public interface SettingInfoFragmentInterface{
        void onSavedImage(boolean isImgChanged);
    }
    public SettingInfoFragmentInterface settingInfoFragmentInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_setting, container, false);
        nameEdit = view.findViewById(R.id.nameEdit);
        emailEdit = view.findViewById(R.id.emailEdit);
        bioEdit = view.findViewById(R.id.bioEdit);
        locationEdit = view.findViewById(R.id.locationEdit);
        urlEdit = view.findViewById(R.id.urlEdit);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        profileImg = view.findViewById(R.id.profileImg);
        countryArrayValue = getResources().getStringArray(R.array.array_country_value);

        storageRef = storage.getReference();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("saving");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        firebaseController = FirebaseController.getInstance();
        userData = firebaseController.getUserData();
        usersRef = database.getReference("users/" + userData.userID);
        if(userData != null){
            System.out.println("SettingInfoFragment:User aru:" + userData.username);
            System.out.println("SettingInfoFragment:User aru:" + userData.country);
            System.out.println("SettingInfoFragment:User aru:" + userData.bio);
            changeUI();
        }


        return view;
    }
    private void changeUI(){
        nameEdit.setText(userData.username);
        emailEdit.setText(userData.email);
        bioEdit.setText(userData.bio);
        locationEdit.setText(userData.location);
        urlEdit.setText(userData.url);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        settingInfoFragmentInterface = (SettingInfoFragmentInterface) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        settingInfoFragmentInterface = null;
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
//                userData.photourl = uri.toString();
            }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("userImagesRef :: onFailure");
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // ギャラリー呼び出し
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_GALLERY);

//                flagmentManager = getFragmentManager();
//                dialogFragment = new AlertDialogFragment();
//                dialogFragment.show(flagmentManager, "test alert dialog");
            }
        });
        System.out.println("======DatabaseReference========");

        //for devdata
        DatabaseReference devStatusRef = database.getReference("devStatus");
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
                progressDialog.show();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                firebaseController.deleteUserData();
                Intent nextItent;
                nextItent = new Intent(getActivity(), StartActivity.class);
                startActivity(nextItent);
            }
        });

        int posCS = changeUICountrySpinner();
        System.out.println("posCS::" + posCS);
        countrySpinner.setSelection(posCS, false);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                userData.country = countryArrayValue[position].toString();
//                userData.devStatus = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public int changeUIRole(){
        System.out.println("changeUIRole::");
        int pos = 0,
            maxnum = devStatusArray.size();
        if(devStatusArray != null){
            for(int i = 0; i < maxnum; i++){
                if(devStatusArray.get(i).matches(userData.devStatus)){
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }

    public int changeUICountrySpinner(){
        System.out.println("changeUICountrySpinner::"+userData.country);
        int pos = 0,
            maxnum = countryArrayValue.length;
        String LC = (userData.country).toLowerCase();
        if(countryArrayValue != null){
            for(int i = 0; i < maxnum; i++){
                if(countryArrayValue[i].matches(LC)){
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }

    public void getLanLon(String address){
        mQueue = Volley.newRequestQueue(getContext());

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address +
                "&key=" + Config.GOOGLEAPIS_KEY;

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse::","====================");
                        try {
                            JSONArray itemArray = response.getJSONArray("results");
                            JSONObject rootObject = itemArray.getJSONObject(0);
                            JSONArray location_array = rootObject.getJSONArray("address_components");
                            Log.d("getLanLonLOG", "results****: " + location_array);
                            double longitute = rootObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lng");

                            double latitude = rootObject
                                    .getJSONObject("geometry").getJSONObject("location")
                                    .getDouble("lat");
                            Log.d("getLanLonLOG", "results****: " + longitute + ":" +latitude);
                            userData.lat = latitude;
                            userData.lon = longitute;
                            settingInfoFragmentInterface.onSavedImage(isImgChanged);
                            progressDialog.dismiss();
//                            makeDataToListview(location_array);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error:googleapis:","~~~~~~~~~~~~~~~~~~~~~~");
//                        Log.d(LOG_TAG, error.toString());
//                        progressDialog.dismiss();
                    }
                }
        );
        mQueue.add(jsonObjectReq);
    }

    public void writeUser(){
        boolean isLocationChange = false;
        if(!userData.location.matches(locationEdit.getText().toString())){
            isLocationChange = true;
            getLanLon(locationEdit.getText().toString());
            Log.d("isLocationChange:", "isLocationChange?????");
        }

        userData.bio = bioEdit.getText().toString();
        userData.username = nameEdit.getText().toString();
        userData.email = emailEdit.getText().toString();
        userData.location = locationEdit.getText().toString();
        userData.url = urlEdit.getText().toString();

        Map<String, Object> postValues = userData.toMap();
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


        FirebaseUser user = mAuth.getCurrentUser();

        //change display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userData.username)
                .setPhotoUri(Uri.parse(userData.photourl))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("userData.username", "User profile updated.");
                            progressDialog.dismiss();
                        }
                    }
                });
        // change email
        if(!user.getEmail().matches(userData.email)){
            user.updateEmail(userData.email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("writeUser:", "User email address updated.");
                            }
                        }
                    });
        }
        if(!isLocationChange){
            settingInfoFragmentInterface.onSavedImage(isImgChanged);
            Log.d("writeUser:", "settingInfoFragmentInterface.onSavedImage");
            progressDialog.dismiss();
        }

        isImgChanged = false;
    }





    public static class AlertDialogFragment extends DialogFragment {

        private AlertDialog dialog ;
        private AlertDialog.Builder alert;
        private CropImageView cropImageView;
        private Button btnCancel;
        private Button btnUpload;

        public static AlertDialogFragment newInstance() {
            AlertDialogFragment frag = new AlertDialogFragment();
            Bundle args = new Bundle();
            frag.setArguments(args);
            return frag;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Crop this image!");

            View alertView = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);
            btnCancel = alertView.findViewById(R.id.btnCancel);
            btnUpload = alertView.findViewById(R.id.btnUpload);
            cropImageView = (CropImageView) alertView.findViewById(R.id.cropImageView);
            cropImageView.setImageBitmap(img);
            cropImageView.setCropMode(CropImageView.CropMode.SQUARE);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });
            btnUpload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cropImageView.crop(mSourceUri).execute(mCropCallback);
                }
            });
            alert.setView(alertView);
            dialog = alert.create();
            dialog.show();
            return dialog;
        }
        private final CropCallback mCropCallback = new CropCallback() {
            @Override public void onSuccess(Bitmap cropped) {

                profileImg.setImageBitmap(cropped);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                cropped.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference userImagesRef = storageRef.child("images/" + userData.userID + "/profile.jpg");
                UploadTask uploadTask = userImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        System.out.println("onFailure:Handle unsuccessful uploads");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("onSuccess:CropUploadTask");
                        getDialog().dismiss();
                        isImgChanged = true;
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        userData.photourl = downloadUrl.toString();
                    }
                });
            }

            @Override public void onError(Throwable e) {
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SettingInfoFragment", "onStart ーーーーーーーー");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "onActivityResultヨーーーーーーーー");
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                mSourceUri = data.getData();
                InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                img = BitmapFactory.decodeStream(in);
                in.close();
                // 選択した画像を表示
                Log.d("onActivityResult", "imgー");
                System.out.println(img);

                AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance();
//                dialogFragment = new AlertDialogFragment();
                flagmentManager = getFragmentManager();
                dialogFragment.show(flagmentManager, " alert dialog");
//                dialogFragment.setImage(img);

            } catch (Exception e) {
            }
            }
    }
}
