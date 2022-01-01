package com.example.pocketstore_shopownerapp.home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pocketstore_shopownerapp.misc.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InsideShopDatabaseHandler {
// Handles database interaction to initiate shopOwner's connection with customer

    private static final String TAG = "ISDH-debug";

    private DatabaseReference reference;
    private String customerQueueDbPath;
    private InsideShopDatabaseCallback callback;

    public InsideShopDatabaseHandler(DatabaseReference reference, String customerQueueDbPath) {
        this.reference = reference;
        this.customerQueueDbPath = customerQueueDbPath;
    }

    public void notifyCustomerAtTopOfQueue(){
        // fetch and notify the customer at top of queue to connect

        // currently this listener downloads the whole list of customers in queue, but only the top customer is required....
        reference.child(customerQueueDbPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()){
                    callback.onCustomerQueueEmptyInDB();
                    return;
                }

                for(final DataSnapshot childSnapshot: snapshot.getChildren()){
                    // get the first child only
                    // set its "shopWantsToConnect" to true

                    childSnapshot.getRef().child(Utils.shopWantsToConnectValueNode).setValue(true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            callback.onCustomerAtTopNotifiedDB(childSnapshot);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onErrorDB(e.getMessage());
                            Log.d(TAG, "onFailure: error setting shopWantsToConnect = "+e.getMessage());
                        }
                    });

                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: error = "+error.getMessage());
                callback.onErrorDB(error.getMessage());
            }
        });

    }

    public String getPath() {
        return customerQueueDbPath;
    }

    public void setPath(String path) {
        this.customerQueueDbPath = path;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public void setCallback(InsideShopDatabaseCallback callback) {
        this.callback = callback;
    }
}
