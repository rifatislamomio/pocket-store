package com.example.customerqueue_module;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CustomerQueueDatabaseHandler implements ChildEventListener {
// class to handle database interactions of customer queue

    private static final String TAG = "CQDH-debug";

    private DatabaseReference reference;
    // exact path in database node this class is responsible for
    private String path;
    private CustomerQueueDatabaseCallback callback;

    public CustomerQueueDatabaseHandler(DatabaseReference reference, String path)
    {
        this.reference = reference;
        this.path = path;
    }

    public void registerDBChangeListener(){
        // this class will now detect database changes
        reference.child(path).addChildEventListener(this);
    }

    public void removeDBChangeListener(){
        // MUST remove listener to avoid unexpected behaviour
        reference.child(path).removeEventListener(this);
    }


    public void checkIfCustomerQueueEmpty(){

        reference.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    callback.onCustomerQueueEmptyInDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.d(TAG, "onChildAdded: child added");
        callback.onCustomerAddedToQueueInDB(snapshot);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        // customer info can't change while customer is in queue
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        Log.d(TAG, "onChildRemoved: child removed");
        callback.onCustomerRemovedFromQueueInDB(snapshot);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        // not required
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        callback.onDBFailure(error.getMessage());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCallback(CustomerQueueDatabaseCallback callback) {
        this.callback = callback;
    }
}
