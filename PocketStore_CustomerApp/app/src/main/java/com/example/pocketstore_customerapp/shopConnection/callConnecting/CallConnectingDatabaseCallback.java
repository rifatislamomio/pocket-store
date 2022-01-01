package com.example.pocketstore_customerapp.shopConnection.callConnecting;

public interface CallConnectingDatabaseCallback {

    void onShopReadyToConnectDB();
    void onErrorDB(String errorMessage);

}
