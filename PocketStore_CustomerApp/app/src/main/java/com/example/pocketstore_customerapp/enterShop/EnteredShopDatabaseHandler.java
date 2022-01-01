package com.example.pocketstore_customerapp.enterShop;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_customerapp.misc.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EnteredShopDatabaseHandler {

    private static final String TAG = "ESDH-debug";

    // reference to shops/enteredShopId/customerQueue & shops/enteredShopId/status
    private DatabaseReference referenceToCustomerQueue, referenceToShopStatus;
    // push id of the user after entering the customer queue
    private String pushId= null;
    private EnteredShopDatabaseCallback callback=null;

    // listens to shop active/inactive status changes
    private ValueEventListener shopStatusValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String shopStatus = (String) snapshot.getValue();
            if(shopStatus.equals("inactive")){
                callback.onShopOwnerIsInactiveInDB();
                Log.d(TAG, "onDataChange: shop turned inactive");
            }
            else {
                callback.onShopOwnerInActiveInDB();
                Log.d(TAG, "onDataChange: shop turned active");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            callback.onError(error.getMessage());
            Log.d(TAG, "onCancelled: shop active/inactive listener error = "+error.getMessage());
        }
    };

    // listens to shop active/inactive status changes
    private ValueEventListener shopWantsToConnectValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(!snapshot.exists())
                return;

            Boolean shopWantsToConnect = (Boolean) snapshot.getValue();

            if(shopWantsToConnect==true){
                callback.onShopWantsToConnectDB();
                Log.d(TAG, "onDataChange: shop wants to connect");
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            callback.onError(error.getMessage());
            Log.d(TAG, "onCancelled: shop wants to connect listener error = "+error.getMessage());
        }
    };

    public EnteredShopDatabaseHandler(DatabaseReference referenceToCustomerQueue, DatabaseReference referenceToShopStatus) {
        this.referenceToCustomerQueue = referenceToCustomerQueue;
        this.referenceToShopStatus = referenceToShopStatus;
    }

    public void addUserToCustomerQueueInDB(CustomerWaitingInQueue user){
        // add user to customer queue in db

        this.pushId = referenceToCustomerQueue.push().getKey();

        referenceToCustomerQueue.child(pushId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onUserAddedToQueueInDB();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e.getMessage());
                    }
                });

        // this removes user from customer queue when internet is lost
        referenceToCustomerQueue.onDisconnect().setValue(null);

    }

    public void removeUserFromCustomerQueueInDB(){
        // remove user from customerQueue

        if(pushId==null){
            // user doesn't exist in customerQueue
            Log.d(TAG, "removeUserFromCustomerQueueInDB: customer doesn't exist in db!");
            return;
        }

        // delete is done by setValue(null)
        referenceToCustomerQueue.child(pushId).setValue(null);

    }


    public void listenForShopOwnerStatusChange(){
        // listen for if shop is active or not

        referenceToShopStatus.addValueEventListener(shopStatusValueListener);

    }

    public void listenForShopWantsToConnect(){
        // listen for if shop is active or not

        if(pushId==null){
            Log.d(TAG, "listenForShopWantsToConnect: cannot listen for shop wants to connect customer not in queue");
            return;
        }

        referenceToCustomerQueue.child(pushId+"/"+Utils.shopWantsToConnectNodeValue)
                .addValueEventListener(shopWantsToConnectValueListener);

    }

    public void removeShopStatusListener(){
        // value listeners MUST be removed to avoid unintended behavior

        referenceToShopStatus.removeEventListener(shopStatusValueListener);

    }

    public void removeShopWantsToConnectListener(){
        // value listeners MUST be removed to avoid unintended behavior

        if(pushId==null){
            Log.d(TAG, "removeShopWantsToConnectListener: cannot remove for shop wants to connect customer not in queue");
            return;
        }

        referenceToCustomerQueue.child(pushId+"/"+Utils.shopWantsToConnectNodeValue)
                .removeEventListener(shopWantsToConnectValueListener);

    }

    public void setCallback(EnteredShopDatabaseCallback callback) {
        this.callback = callback;
    }

    public EnteredShopDatabaseCallback getCallback() {
        return callback;
    }
}
