package com.example.shinji.addressbook;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

//https://github.com/dwa012/CSCI4669-Fall14-Android/tree/master/Book%20Example%20Code/Android%20Studio/AddressBook/app/src/main

public class MainActivity extends AppCompatActivity implements ContactsFragment.ContactFragmentInterface,
        AddEditFragment.AddEditFragmentInterface,
        DetailFragment.DetailFragmentInterface{

    private ContactsFragment contactsFragment;
    public static final String CONTACT_URI = "contact_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add the fragment into framelayout
        //use fragment transaction

        if (findViewById(R.id.fragmentContainer) != null) { // phone
            contactsFragment = new ContactsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, contactsFragment);
            fragmentTransaction.commit();
        }
        else{
//            contactsFragment = new ContactsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);
            fragmentTransaction.replace(R.id.contactsFragment, contactsFragment);
            fragmentTransaction.commit();
        }

//        displayAddEditFragment(R.id.fragmentContainer, null);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // if contactListFragment is null, activity running on tablet,
        // so get reference from FragmentManager
        if (contactsFragment == null){
            contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactsFragment);
        }
    }
    //display fragment for adding a new or editing an exisiting
    //ViewID is layout id
    //contacturi is the path for contentprovider
    public void displayAddEditFragment(int ViewId, Uri contacturi){

        AddEditFragment addEditFragment = new AddEditFragment();
        if(contacturi != null){
            Bundle argument = new Bundle();
            argument.putParcelable(CONTACT_URI, contacturi);
            addEditFragment.setArguments(argument);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(ViewId, addEditFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAddContact() {

        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, null);
        else
            displayAddEditFragment(R.id.rightPaneContainer, null);

    }

    @Override
    public void onContactSelected(Uri uri) {
        DetailFragment detailFragment = new DetailFragment();
        //use FragmentTranscation
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (findViewById(R.id.fragmentContainer) != null) // phone
            transaction.replace(R.id.fragmentContainer, detailFragment);
        else // tablet
        {
            getFragmentManager().popBackStack(); // removes top of back stack
            transaction.replace(R.id.rightPaneContainer, detailFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();

        //create a bundle object that will pass selected
        // row uri to detailFragment
        Bundle bundle = new Bundle();
        Log.d("uri " , uri.toString());
        bundle.putParcelable(CONTACT_URI,uri);
        detailFragment.setArguments(bundle);
    }

    @Override
    public void onAddEditComplete(Uri uri) {
        getSupportFragmentManager().popBackStack();
//        contactsFragment.updateContactList();
    }

    @Override
    public void onEditContact(Uri uri) {

        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, uri);
        else
            displayAddEditFragment(R.id.rightPaneContainer, null);
    }

    @Override
    public void onContactDeleted() {

        getSupportFragmentManager().popBackStack();
//        contactsFragment = new ContactsFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragmentContainer, contactsFragment);
//        fragmentTransaction.commit();

    }
}
