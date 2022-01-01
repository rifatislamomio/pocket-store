package com.example.pocketstore_customerapp.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.menu.scanner.ScannerActivity;

public class MenuActivity extends AppCompatActivity {
    Button toolbar_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar_home = findViewById(R.id.home_button_toolbar);
        toolbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                slideInRightOutLeft();
            }
        });
    }


    public void startEditProfileActivity(View view)
    {
        //startActivity(new Intent(getApplicationContext(), ));
        slideInRightOutLeft();
    }

    public void startPendingOrderActivity(View view)
    {
        //startActivity(new Intent(getApplicationContext(), ));
        slideInRightOutLeft();
    }

    public void startOrderHistoryActivity(View view)
    {
        //startActivity(new Intent(getApplicationContext(), ));
        slideInRightOutLeft();
    }


    public void startWebAppLogOnActivity(View view)
    {
        startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
        slideInRightOutLeft();
    }

    public void startSettingsActivity(View view)
    {
        //startActivity(new Intent(getApplicationContext(), ));
        slideInRightOutLeft();
    }


    //UI Transactions
    public void slideInRightOutLeft()
    {
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    public void slideInLeftOutRight()
    {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}