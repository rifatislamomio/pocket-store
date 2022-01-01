package com.example.pocketstore_customerapp.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class NearbyShopsDatabaseHandler implements ValueEventListener {
// handles database actions for 'NearbyShopsPresenter'

    private DatabaseReference reference;
    private NearbyShopsDatabaseCallback callback;

    private static final String TAG  = "NSDH-debug";

    public NearbyShopsDatabaseHandler() {
    }

    public NearbyShopsDatabaseHandler(DatabaseReference reference, NearbyShopsDatabaseCallback callback) {
        this.reference = reference;
        this.callback = callback;
    }

    public void registerListener(){
        // onDataChange method starts working
        this.reference.addValueEventListener(this);
    }
    public void unregisterListener(){
        // MUST be unregistered to avoid unnecessary downloads from db!
        this.reference.removeEventListener(this);
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public void setCallbacks(NearbyShopsDatabaseCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        callback.onFetchedNearbyShopsFromDB(snapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        callback.onDatabaseError("something went wrong! please check you internet");
        Log.d(TAG, "onCancelled: "+error.getMessage());
    }

}
