package com.example.appuitestbench;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity {
    Button homeButton;
    ImageButton settingsButton, orderHistoryButton, editProfileButton, pendingOrderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        homeButton = findViewById(R.id.home_button_toolbar);
        settingsButton = findViewById(R.id.settings_menu_button);
        orderHistoryButton = findViewById(R.id.order_history_menu_button);
        editProfileButton = findViewById(R.id.edit_profile_menu_button);
        pendingOrderButton = findViewById(R.id.pending_order_menu_button);


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),));
                slideInRightOutLeft();
            }
        });

        orderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),));
                slideInRightOutLeft();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),));
                slideInRightOutLeft();
            }
        });

        pendingOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),));
                slideInRightOutLeft();
            }
        });
    }


    public void slideInRightOutLeft()
    {
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void slideInLeftOutRight()
    {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}