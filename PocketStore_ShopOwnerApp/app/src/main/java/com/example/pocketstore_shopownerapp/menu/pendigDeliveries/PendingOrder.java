package com.example.pocketstore_shopownerapp.menu.pendigDeliveries;

import com.example.customerqueue_module.CustomerWaitingInQueue;

public class PendingOrder {

    CustomerWaitingInQueue customer;

    public PendingOrder() {
    }

    public PendingOrder(CustomerWaitingInQueue customer) {
        this.customer = customer;
    }

    public CustomerWaitingInQueue getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerWaitingInQueue customer) {
        this.customer = customer;
    }
}
