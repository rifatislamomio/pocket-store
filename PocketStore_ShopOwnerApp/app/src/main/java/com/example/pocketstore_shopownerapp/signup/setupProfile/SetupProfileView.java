package com.example.pocketstore_shopownerapp.signup.setupProfile;

public interface SetupProfileView {

    void onProfileCreationLoading();
    void onProfileCreationSuccess();
    void onProfileCreationFailed(String errorMessage);

    void onShopNameIsWrong(String errorMessage);
    void onFullNameIsWrong(String errorMessage);
    void onAddressIsWrong(String errorMessage);
}
