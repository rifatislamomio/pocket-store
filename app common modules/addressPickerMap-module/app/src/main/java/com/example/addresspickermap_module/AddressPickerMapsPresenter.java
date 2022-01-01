package com.example.addresspickermap_module;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddressPickerMapsPresenter {
    // bridge between AddressPickerMapsActivity and others

    private static final String TAG = "ap-presenter-debug";

    AddressPickerMapsView addressPickerMapsView;
    AddressComposite addressComposite;

    public AddressPickerMapsPresenter(AddressPickerMapsView addressPickerMapsView, AddressComposite addressComposite) {
        this.addressPickerMapsView = addressPickerMapsView;
        this.addressComposite = addressComposite;
    }

    public void getAddress(LatLng latLng, final Geocoder geocoder, final Activity activity){

        double latitude = latLng.latitude, longitude = latLng.longitude;

        if(latitude>90||latitude<-90||longitude>180||longitude<-180){
            addressPickerMapsView.fetchAddressFailed("invalid input!");
            return;
        }

        addressComposite.setLatitude(latitude);
        addressComposite.setLongitude(longitude);

        if(!geocoder.isPresent()){
            addressPickerMapsView.fetchAddressFailed("geo-coder not present!");
            return;
        }

        addressPickerMapsView.fetchingAddressUI();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // i/o blocking operations need to be done in non-ui thread
                reverseGeoCodingProcess(geocoder, activity);
            }
        }).start();

    }

    private void reverseGeoCodingProcess(Geocoder geocoder, Activity activity) {
        // fetch address from latLng with reverse geocoding
        // this method needs to run on a non-ui thread

        List<Address> addresses = null;

        try{

            addresses = geocoder.getFromLocation(
                    addressComposite.getLatitude(),
                    addressComposite.getLongitude(),
                    // get only the first available address (?)
                    1);

        }catch (IOException e){
            Log.d(TAG, "getAddress: "+e.getMessage());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // update UI
                    addressPickerMapsView.fetchAddressFailed("something went wrong!");
                }
            });

        }catch (IllegalArgumentException e){
            Log.d(TAG, "getAddress: "+e.getMessage());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // update UI
                    addressPickerMapsView.fetchAddressFailed("invalid location!");
                }
            });

        }

        if(addresses==null || addresses.isEmpty()){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // update UI
                    addressPickerMapsView.fetchAddressFailed("Address for the location was not found!");
                }
            });

            Log.d(TAG, "getAddress: address not found");
            return;
        }

        Address address = addresses.get(0);

        addressComposite.setAddress(address);

        String addressString = breakDownAddress(address);
        addressComposite.setAddressString(addressString);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // success! update UI
                addressPickerMapsView.fetchAddressDoneUI(addressComposite.getAddressString());
            }
        });

    }

    private String breakDownAddress(Address address) {
        // breakdown fetched "Address" object to user readable form

        List<String> addressFragments = new ArrayList<String>();

        // Fetch the address lines using getAddressLine,
        // join them, and send them to the UI thread.
        for(int i = 0; i <= address.getMaxAddressLineIndex(); i++)
            addressFragments.add(address.getAddressLine(i));

        // fix too long addresses
        String addressString = "";
        if(addressFragments.size()>=5)
            addressString = addressFragments.get(addressFragments.size()-3)+", "
                    +addressFragments.get(addressFragments.size()-2)+", "
                    +addressFragments.get(addressFragments.size()-1);
        else
            addressString = TextUtils.join(", ", addressFragments);

        return addressString;
    }


}
