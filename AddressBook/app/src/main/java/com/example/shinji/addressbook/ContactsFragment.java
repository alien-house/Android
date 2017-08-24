package com.example.shinji.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.shinji.addressbook.data.DatabaseDescription;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //interface .: have only methods no implementation
    //Class implementing interface will have code
    public interface ContactFragmentInterface{
        void onAddContact();
        void onContactSelected(Uri uri);
    }

    private ContactAdapter contactAdapter;
    private int contact_loader = 0;
    public ContactFragmentInterface contactFragmentInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Inialize the loader whenever the fragment

        setHasOptionsMenu(true); // fragment has menu items to display
        // inflate GUI and get reference to the RecyclerView
        View view = inflater.inflate(
                R.layout.fragment_contacts, container, false
        );
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //set layoutManager
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getActivity().getBaseContext()
                )
        );
        //set Adapter
// recyclerView should display items in a vertical list
// use LinearLayoutManager
//set the adapter to recyclerview
// attach a custom ItemDecorator to draw dividers between list items
        //set Adapter
        contactAdapter =  new ContactAdapter(new ContactAdapter.ContactAdapterInterface() {
            @Override
            public void onClick(Uri uri) {
                contactFragmentInterface.onContactSelected(uri);
            }
        });
        recyclerView.setAdapter(contactAdapter);
        recyclerView.addItemDecoration( new ItemDivider(getContext()) );
        // improves performance if RecyclerView's layout size never changes
        recyclerView.setHasFixedSize(true);

// get the FloatingActionButton
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactFragmentInterface.onAddContact();
            }
        });
        return view;
//        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contactFragmentInterface = (ContactFragmentInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactFragmentInterface = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(contact_loader, null, this);
    }

    @Override
    //create a loader object and start loading the data into cursor
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader c = new CursorLoader(
                getActivity(),
                DatabaseDescription.Contact.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        return c;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        contactAdapter.notifyChange(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactAdapter.notifyChange(null);
    }

}
