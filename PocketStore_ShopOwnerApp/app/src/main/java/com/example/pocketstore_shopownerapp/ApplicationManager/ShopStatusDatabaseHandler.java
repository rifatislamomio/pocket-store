package com.example.pocketstore_shopownerapp.ApplicationManager;

import android.util.Log;

import com.example.pocketstore_shopownerapp.misc.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ShopStatusDatabaseHandler {
    // this class is tied with ApplicationManager to handle shop active/inactive state

    private static final String TAG = "SSDH-debug";

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    public ShopStatusDatabaseHandler(FirebaseAuth firebaseAuth, DatabaseReference reference) {
        this.firebaseAuth = firebaseAuth;
        this.reference = reference;
    }

    public void setShopStateToActive(){
        reference.child(Utils.shopsNode+"/"+firebaseAuth.getUid()+"/"+Utils.shopStatusValueNode).setValue("active");
    }

    public void setShopStateToInActive(){
        reference.child(Utils.shopsNode+"/"+firebaseAuth.getUid()+"/"+Utils.shopStatusValueNode).setValue("inactive");
    }

    public boolean isUserLoggedIn(){
        if(firebaseAuth.getUid()==null)
            return false;

        return true;
    }


    public void enableSetShopInactiveWhenDisconnectedUnintentionally() {

        reference.child(Utils.shopsNode+"/"+firebaseAuth.getUid()+"/"+Utils.shopStatusValueNode)
                .onDisconnect().setValue("inactive")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: unintended disconnection handled!");
                    }
                });

    }
}
