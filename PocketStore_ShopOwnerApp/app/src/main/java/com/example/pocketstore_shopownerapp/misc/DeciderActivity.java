package com.example.pocketstore_shopownerapp.misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pocketstore_shopownerapp.ApplicationManager.ApplicationManager;
import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.home.InsideShopActivity;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.example.pocketstore_shopownerapp.signup.setupProfile.SetupProfileActivity;
import com.example.pocketstore_shopownerapp.signup.setupProfile.Shop;
import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class DeciderActivity extends AppCompatActivity {
    // this activity decides what to do after phone verification is done

    private static final String TAG = "DA-debug";

    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider);

        Log.d(TAG, "onCreate: decider activity opened");

        progressDialog = new CustomProgressDialog();
        
        decide();

    }

    private void decide(){

        // show progressbar while deciding
        progressDialog.show(getSupportFragmentManager(),"");

        String userID= FirebaseAuth.getInstance().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Utils.shopsNode);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Intent intent;

                Log.d(TAG, "onDataChange: inside decider activity");

                if(snapshot.exists()) {
                    // user already has an account

                    String userID = (String) snapshot.child(Utils.shopIdValueNode).getValue();
                    String shopownerName = (String) snapshot.child(Utils.shopOwnerNameValueNode).getValue();
                    String shopPhoneNumber = (String) snapshot.child(Utils.shopPhoneNumberValueNode).getValue();
                    String shopType = (String) snapshot.child(Utils.shopTypeValueNode).getValue();
                    String shopName = (String) snapshot.child(Utils.shopNameValueNode).getValue();
                    String shopAddress = (String) snapshot.child(Utils.shopAddressValueNode).getValue();
                    double shopLatitude = (double) snapshot.child(Utils.shopLatitudeValueNode).getValue();
                    double shopLongitude = (double) snapshot.child(Utils.shopLongitudeValueNode).getValue();
                    double perimeterRadius = (long) snapshot.child(Utils.perimeterRadiusValueNode).getValue();

                    // set shop state to active in database and locally
                    String status = "active";
                    ApplicationManager.getShopStatusDatabaseHandler().setShopStateToActive();

                    Shop shop =
                            new Shop(userID, shopownerName, shopPhoneNumber, shopType, shopName,
                                    shopAddress, shopLatitude, shopLongitude, perimeterRadius, status);

                    storeShopInfoLocally(shop);

                    // go to home activity
                    intent = new Intent(DeciderActivity.this, InsideShopActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toasty.success(getApplicationContext(),"Welcome back!").show();

                }

                else {
                    // first time user, go to setupProfile

                    intent = new Intent(DeciderActivity.this, SetupProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toasty.info(getApplicationContext(),"Provide necessary user information!").show();

                }

                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(getApplicationContext(),"Something went wrong!").show();
                progressDialog.dismiss();
            }
        });

    }

    private void storeShopInfoLocally(Shop shop) {
        // store signed up shop's information as shared preferences

        ShopInfoSharedPreference.setUserID(shop.getUserID(), getApplicationContext());
        ShopInfoSharedPreference.setShopName(shop.getShopName(), getApplicationContext());
        ShopInfoSharedPreference.setShopownerName(shop.getShopownerName(), getApplicationContext());
        ShopInfoSharedPreference.setShopType(shop.getShopType(), getApplicationContext());
        ShopInfoSharedPreference.setShopPhoneNumber(shop.getShopPhoneNumber(), getApplicationContext());
        ShopInfoSharedPreference.setShopAddress(shop.getShopAddress(), getApplicationContext());
        ShopInfoSharedPreference.setShopLatitude(shop.getShopLatitude(), getApplicationContext());
        ShopInfoSharedPreference.setShopLongitude(shop.getShopLongitude(), getApplicationContext());
        ShopInfoSharedPreference.setPerimeterRadius(shop.getPerimeterRadius(), getApplicationContext());
        ShopInfoSharedPreference.setStatus(shop.getStatus(), getApplicationContext());

    }

}