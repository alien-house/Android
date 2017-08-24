package com.example.shinji.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shinji.addressbook.data.DatabaseDescription;

/**
 * Created by shinji on 2017/08/16.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //callback methods implemented by Mainactivity
    public interface DetailFragmentInterface{
        //method for edit and delete
        // pass uri of the selected contact for edition
        void onEditContact(Uri uri);
        void onContactDeleted();
    }
    //object for interface
    public DetailFragmentInterface detailFragmentInterface;

    //create an ID for loader
    private static  final int CONTACT_LOADER = 0;
    //uri of selected contact
    private Uri contactUri;
    private TextView nameTextView; // displays contact's name
    private TextView phoneTextView; // displays contact's phone
    private TextView emailTextView; // displays contact's email
    private TextView streetTextView; // displays contact's street
    private TextView cityTextView; // displays contact's city
    private TextView stateTextView; // displays contact's state
    private TextView zipTextView; // displays contact's zip

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //only this fragment is having optionMenu
        //in others it set to false
        setHasOptionsMenu(true);
        //create view from fragment_deatil.xml
        View view = inflater.inflate(
                R.layout.fragment_details,
                container,
                false
        );
        //get the reference of Textview
        nameTextView  =(TextView)view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView)view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        streetTextView = (TextView) view.findViewById(R.id.streetTextView);
        cityTextView = (TextView) view.findViewById(R.id.cityTextView);
        stateTextView = (TextView) view.findViewById(R.id.stateTextView);
        zipTextView = (TextView) view.findViewById(R.id.zipTextView);
        //get the bundle value for selected contact URI
        Bundle arg = getArguments();
        contactUri = arg.getParcelable(
                MainActivity.CONTACT_URI);
        Log.d("Detailed uri " , contactUri.toString());

        //Get the Loader to load the contact
        getLoaderManager().initLoader(CONTACT_LOADER,
                null,this);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        detailFragmentInterface = (DetailFragmentInterface) context;
    }

    //destoryed when fragment
    @Override
    public void onDetach() {
        super.onDetach();
        detailFragmentInterface = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit :
                //it will take you to Mainactitiy
                detailFragmentInterface.onEditContact(contactUri);
                return true;
            case R.id.action_delete :
                deleteContact();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteContact(){

        // use FragmentManager to display the confirmDelete DialogFragment

        new android.support.v7.app.AlertDialog.Builder(getContext())
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        String mSelectionClause = DatabaseDescription.Contact._ID + " LIKE ?";
                        String[] mSelectionArgs = {"user"};
                        Uri newContactUri = getActivity().getContentResolver().delete(
                                DatabaseDescription.Contact.CONTENT_URI,
                                mSelectionClause,
                                mSelectionArgs);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                contactUri, //Uri value which
                // display a specific contact
                null, // null projection returns all column
                null, //null selection return all rows
                null, // null selection selection value
                null  //sorting order
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//we should pass the data to UI
        //if your contact details in database exist
        if(data != null && data.moveToFirst()){
            int nameIndex = data.getColumnIndex
                    (DatabaseDescription.Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(
                    DatabaseDescription.Contact.COLUMN_PHONE);
            int emailIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_EMAIL);
            int streetIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STREET);
            int cityIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_STATE);
            int zipIndex = data.getColumnIndex(DatabaseDescription.Contact.COLUMN_ZIP);

            //fill textview with retrieved data
            nameTextView.setText(data.getString(nameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            emailTextView.setText(data.getString(emailIndex));
            streetTextView.setText(data.getString(streetIndex));
            cityTextView.setText(data.getString(cityIndex));
            stateTextView.setText(data.getString(stateIndex));
            zipTextView.setText(data.getString(zipIndex));
            Log.d("detail name I  ", String.valueOf(nameIndex));
            Log.d("detail name V ", data.getString(nameIndex));
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    // DialogFragment to confirm deletion of contact
//    private DialogFragment confirmDelete = new DialogFragment(){
//                // create an AlertDialog and return it
//                @Override
//                public Dialog onCreateDialog(Bundle bundle)
//                {
//                    // create a new AlertDialog Builder
//                    AlertDialog.Builder builder =
//                            new AlertDialog.Builder(getActivity());
//
//                    builder.setTitle(R.string.confirm_title);
//                    builder.setMessage(R.string.confirm_message);
//
//                    // provide an OK button that simply dismisses the dialog
//                    builder.setPositiveButton(R.string.button_delete,
//                            new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(
//                                        DialogInterface dialog, int button)
//                                {
//                                    final DatabaseConnector databaseConnector =
//                                            new DatabaseConnector(getActivity());
//
//                                    // AsyncTask deletes contact and notifies listener
//                                    AsyncTask<Long, Object, Object> deleteTask =
//                                            new AsyncTask<Long, Object, Object>()
//                                            {
//                                                @Override
//                                                protected Object doInBackground(Long... params)
//                                                {
//                                                    databaseConnector.deleteContact(params[0]);
//                                                    return null;
//                                                }
//
//                                                @Override
//                                                protected void onPostExecute(Object result)
//                                                {
//                                                    listener.onContactDeleted();
//                                                }
//                                            }; // end new AsyncTask
//
//                                    // execute the AsyncTask to delete contact at rowID
//                                    deleteTask.execute(new Long[] { rowID });
//                                } // end method onClick
//                            } // end anonymous inner class
//                    ); // end call to method setPositiveButton
//
//                    builder.setNegativeButton(R.string.button_cancel, null);
//                    return builder.create(); // return the AlertDialog
//                }
//            }; // end DialogFragment anonymous inner class

}
