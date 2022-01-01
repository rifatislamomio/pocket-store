package com.example.pocketstore_shopownerapp.home;

import com.google.firebase.database.DataSnapshot;

public interface InsideShopDatabaseCallback {
// interface for InsideShopPresenter to get notified of database events

    void onCustomerAtTopNotifiedDB(DataSnapshot dataSnapshot);
    void onCustomerQueueEmptyInDB();
    void onErrorDB(String errorMessage);

}
