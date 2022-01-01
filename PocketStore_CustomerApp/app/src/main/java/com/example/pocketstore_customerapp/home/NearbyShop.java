package com.example.pocketstore_customerapp.home;

import com.example.pocketstore_customerapp.misc.abstractModels.Shop;

public class NearbyShop extends Shop {
// model for nearbyShops view


    public NearbyShop() {
    }

    public NearbyShop(String shopID, String shopName, String shopType, String shopPhoneNumber,
                      String shopAddress, double shopLatitude, double shopLongitude,
                      String status)
    {
        super(shopID, shopName, shopType, shopPhoneNumber, shopAddress, shopLatitude, shopLongitude, status);
    }

    @Override
    public String toString() {
        return "\nNearbyShop{" +
                "shopID='" + shopID + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopType='" + shopType + '\'' +
                ", shopPhoneNumber='" + shopPhoneNumber + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopLatitude=" + shopLatitude +
                ", shopLongitude=" + shopLongitude +
                ", status='" + status + '\'' +
                "}\n";
    }
}
