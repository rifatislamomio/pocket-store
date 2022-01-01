package com.example.pocketstore_shopownerapp.customerConnection.conversation;

import androidx.annotation.NonNull;

import com.example.pocketstore_shopownerapp.misc.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConversationDatabaseHandler {
// class to detect when shop confirms in database

    private DatabaseReference reference;
    private ConversationDatabaseCallback callback;


    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(snapshot.exists())
                callback.onCustomerConfirmationUpdatedInDB( (boolean) snapshot.getValue() );
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            callback.onErrorDB(error.getMessage());
        }
    };

    public ConversationDatabaseHandler(DatabaseReference reference) {
        this.reference = reference;
    }


    public void listenForShopConfirmationChanges(){
        reference.child(Utils.customerConfirmationValueNode).addValueEventListener(valueEventListener);
    }

    public void enableOnDisconnect(){
        if(reference!=null)
            reference.onDisconnect().removeValue();
    }

    public void removeNode(){
        reference.setValue(null);
    }

    public void removeDbListeners(){
        reference.removeEventListener(valueEventListener);
    }

    public void updateCustomerConfirmationInDB(boolean confirmationState){
        reference.child(Utils.shopConfirmationValueNode).setValue(confirmationState);
    }

    public void setCallback(ConversationDatabaseCallback callback) {
        this.callback = callback;
    }
}
