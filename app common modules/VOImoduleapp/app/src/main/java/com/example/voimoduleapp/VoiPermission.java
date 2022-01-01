package com.example.voimoduleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiPermission {
    // this class is for handling permissions

    private static final String TAG = "permission-debug";

    private Activity activity;

    // list of required permissions
    private String[] appPermissions;

    private int PERMISSION_REQUEST_CODE;

    private List<String> permissionsRequired = new ArrayList<>();

    public VoiPermission(Activity activity, String[] appPermissions, int PERMISSION_REQUEST_CODE) {
        this.activity = activity;
        this.appPermissions = appPermissions;
        this.PERMISSION_REQUEST_CODE = PERMISSION_REQUEST_CODE;
    }

    public int getPERMISSION_REQUEST_CODE() {
        return PERMISSION_REQUEST_CODE;
    }

    public boolean checkPermissions(){

        /*
        return true if all permissions are granted
        else return false and store unresolved permissions in list
        call askPermissions() and resolvePermissions()
        after this method is invoked
         */

        //clear out permissionRequired arraylist
        permissionsRequired = new ArrayList<>();

        //get required permissions into permissionsRequired arraylist
        for(String permission: appPermissions){
            if(ContextCompat.checkSelfPermission(this.activity.getApplicationContext(),permission)
                    == PackageManager.PERMISSION_DENIED
            ){
                this.permissionsRequired.add(permission);
                Log.d(TAG, "checkPermissions: "+permission+" not granted");
            }
        }

        if(!this.permissionsRequired.isEmpty())
            return false;

        Log.d(TAG, "checkPermissions: all permissions granted hurrah!");
        return true;

    }


    public void askPermissions(){
        //ask for permission initially

        ActivityCompat.requestPermissions(
                activity,
                this.permissionsRequired.toArray(new String[this.permissionsRequired.size()]),
                PERMISSION_REQUEST_CODE
        );
    }

    private void askSinglePermission(String permission) {

        ActivityCompat.requestPermissions(
                activity,
                new String[] {permission},
                PERMISSION_REQUEST_CODE
        );
    }


    public void resolvePermissions(String[] permissions, int[] grantResults,
                                   String explanationMessage,
                                   final boolean isPermissionAbsolutelyNecessary){
        // ask permission if not granted
        // show dialog box if not allowed chosen the first time

        HashMap<String, Integer> permissionResult = new HashMap<>();

        for (int i = 0; i < grantResults.length; i++) {
            //get the still not allowed permissions

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionResult.put(permissions[i], grantResults[i]);
                Log.d(TAG, "resolvePermissions: denied permission = "+permissions[i]+" grant result = "+grantResults[i]);
            }
        }

        if (!permissionResult.isEmpty()) {

            for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                //request permission one by one with proper explanation

                final String permission = entry.getKey();
                int resultCode = entry.getValue();
                Log.d(TAG, "resolvePermissions: permission = " + permission + " result code = " + resultCode);

                if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, permission)) {
                    //user denied collective permission once but hasn't picked never allow

                    VoiPermission.this.alertDialog(

                            explanationMessage,

                            //positive listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Log.d(TAG, "onClick: dialog.dismiss() called");

                                    VoiPermission.this.askSinglePermission(permission);
                                    // Permission.this.checkPermissions();
                                    // Permission.this.askPermissions();
                                }
                            },

                            //negative listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // terminate the app if permission is absolutely necessary
                                    if(isPermissionAbsolutelyNecessary)
                                        VoiPermission.this.activity.finish();
                                }
                            }

                    );

                } else if(resultCode == PackageManager.PERMISSION_DENIED){
                    //user has picked never allow

                    Log.d(TAG, "resolvePermissions: never allow dise -_-");
                    //TODO: show user dialog box then prompt user to go to settings and allow
                }

            }
        }

        else {
            Log.d(TAG, "resolvePermissions: All permissions granted");

            this.permissionsRequired.clear();
        }
    }


    private void alertDialog(String message, DialogInterface.OnClickListener positiveListener,
                             DialogInterface.OnClickListener negativeListener)
    {
        // show a alert dialog box with positive and negative listeners

        Log.d(TAG, "alertDialog: creating alert-box");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton( "ok" , positiveListener)
                .setNegativeButton( "not ok" , negativeListener)
        ;


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}