package com.example.appuitestbench;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    ImageButton passwordVisibilityBtn;
    boolean isPassHidden = true;
    EditText passwordField, emailField;
    ImageView emailValidationAlertIcon;
    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordField = findViewById(R.id.passwordEditext);
        passwordVisibilityBtn = findViewById(R.id.password_visibility_button);
        emailField = findViewById(R.id.loginEmail);
        emailValidationAlertIcon = findViewById(R.id.emailValidationAlertIcon);
        homeButton = findViewById(R.id.home_button_toolbar);


        //Home button listener
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Hide and show password functionality in EditText
        passwordVisibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPassHidden)
                {
                    passwordVisibilityBtn.setImageResource(R.drawable.ic_visibility_on);
                    passwordField.setTransformationMethod(null);
                    isPassHidden = false;
                }
                else
                {
                    passwordVisibilityBtn.setImageResource(R.drawable.ic_visibility_off);
                    passwordField.setTransformationMethod(new PasswordTransformationMethod());
                    isPassHidden = true;
                }
            }
        });


        //Email validation
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                {
                    if(isEmailValid(s.toString()))
                    {
                        emailValidationAlertIcon.setImageResource(R.drawable.ic_valid_email);
                    }
                    else
                    {
                        emailValidationAlertIcon.setImageResource(R.drawable.ic_invalid_email);
                    }
                    emailValidationAlertIcon.setVisibility(View.VISIBLE);
                }
                else {
                    emailValidationAlertIcon.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}