package com.example.addresspickermap_module;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class AddressPickerMapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationButtonClickListener,
        AddressPickerMapsView{
    // map view to pick address from

    // keys for values passed to calling activity
    public static final String ADDRESS_KEY = "address";
    public static final String LATLNG_KEY = "latlng";

    // log tag
    private static final String TAG = "address-picker-debug";

    // presenter
    AddressPickerMapsPresenter addressPickerMapsPresenter;

    // map components
    private GoogleMap mMap;
    private MarkerOptions pickedLocationMarker;

    // places api client (why was this not required??)
    private PlacesClient placesClient;

    // UI components
    private LinearLayout boxBelow;
    private Button btnConfirm;
    private TextView TVhint;

    // permission
    private Permission permission;
    private static final String[] permissionStrings = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int PERMISSION_CODE = 833;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_picker_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // init UIs
        boxBelow = findViewById(R.id.boxBelow);
        btnConfirm = findViewById(R.id.BtnConfirm);
        TVhint = findViewById(R.id.TVhint);

        // init presenter
        addressPickerMapsPresenter = new AddressPickerMapsPresenter(this, new AddressComposite());

        initPlacesApi();
    }

    private void initPlacesApi() {

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        // initialize places searchbox fragment
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // specify place type (find out more)
        autocompleteFragment
                .setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG))
                .setCountries("BD")
                .setTypeFilter(TypeFilter.GEOCODE);

        // place selection listener
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                // move camera to search place
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16.0f));

                // show user location picking hint
                boxBelow.setVisibility(View.VISIBLE);
                TVhint.setText("long press on map to pick you address!");

                Log.d(TAG, "onPlaceSelected: place selected = " + place.getName() + " " + place.getLatLng());

            }

            @Override
            public void onError(@NonNull Status status) {

                Toasty.warning(AddressPickerMapsActivity.this, "please try again!");

                Log.d(TAG, "onError: place selection error = " + status.toString());

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // move the camera to Dhaka
        LatLng dhaka = new LatLng(23.777176, 90.399452);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 10.0f));

        // enable necessary map events
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        // need location permission to enable built in "my location" blue-dot
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);

        promptPermission();
    }

    private void promptPermission() {
        // ask/check location permissions

        permission = new Permission(this, permissionStrings, PERMISSION_CODE);

        if (!permission.checkPermissions())
            permission.askPermissions();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_CODE:

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // permission granted yay!
                    mMap.setMyLocationEnabled(true);
                    return;
                }

                // permission was denied, respond accordingly
                try {
                    permission.resolvePermissions(permissions, grantResults,
                            "Please allow this if you want to be guided to your current location on map",
                            false);
                } catch (Exception e) {
                    Log.d(TAG, "onRequestPermissionsResult: error = " + e.getMessage());
                }

                break;
        }

    }

    public void addressConfirmClick(View view) {

        // start address fetching here
        addressPickerMapsPresenter.getAddress(
                pickedLocationMarker.getPosition(),
                new Geocoder(this, Locale.getDefault()),
                this
        );

        Toast.makeText(this, pickedLocationMarker.getPosition().toString(), Toast.LENGTH_LONG);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // long click on map will select the location

        mMap.clear();

        // show confirmation text
        boxBelow.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        TVhint.setText("Pick this address?");

        // TODO: set a proper icon in the marker
        pickedLocationMarker = new MarkerOptions()
                .position(latLng)
                .title("picked location");

        mMap.addMarker(pickedLocationMarker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public boolean onMyLocationButtonClick() {
        // moves camera to blu-dot, which is user's current location

        String toastText = "";
        if(!isWifiEnabled() && !isLocationEnabled())
            toastText = "Turn On both WiFi & Location";
        else if(!isLocationEnabled())
            toastText = "Turn On Location";
        else if(!isWifiEnabled())
            toastText = "Turn On WiFi";

        if(!toastText.equals(""))
            Toasty.normal(AddressPickerMapsActivity.this,
                    toastText + " to show your location");

        return false;
    }

    @Override
    public void fetchingAddressUI() {
        Toasty.normal(this, "fetching address please wait...");
    }

    @Override
    public void fetchAddressDoneUI(String address) {

        Toasty.success(this, "Address fetch successful!");

        Log.d(TAG, "fetchAddressDoneUI: address = " + address);

        goBackToParentActivityWithLocation(pickedLocationMarker.getPosition(), address);
    }

    @Override
    public void fetchAddressFailed(String error) {

        Toasty.error(this, "Address fetch failed! Pick again please.");
        Log.d(TAG, "fetchAddressFailed: error = " + error);

        goBackToParentActivityWithLocation(pickedLocationMarker.getPosition(), null);
    }

    private void goBackToParentActivityWithLocation(LatLng latLng, String address){
        // send picked LatLng and Address to parent activity

        Intent resultIntent = new Intent();
        resultIntent.putExtra(LATLNG_KEY, latLng.toString());
        resultIntent.putExtra(ADDRESS_KEY, address);
        setResult(RESULT_OK, resultIntent);
        finish();

    }



    private boolean isWifiEnabled(){
        WifiManager wifi = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}