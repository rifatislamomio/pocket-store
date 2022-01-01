package com.example.pocketstore_customerapp.enterShop;

public interface EnteredShopDatabaseCallback {
// callbacks for EnteredShopPresenter for db events

    void onUserAddedToQueueInDB();
    void onError(String errorMessage);
    void onShopOwnerIsInactiveInDB();
    void onShopOwnerInActiveInDB();

    void onShopWantsToConnectDB();

}
