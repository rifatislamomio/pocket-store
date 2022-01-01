package com.example.pocketstore_shopownerapp.customerConnection.conversation;

import android.util.Log;

import com.example.voimoduleapp.VoiHandler;

public class ConversationPresenter implements VoiHandler.CallObserver, VoiHandler.CallClientSetupObserver, ConversationDatabaseCallback {
// presenter for handling app-to-app conversation & order-list generation

    private final String TAG = "CP-debug";

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

        voiHandler.setCallObserver(this);
        voiHandler.setCallClientSetupObserver(this);
    }


    public void hangUpCall(){
        voiHandler.hangUpCall();
    }

    public void updateShopConfirmation(boolean confirmationState) {

        conversation.setShopConfirmed(confirmationState);
        conversationDatabaseHandler.updateCustomerConfirmationInDB(confirmationState);

    }

    public void onDestroy(){
        voiHandler.hangUpCall();
        // MUST remove api client which has App wide context
        voiHandler.terminateClient();
        // must remove db value listeners
        conversationDatabaseHandler.removeDbListeners();
    }


    // VoiHandler.CallObserver callbacks
    @Override
    public void onCallerCalling() {
    // empty method
    // shop is not allowed to make calls
    }

    @Override
    public void onCallConnected(String s) {

        conversation.getCustomer().setCustomerId(s);

        conversationView.onCallConnectedUI();

    }

    @Override
    public void onCallDisconnected() {

        if(!conversation.isShopConfirmed())
            conversationDatabaseHandler.removeNode();

        conversationView.onCallDisconnectedUI();
    }

    @Override
    public void onCallIncoming(String s) {

        // answer incoming call immediately
        voiHandler.answerIncomingCall();

    }

    @Override
    public void onSuccess(String s) {
        Log.d(TAG, "onSuccess: voi callObserver success = "+s);
    }

    @Override
    public void onFailed(String s) {

        Log.d(TAG, "onFailed: voi callObserver error = "+s);

        conversationView.onErrorUI("An unexpected error occurred! Call disconnected.");

    }


    // VoiHandler.CallClientObserver callbacks
    @Override
    public void onClientSetupDone() {
        // this should never be called
        // since client should be already set up
        Log.d(TAG, "onClientSetupDone: why was this called?");
    }

    @Override
    public void onClientStopped() {

        Log.d(TAG, "onClientStopped: voi client stopped");

    }

    @Override
    public void onClientSetupFailed(String s) {
        // this should never be called
        // since client should be already set up
        Log.d(TAG, "onClientSetupDone: why was this called?");
    }


    @Override
    public void onCustomerConfirmationUpdatedInDB(boolean confirmationState) {
        conversation.setCustomerConfirmed(confirmationState);
    }

    @Override
    public void onErrorDB(String errorMessage) {
        conversationView.onErrorUI("something went wrong!");
    }


    // getters & setters
    public Conversation getConversation() {
        return conversation;
    }

    public ConversationDatabaseHandler getConversationDatabaseHandler() {
        return conversationDatabaseHandler;
    }
}
