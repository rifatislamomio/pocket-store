package com.example.pocketstore_shopownerapp.signup.setupProfile;

public class Shop {
    private String userID;
    private String shopownerName;
    private String shopPhoneNumber;
    private String shopType;
    private String shopName;
    private String shopAddress;
    private double shopLatitude;
    private double shopLongitude;
    private double perimeterRadius;
    private String status = "active";

    public Shop() {
    }

    public Shop(String userID, String shopownerName, String shopPhoneNumber, String shopType, String shopName,
                String shopAddress, double shopLatitude, double shopLongitude, double perimeterRadius,
                String status)
    {
        this.userID = userID;
        this.shopownerName = shopownerName;
        this.shopPhoneNumber = shopPhoneNumber;
        this.shopType = shopType;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopLatitude = shopLatitude;
        this.shopLongitude = shopLongitude;
        this.perimeterRadius = perimeterRadius;
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getShopownerName() {
        return shopownerName;
    }

    public void setShopownerName(String shopownerName) {
        this.shopownerName = shopownerName;
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

    public double getPerimeterRadius() {
        return perimeterRadius;
    }

    public void setPerimeterRadius(double perimeterRadius) {
        this.perimeterRadius = perimeterRadius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
