package com.example.pocketstore_shopownerapp.ApplicationManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ApplicationManager extends Application implements LifecycleObserver {
    // class that observes application lifecycle and reacts accordingly (sets in db shop active or not)

    // database handler to set status="inactive" when app is closed
    private static ShopStatusDatabaseHandler shopStatusDatabaseHandler;

    private static final String TAG = "AM-debug";

    @Override
    public void onCreate() {
        super.onCreate();

        // set this class to observe app lifecycle events like- app open/close/pause
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        shopStatusDatabaseHandler = new ShopStatusDatabaseHandler(
                FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference());

        Log.d(TAG, "onCreate: app created");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppStart(){
        // called when app is brought to foreground

        if(shopStatusDatabaseHandler.isUserLoggedIn()){
            // user already logged in

            shopStatusDatabaseHandler.setShopStateToActive();

            shopStatusDatabaseHandler.enableSetShopInactiveWhenDisconnectedUnintentionally();

            ShopInfoSharedPreference.setStatus("active", getApplicationContext());

            Log.d(TAG, "onAppStart: user logged in");
        }

        Log.d(TAG, "onAppStart: app started");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppStop(){
        // called when app is removed from foreground

        if(shopStatusDatabaseHandler.isUserLoggedIn()){

            shopStatusDatabaseHandler.setShopStateToInActive();

            ShopInfoSharedPreference.setStatus("inactive", getApplicationContext());

            Log.d(TAG, "onAppStop: user logged in");
        }

        Log.d(TAG, "onAppStop: app closed");

    }

    public static ShopStatusDatabaseHandler getShopStatusDatabaseHandler() {
        return shopStatusDatabaseHandler;
    }
}
