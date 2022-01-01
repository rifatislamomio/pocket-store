package com.example.appuitestbench;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class VerificationPageActivity extends AppCompatActivity {

    EditText digit1,digit2,digit3,digit4,digit5,digit6;
    Button homeButton;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        digit1=findViewById(R.id.editTextDigit1);
        digit2=findViewById(R.id.editTextDigit2);
        digit3=findViewById(R.id.editTextDigit3);
        digit4=findViewById(R.id.editTextDigit4);
        digit5=findViewById(R.id.editTextDigit5);
        digit6=findViewById(R.id.editTextDigit6);
        homeButton = findViewById(R.id.home_button_toolbar);



        //Home button listener
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Text watcher for input fields
        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit1.setTextColor(Color.WHITE);
                    digit2.clearFocus();
                    digit2.requestFocus();
                }
                else
                {
                    digit1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit1.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit2.setTextColor(Color.WHITE);
                    digit3.clearFocus();
                    digit3.requestFocus();
                }
                else
                {
                    digit2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit2.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit3.setTextColor(Color.WHITE);
                    digit4.clearFocus();
                    digit4.requestFocus();
                }
                else
                {
                    digit3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit3.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit4.setTextColor(Color.WHITE);
                    digit5.clearFocus();
                    digit5.requestFocus();
                }
                else
                {
                    digit4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit4.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        digit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit5.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit5.setTextColor(Color.WHITE);
                    digit6.clearFocus();
                    digit6.requestFocus();
                }
                else
                {
                    digit5.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit5.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        digit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals(" ") && s.length()!=0)
                {
                    digit6.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox_filled));
                    digit6.setTextColor(Color.WHITE);
                    digit6.clearFocus();
                    hideSoftInput();
                }
                else
                {
                    digit6.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.otp_code_textbox));
                    digit6.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
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