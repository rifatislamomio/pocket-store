package com.example.pocketstore_shopownerapp.misc;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    /* Database nodes */

    // shops
    public static String shopsNode = "shops";
    public static String shopIdValueNode = "userID";
    public static String perimeterRadiusValueNode = "perimeterRadius";
    public static String shopAddressValueNode = "shopAddress";
    public static String shopLatitudeValueNode = "shopLatitude";
    public static String shopLongitudeValueNode = "shopLongitude";
    public static String shopNameValueNode = "shopName";
    public static String shopPhoneNumberValueNode = "shopPhoneNumber";
    public static String shopTypeValueNode = "shopType";
    public static String shopOwnerNameValueNode = "shopownerName";
    public static String shopStatusValueNode = "status";

    // customerInQueue
    public static String customerQueueNode = "customerQueue";
    public static String customerIdValueNode = "customerID";
    public static String usernameValueNode = "username";
    public static String homeAddressValueNode = "homeAddress";
    public static String customerLatitudeValueNode = "customerLatitude";
    public static String customerLongitudeValueNode = "customerLongitude";
    public static String customerPhoneNumberValueNode = "customerPhoneNumber";
    public static String shopWantsToConnectValueNode = "shopWantsToConnect";

    // customerShopInteraction
    public static String customerShopInteractionNode = "customerShopInteraction";
    public static String connectedShopNode = "shop";
    public static String connectedCustomerNode = "customer";
    public static String shopOwnerReadyToConnectValueNode = "shopOwnerReadyToConnect";
    public static String shopConfirmationValueNode = "shopConfirmation";
    public static String customerConfirmationValueNode = "customerConfirmation";



    /* Shared Preference Ids */

    // logged in user info
    public static String SHOP_INFO_SHARED_PREFERENCES = "shop-info-sp";
    public static String shopIdSP = "shop-id";
    public static String shopNameSP = "shop-name";
    public static String shopOwnerNameSP = "shop-owner-name";
    public static String shopTypeSP = "shop-type";
    public static String shopPhoneNumberSP = "shop-phone-number";
    public static String shopLatitudeSP = "shop-latitude";
    public static String shopLongitudeSP = "shop-longitude";
    public static String shopAddressSP = "shop-address";
    public static String shopPerimeterRadiusSP = "shop-perimeter-radius";
    public static String shopStatusSP = "shop-status";

    // connected customer info
    public static String CUSTOMER_INFO_SHARED_PREFERENCES = "customer-info-sp";
    public static String customerIdSP = "customer-id";
    public static String customerNameSP = "customer-name";
    public static String customerPhoneNumberSP = "customer-phone-number";
    public static String customerLatitudeSP = "customer-latitude";
    public static String customerLongitudeSP = "customer-longitude";
    public static String customerAddressSP = "customer-address";



    // internet availability check
    public static boolean isInternetConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!= null && activeNetwork.isConnectedOrConnecting();
    }

    // alert dialog
    public static AlertDialog createAlertDialogBuilder(
            Context context,
            String message,
            String positiveMsg, DialogInterface.OnClickListener positiveListener,
            String negativeMsg, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false);

        if(positiveListener!=null)
            builder.setPositiveButton(positiveMsg, positiveListener);

        if(negativeListener!=null)
            builder.setNegativeButton(negativeMsg, negativeListener);

        return builder.create();
    }

}
