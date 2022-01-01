package com.example.pocketstore_shopownerapp.home;

public class Customer {
// model class for customer whom to connect with

    private String customerId;
    private String username;
    private String customerPhoneNumber;

    private String homeAddress;
    private double customerLatitude;
    private double customerLongitude;

    public Customer() {
    }

    public Customer(String customerId, String username, String customerPhoneNumber,
                    String homeAddress, double customerLatitude, double customerLongitude)
    {
        this.customerId = customerId;
        this.username = username;
        this.customerPhoneNumber = customerPhoneNumber;
        this.homeAddress = homeAddress;
        this.customerLatitude = customerLatitude;
        this.customerLongitude = customerLongitude;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "\nCustomer{" +
                "customerId='" + customerId + '\'' +
                ", username='" + username + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", customerLatitude=" + customerLatitude +
                ", customerLongitude=" + customerLongitude +
                "}\n";
    }
}
