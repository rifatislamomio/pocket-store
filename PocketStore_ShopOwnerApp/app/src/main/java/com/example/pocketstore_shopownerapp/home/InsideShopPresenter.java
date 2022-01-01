package com.example.pocketstore_shopownerapp.home;

import android.util.Log;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.google.firebase.database.DataSnapshot;

public class InsideShopPresenter implements InsideShopDatabaseCallback{
// presenter for InsideShopActivity to handle business logic

    private static final String TAG = "ISP-debug";

    private InsideShopView insideShopView;
    private CustomerWaitingInQueue customerAtTop;
    private InsideShopDatabaseHandler insideShopDatabaseHandler;

    public InsideShopPresenter(InsideShopView insideShopView,
                               CustomerWaitingInQueue customerAtTop,
                               InsideShopDatabaseHandler insideShopDatabaseHandler)
    {
        this.insideShopView = insideShopView;
        this.customerAtTop = customerAtTop;
        this.insideShopDatabaseHandler = insideShopDatabaseHandler;

        insideShopDatabaseHandler.setCallback(this);
    }

    public void notifyCustomerAtTopOfQueue(){
        // start the customer at top of Q fetch process

        insideShopDatabaseHandler.notifyCustomerAtTopOfQueue();

    }

//    public void connectWithCustomerAtTop(){
//        // make db changes that let customer know shop wants to connect
//
//        if(customerAtTop==null) {
//            Log.d(TAG, "connectWithCustomerAtTop: customer at top was not fetched yet");
//            return;
//        }
//
//        insideShopDatabaseHandler.setShopOwnerWantsToConnect(customerAtTop.getCustomerId());
//
//    }

    @Override
    public void onCustomerAtTopNotifiedDB(DataSnapshot dataSnapshot) {
        customerAtTop = dataSnapshot.getValue(CustomerWaitingInQueue.class);
        Log.d(TAG, "onCustomerAtTopFetchedDB: customer at top = "+customerAtTop.toString());
        insideShopView.onCustomerAtTopOfQueueNotifiedUI(customerAtTop);
    }

    @Override
    public void onCustomerQueueEmptyInDB() {
        insideShopView.onNoNextCustomerUI();
    }

    @Override
    public void onErrorDB(String errorMessage) {
        Log.d(TAG, "onErrorDB: error = "+errorMessage);
        insideShopView.onFailed("something went wrong! make sure you are connected to the internet");
    }

}
