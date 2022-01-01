package com.example.pocketstore_customerapp.signup.setupProfile;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class SetupProfilePresenter {

    private static final String TAG = "SPP-debug";

    SetupProfileView view;
    DatabaseReference databaseReference;
    Customer customer;
    FirebaseAuth firebaseAuth;

    public SetupProfilePresenter(SetupProfileView view, Customer customer, DatabaseReference databaseReference, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.customer = customer;
        this.firebaseAuth = firebaseAuth;
        this.databaseReference = databaseReference ; //FirebaseDatabase.getInstance().getReference().child("customers");
    }

    public boolean validateFormInputs(String fullName, String email, String address, double latitude, double longitude, String phoneNumber){

        if(!validateUIEmailInput(email)){
            view.onEmailIsWrong("Not valid email");
            return false;
        }

        if(fullName.length()<4){
            view.onFullNameIsWrong("Name must be 4 character long");
            return false;
        }

        if(address.isEmpty()){
            view.onAddressIsWrong("Address is empty!");
            return  false;
        }

        if(latitude==0.0||longitude==0.0){
            view.onAddressIsWrong("Click the location icon to pick address from map.");
            return false;
        }

        customer.setUsername(fullName);
        customer.setHomeAddress(address);
        customer.setEmail(email);
        customer.setCustomerLatitude(latitude);
        customer.setCustomerLongitude(longitude);
        customer.setUserID(firebaseAuth.getUid());
        customer.setCustomerPhoneNumber(phoneNumber);
        return true;
    }

    public void createProfile(final Context context)
    {

        view.onProfileCreationLoading();

        databaseReference.child("customers").child(customer.getUserID()).setValue(customer).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                view.onProfileCreationFailed("something went wrong!");

                Log.d(TAG, "onFailure: error = "+e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storeInfoLocally(context);
                view.onProfileCreationSuccess();
            }
        });
    }

    private void storeInfoLocally(Context context) {
        // locally store profile information in shared preferences

        UserInfoSharedPreferences.setUserID(customer.getUserID(), context);
        UserInfoSharedPreferences.setUsername(customer.getUsername(), context);
        UserInfoSharedPreferences.setCustomerPhoneNumber(customer.getCustomerPhoneNumber(), context);
        UserInfoSharedPreferences.setEmail(customer.getEmail(), context);
        UserInfoSharedPreferences.setHomeAddress(customer.getHomeAddress(), context);
        UserInfoSharedPreferences.setCustomerLatitude(customer.getCustomerLatitude(), context);
        UserInfoSharedPreferences.setCustomerLongitude(customer.getCustomerLongitude(), context);

    }

    private boolean validateUIEmailInput(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(!email.matches(regex))
        {
            return false;
        }
        return  true;
    }
}