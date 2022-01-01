package com.example.pocketstore_customerapp.shopConnection.conversation;

import android.util.Log;

import com.example.voimoduleapp.VoiHandler;

public class ConversationPresenter implements VoiHandler.CallClientSetupObserver, VoiHandler.CallObserver,
ConversationDatabaseCallback{
// presenter that initiates & manages the ongoing app-to-app call

    private static final String TAG = "CP-debug";

    private ConversationView conversationView;
    private VoiHandler voiHandler;
    private Conversation conversation;
    private ConversationDatabaseHandler conversationDatabaseHandler;

    public ConversationPresenter(ConversationView conversationView,
                                 VoiHandler voiHandler, Conversation conversation,
                                 ConversationDatabaseHandler conversationDatabaseHandler) {
        this.conversationView = conversationView;
        this.voiHandler = voiHandler;
        this.conversation = conversation;
        this.conversationDatabaseHandler = conversationDatabaseHandler;

        this.conversationDatabaseHandler.setCallback(this);
        this.conversationDatabaseHandler.listenForShopConfirmationChanges();

        this.voiHandler.setCallClientSetupObserver(this);
        this.voiHandler.setCallObserver(this);
    }


    public void makeCall(){
    // makes outgoing call to shop

        voiHandler.callUser(conversation.getShop().getShopID());

    }

    public void hangUpCall(){

        voiHandler.hangUpCall();

    }

    public void updateCustomerConfirmation(boolean confirmationState){
        conversation.setCustomerConfirmation(confirmationState);
        conversationDatabaseHandler.updateCustomerConfirmationInDB(conversation.isCustomerConfirmation());
    }

    public void onDestroy(){

        // absolutely necessary to release the api client
        // which was built with application context
        voiHandler.terminateClient();

        // must also hangup call because even if client is terminated call can continue (why man)
        voiHandler.hangUpCall();

        // must remove db value listeners
        conversationDatabaseHandler.removeDbListeners();

    }


    // CallObserver callbacks
    @Override
    public void onCallerCalling() {

        conversationView.onCallOutgoingUI();

    }

    @Override
    public void onCallConnected(String s) {
    // call connected, (String s = shopId was not needed)

        conversationView.onCallConnectedUI();

    }

    @Override
    public void onCallDisconnected() {

        if(!conversation.isCustomerConfirmation())
            conversationDatabaseHandler.removeNode();

        conversationView.onCallDisconnectedUI();

    }

    @Override
    public void onCallIncoming(String s) {
    // empty method
    // customer app CANNOT be called
    }

    @Override
    public void onSuccess(String s) {
        Log.d(TAG, "onSuccess: VoiHandler.CallObserver success = "+s);
    }

    @Override
    public void onFailed(String s) {

        Log.d(TAG, "onFailed: voi CallObserver failed error = "+s);

        conversationView.onErrorUI("An unexpected error occurred! Call disconnected.");

    }


    // CallClientSetupObserver callbacks
    @Override
    public void onClientSetupDone() {
    // this will never be called! client is already setup
        Log.d(TAG, "onClientSetupDone: why was this called?");
    }

    @Override
    public void onClientStopped() {
        Log.d(TAG, "onClientStopped: voi client was terminated");
    }

    @Override
    public void onClientSetupFailed(String s) {
    // this will never be called! client is already setup
        Log.d(TAG, "onClientSetupDone: why was this called?");
    }


    // getters & setters
    public Conversation getConversation() {
        return conversation;
    }

    @Override
    public void onShopConfirmationUpdatedInDB(boolean confirmationState) {
        conversation.setShopConfirmation(confirmationState);
    }

    @Override
    public void onErrorDB(String errorMessage) {
        conversationView.onErrorUI("something went wrong!");
    }
}
