package com.example.shinji.addressbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shinji.addressbook.data.DatabaseDescription;

/**
 * Created by shinji on 2017/08/16.
 */

//user LoaderManager.LoaderCallback<> interface
// for loading Asyn data from content provider
public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    //interface
    public  interface AddEditFragmentInterface{
        void onAddEditComplete(Uri uri);
    }
    public AddEditFragmentInterface addEditFragmentInterface;
    //Every Loader needs a Unique ID
    //Integer constant for Loader
    private static final int CONTACT_LOADER = 0;
    //create a contact uri
    private Uri contactUri; // contacturi value will come from MainActivity

    // edittext for contact
    private TextInputLayout nameTextInput;
    private TextInputLayout phoneTextInput;
    private TextInputLayout emailTextInput;
    private TextInputLayout streetTextInput;
    private TextInputLayout cityTextInput;
    private TextInputLayout stateTextInput;
    private TextInputLayout zipTextInput;
    private FloatingActionButton saveContactFab;
    //check whether insert or update
    private boolean addingNewContact = true;
    //create a View for Fragment
    private ViewGroup viewContainer;

    @Nullable
    @Override
    // container: parent actitivy
    // savedInstanceState : you can pass the data
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // inflate the GUI and get the reference for Edittext
        View view = inflater.inflate(
                R.layout.fragment_add_edit,
                container,
                false
        );
        viewContainer = container;
        nameTextInput = (TextInputLayout) view.findViewById(R.id.nameTextInputLayout);
        phoneTextInput = (TextInputLayout) view.findViewById(R.id.phoneTextInputLayout);
        emailTextInput = (TextInputLayout) view.findViewById(R.id.emailTextInputLayout);
        streetTextInput = (TextInputLayout) view.findViewById(R.id.stateTextInputLayout);
        cityTextInput = (TextInputLayout) view.findViewById(R.id.cityTextInputLayout);
        stateTextInput = (TextInputLayout) view.findViewById(R.id.stateTextInputLayout);
        zipTextInput = (TextInputLayout) view.findViewById(R.id.zipTextInputLayout);
        // set FloatingActionButton's event Listener
        saveContactFab = (FloatingActionButton) view.findViewById(R.id.saveFloatingActionButton);
        saveContactFab.setOnClickListener(saveDataListener);

        //will have selected contact Id if editing
        //otherwise a null
        Bundle argument = getArguments();
        if(argument != null){
            addingNewContact = false;
            contactUri = argument.getParcelable(MainActivity.CONTACT_URI);
        }
        //loda the data fr seleted contact from contents
        //dataase reading operation
        if(contactUri != null){
            getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        }
        return view;

    }

    private View.OnClickListener saveDataListener =
            new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    saveContact();//this is method to save to database
                }
            };

    //saveContact() saves infromaition into database
    //insert() : requiered contentValues
    private void saveContact(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseDescription.Contact.COLUMN_NAME, nameTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_PHONE, phoneTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_EMAIL, emailTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_STREET, streetTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_CITY, cityTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_STATE, stateTextInput.getEditText().getText().toString());
        contentValues.put(DatabaseDescription.Contact.COLUMN_ZIP, zipTextInput.getEditText().getText().toString());

        if(addingNewContact) {
            //you need a uri to insert()
            //this Uri is used to call contentResolver insert the data into addressBook content
            Uri newContactUri = getActivity().getContentResolver().insert(
                    DatabaseDescription.Contact.CONTENT_URI,
                    contentValues);
            Snackbar.make(viewContainer, "Hello", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Snackbar.onClick", "Data insert Succesfully");
                        }
                    })
                    .show();
//            Toast.makeText(getActivity(), "Data insert Succesfully", Toast.LENGTH_SHORT).show();
            // Change the Toast to SnackBar
            // SnackBar = notification feedback to the user
            //and you add actions to snackBar like undo, cancel, ok
            addEditFragmentInterface.onAddEditComplete(newContactUri);
        }else{
            //use contentResolvers update method
            //it will returns the int that how many low
            int updatedRows = getActivity().getContentResolver().update(
                    contactUri,
                    contentValues,
                    null,
                    null
            );
            //if sucess
            if(updatedRows > 0){
                Toast.makeText(getContext(), R.string.contact_updated, Toast.LENGTH_SHORT).show();
                Snackbar.make(viewContainer, "Hello", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Snackbar.onClick", "UNDO Clicked");
                            }
                        })
                        .show();
                addEditFragmentInterface.onAddEditComplete(contactUri);
                //onAddEdit() implemented in MainActitiviy
            }
            //failure
            else{
                Toast.makeText(getContext(), R.string.contact_not_added, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addEditFragmentInterface = (AddEditFragmentInterface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addEditFragmentInterface = null;
    }

    @Override
    //This one will create a LoaderManager Start Loading the data from Content Provider
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Create a CursorLoader based on ID
        //getActitivy() : this is a fragment attached to the activity thats why we are using.
        CursorLoader c = new CursorLoader(
                getActivity(),
                contactUri, // Uri of the contact to display
                null, //null means all columns
                null, //null return all rows
                null, // no where clause
                null
        );
        return c;
    }

    @Override
    //Once the loading of data is finished than This method in from here you can give the data back to UI
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Called when loading is completed
        if(data != null && data.moveToFirst()){
            //get the ColumnIdex for each data item
            int nameIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_PHONE);
            int emailIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_EMAIL);
            int streetIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STREET);
            int cityIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STATE);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            // find the remaingin column index
            //fill my edittext with retrieved
            nameTextInput.getEditText().setText( data.getString(nameIndex) );
            phoneTextInput.getEditText().setText( data.getString(phoneIndex) );
            emailTextInput.getEditText().setText( data.getString(emailIndex) );
            streetTextInput.getEditText().setText( data.getString(streetIndex) );
            cityTextInput.getEditText().setText( data.getString(cityIndex) );
            stateTextInput.getEditText().setText( data.getString(stateIndex) );
            zipTextInput.getEditText().setText( data.getString(zipIndex) );
            //fill the
        }
    }

    @Override
    //It will reset the Loader release the reference for Loader
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
