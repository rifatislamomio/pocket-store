package com.example.pocketstore_shopownerapp.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.menu.pendigDeliveries.PendingOrdersMapsActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startEditProfileActivity(View view) {
    }

    public void startPendingOrderActivity(View view) {

        startActivity(new Intent(this, PendingOrdersMapsActivity.class));

    }

    public void startOrderHistoryActivity(View view) {
    }

    public void startSettingsActivity(View view) {
    }
}