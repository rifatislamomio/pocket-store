package com.example.pocketstore_customerapp.enterShop;

import com.example.pocketstore_customerapp.misc.abstractModels.Shop;

public class EnteredShop extends Shop {
// model for currently entered shop of EnterShop view

    public EnteredShop() {
    }

    public EnteredShop(String shopID, String shopName, String shopType, String shopPhoneNumber,
                       String shopAddress, double shopLatitude, double shopLongitude,
                       String status)
    {
        super(shopID, shopName, shopType, shopPhoneNumber, shopAddress, shopLatitude, shopLongitude, status);
    }


}
