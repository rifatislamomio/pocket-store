package com.example.pocketstore_customerapp.enterShop;

import com.example.customerqueue_module.CustomerWaitingInQueue;

public class EnteredShopPresenter implements EnteredShopDatabaseCallback{
// presenter that connects model,db-handler, view

    private EnteredShopView enteredShopView;
    private CustomerWaitingInQueue user;
    private EnteredShop enteredShop;
    private EnteredShopDatabaseHandler enteredShopDatabaseHandler;

    public EnteredShopPresenter(EnteredShopView enteredShopView,
                                EnteredShop enteredShop, CustomerWaitingInQueue user,
                                EnteredShopDatabaseHandler enteredShopDatabaseHandler)
    {
        this.enteredShopView = enteredShopView;
        this.enteredShop = enteredShop;
        this.user = user;
        this.enteredShopDatabaseHandler = enteredShopDatabaseHandler;

        enteredShopDatabaseHandler.setCallback(this);
    }

    public void registerDbListeners(){
        // activate the database handler listeners

        enteredShopDatabaseHandler.listenForShopOwnerStatusChange();
        enteredShopDatabaseHandler.listenForShopWantsToConnect();
    }

    public void insertUserToQueue(){
        // add customer to db and

        enteredShopDatabaseHandler.addUserToCustomerQueueInDB(user);

    }

    public void removeUserFromQueue(){
        // remove user from customer queue

        if(enteredShopDatabaseHandler.getCallback()==null)
            // user was never added to queue
            return;

        enteredShopDatabaseHandler.removeUserFromCustomerQueueInDB();

    }

    public void onDestroy(){
        // removes db callbacks
        // MUST be called
        enteredShopDatabaseHandler.removeShopStatusListener();
        enteredShopDatabaseHandler.removeShopWantsToConnectListener();
    }

    @Override
    public void onUserAddedToQueueInDB() {
        enteredShopView.onUserAddedToQueueUI();
    }

    @Override
    public void onError(String errorMessage) {
        enteredShopView.onErrorUI("something went wrong! check your internet connection");
    }

    @Override
    public void onShopOwnerIsInactiveInDB() {
        enteredShopView.onShopOwnerInactiveUI();
    }

    @Override
    public void onShopOwnerInActiveInDB() {
        enteredShopView.onShopOwnerActiveUI();
    }

    @Override
    public void onShopWantsToConnectDB() {
        // shop owner wants to initiate app-to-app voice call

        enteredShopView.onShopWantsToConnectUI();

    }

    public CustomerWaitingInQueue getUser() {
        return user;
    }

    public EnteredShop getEnteredShop() {
        return enteredShop;
    }
}
