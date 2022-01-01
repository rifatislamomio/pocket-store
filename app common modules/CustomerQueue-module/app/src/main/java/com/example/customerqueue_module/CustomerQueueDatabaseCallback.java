package com.example.customerqueue_module;

import com.google.firebase.database.DataSnapshot;

public interface CustomerQueueDatabaseCallback {

    void onCustomerAddedToQueueInDB(DataSnapshot dataSnapshot);
    void onCustomerRemovedFromQueueInDB(DataSnapshot dataSnapshot);

    void onCustomerQueueEmptyInDB();
    void onDBFailure(String errorMessage);

}
