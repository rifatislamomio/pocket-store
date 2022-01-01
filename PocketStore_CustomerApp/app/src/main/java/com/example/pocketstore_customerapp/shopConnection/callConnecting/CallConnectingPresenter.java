package com.example.pocketstore_customerapp.shopConnection.callConnecting;

import android.util.Log;

import com.example.voimoduleapp.VoiHandler;

public class CallConnectingPresenter implements CallConnectingDatabaseCallback, VoiHandler.CallClientSetupObserver {
// presenter to build app-to-app api client and listen to db changes related to it

    private final String TAG = "CCP-debug";

    private CallConnectingView callConnectingView;
    private CallConnectingDatabaseHandler callConnectingDatabaseHandler;
    private VoiHandler voiHandler;

    public CallConnectingPresenter(CallConnectingView callConnectingView,
                                   CallConnectingDatabaseHandler callConnectingDatabaseHandler,
                                   VoiHandler voiHandler)
    {
        this.callConnectingView = callConnectingView;
        this.callConnectingDatabaseHandler = callConnectingDatabaseHandler;
        this.voiHandler = voiHandler;

        this.callConnectingDatabaseHandler.setCallback(this);
        this.voiHandler.setCallClientSetupObserver(this);
    }

    public void registerDBListeners(){

        callConnectingDatabaseHandler.listenForShopReadyToConnectDB();

    }

    public void onDestroy(){
        // MUST REMOVE VALUE LISTENERS

        callConnectingDatabaseHandler.removeShopReadyToConnectListenerDB();
    }

    public void onDestroyedUnexpectedly(){
        // terminate voi api client

        voiHandler.terminateClient();

    }

    // database callbacks
    @Override
    public void onShopReadyToConnectDB() {
    // shop is ready for call, get customer client setup

        voiHandler.setUpClient();

    }

    @Override
    public void onErrorDB(String errorMessage) {
        Log.d(TAG, "onErrorDB: error = "+errorMessage);
        callConnectingView.onFailedUI("An unexpected error occurred! please check your internet");
    }


    // VoiHandler callbacks
    @Override
    public void onClientSetupDone() {
        // client setup done ready to make call to shop

        callConnectingView.onReadyToMakeCallUI();
    }

    @Override
    public void onClientStopped() {

        Log.d(TAG, "onClientStopped: why did client stop?");

        callConnectingView.onFailedUI("An unexpected error occurred!");

    }

    @Override
    public void onClientSetupFailed(String s) {

        Log.d(TAG, "onClientSetupFailed: error = "+s);

        callConnectingView.onFailedUI("An unexpected error occurred! check your internet connection");

    }

    // getters & setters
    public VoiHandler getVoiHandler() {
        return voiHandler;
    }
}
