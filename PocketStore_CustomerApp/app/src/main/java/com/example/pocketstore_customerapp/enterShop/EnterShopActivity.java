package com.example.pocketstore_customerapp.enterShop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.customerqueue_module.CustomerQueueFragment;
import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.enterShop.chatFeed.ChatFragment;
import com.example.pocketstore_customerapp.misc.Utils;
import com.example.pocketstore_customerapp.misc.sharedPreferences.EnteredShopSharedPreferences;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.example.pocketstore_customerapp.shopConnection.callConnecting.CallConnectingActivity;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class EnterShopActivity extends AppCompatActivity implements EnteredShopView{

    private static final String TAG = "ESA-debug";

    // presenter
    private EnteredShopPresenter enteredShopPresenter;

    // ui
    private AlertDialog shopOwnerInactiveAlertDialog, backPressAlertDialog;
    private TextView shopNameTitleTV;
    private Button enterQBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_shop);

        // setup presenter
        init();
        // uses model info from presenter must be called after presenter is set
        initUI();

    }

    private void init() {
        // initialize presenter

        // load the shop infos from shared preference manager
        EnteredShop enteredShop = getEnteredShop();
        Log.d(TAG, "init: entered shop -> "+enteredShop.toString());

        // load the customer infos from shared preferences
        CustomerWaitingInQueue user = getUser();
        Log.d(TAG, "init: user -> "+user.toString());

        String dbPathShopToCustomerQueue = Utils.shopsNode
                + "/" + enteredShop.getShopID()
                + "/" + Utils.customerQueueNode;

        String dbPathToShopStatus = Utils.shopsNode + "/" +enteredShop.getShopID() + "/" + Utils.shopStatusValueNode;

        enteredShopPresenter = new EnteredShopPresenter(
                this,
                enteredShop,
                user,
                new EnteredShopDatabaseHandler(
                        FirebaseDatabase.getInstance().getReference().child(dbPathShopToCustomerQueue),
                        FirebaseDatabase.getInstance().getReference().child(dbPathToShopStatus)
                )
        );

    }

    private CustomerWaitingInQueue getUser() {

        String customerId = UserInfoSharedPreferences.getUserID(getApplicationContext());
        String username = UserInfoSharedPreferences.getUsername(getApplicationContext());
        String customerPhoneNumber = UserInfoSharedPreferences.getCustomerPhoneNumber(getApplicationContext());
        String customerHomeAddress = UserInfoSharedPreferences.getHomeAddress(getApplicationContext());
        double customerLatitude = UserInfoSharedPreferences.getCustomerLatitude(getApplicationContext());
        double customerLongitude = UserInfoSharedPreferences.getCustomerLongitude(getApplicationContext());

        return new CustomerWaitingInQueue(
                customerId, username, customerPhoneNumber, customerHomeAddress, customerLatitude, customerLongitude);

    }

    private EnteredShop getEnteredShop() {

        String shopID = EnteredShopSharedPreferences.getShopID(getApplicationContext());
        String shopName = EnteredShopSharedPreferences.getShopName(getApplicationContext());
        String shopType = EnteredShopSharedPreferences.getShopType(getApplicationContext());
        String shopPhoneNumber = EnteredShopSharedPreferences.getShopPhoneNumber(getApplicationContext());
        String shopAddress = EnteredShopSharedPreferences.getShopAddress(getApplicationContext());
        double shopLatitude = EnteredShopSharedPreferences.getShopLatitude(getApplicationContext());
        double shopLongitude = EnteredShopSharedPreferences.getShopLongitude(getApplicationContext());
        String status = EnteredShopSharedPreferences.getStatus(getApplicationContext());

        return new EnteredShop( shopID, shopName, shopType, shopPhoneNumber,
                shopAddress, shopLatitude, shopLongitude, status );

    }


    private void initUI() {
        // initialize all UI components

        enterQBtn = findViewById(R.id.enterQueueBtn);
        shopNameTitleTV = findViewById(R.id.shop_name_text_view);
        shopNameTitleTV.setText(enteredShopPresenter.getEnteredShop().getShopName());

        // initial fragment is the customer queue
        loadCustomerQueueFragment();

        // shopOwner inactive alert dialog
        shopOwnerInactiveAlertDialog = Utils.createAlertDialog(
                this,
                "shop owner left",
                "Wait",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },
                "Leave Now",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enteredShopPresenter.removeUserFromQueue();
                        enteredShopPresenter.onDestroy();
                        dialog.dismiss();
                        finish();
                    }
                });

        // user pressed back alert dialog
        backPressAlertDialog = Utils.createAlertDialog(
                this,
                "Are you sure you want to leave the shop?",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enteredShopPresenter.removeUserFromQueue();
                        enteredShopPresenter.onDestroy();
                        dialog.dismiss();
                        finish();
                    }
                },
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void loadCustomerQueueFragment() {
        // show the customer queue fragment in frameLayout

        // MUST set db path to CustomerQ otherwise fragment won't open
        String customerQueueDbPath = Utils.shopsNode +
                "/" + enteredShopPresenter.getEnteredShop().getShopID() +
                "/customerQueue";
        CustomerQueueFragment.setDbNodePath(customerQueueDbPath);

        CustomerQueueFragment fragment = new CustomerQueueFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.cqueueframelayout,fragment);
        fragmentTransaction.commit();
    }

    private void loadChatFragment()
    {
        ChatFragment chatFragment = new ChatFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.cqueueframelayout,chatFragment);
    }


    @Override
    protected void onDestroy() {
        enteredShopPresenter.removeUserFromQueue();
        // must remove any hanging db listeners
        enteredShopPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backPressAlertDialog.show();
    }


    @Override
    public void onShopOwnerInactiveUI() {
        shopOwnerInactiveAlertDialog.show();
    }

    @Override
    public void onShopOwnerActiveUI() {
        shopOwnerInactiveAlertDialog.hide();
        Toasty.success(this, "shop owner is online now!").show();
    }

    @Override
    public void onUserAddedToQueueUI() {

        // attach db listeners
        enteredShopPresenter.registerDbListeners();

        Toasty.info(this, "You are added to the queue!").show();

        enterQBtn.setVisibility(View.GONE);

    }

    @Override
    public void onShopWantsToConnectUI() {

        Toasty.success(this, "shop owner is ready for you now!").show();

        startActivity(new Intent(this, CallConnectingActivity.class));

        // can't come back to EnterShopActivity after call initiation
        enteredShopPresenter.removeUserFromQueue();
        enteredShopPresenter.onDestroy();
        finish();

    }

    @Override
    public void onErrorUI(String errorMessage) {
        Toasty.error(this, errorMessage).show();
    }


    public void enterQueueClicked(View view) {
        // enter queue button press event

        enteredShopPresenter.insertUserToQueue();
    }
}