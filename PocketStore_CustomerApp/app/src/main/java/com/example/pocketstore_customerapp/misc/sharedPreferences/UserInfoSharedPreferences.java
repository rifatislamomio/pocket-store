package com.example.pocketstore_customerapp.misc.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pocketstore_customerapp.misc.Utils;

public class UserInfoSharedPreferences {
// locally stores customer info in shared preferences

    private static SharedPreferences userInfo;

    private static void setUpSharedPreference(Context context){
        userInfo = context.getSharedPreferences(Utils.CUSTOMER_INFO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }


    public static String getUserID(Context context) {
        setUpSharedPreference(context);
        return userInfo.getString(Utils.userIdSP, "");
    }

    public static void setUserID(String userID, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putString(Utils.userIdSP, userID).apply();
    }

    public static String getUsername(Context context) {
        setUpSharedPreference(context);
        return userInfo.getString(Utils.userNameSP, "");
    }

    public static void setUsername(String username, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putString(Utils.userNameSP, username).apply();
    }

    public static String getCustomerPhoneNumber(Context context) {
        setUpSharedPreference(context);
        return userInfo.getString(Utils.customerPhoneNumberSP, "");
    }

    public static void setCustomerPhoneNumber(String customerPhoneNumber, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putString(Utils.customerPhoneNumberSP, customerPhoneNumber).apply();
    }

    public static String getHomeAddress(Context context) {
        setUpSharedPreference(context);
        return userInfo.getString(Utils.homeAddressSP, "");
    }

    public static void setHomeAddress(String homeAddress, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putString(Utils.homeAddressSP, homeAddress).apply();
    }

    public static String getEmail(Context context) {
        setUpSharedPreference(context);
        return userInfo.getString(Utils.emailSP, "");
    }

    public static void setEmail(String email, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putString(Utils.emailSP, email).apply();
    }

    public static double getCustomerLatitude(Context context) {
        setUpSharedPreference(context);
        return userInfo.getFloat(Utils.customerLatitudeSP, -1);
    }

    public static void setCustomerLatitude(double customerLatitude, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putFloat(Utils.customerLatitudeSP, (float) customerLatitude).apply();
    }

    public static double getCustomerLongitude(Context context) {
        setUpSharedPreference(context);
        return userInfo.getFloat(Utils.customerLongitudeSP, -1);
    }

    public static void setCustomerLongitude(double customerLongitude, Context context) {
        setUpSharedPreference(context);
        userInfo.edit().putFloat(Utils.customerLongitudeSP, (float) customerLongitude).apply();
    }

    public void clear(Context context){
        // clear out all the shared preference data

        setCustomerLatitude(0.0, context);
        setCustomerLongitude(0.0, context);
        setCustomerPhoneNumber(null, context);
        setEmail(null, context);
        setHomeAddress(null, context);
        setUserID(null, context);
        setUsername(null, context);

    }
}
