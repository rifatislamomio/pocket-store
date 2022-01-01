package com.example.pocketstore_customerapp.misc.abstractModels;

public abstract class Shop {
// shop superclass with minimum variables that customer needs to access

    protected String shopID;

    protected String shopName;
    protected String shopType;
    protected String shopPhoneNumber;

    protected String shopAddress;
    protected double shopLatitude;
    protected double shopLongitude;

    protected String status;

    public Shop() {
    }

    public Shop(String shopID, String shopName, String shopType, String shopPhoneNumber,
                String shopAddress, double shopLatitude, double shopLongitude,
                String status)
    {
        this.shopID = shopID;
        this.shopName = shopName;
        this.shopType = shopType;
        this.shopPhoneNumber = shopPhoneNumber;
        this.shopAddress = shopAddress;
        this.shopLatitude = shopLatitude;
        this.shopLongitude = shopLongitude;
        this.status = status;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopPhoneNumber() {
        return shopPhoneNumber;
    }

    public void setShopPhoneNumber(String shopPhoneNumber) {
        this.shopPhoneNumber = shopPhoneNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\nShop{" +
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
