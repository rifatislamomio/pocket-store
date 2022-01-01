package com.example.pocketstore_customerapp.shopConnection.callConnecting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.misc.Utils;
import com.example.pocketstore_customerapp.misc.sharedPreferences.EnteredShopSharedPreferences;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.example.pocketstore_customerapp.shopConnection.conversation.ConversationActivity;
import com.example.uicomponents_module.CustomProgressDialog;
import com.example.voimoduleapp.SinchHandler;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class CallConnectingActivity extends AppCompatActivity implements CallConnectingView {

    // presenter
    private CallConnectingPresenter callConnectingPresenter;

    // ui
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_connecting);

        initUI();

        init();

    }

    private void initUI() {
    // initialize UI components

        customProgressDialog = new CustomProgressDialog();

        customProgressDialog.show(getSupportFragmentManager(), "");

    }

    private void init(){
    // initialize presenter

        String customerId = UserInfoSharedPreferences.getUserID(getApplicationContext());
        String shopId = EnteredShopSharedPreferences.getShopID(getApplicationContext());

        String dbPathToShopReadyToConnect = Utils.customerShopInteractionNode
                + "/" + customerId + shopId
                + "/" + Utils.shopOwnerReadyToConnectValueNode;

        callConnectingPresenter = new CallConnectingPresenter(this,
                new CallConnectingDatabaseHandler(
                        FirebaseDatabase.getInstance().getReference().child(dbPathToShopReadyToConnect)),
                //TODO: remove CallClientSetupObserver from constructor
                new SinchHandler(customerId, getApplicationContext(), null)
        );

        callConnectingPresenter.registerDBListeners();

    }

    @Override
    protected void onDestroy() {

        // MUST REMOVE DB VALUE LISTENERS
        callConnectingPresenter.onDestroy();

        super.onDestroy();
    }


    @Override
    public void onReadyToMakeCallUI() {

        customProgressDialog.dismiss();

        // MUST set this up before opening ConversationActivity
        ConversationActivity.setVoiHandler(callConnectingPresenter.getVoiHandler());

        startActivity( new Intent(this, ConversationActivity.class));

        // only job of this activity is to setup app-to-app api client after listening to db changes
        // terminate immediately
        finish();

    }

    @Override
    public void onFailedUI(String message) {

        Toasty.error(this, message).show();

        customProgressDialog.dismiss();

        // only job of this activity is to setup app-to-app api client after listening to db changes
        // terminate immediately
        callConnectingPresenter.onDestroyedUnexpectedly();
        finish();

    }

    public void endCallClick(View view) {
        // only job of this activity is to setup app-to-app api client after listening to db changes
        // terminate immediately
        callConnectingPresenter.onDestroyedUnexpectedly();
        finish();
    }
}