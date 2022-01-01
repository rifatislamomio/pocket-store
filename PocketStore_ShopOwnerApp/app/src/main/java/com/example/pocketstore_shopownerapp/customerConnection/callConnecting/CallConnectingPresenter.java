package com.example.pocketstore_shopownerapp.customerConnection.callConnecting;

import android.util.Log;

import com.example.pocketstore_shopownerapp.home.Customer;
import com.example.voimoduleapp.VoiHandler;

public class CallConnectingPresenter implements CallConnectingDatabaseCallbacks, VoiHandler.CallClientSetupObserver {
// presenter acting as middleground for UI, DB & app-to-app api client builder

    private final String TAG = "CCP-debug";

    private CallConnectingView callConnectingView;
    private Customer customer;
    private ConnectedShop shop;
    private CallConnectingDatabaseHandler callConnectingDatabaseHandler;
    private VoiHandler voiHandler;

    public CallConnectingPresenter(CallConnectingView callConnectingView,
                                   Customer customer, ConnectedShop shop,
                                   CallConnectingDatabaseHandler callConnectingDatabaseHandler,
                                   VoiHandler voiHandler)
    {
        this.callConnectingView = callConnectingView;
        this.customer = customer;
        this.shop = shop;
        this.callConnectingDatabaseHandler = callConnectingDatabaseHandler;
        this.voiHandler = voiHandler;

        this.voiHandler.setCallClientSetupObserver(this);
        this.callConnectingDatabaseHandler.setCallbacks(this);

    }

    public void startConnection(){
        // initiate db changes and app-to-app API client setup

        callConnectingDatabaseHandler.createCustomerShopInteractionEntry(shop, customer);
        // unexpected internet connection loss
        //callConnectingDatabaseHandler.enableRemoveInteractionEntryIfDisconnectedDB();

        // setup app-to-app call api client
        voiHandler.setUpClient();
    }

    public void onDestroyedUnexpectedly(){
        // terminate api client
        voiHandler.terminateClient();
    }


    // Database callbacks
    @Override
    public void onFailedDB(String errorMessage) {

        Log.d(TAG, "onFailedDB: error = "+errorMessage);

        callConnectingView.onFailedUI("something went wrong! please check your internet");

    }


    // VoiHandler callbacks
    @Override
    public void onClientSetupDone() {
        // app-to-app call api client setup done,

        // let customer know
        callConnectingDatabaseHandler.setShopReadyToConnect();

        // now shop is ready to receive call
        callConnectingView.onReadyToReceiveCallUI();

    }

    @Override
    public void onClientStopped() {

        callConnectingView.onFailedUI("An error occurred while trying to connect with customer!");


    }

    @Override
    public void onClientSetupFailed(String s) {

        Log.d(TAG, "onClientSetupFailed: error = "+s);

        callConnectingView.onFailedUI("An error occurred while trying to connect with customer!");

    }


    // getters, setters
    public VoiHandler getVoiHandler() {
        return voiHandler;
    }
}
