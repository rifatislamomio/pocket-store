package com.example.pocketstore_shopownerapp.customerConnection.callConnecting;

public class ConnectedShop {

    private String shopID;
    private String shopPhoneNumber;
    private String shopType;
    private String shopName;
    private String shopAddress;
    private double shopLatitude;
    private double shopLongitude;

    public ConnectedShop() {
    }

    public ConnectedShop(String shopID, String shopName,
                         String shopPhoneNumber, String shopType,
                         String shopAddress, double shopLatitude, double shopLongitude)
    {
        this.shopID = shopID;
        this.shopPhoneNumber = shopPhoneNumber;
        this.shopType = shopType;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopLatitude = shopLatitude;
        this.shopLongitude = shopLongitude;
    }


    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public double getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(double shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public double getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(double shopLongitude) {
        this.shopLongitude = shopLongitude;
    }
}
