package com.example.shinji.datatimepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by shinji on 2017/07/27.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerDialogListener {
        public void onDialogTimePickerClick(DialogFragment dialog,int i, int i1);
    }

    // Use this instance of the interface to deliver action events
    TimePickerDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TimePickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    //タイマーダイアログを呼び出す
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), R.style.MyDialog, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //タイマーを動かす
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        System.out.println("TimePicker:::"+i+":"+i1);
        mListener.onDialogTimePickerClick(TimePickerFragment.this, i, i1);
//        tv.setText("TimePicker:::"+i+":"+i1);
    }

}