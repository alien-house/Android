package com.example.shinji.datatimepicker;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TimePickerFragment.TimePickerDialogListener,DatePickerFragment.DatePickerDialogListener,AdapterView.OnItemSelectedListener {

    TextView tvTime;
    TextView tvDate;
    Spinner spinner,sper1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        spinner = (Spinner) findViewById(R.id.sp_data);
//        sper1 = (Spinner) findViewById(R.id.sp_planet);
//
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(DatePicker.this, R.array.planetnames, android.);
//
//        spinner
        Spinner spinner = (Spinner) findViewById(R.id.sp_planet);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planetnames, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        Button btnTimer = (Button) findViewById(R.id.btnTimer);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDataPickerDialog(view);
            }
        });
        tvTime = (TextView) findViewById(R.id.TextViews);
        tvDate = (TextView) findViewById(R.id.TextViewDate);
    }


    public void showTimePickerDialog(View v){
        TextView textView = (TextView) findViewById(R.id.TextViews);
        DialogFragment newFragment = new TimePickerFragment();

        newFragment.show(getFragmentManager(), "timePicker");
    }
    public void showDataPickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public void onDialogTimePickerClick(DialogFragment dialog, int i, int i1) {
        tvTime.setText(i + ":" + i1);
    }

    @Override
    public void onDialogDatePickerClick(int year, int month, int day) {
        tvDate.setText(year + "." + month + "." + day);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemSelected:::",String.valueOf(i));
        Log.d("on",String.valueOf(i)+adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
