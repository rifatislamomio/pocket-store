package com.example.pocketstore_customerapp.home;

import java.util.ArrayList;

public interface NearbyShopsView {

    void onNearbyShopsFetchedUI(ArrayList<NearbyShop> nearbyShops);
    void onFetchFailedUI(String message);

    void enterShopUI(NearbyShop nearbyShop);
    void onEnterShopInactiveUI();

}
