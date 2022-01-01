package com.example.pocketstore_customerapp.enterShop;

public interface EnteredShopView {
// interface for EnteredShopActivity to receive updates from EnteredShopPresenter

    void onShopOwnerInactiveUI();
    void onShopOwnerActiveUI();

    void onUserAddedToQueueUI();

    void onShopWantsToConnectUI();

    void onErrorUI(String errorMessage);

}
