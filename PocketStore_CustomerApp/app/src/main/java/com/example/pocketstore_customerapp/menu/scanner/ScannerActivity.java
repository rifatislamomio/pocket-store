package com.example.pocketstore_customerapp.menu.scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class ScannerActivity extends AppCompatActivity {

    Button toolbar_home;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        toolbar_home = findViewById(R.id.home_button_toolbar);
        toolbar_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                slideInLeftOutRight();
            }
        });
    }

    public void Scan(View view)
    {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult!=null)
        {
            if(intentResult.getContents()==null)
            {
                Toasty.error(getApplicationContext(),"Failed to scan!").show();
            }
            else
            {

                //Store to database
                String user_id = UserInfoSharedPreferences.getUserID(getApplicationContext());
                String qrcode = intentResult.getContents();
                HashMap<String,String> map = new HashMap<>();
                map.put("qrCode",qrcode);
                map.put("uid",user_id);
                databaseReference = FirebaseDatabase.getInstance().getReference("loginCodes");
                databaseReference.child(qrcode).setValue(map).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getApplicationContext(),"Failed to send data, check internet connection!").show();
                        return;
                    }
                });

                Toasty.success(getApplicationContext(),"Scan complete!").show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void slideInLeftOutRight()
    {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}