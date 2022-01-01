package com.example.phonenumberverification_module;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class RegisterNumberActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button homeButton, confirmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);
        phoneNumber = findViewById(R.id.editText_phoneNumber);
        //homeButton = findViewById(R.id.home_button_toolbar);
        confirmButton = findViewById(R.id.buttonContinue);



        //Confirm button listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInternetAvailable()) {
                    //Toast.makeText(RegisterNumberActivity.this, "no internet!", Toast.LENGTH_LONG).show();
                    Toasty.warning(getApplicationContext(),"No internet connection!").show();
                    return;
                }

                if(phoneNumber.getText().toString().length()==16)
                {
                    Intent intent = new Intent(getApplicationContext(),VerificationPageActivity.class);
                    String phoneNo = phoneNumber.getText().toString();
                    phoneNo = phoneNo.replace('-',' ');
                    phoneNo = phoneNo.replaceAll(" ","");
                    intent.putExtra("phoneNumber",phoneNo);
                    startActivity(intent);
                }
                else
                {
                    Toasty.warning(getApplicationContext(),"Enter a valid phone number").show();
                    phoneNumber.setError("Invalid Phone Number");
                }

            }
        });


        //Home button listener
        /*homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

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

    private boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
    }

    public void hideSoftInput() {
        View view1 = this.getCurrentFocus();
        if(view1!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

}