package com.example.shinji.datatimepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by shinji on 2017/07/27.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerDialogListener {
        public void onDialogDatePickerClick(int year, int month, int day);
    }

    // Use this instance of the interface to deliver action events
    DatePickerFragment.DatePickerDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DatePickerFragment.DatePickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,this, year, month, day);
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // my way
        mListener.onDialogDatePickerClick(year, month, day);

        //mitali's way
//        String s =  String.valueOf(year) + ":" + String.valueOf(month) + ":" + String.valueOf(day);
//        DateInterface d = (DateInterface) getActivity();
//        d.onReturnValue(s);


    }
}