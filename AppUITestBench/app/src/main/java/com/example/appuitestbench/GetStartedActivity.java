package com.example.appuitestbench;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_get_started_that_may_work_perfectly);
        setContentView(R.layout.activity_get_started);
        startActivity(new Intent(getApplicationContext(), SetupProfileActivity.class));


    }
}