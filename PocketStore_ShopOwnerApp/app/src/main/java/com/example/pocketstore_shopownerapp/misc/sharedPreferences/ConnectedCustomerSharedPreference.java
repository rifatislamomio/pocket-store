package com.example.pocketstore_shopownerapp.misc.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pocketstore_shopownerapp.misc.Utils;

public class ConnectedCustomerSharedPreference {

    private static SharedPreferences connectedCustomerInfo;

    private static void setUpSharedPreference(Context context){
        connectedCustomerInfo = context.getSharedPreferences(Utils.CUSTOMER_INFO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static String getCustomerId(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getString(Utils.customerIdSP, "");
    }

    public static void setCustomerId(String customerId, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putString(Utils.customerIdSP, customerId).apply();
    }

    public static String getUsername(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getString(Utils.customerNameSP, "");
    }

    public static void setUsername(String username, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putString(Utils.customerNameSP, username).apply();
    }

    public static String getCustomerPhoneNumber(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getString(Utils.customerPhoneNumberSP, "");
    }

    public static void setCustomerPhoneNumber(String customerPhoneNumber, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putString(Utils.customerPhoneNumberSP, customerPhoneNumber).apply();
    }

    public static String getHomeAddress(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getString(Utils.customerAddressSP, "");
    }

    public static void setHomeAddress(String homeAddress, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putString(Utils.customerAddressSP, homeAddress).apply();
    }

    public static double getCustomerLatitude(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getFloat(Utils.customerLatitudeSP, -1);
    }

    public static void setCustomerLatitude(double customerLatitude, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putFloat(Utils.customerLatitudeSP, (float) customerLatitude).apply();
    }

    public static double getCustomerLongitude(Context context) {
        setUpSharedPreference(context);
        return connectedCustomerInfo.getFloat(Utils.customerLongitudeSP, -1);
    }

    public static void setCustomerLongitude(double customerLongitude, Context context) {
        setUpSharedPreference(context);
        connectedCustomerInfo.edit().putFloat(Utils.customerLongitudeSP, (float) customerLongitude).apply();
    }

    public static void clear(Context context){

        setCustomerId(null, context);
        setUsername(null, context);
        setHomeAddress(null, context);
        setCustomerPhoneNumber(null, context);
        setCustomerLatitude(-1, context);
        setCustomerLongitude(-1, context);

    }

}
