package com.example.addresspickermap_module;

import android.location.Address;

public class AddressComposite {
    // model for the address fetched from map

    private double latitude, longitude;
    private String addressString;
    private Address address;

    public AddressComposite() {
    }

    public AddressComposite(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressComposite(double latitude, double longitude, String addressString) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressString = addressString;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}