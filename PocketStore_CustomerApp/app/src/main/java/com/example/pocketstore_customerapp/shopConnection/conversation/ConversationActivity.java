package com.example.pocketstore_customerapp.shopConnection.conversation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customerqueue_module.CustomerWaitingInQueue;
import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.home.NearbyShop;
import com.example.pocketstore_customerapp.misc.Utils;
import com.example.pocketstore_customerapp.misc.abstractModels.Shop;
import com.example.pocketstore_customerapp.misc.sharedPreferences.EnteredShopSharedPreferences;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.example.uicomponents_module.CustomProgressDialog;
import com.example.voimoduleapp.VoiHandler;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class ConversationActivity extends AppCompatActivity implements ConversationView {

    // the api client set up object for app-to-app call
    private static VoiHandler VOI_HANDLER;
    public static void setVoiHandler(VoiHandler voiHandler) {
        // MUST be setup by the parent activity
        VOI_HANDLER = voiHandler;
    }

    // presenter
    private ConversationPresenter conversationPresenter;

    // ui
    private AlertDialog hangUpWithoutConfirmationAD, hangUpWithConfirmationAD;
    private TextView callConnectingShopNameTV, onCallShopNameTV;
    private ImageView callConnectingShopIconIV, onCallShopIconIV;
    private ImageView speakerImgView;
    // audio manager to set/unset speaker
    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_call_connecting_view);

        // must be called before initUI(), because UI uses data from models of presented
        init();

        initUI();

    }

    private void init() {

        Conversation conversation = getConnectedCustomerShopInfoFromLocalStorage();

        String dbPath = Utils.customerShopInteractionNode+"/"+
                conversation.getCustomer().getCustomerId()+conversation.getShop().getShopID();

        conversationPresenter = new ConversationPresenter(
                this,
                VOI_HANDLER, conversation,
                new ConversationDatabaseHandler(
                        FirebaseDatabase.getInstance().getReference().child(dbPath)
                )
        );

        // make call immediately
        conversationPresenter.makeCall();

    }

    private Conversation getConnectedCustomerShopInfoFromLocalStorage() {

        // get user
        String customerId = UserInfoSharedPreferences.getUserID(getApplicationContext());
        String customerUsername = UserInfoSharedPreferences.getUsername(getApplicationContext());
        String customerPhoneNumber = UserInfoSharedPreferences.getCustomerPhoneNumber(getApplicationContext());
        String homeAddress = UserInfoSharedPreferences.getHomeAddress(getApplicationContext());
        double customerLatitude = UserInfoSharedPreferences.getCustomerLatitude(getApplicationContext());
        double customerLongitude = UserInfoSharedPreferences.getCustomerLongitude(getApplicationContext());

        CustomerWaitingInQueue customer = new CustomerWaitingInQueue(
                customerId, customerUsername, customerPhoneNumber,
                homeAddress, customerLatitude, customerLongitude);

        // get entered shop
        String shopId = EnteredShopSharedPreferences.getShopID(getApplicationContext());
        String shopName = EnteredShopSharedPreferences.getShopName(getApplicationContext());
        String shopType = EnteredShopSharedPreferences.getShopType(getApplicationContext());
        String shopPhoneNumber = EnteredShopSharedPreferences.getShopPhoneNumber(getApplicationContext());
        String shopAddress = EnteredShopSharedPreferences.getShopAddress(getApplicationContext());
        double shopLatitude = EnteredShopSharedPreferences.getShopLatitude(getApplicationContext());
        double shopLongitude = EnteredShopSharedPreferences.getShopLongitude(getApplicationContext());
        String shopStatus = EnteredShopSharedPreferences.getStatus(getApplicationContext());

        Shop shop = new NearbyShop(shopId, shopName, shopType, shopPhoneNumber,
                shopAddress, shopLatitude, shopLongitude, shopStatus);

        return new Conversation(shop, customer);

    }

    private void initUI() {

        hangUpWithoutConfirmationAD = Utils.createAlertDialog(
                this,
                "Do you want to hang up discarding the order?",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        conversationPresenter.hangUpCall();
                        finish();
                    }
                },
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        hangUpWithConfirmationAD = Utils.createAlertDialog(
                this,
                "Are you sure you want to confirm the order?",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(conversationPresenter.getConversation().isShopConfirmation()) {
                            conversationPresenter.hangUpCall();
                            finish();
                        }

                        else
                            Toasty.warning(ConversationActivity.this, "shop hasn't confirmed yet!").show();

                    }
                },
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        callConnectingShopNameTV = findViewById(R.id.callConnectingShopNameTV);
        callConnectingShopIconIV = findViewById(R.id.callConnectingShopIconIV);

        setupCallConnectingView();

    }


    @Override
    public void onBackPressed() {
        hangUpWithoutConfirmationAD.show();
    }

    @Override
    protected void onDestroy() {

        conversationPresenter.onDestroy();

        super.onDestroy();
    }

    @Override
    public void onCallOutgoingUI() {


    }

    private void setupCallConnectingView() {
        callConnectingShopNameTV.setText( conversationPresenter.getConversation().getShop().getShopName() );

        String shopType = conversationPresenter.getConversation().getShop().getShopType();
        switch (shopType){
            case Utils.SHOP_CONVENTIONAL:
                callConnectingShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_conventional_v2));
                break;

            case Utils.SHOP_STATIONARY:
                callConnectingShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_stationary_v2));
                break;

            case Utils.SHOP_BAKERY:
                callConnectingShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_bakery_v2));
                break;
        }
    }

    @Override
    public void onCallConnectedUI() {

        setupOnCallView();

        // set phone volume up-down button to control call volume (default is media volume)
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        // set speaker on
        audioManager.setSpeakerphoneOn(true);

        Toasty.success(this, "call connected!").show();

    }

    private void setupOnCallView() {
    // change the ui from connecting to connected

        // call connected view
        setContentView(R.layout.activity_conversation_on_call_view);

        onCallShopNameTV = findViewById(R.id.onCallShopNameTV);
        onCallShopIconIV = findViewById(R.id.onCallShopIconIV);

        onCallShopNameTV.setText( conversationPresenter.getConversation().getShop().getShopName() );

        String shopType = conversationPresenter.getConversation().getShop().getShopType();
        switch (shopType){
            case Utils.SHOP_CONVENTIONAL:
                onCallShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_conventional_v2));
                break;

            case Utils.SHOP_STATIONARY:
                onCallShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_stationary_v2));
                break;

            case Utils.SHOP_BAKERY:
                onCallShopIconIV.setImageDrawable(getDrawable(R.drawable.ic_bakery_v2));
                break;
        }

    }

    @Override
    public void onCallDisconnectedUI() {

	if(conversationPresenter.getConversation().isCustomerConfirmation() && conversationPresenter.getConversation().isShopConfirmation())
            Toasty.success(this, "order placed!").show();
        else
            Toasty.error(this, "call ended").show();

        // reset Audio controls
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        audioManager.setSpeakerphoneOn(false);


        finish();

    }

    @Override
    public void onErrorUI(String message) {

        Toasty.error(this, message);

        // finish here?
        finish();

    }


    // onClick listeners
    public void endCallClick(View view) {

        if(conversationPresenter.getConversation().isCustomerConfirmation())
            hangUpWithConfirmationAD.show();
        else
            hangUpWithoutConfirmationAD.show();

    }

    public void speakerClick(View view) {

        if(audioManager==null)
            return;
        if(speakerImgView==null)
            speakerImgView = findViewById(R.id.speaker_imageView);

        if(audioManager.isSpeakerphoneOn())
        {
            speakerImgView.setImageDrawable(getDrawable(R.drawable.ic_baseline_mute));
        }
        else
        {
            speakerImgView.setImageDrawable(getDrawable(R.drawable.ic_loud_speaker));
        }

        audioManager.setSpeakerphoneOn(!audioManager.isSpeakerphoneOn());

    }

    public void confirmCheckboxClicked(View view) {

        boolean confirmation = ((CheckBox) view).isChecked();

        conversationPresenter.updateCustomerConfirmation(confirmation);

    }
}
