package com.example.pocketstore_customerapp.home;

import com.google.firebase.database.DataSnapshot;

public interface NearbyShopsDatabaseCallback {
    // hold callbacks for 'NearbyShopsPresenter'

    void onFetchedNearbyShopsFromDB(DataSnapshot dataSnapshot);
    void onDatabaseError(String message);
}
