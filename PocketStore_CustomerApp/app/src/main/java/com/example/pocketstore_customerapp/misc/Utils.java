package com.example.pocketstore_customerapp.misc;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public abstract class Utils {

    /* Database nodes */

    // shops
    public static final String shopsNode = "shops";

    // nearbyShops
    public static final String nearbyShopNode = "nearbyShops";
    public static final String perimeterRadiusValueNode = "perimeterRadius";
    public static final String shopAddressValueNode = "shopAddress";
    public static final String shopLatitudeValueNode = "shopLatitude";
    public static final String shopLongitudeValueNode = "shopLongitude";
    public static final String shopNameValueNode = "shopName";
    public static final String shopPhoneNumberValueNode = "shopPhoneNumber";
    public static final String shopTypeValueNode = "shopType";
    public static final String shopStatusValueNode = "status";

    // customers
    public static final String customersNode = "customers";
    public static final String userIdNode = "userID";
    public static final String userNameNode = "username";
    public static final String emailNode = "email";
    public static final String customerPhoneNumberNode = "customerPhoneNumber";
    public static final String homeAddressNode = "homeAddress";
    public static final String customerLatitudeNode = "customerLatitude";
    public static final String customerLongitudeNode = "customerLongitude";

    // customer queue
    public static final String customerQueueNode = "customerQueue";
    public static final String shopWantsToConnectNodeValue = "shopWantsToConnect";

    // customerShopInteraction
    public static final String customerShopInteractionNode = "customerShopInteraction";
    public static final String connectedShopNode = "shop";
    public static final String connectedCustomerNode = "customer";
    public static final String shopOwnerReadyToConnectValueNode = "shopOwnerReadyToConnect";
    public static final String shopConfirmationValueNode = "shopConfirmation";
    public static final String customerConfirmationValueNode = "customerConfirmation";


    /* Shared Preference Ids */

    // logged in user info
    public static final String CUSTOMER_INFO_SHARED_PREFERENCES = "customer-info-sp";
    public static final String userIdSP = "user-id";
    public static final String userNameSP = "user-name";
    public static final String emailSP = "customer-email";
    public static final String customerPhoneNumberSP = "customer-phone-number";
    public static final String customerLatitudeSP = "customer-latitude";
    public static final String customerLongitudeSP = "customer-longitude";
    public static final String homeAddressSP = "home-address";

    // currently entered shop info
    public static final String ENTERED_SHOP_SHARED_PREFERENCES = "entered-shop-info-sp";
    public static final String shopIdSP = "shop-id";
    public static final String shopPerimeterRadiusSP = "shop-perimeter-radius";
    public static final String shopAddressSP = "shop-address";
    public static final String shopLatitudeSP = "shop-latitude";
    public static final String shopLongitudeSP = "shop-longitude";
    public static final String shopNameSP = "shop-name";
    public static final String shopPhoneNumberSP = "shop-phone-number";
    public static final String shopTypeSP = "shop-type";
    public static final String shopStatusSP = "shop-status";

    // shop types
    public static final String SHOP_CONVENTIONAL = "Conventional";
    public static final String SHOP_BAKERY = "Bakery";
    public static final String SHOP_STATIONARY = "Stationary";


    // common Alert dialog builder
    public static AlertDialog createAlertDialog(
            Context context,
            String message,
            String positiveMsg, DialogInterface.OnClickListener positiveListener,
            String negativeMsg, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveMsg, positiveListener)
                .setNegativeButton(negativeMsg, negativeListener);

        return builder.create();
    }

}
