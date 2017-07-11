package com.example.shinji.mockup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText txtName;
    String pizzaSizeTxt;
    HashSet<String> setToppings = new HashSet<String>();
    int sizePrice = 0;
    int toppingPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (EditText) findViewById(R.id.editText1);
        Button btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if((txtName.getText() == null) || (pizzaSizeTxt == null) || (sizePrice+toppingPrice)==0){
                Toast toastAlert = Toast.makeText(getApplicationContext(), "plz input something", Toast.LENGTH_SHORT);
                toastAlert.show();
                return;
            }


            String txt = "Name : " + txtName.getText().toString();
            txt += "\nSize : " + pizzaSizeTxt;
            txt += "\nToppings : ";

            Iterator<String> itr = setToppings.iterator();
            while(itr.hasNext()) {
                txt += itr.next();
                if (itr.hasNext()) {
                    txt += ", ";
                }
            }
            txt += "\nPrice : " + String.valueOf(sizePrice+toppingPrice);
            CharSequence text = txt;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();

            String subject = "Order From Pizza Order";
            txt += "\n\n Thank you so much!";
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto","ucyuujinoco@gmail.com",null));
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, txt);
            startActivity(Intent.createChooser(i, "Send email"));
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.pizzaSizeRadioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
            switch(checkedId){
                case R.id.pzRadioReg:
                    System.out.println("regular");
                    pizzaSizeTxt = "REGULAR";
                    sizePrice = 8;
                    break;
                case R.id.pzRadioMed:
                    System.out.println("midum");
                    pizzaSizeTxt = "MEDIUM";
                    sizePrice = 10;
                    break;
                case R.id.pzRadioLag:
                    System.out.println("large");
                    pizzaSizeTxt = "LARGE";
                    sizePrice = 12;
                    break;
            }
            }
        });
    }



    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_pep:
                checkChkBox(checked,"Peperoni");
                break;
            case R.id.checkbox_mash:
                checkChkBox(checked,"Mashroom");
                break;
            case R.id.checkbox_oli:
                checkChkBox(checked,"Black Olives");
                break;
            case R.id.checkbox_pine:
                checkChkBox(checked,"Pineapple");
                break;
        }
    }
    public void checkChkBox(boolean checked, String topping) {
        if (checked) {
            setToppings.add(topping);
            toppingPrice += 2;
        }else {
            setToppings.remove(topping);
            toppingPrice -= 2;
        }
    }
}