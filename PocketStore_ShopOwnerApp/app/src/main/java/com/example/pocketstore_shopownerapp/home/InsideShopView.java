package com.example.pocketstore_shopownerapp.home;

import com.example.customerqueue_module.CustomerWaitingInQueue;

public interface InsideShopView {
// interface for InsideShopActivity to get notified for UI changes

    void onCustomerAtTopOfQueueNotifiedUI(CustomerWaitingInQueue customerAtTop);
    void onNoNextCustomerUI();
    void onFailed(String errorMessage);
}
