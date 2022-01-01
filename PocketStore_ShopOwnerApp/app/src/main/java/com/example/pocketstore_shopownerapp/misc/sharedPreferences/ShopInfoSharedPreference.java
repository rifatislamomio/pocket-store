package com.example.pocketstore_shopownerapp.misc.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pocketstore_shopownerapp.misc.Utils;

public class ShopInfoSharedPreference {

    private static SharedPreferences shopInfo;

    private static void setUpSharedPreference(Context context){
        shopInfo = context.getSharedPreferences(Utils.SHOP_INFO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static String getUserID(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopIdSP, "");
    }

    public static void setUserID(String userID, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopIdSP, userID).apply();
    }

    public static String getShopownerName(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopOwnerNameSP, "");
    }

    public static void setShopownerName(String shopownerName, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopOwnerNameSP, shopownerName).apply();
    }

    public static String getShopPhoneNumber(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopPhoneNumberSP, "");
    }

    public static void setShopPhoneNumber(String shopPhoneNumber, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopPhoneNumberSP, shopPhoneNumber).apply();
    }

    public static String getShopType(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopTypeSP, "");
    }

    public static void setShopType(String shopType, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopTypeSP, shopType).apply();
    }

    public static String getShopName(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopNameSP, "");
    }

    public static void setShopName(String shopName, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopNameSP, shopName).apply();
    }

    public static String getShopAddress(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopAddressSP, "");
    }

    public static void setShopAddress(String shopAddress, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopAddressSP, shopAddress).apply();
    }

    public static double getShopLatitude(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getFloat(Utils.shopLatitudeSP, -1);
    }

    public static void setShopLatitude(double shopLatitude, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putFloat(Utils.shopLatitudeSP, (float) shopLatitude).apply();
    }

    public static double getShopLongitude(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getFloat(Utils.shopLongitudeSP, -1);
    }

    public static void setShopLongitude(double shopLongitude, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putFloat(Utils.shopLongitudeSP, (float) shopLongitude).apply();
    }

    public static double getPerimeterRadius(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getFloat(Utils.shopPerimeterRadiusSP, 100);
    }

    public static void setPerimeterRadius(double perimeterRadius, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putFloat(Utils.shopPerimeterRadiusSP, (float) perimeterRadius).apply();
    }

    public static String getStatus(Context context) {
        setUpSharedPreference(context);
        return shopInfo.getString(Utils.shopStatusSP, "");
    }

    public static void setStatus(String status, Context context) {
        setUpSharedPreference(context);
        shopInfo.edit().putString(Utils.shopStatusSP, status).apply();
    }

    public static void clear(Context context){

        setUserID(null, context);
        setShopName(null, context);
        setShopownerName(null, context);
        setShopAddress(null, context);
        setShopPhoneNumber(null, context);
        setStatus(null, context);
        setShopType(null, context);
        setShopLongitude(-1, context);
        setShopLatitude(-1, context);
        setPerimeterRadius(-1, context);

    }

}
