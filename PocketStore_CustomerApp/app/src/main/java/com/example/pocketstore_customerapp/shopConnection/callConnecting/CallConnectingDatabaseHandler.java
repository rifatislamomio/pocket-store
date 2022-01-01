package com.example.pocketstore_customerapp.shopConnection.callConnecting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CallConnectingDatabaseHandler {
// get ready for app-to-app call by listening to db changes

    private static final String TAG = "CCDH-debug";

    private DatabaseReference referenceToShopReadyToConnect;
    private CallConnectingDatabaseCallback callback;

    private ValueEventListener shopReadyToConnectValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(!snapshot.exists())
                return;

            Boolean shopReadyToConnect = (Boolean)snapshot.getValue();

            if(shopReadyToConnect){

                callback.onShopReadyToConnectDB();

                Log.d(TAG, "onDataChange: shop ready to connect!");

            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            callback.onErrorDB(error.getMessage());

            Log.d(TAG, "onCancelled: error listening to shop ready to connect = "+ error.getMessage());

        }
    };

    public CallConnectingDatabaseHandler(DatabaseReference referenceToShopReadyToConnect) {
        this.referenceToShopReadyToConnect = referenceToShopReadyToConnect;
    }

    public void listenForShopReadyToConnectDB(){

        referenceToShopReadyToConnect.addValueEventListener(shopReadyToConnectValueListener);

    }

    public void removeShopReadyToConnectListenerDB(){

        referenceToShopReadyToConnect.removeEventListener(shopReadyToConnectValueListener);

    }

    public void setCallback(CallConnectingDatabaseCallback callback) {
        this.callback = callback;
    }
}
