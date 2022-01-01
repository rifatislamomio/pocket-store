package com.example.customerqueue_module;

import java.util.ArrayList;

public interface CustomerQueueView {

    void onCustomerQueueFetchDoneUI(ArrayList<CustomerWaitingInQueue> customers);
    void onCustomerQueueFetchFailedUI(String errorMessage);
    void onCustomerQueueEmptyUI();

    void onCustomerAddedToQueueUI(CustomerWaitingInQueue customer, int position);
    void onCustomerRemovedFromQueueUI(CustomerWaitingInQueue customer, int position);

}
