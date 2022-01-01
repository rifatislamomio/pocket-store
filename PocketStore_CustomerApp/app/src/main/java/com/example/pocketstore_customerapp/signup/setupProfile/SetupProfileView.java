package com.example.pocketstore_customerapp.signup.setupProfile;

public interface SetupProfileView {

    void onProfileCreationLoading();
    void onProfileCreationSuccess();
    void onProfileCreationFailed(String errorMessage);

    void onEmailIsWrong(String errorMessage);
    void onFullNameIsWrong(String errorMessage);
    void onAddressIsWrong(String errorMessage);
}
