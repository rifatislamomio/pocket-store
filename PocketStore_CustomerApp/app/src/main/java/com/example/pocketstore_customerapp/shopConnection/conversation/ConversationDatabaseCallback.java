package com.example.pocketstore_customerapp.shopConnection.conversation;

import com.google.firebase.database.DataSnapshot;

public interface ConversationDatabaseCallback {

    void onShopConfirmationUpdatedInDB(boolean confirmationState);

    void onErrorDB(String errorMessage);

}
