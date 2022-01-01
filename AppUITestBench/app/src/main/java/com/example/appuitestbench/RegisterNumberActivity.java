package com.example.appuitestbench;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class RegisterNumberActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);
        phoneNumber = findViewById(R.id.editText_phoneNumber);
        homeButton = findViewById(R.id.home_button_toolbar);



        //Home button listener
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Edit text formation
        phoneNumber.clearFocus();
        phoneNumber.setSelection(phoneNumber.getText().toString().length());
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            int countB=phoneNumber.getText().toString().length(),countA=0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phoneNumber.getText().toString().length()<5)
                {
                    phoneNumber.setText("+880 ");
                    phoneNumber.setSelection(phoneNumber.getText().toString().length());
                }

                countA = phoneNumber.getText().toString().length();

                if(phoneNumber.getText().toString().length()==9 && countA>countB)
                {
                    phoneNumber.setText(phoneNumber.getText().toString()+"-");
                    phoneNumber.setSelection(phoneNumber.getText().toString().length());
                }
                countB = countA;
                if(phoneNumber.getText().toString().length()==16)
                {
                    hideSoftInput();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) phoneNumber.setCursorVisible(true);
                else phoneNumber.setCursorVisible(false);
            }
        });


    }

    public void hideSoftInput() {
        View view1 = this.getCurrentFocus();
        if(view1!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

}