package com.example.pocketstore_shopownerapp.signup.setupProfile;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class SetupProfilePresenter {

    private static final String TAG = "SPP-debug";

    SetupProfileView view;
    DatabaseReference databaseReference;
    Shop shop;
    FirebaseAuth firebaseAuth;

    public SetupProfilePresenter(SetupProfileView view, Shop shop, DatabaseReference databaseReference, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.shop = shop;
        this.firebaseAuth = firebaseAuth;
        this.databaseReference = databaseReference ; //FirebaseDatabase.getInstance().getReference().child("shops");
    }

    public boolean validateFormInputs(String fullName, String shopName,
                                      String address, double latitude, double longitude,
                                      String phoneNumber, double radius, String shopType)
    {

        if(shopName.length()<5){
            view.onShopNameIsWrong("Shop name must be 5 character long");
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

        if(latitude==0.0 || longitude==0.0){
            view.onAddressIsWrong("Click the location icon to pick address in map");
            return false;
        }

        shop.setShopownerName(fullName);
        shop.setShopAddress(address);
        shop.setShopName(shopName);
        shop.setShopLatitude(latitude);
        shop.setShopLongitude(longitude);
        shop.setUserID(firebaseAuth.getUid());
        shop.setShopPhoneNumber(phoneNumber);
        shop.setPerimeterRadius(radius);
        shop.setShopType(shopType);
        return true;
    }

    public void createProfile(final Context context)
    {
        view.onProfileCreationLoading();

        databaseReference.child("shops").child(shop.getUserID()).setValue(shop).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                view.onProfileCreationFailed("Something went wrong!");

                Log.d(TAG, "onFailure: error = "+e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storeShopInfoLocally(context);
                view.onProfileCreationSuccess();
            }
        });
    }

    private void storeShopInfoLocally(Context context) {
        // store signed up shop's information as shared preferences

        ShopInfoSharedPreference.setUserID(shop.getUserID(), context);
        ShopInfoSharedPreference.setShopName(shop.getShopName(), context);
        ShopInfoSharedPreference.setShopownerName(shop.getShopownerName(), context);
        ShopInfoSharedPreference.setShopType(shop.getShopType(), context);
        ShopInfoSharedPreference.setShopPhoneNumber(shop.getShopPhoneNumber(), context);
        ShopInfoSharedPreference.setShopAddress(shop.getShopAddress(), context);
        ShopInfoSharedPreference.setShopLatitude(shop.getShopLatitude(), context);
        ShopInfoSharedPreference.setShopLongitude(shop.getShopLongitude(), context);
        ShopInfoSharedPreference.setPerimeterRadius(shop.getPerimeterRadius(), context);
        ShopInfoSharedPreference.setStatus(shop.getStatus(), context);

    }

}