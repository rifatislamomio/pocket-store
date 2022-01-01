package com.example.pocketstore_customerapp.home;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.enterShop.EnterShopActivity;
import com.example.pocketstore_customerapp.menu.MenuActivity;
import com.example.pocketstore_customerapp.misc.Utils;
import com.example.pocketstore_customerapp.misc.sharedPreferences.EnteredShopSharedPreferences;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.example.uicomponents_module.CustomProgressDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class NearbyShopsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
        NearbyShopsView
{
    private static final String TAG = "NSA-debug";

    // ui
    private GoogleMap mMap;
    CustomProgressDialog customProgressDialog;
    private LinearLayout shopInfoCV;
    private TextView shopNameTVinCV, shopStatusTVinCV, shopPhoneNoTVinCV;
    // presenter
    private NearbyShopsPresenter nearbyShopsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_shops);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);

        init();

    }

    private void init() {

        nearbyShopsPresenter = new NearbyShopsPresenter(
                this,
                new NearbyShopsDatabaseHandler(),
                new ArrayList<NearbyShop>());

        // progress bar
        customProgressDialog = new CustomProgressDialog();
        customProgressDialog.show(getSupportFragmentManager(), "");

        // shop info card view
        shopInfoCV = findViewById(R.id.shop_detail_view);
        shopNameTVinCV = findViewById(R.id.store_name);
        shopPhoneNoTVinCV = findViewById(R.id.store_phone);
        shopStatusTVinCV = findViewById(R.id.store_status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // removes database listener
        nearbyShopsPresenter.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // enable map-styles that remove all business points from map
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.nearby_shops_map_style));

            if (!success) {
                Log.d(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.d(TAG, "Can't find style. Error: ", e);
        }

        // Move the camera to user home address
        LatLng userHome = new LatLng(
                UserInfoSharedPreferences.getCustomerLatitude(this),
                UserInfoSharedPreferences.getCustomerLongitude(this)
        );

        Log.d(TAG, "onMapReady: latlng = "
                + UserInfoSharedPreferences.getCustomerLatitude(this) + " "
                + UserInfoSharedPreferences.getCustomerLongitude(this));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userHome, 19));

        // no need for these default map UI features
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // map listens to marker clicks
        mMap.setOnMarkerClickListener(this);
        // map clicks are listened
        mMap.setOnMapClickListener(this);

        // map is ready! start fetching nearby stores
        String dbPath = Utils.customersNode+"/"
                        + UserInfoSharedPreferences.getUserID(this)+"/"
                        +Utils.nearbyShopNode;
        nearbyShopsPresenter.fetchNearbyShops(dbPath);
    }


    @Override
    public void onNearbyShopsFetchedUI(ArrayList<NearbyShop> nearbyShops) {
        // show the fetched nearby shops

        // must hide NON-CANCELLABLE progress bar
        customProgressDialog.dismiss();

        // clear out existing markers (if any)
        mMap.clear();

        for (NearbyShop nearbyShop: nearbyShops) {
            addShopToMap(nearbyShop);
        }


    }

    private void addShopToMap(NearbyShop nearbyShop){
        // adds a shop icon(marker) to map

        String shopID = nearbyShop.getShopID();
        LatLng shopLatLng = new LatLng( nearbyShop.getShopLatitude(), nearbyShop.getShopLongitude() );

        // setup marker UI
        MarkerOptions markerOptions = new MarkerOptions()
                .position(shopLatLng)
                .title(nearbyShop.getShopName())
                // no need for snippet can show ONLY ONE infoWindow at a time -_-
                .snippet(nearbyShop.getShopPhoneNumber());

        switch (nearbyShop.getShopType()){
            // show icon based on shop status: active/inactive

            case Utils.SHOP_CONVENTIONAL:
                if(nearbyShop.getStatus().equals("active"))
                    markerOptions.icon( bitmapDescriptorFromVector(this, R.drawable.ic_conventional_green_dot) );
                else
                    markerOptions.icon(bitmapDescriptorFromVector(this, R.drawable.ic_conventional_red_dot));
                break;

            case Utils.SHOP_STATIONARY:
                if(nearbyShop.getStatus().equals("active"))
                    markerOptions.icon(bitmapDescriptorFromVector(this, R.drawable.ic_stationary_green_dot));
                else
                    markerOptions.icon(bitmapDescriptorFromVector(this, R.drawable.ic_stationary_red_dot));
                break;

            case Utils.SHOP_BAKERY:
                if(nearbyShop.getStatus().equals("active"))
                    markerOptions.icon(bitmapDescriptorFromVector(this, R.drawable.ic_bakery_green_dot));
                else
                    markerOptions.icon(bitmapDescriptorFromVector(this, R.drawable.ic_bakery_red_dot));
                break;
        }


        Marker shopMarker = mMap.addMarker(markerOptions);
        // unique id for each shop
        shopMarker.setTag(shopID);
    }

    @Override
    public void onFetchFailedUI(String message) {

        // must hide NON-CANCELLABLE progress bar
        customProgressDialog.dismiss();

        Log.d(TAG, "onFetchFailedUI: fetch failed! error = "+message);
    }

    @Override
    public void enterShopUI(NearbyShop nearbyShop) {
    // open EnterShopActivity for the selected shop

        storeEnteringShopInfoLocally(nearbyShop);

        startActivity( new Intent(this, EnterShopActivity.class));

    }

    @Override
    public void onEnterShopInactiveUI() {
        Toasty.error(this, "shop is closed!").show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // market tag contains shopId
        nearbyShopsPresenter.selectShop((String) marker.getTag());

        // show the card view with selected shop's info
        shopNameTVinCV.setText(nearbyShopsPresenter.getSelectedShop().getShopName());
        shopPhoneNoTVinCV.setText(nearbyShopsPresenter.getSelectedShop().getShopPhoneNumber());
        shopStatusTVinCV.setText(nearbyShopsPresenter.getSelectedShop().getStatus());
        if(nearbyShopsPresenter.getSelectedShop().getStatus().equals("inactive"))
            shopStatusTVinCV.setTextColor( getResources().getColor(R.color.errorColor) );
        else
            shopStatusTVinCV.setTextColor(getResources().getColor(R.color.quantum_lightgreen));
        shopInfoCV.setVisibility(View.VISIBLE);

        // DO NOT REMOVE return false;
        return false;
    }

    public void enterShopClick(View view) {
    // shop info card view enter shop button click

        // enter the shop that was selected
        nearbyShopsPresenter.enterShop();

    }

    private void storeEnteringShopInfoLocally(NearbyShop nearbyShop) {
        // store entering shop's data locally

        EnteredShopSharedPreferences.setShopID(nearbyShop.getShopID(), getApplicationContext());
        EnteredShopSharedPreferences.setShopName(nearbyShop.getShopName(), getApplicationContext());

        EnteredShopSharedPreferences.setShopType(nearbyShop.getShopType(), getApplicationContext());
        EnteredShopSharedPreferences.setShopPhoneNumber(nearbyShop.getShopPhoneNumber(), getApplicationContext());

        EnteredShopSharedPreferences.setShopAddress(nearbyShop.getShopAddress(), getApplicationContext());
        EnteredShopSharedPreferences.setShopLatitude(nearbyShop.getShopLatitude(), getApplicationContext());
        EnteredShopSharedPreferences.setShopLongitude(nearbyShop.getShopLongitude(), getApplicationContext());

        EnteredShopSharedPreferences.setStatus(nearbyShop.getStatus(), getApplicationContext());
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        // xml -> bitmap [for map marker icons]
        // courtesy-
        // https://stackoverflow.com/questions/42365658/custom-marker-in-google-maps-in-android-with-vector-asset-icon

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // hide the card view (if showing) whenever clicked on screen
        shopInfoCV.setVisibility(View.GONE);
    }

    public void slideInLeftOutRight()
    {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    public void StartMenuActivity(View view)
    {
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        slideInLeftOutRight();
    }

}