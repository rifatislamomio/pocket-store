package com.example.pocketstore_customerapp.getStarted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.phonenumberverification_module.RegisterNumberActivity;
import com.example.phonenumberverification_module.VerificationPageActivity;
import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.misc.DeciderActivity;
import com.example.voimoduleapp.VoiPermission;
import com.google.firebase.auth.FirebaseAuth;


public class GetStartedActivity extends AppCompatActivity implements GetStartedView{

    private static final String TAG = "GS-debug";

    private Button button;
    private GetStartedPresenter presenter;

    // absolutely necessary permissions
    private VoiPermission permission;
    private static final String[] permissionStrings = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_PHONE_STATE
    };
    private Boolean allPermissionsGranted = false;
    private static final int PERMISSION_CODE = 375;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // ask permission
        promptPermissions();

    }

    private void promptPermissions() {
        // ask for permissions regarding app-to-app call, absolutely necessary for app features

        permission = new VoiPermission(this, permissionStrings, PERMISSION_CODE);

        allPermissionsGranted = permission.checkPermissions();

        if(allPermissionsGranted)
            // have all the permissions
            init();
        else
            // permissions not granted
            permission.askPermissions();

    }

    private void init() {

        button = findViewById(R.id.btn_getStarted);

        presenter = new GetStartedPresenter(this, FirebaseAuth.getInstance());

        if(presenter.doesAuthUserExist())
            // user is already logged in
            OnAuthUserExistsUI();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnAuthUserNotExistsUI();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case PERMISSION_CODE:

                allPermissionsGranted = true;

                for(String permission: permissions){

                    if(ActivityCompat.checkSelfPermission(this, permission)
                            == PackageManager.PERMISSION_DENIED) {
                        // one of the permissions was denied
                        allPermissionsGranted = false;
                        break;
                    }

                }

                if(allPermissionsGranted)
                    init();
                else{
                    permission.resolvePermissions(permissions, grantResults,
                            "Audio recording permissions are necessary for app-to-app voice call",
                            true);
                }

                break;

        }

    }


    @Override
    public void OnAuthUserExistsUI() {
        //Start to HomeActivity
        // TODO: send to DeciderActivity?
        startActivity(new Intent(this, DeciderActivity.class));
        finish();
    }

    @Override
    public void OnAuthUserNotExistsUI() {
        // phone verification is imported from aar library
        // need to set its next activity class name for this app
        VerificationPageActivity.setNextActivityClassName("com.example.pocketstore_customerapp.misc.DeciderActivity");
        Intent intent = new Intent(getApplicationContext(), RegisterNumberActivity.class);
        startActivity(intent);
    }
}