package com.example.pocketstore_shopownerapp.customerConnection.callConnecting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.customerConnection.conversation.ConversationActivity;
import com.example.pocketstore_shopownerapp.home.Customer;
import com.example.pocketstore_shopownerapp.misc.Utils;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ConnectedCustomerSharedPreference;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.example.uicomponents_module.CustomProgressDialog;
import com.example.voimoduleapp.SinchHandler;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class CallConnectingActivity extends AppCompatActivity implements CallConnectingView {
// activity to show progress bar while setting up app-to-app api & necessary db changes

    // presenter
    private CallConnectingPresenter callConnectingPresenter;

    //ui
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_connecting);

        initUI();

        init();

    }

    private void initUI() {
    // initiate UI components

        customProgressDialog = new CustomProgressDialog();

        customProgressDialog.show(getSupportFragmentManager(), "");

    }

    private void init() {
    // initiate the presenter

        Customer customer = getCustomerFromLocalStorage();
        ConnectedShop shop = getShopFromLocalStorage();

        String dbNodePath = Utils.customerShopInteractionNode+"/"+customer.getCustomerId()+shop.getShopID();

        callConnectingPresenter = new CallConnectingPresenter(
                this,
                customer, shop,
                new CallConnectingDatabaseHandler(
                        FirebaseDatabase.getInstance().getReference(), dbNodePath),
                // TODO: remove SinchHanler constructor's callClientSetupObserver parameter
                new SinchHandler(shop.getShopID(), getApplicationContext(), null)
        );

        // start immediately
        callConnectingPresenter.startConnection();

    }


    private Customer getCustomerFromLocalStorage() {

        Customer customer = new Customer();

        customer.setCustomerId(ConnectedCustomerSharedPreference.getCustomerId(getApplicationContext()));
        customer.setUsername(ConnectedCustomerSharedPreference.getUsername(getApplicationContext()));
        customer.setCustomerPhoneNumber(ConnectedCustomerSharedPreference.getCustomerPhoneNumber(getApplicationContext()));
        customer.setHomeAddress(ConnectedCustomerSharedPreference.getHomeAddress(getApplicationContext()));
        customer.setCustomerLatitude(ConnectedCustomerSharedPreference.getCustomerLatitude(getApplicationContext()));
        customer.setCustomerLongitude(ConnectedCustomerSharedPreference.getCustomerLongitude(getApplicationContext()));

        return customer;
    }

    private ConnectedShop getShopFromLocalStorage() {

        ConnectedShop connectedShop = new ConnectedShop();

        connectedShop.setShopID(ShopInfoSharedPreference.getUserID(getApplicationContext()));
        connectedShop.setShopName(ShopInfoSharedPreference.getShopName(getApplicationContext()));
        connectedShop.setShopType(ShopInfoSharedPreference.getShopType(getApplicationContext()));
        connectedShop.setShopPhoneNumber(ShopInfoSharedPreference.getShopPhoneNumber(getApplicationContext()));
        connectedShop.setShopAddress(ShopInfoSharedPreference.getShopAddress(getApplicationContext()));
        connectedShop.setShopLatitude(ShopInfoSharedPreference.getShopLatitude(getApplicationContext()));
        connectedShop.setShopLongitude(ShopInfoSharedPreference.getShopLongitude(getApplicationContext()));

        return connectedShop;

    }

    @Override
    public void onReadyToReceiveCallUI() {
        // ready to start conversation activity

        // MUST setup the api voiHandler with api client set up before starting conversation activity
        ConversationActivity.setVoiHandler(callConnectingPresenter.getVoiHandler());
        startActivity( new Intent(this, ConversationActivity.class));

        // only purpose for this activity is to setup db and app-to-app api client
        // finish immediately when done
        finish();

    }

    @Override
    public void onFailedUI(String message) {

        Toasty.error(this, message).show();

        // only purpose for this activity is to setup db and app-to-app api client
        // finish immediately if failed
        callConnectingPresenter.onDestroyedUnexpectedly();
        finish();

    }
}