package com.example.shinji.fragmentdemo;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by shinji on 2017/07/26.
 */

public class AlertDialogFragment extends android.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog ad = new AlertDialog.Builder(getActivity()).setTitle("Alert Dialog")
                .setMessage("Choose anyone!!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //show a toast mmessage
                    }
                })
                .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        return ad;
    }

}
