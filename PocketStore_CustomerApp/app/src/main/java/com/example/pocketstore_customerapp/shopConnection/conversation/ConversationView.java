package com.example.pocketstore_customerapp.shopConnection.conversation;

public interface ConversationView {

    void onCallOutgoingUI();

    void onCallConnectedUI();

    void onCallDisconnectedUI();

    void onErrorUI(String message);

}
