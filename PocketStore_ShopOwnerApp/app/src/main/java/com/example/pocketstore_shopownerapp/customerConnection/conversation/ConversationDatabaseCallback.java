package com.example.pocketstore_shopownerapp.customerConnection.conversation;

public interface ConversationDatabaseCallback {

    void onCustomerConfirmationUpdatedInDB(boolean confirmationState);
    void onErrorDB(String errorMessage);

}
