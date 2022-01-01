package com.example.pocketstore_shopownerapp.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.customerqueue_module.CustomerQueueFragment;
import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.customerConnection.callConnecting.CallConnectingActivity;
import com.example.pocketstore_shopownerapp.menu.MenuActivity;
import com.example.pocketstore_shopownerapp.misc.Utils;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ConnectedCustomerSharedPreference;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class InsideShopActivity extends AppCompatActivity implements InsideShopView {
// HomePage of the app, (will) contains three fragments -> Customer Queue, Chat Feed, Show Items

    private static final String TAG = "ISA-debug";
    // presenter
    private InsideShopPresenter insideShopPresenter;
    // path to customerQueue db node
    private String customerQueueDbPath;
    // ui
    private TextView shopNameTV;
    private AlertDialog noInternetAD, backPressAD;
    private CustomProgressDialog customProgressDialog;
    private AppCompatImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_shop);

        init();

        initUI();

    }

    private void init() {
        // initialize presenter

        // path to shops/shopId/customerQueue
        customerQueueDbPath = Utils.shopsNode
                +"/"+ ShopInfoSharedPreference.getUserID(getApplicationContext())
                +"/"+Utils.customerQueueNode;
        Log.d(TAG, "init: CQDbPath = "+customerQueueDbPath);

        insideShopPresenter = new InsideShopPresenter(
                this,
                new CustomerWaitingInQueue(),
                new InsideShopDatabaseHandler(
                        FirebaseDatabase.getInstance().getReference(),
                        customerQueueDbPath)
        );

    }

    private void initUI() {
    // initialize ui components MUST be called AFTER PRESENTER IS INITIALIZED

        customProgressDialog = new CustomProgressDialog();

        shopNameTV = findViewById(R.id.shop_name_text_view);

        // bring the shop name from shared preferences
        shopNameTV.setText(ShopInfoSharedPreference.getShopName(getApplicationContext()));

        noInternetAD = Utils.createAlertDialogBuilder(
                this,
                "You are not connected to internet!",
                "ok",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },
                "", null
        );

        backPressAD = Utils.createAlertDialogBuilder(
                this,
                "Are you sure you want to exit shop?",
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                },
                "no",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(InsideShopActivity.this, MenuActivity.class));
            }
        });

        loadCustomerQueueFragment();
    }

    private void loadCustomerQueueFragment() {
        // show the customer queue fragment in frameLayout

        // MUST pass customerQueue db path to CQ-module to open fragment
        CustomerQueueFragment.setDbNodePath(customerQueueDbPath);

        CustomerQueueFragment fragment = new CustomerQueueFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.cqueueframelayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        backPressAD.show();
    }


    public void connectNextCustomerClick(View view) {
    // connect next customer button click event

        insideShopPresenter.notifyCustomerAtTopOfQueue();

        customProgressDialog.show(getSupportFragmentManager(), "");

    }

    @Override
    public void onCustomerAtTopOfQueueNotifiedUI(CustomerWaitingInQueue customerAtTop) {

        //TODO: open connection activity pass in the customer object

        //insideShopPresenter.connectWithCustomerAtTop();

        // save the connected customer info in local storage
        storeConnectedCustomerLocally(customerAtTop);

        customProgressDialog.dismiss();
        Toasty.success(this, "Connecting with "+customerAtTop.getUsername()).show();

        startActivity( new Intent(this, CallConnectingActivity.class));

    }

    private void storeConnectedCustomerLocally(CustomerWaitingInQueue customerAtTop) {
        // saves connected customer info locally

        ConnectedCustomerSharedPreference.setCustomerId(customerAtTop.getCustomerId(), getApplicationContext());
        ConnectedCustomerSharedPreference.setUsername(customerAtTop.getUsername(), getApplicationContext());
        ConnectedCustomerSharedPreference.setCustomerPhoneNumber(customerAtTop.getCustomerPhoneNumber(), getApplicationContext());
        ConnectedCustomerSharedPreference.setHomeAddress(customerAtTop.getHomeAddress(), getApplicationContext());
        ConnectedCustomerSharedPreference.setCustomerLatitude(customerAtTop.getCustomerLatitude(), getApplicationContext());
        ConnectedCustomerSharedPreference.setCustomerLongitude(customerAtTop.getCustomerLongitude(), getApplicationContext());
    }

    @Override
    public void onNoNextCustomerUI() {

        customProgressDialog.dismiss();

        Toasty.warning(this, "no customer in queue!").show();

    }

    @Override
    public void onFailed(String errorMessage) {

        // TODO: internet connection check (detect internet connection loss with broadcast receiver)
//        if(!Utils.isInternetConnected(this)) {
//            noInternetAD.show();
//        }

        customProgressDialog.dismiss();

        Toasty.error(this, errorMessage).show();

    }
}