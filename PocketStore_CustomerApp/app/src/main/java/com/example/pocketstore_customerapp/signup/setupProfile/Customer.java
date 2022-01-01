package com.example.pocketstore_customerapp.signup.setupProfile;

public class Customer {
    private String userID;
    private String username;
    private String customerPhoneNumber;
    private String homeAddress;
    private String email;
    private double customerLatitude;
    private double customerLongitude;

    public Customer() { }

    public Customer(String userID, String username, String customerPhoneNumber, String homeAddress, String email, double customerLatitude, double customerLongitude) {
        this.userID = userID;
        this.username = username;
        this.customerPhoneNumber = customerPhoneNumber;
        this.homeAddress = homeAddress;
        this.email = email;
        this.customerLatitude = customerLatitude;
        this.customerLongitude = customerLongitude;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }
}
