package com.example.phonenumberverification_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class VerificationPageActivity extends AppCompatActivity {

    private static final String TAG = "VPA-debug";


    private static String NEXT_ACTIVITY = "";
    private static String PHONE_NUMBER = "";

    EditText digit1,digit2,digit3,digit4,digit5,digit6;
    Button homeButton;
    Button submitCode;
    String verificationCodeBySystem, codeInputedByUser;
    FirebaseAuth firebaseAuth;
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
        //homeButton = findViewById(R.id.home_button_toolbar);
        submitCode = findViewById(R.id.btn_continue);
        firebaseAuth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        PHONE_NUMBER = phoneNumber;

        sendVerificationCodeToUser(phoneNumber);


        submitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(codeInputedByUser.length()<6))
                {
                    verifyCode(codeInputedByUser);
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
                    codeInputedByUser = digit1.getText().toString()+
                            digit2.getText().toString()+digit3.getText().toString()+digit4.getText().toString()+
                    digit5.getText().toString()+digit6.getText().toString();
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

    //Private Methods

    private void sendVerificationCodeToUser(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                digit1.setText(code.charAt(0)+"");
                digit2.setText(code.charAt(1)+"");
                digit3.setText(code.charAt(2)+"");
                digit4.setText(code.charAt(3)+"");
                digit5.setText(code.charAt(4)+"");
                digit6.setText(code.charAt(5)+"");
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInTheUserByCredential(credential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    startAppWiseDeciderActivity();
                }
                else
                {
                    if(isInternetAvailable())
                    {
                        Toasty.error(getApplicationContext(),"Invalid verification code!").show();
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(),"No internet connection!").show();
                    }

                    //Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startAppWiseDeciderActivity() {

        Log.d(TAG, "startAppWiseDeciderActivity: decider activity open called from VerificationPageActivity");
        
        try{
            Intent intent = new Intent(VerificationPageActivity.this,
                    Class.forName(NEXT_ACTIVITY));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            
            finish();

        }catch (ClassNotFoundException e){
            Log.d(TAG, "onDataChange: error = "+e.getMessage());
        }
    }


    private void hideSoftInput() {
        View view1 = this.getCurrentFocus();
        if(view1!= null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
    }

    public static void setNextActivityClassName(String nextActivityName)
    {
        NEXT_ACTIVITY = nextActivityName;
    }

    public static String getPhoneNumber()
    {
        return PHONE_NUMBER;
    }

    private boolean isInternetAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
    }

}