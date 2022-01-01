package com.example.pocketstore_shopownerapp.customerConnection.conversation;

public interface ConversationView {

    void onCallConnectedUI();
    void onCallDisconnectedUI();

    void onErrorUI(String message);

}
