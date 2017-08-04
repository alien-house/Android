package com.example.shinji.flagquiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shinji on 2017/08/03.
 */

public class ResultDialogAlert extends android.app.DialogFragment{
    private int countGuess = 0;
    private int count = 0;
    private double results = 0;

//    ResultDialogAlert.ResultDialogAlertDialogListener mListener;
//
//    public interface ResultDialogAlertDialogListener {
//        public void onResultDialogAlertClick();
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        countGuess = getArguments().getInt("countGuessInt");
        count = getArguments().getInt("count");
        results = 100 / (countGuess + 1);
        dialog.setMessage("The Correct Answer Rate = " + results + "%\n" +
                "Wrong Guess = " + countGuess + "\n" +
                "Do you want to Restart the Quiz?" + "\n" +
                "(Total "+getString(R.string.results,
                        (countGuess + count),
                        (1000/(double)(countGuess + count)))+")")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getContext(), "You clicked Restart Quiz", Toast.LENGTH_SHORT).show();
                        FuncInterface d = (FuncInterface) getContext();
                        d.onReturnFnc();
                    }
                })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Thank you for playing this game:)", Toast.LENGTH_SHORT).show();
            }
        });

        return dialog.create();
    }
}
