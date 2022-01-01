package com.example.pocketstore_customerapp.shopConnection.conversation;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_customerapp.misc.abstractModels.Shop;

public class Conversation {
// model class to store conversation(app-to-app call) information

    private Shop shop;
    private CustomerWaitingInQueue customer;
    private boolean customerConfirmation = false;
    private boolean shopConfirmation = false;

    public Conversation() {
    }

    public Conversation(Shop shop, CustomerWaitingInQueue customer) {
        this.shop = shop;
        this.customer = customer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public CustomerWaitingInQueue getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerWaitingInQueue customer) {
        this.customer = customer;
    }

    public boolean isCustomerConfirmation() {
        return customerConfirmation;
    }

    public void setCustomerConfirmation(boolean customerConfirmation) {
        this.customerConfirmation = customerConfirmation;
    }

    public boolean isShopConfirmation() {
        return shopConfirmation;
    }

    public void setShopConfirmation(boolean shopConfirmation) {
        this.shopConfirmation = shopConfirmation;
    }
}
