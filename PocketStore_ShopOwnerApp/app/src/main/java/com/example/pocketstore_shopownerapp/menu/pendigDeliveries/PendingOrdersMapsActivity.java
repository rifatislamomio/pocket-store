package com.example.pocketstore_shopownerapp.menu.pendigDeliveries;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.misc.Utils;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOrdersMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private ArrayList<PendingOrder> pendingOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pendingOrders = new ArrayList<>();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // no need for these default map UI features
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        showAllPendingOrdersInMap();

        // Add a marker in Sydney and move the camera
        LatLng shopLocation = new LatLng(ShopInfoSharedPreference.getShopLatitude(getApplicationContext()),
                ShopInfoSharedPreference.getShopLongitude(getApplicationContext()));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(shopLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 18));
    }

    private void showAllPendingOrdersInMap() {

        FirebaseDatabase.getInstance().getReference()
                .child(Utils.shopsNode+"/"+ ShopInfoSharedPreference.getUserID(getApplicationContext())+
                        "/pendingOrders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){

                    PendingOrder pendingOrder = new PendingOrder( snap.getValue(CustomerWaitingInQueue.class) );

                    pendingOrders.add(pendingOrder);

                    LatLng latLng = new LatLng(pendingOrder.getCustomer().getCustomerLatitude(),
                            pendingOrder.getCustomer().getCustomerLongitude());

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(pendingOrder.getCustomer().getUsername())
                            .snippet(pendingOrder.getCustomer().getCustomerPhoneNumber());

                    Marker marker = mMap.addMarker(markerOptions);

                    marker.setTag(pendingOrders.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}