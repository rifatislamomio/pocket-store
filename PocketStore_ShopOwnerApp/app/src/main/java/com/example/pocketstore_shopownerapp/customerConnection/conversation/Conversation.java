package com.example.pocketstore_shopownerapp.customerConnection.conversation;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_shopownerapp.customerConnection.callConnecting.ConnectedShop;

public class Conversation {
// model for app-to-app call

    private ConnectedShop shop;
    private CustomerWaitingInQueue customer;
    private boolean shopConfirmed=false;
    private boolean customerConfirmed=false;

    public Conversation() {
    }

    public Conversation(ConnectedShop shop, CustomerWaitingInQueue customer) {
        this.shop = shop;
        this.customer = customer;
    }

    public ConnectedShop getShop() {
        return shop;
    }

    public void setShop(ConnectedShop shop) {
        this.shop = shop;
    }

    public CustomerWaitingInQueue getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerWaitingInQueue customer) {
        this.customer = customer;
    }

    public boolean isShopConfirmed() {
        return shopConfirmed;
    }

    public void setShopConfirmed(boolean shopConfirmed) {
        this.shopConfirmed = shopConfirmed;
    }

    public boolean isCustomerConfirmed() {
        return customerConfirmed;
    }

    public void setCustomerConfirmed(boolean customerConfirmed) {
        this.customerConfirmed = customerConfirmed;
    }
}
