package com.example.pocketstore_shopownerapp.customerConnection.callConnecting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pocketstore_shopownerapp.home.Customer;
import com.example.pocketstore_shopownerapp.misc.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class CallConnectingDatabaseHandler {
// class responsible for making db changes to start conversation

    private static final String TAG = "CCDH-debug";

    private DatabaseReference reference;
    private String customerShopOwnerInteractionPath;
    private CallConnectingDatabaseCallbacks callbacks;

    public CallConnectingDatabaseHandler(DatabaseReference reference, String customerShopOwnerInteractionPath) {
        this.reference = reference;
        this.customerShopOwnerInteractionPath = customerShopOwnerInteractionPath;
    }

    public void createCustomerShopInteractionEntry(ConnectedShop shop, Customer customer){
        // create the db node that will contain all interaction data

        reference.child(customerShopOwnerInteractionPath+"/"+Utils.connectedShopNode)
                .setValue(shop)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbacks.onFailedDB(e.getMessage());
                Log.d(TAG, "onFailure: failed to create db entry(shop) = "+e.getMessage());
            }
        });

        reference.child(customerShopOwnerInteractionPath+"/"+Utils.connectedCustomerNode)
                .setValue(customer)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbacks.onFailedDB(e.getMessage());
                Log.d(TAG, "onFailure: failed to create db entry(customer) = "+e.getMessage());
            }
        });

    }

    public void setShopReadyToConnect(){
        // let the customer know shop owner is ready for app-to-app call

        reference.child(customerShopOwnerInteractionPath+"/"+Utils.shopOwnerReadyToConnectValueNode)
                .setValue(true)
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbacks.onFailedDB(e.getMessage());
                Log.d(TAG, "onFailure: error setting shop ready = "+e.getMessage());
            }
        });
    }
    
    public void enableRemoveInteractionEntryIfDisconnectedDB(){
        // remove the customer interaction node (in case any unintended error occurs midway)

        reference.child(customerShopOwnerInteractionPath)
                .onDisconnect().removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: interaction node removed!");
                    }
                });
        
    }

    public void setCallbacks(CallConnectingDatabaseCallbacks callbacks) {
        this.callbacks = callbacks;
    }
}
