package com.example.pocketstore_customerapp.home;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NearbyShopsPresenter implements NearbyShopsDatabaseCallback {
    // holds business logic for 'NearbyShopsView'

    private static final String TAG = "NSP-debug";

    private NearbyShopsView nearbyShopsView;

    private NearbyShopsDatabaseHandler nearbyShopsDatabaseHandler;

    private ArrayList<NearbyShop> nearbyShops;
    private NearbyShop selectedShop;

    public NearbyShopsPresenter(NearbyShopsView nearbyShopsView,
                                NearbyShopsDatabaseHandler nearbyShopsDatabaseHandler,
                                ArrayList<NearbyShop> nearbyShops)
    {
        this.nearbyShopsView = nearbyShopsView;
        this.nearbyShopsDatabaseHandler = nearbyShopsDatabaseHandler;
        this.nearbyShops = nearbyShops;
    }


    public void fetchNearbyShops(String path){
        // start the fetch process from db using database handler

        // set this presenter as subject to the databaseManager observer
        nearbyShopsDatabaseHandler.setCallbacks(this);
        // set the proper node of database
        nearbyShopsDatabaseHandler.setReference(FirebaseDatabase.getInstance().getReference().child(path));
        // listen for any changes in database
        nearbyShopsDatabaseHandler.registerListener();

        //simulateWithDummyNearbyShops();

    }

    public void selectShop(String shopId){

        for (NearbyShop nearbyShop: nearbyShops) {
            if(nearbyShop.getShopID().equals(shopId)){
                selectedShop = nearbyShop;
                Log.d(TAG, "selectShop: selected shop -> "+selectedShop.toString() + selectedShop.getStatus());
                return;
            }
        }

    }

    public void enterShop(){
    // pass the NearbyShop shop with shopId to view

        if(selectedShop==null)
            // no shop selected
            return;

        if(selectedShop.getStatus().equals("inactive")){
            nearbyShopsView.onEnterShopInactiveUI();
            return;
        }

        nearbyShopsView.enterShopUI(selectedShop);

    }

    public void onDestroy(){
        nearbyShopsDatabaseHandler.unregisterListener();
    }


    @Override
    public void onFetchedNearbyShopsFromDB(DataSnapshot dataSnapshot) {

        nearbyShops.clear();

        for(DataSnapshot nearbyShopSnapshot: dataSnapshot.getChildren()) {
            nearbyShops.add(nearbyShopSnapshot.getValue(NearbyShop.class));
            // TODO: change the signup triggers to store shopId as a child of nearbyShops/shopId and remove quick fix
            // QUICK FIX: shop id is the key of this snapshot
            nearbyShops.get(nearbyShops.size()-1).setShopID(nearbyShopSnapshot.getKey());
        }

        nearbyShopsView.onNearbyShopsFetchedUI(nearbyShops);
    }

    @Override
    public void onDatabaseError(String message) {
        nearbyShopsView.onFetchFailedUI(message);
    }

    public NearbyShop getSelectedShop() {
        return this.selectedShop;
    }
}
