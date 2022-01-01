package com.example.pocketstore_shopownerapp.customerConnection.conversation;

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
import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.customerConnection.callConnecting.ConnectedShop;
import com.example.pocketstore_shopownerapp.misc.Utils;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ConnectedCustomerSharedPreference;
import com.example.pocketstore_shopownerapp.misc.sharedPreferences.ShopInfoSharedPreference;
import com.example.uicomponents_module.CustomProgressDialog;
import com.example.voimoduleapp.VoiHandler;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class ConversationActivity extends AppCompatActivity implements ConversationView {
// Activity for conversation with customer & order list generation

    // need the sinch api client that was setup on parent activity
    private static VoiHandler VOI_HANDLER;
    public static void setVoiHandler(VoiHandler voiHandler) {
        // MUST be setup before starting this activity
        ConversationActivity.VOI_HANDLER = voiHandler;
    }


    // presenter
    private ConversationPresenter conversationPresenter;

    // ui
    private AlertDialog hangUpWithoutConfirmation, hangUpWithConfirmation;
    private TextView callConnectingCutomerNameTV, onCallCutomerNameTV;
    private ImageView speakerImgView;
    // for audio controlling
    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_call_connecting_view);

        // MUST be called before intiUI(), ui uses data from models of presenter
        init();

        initUI();

    }

    private void initUI() {

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        hangUpWithoutConfirmation = Utils.createAlertDialogBuilder(
                this,
                "Do you want to hangup and cancel the order?",
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

        hangUpWithConfirmation = Utils.createAlertDialogBuilder(
                this,
                "Are you sure you want to confirm the order?",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(conversationPresenter.getConversation().isCustomerConfirmed()){
                            conversationPresenter.hangUpCall();
                            finish();
                        }
                        else
                            Toasty.warning(ConversationActivity.this, "customer has not confirmed yet!")
                                    .show();

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

        speakerImgView = findViewById(R.id.speaker_imageView);
        callConnectingCutomerNameTV = findViewById(R.id.callConnectingCustomerNameTV);
        callConnectingCutomerNameTV.setText(conversationPresenter.getConversation().getCustomer().getUsername());

    }

    private void init(){
    // initialize the presenter

        // load model from database
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

    }

    private Conversation getConnectedCustomerShopInfoFromLocalStorage() {

        // get user
        String customerId = ConnectedCustomerSharedPreference.getCustomerId(getApplicationContext());
        String customerUsername = ConnectedCustomerSharedPreference.getUsername(getApplicationContext());
        String customerPhoneNumber = ConnectedCustomerSharedPreference.getCustomerPhoneNumber(getApplicationContext());
        String homeAddress = ConnectedCustomerSharedPreference.getHomeAddress(getApplicationContext());
        double customerLatitude = ConnectedCustomerSharedPreference.getCustomerLatitude(getApplicationContext());
        double customerLongitude = ConnectedCustomerSharedPreference.getCustomerLongitude(getApplicationContext());

        CustomerWaitingInQueue customer = new CustomerWaitingInQueue(
                customerId, customerUsername, customerPhoneNumber,
                homeAddress, customerLatitude, customerLongitude);

        // get entered shop
        String shopId = ShopInfoSharedPreference.getUserID(getApplicationContext());
        String shopName = ShopInfoSharedPreference.getShopName(getApplicationContext());
        String shopType = ShopInfoSharedPreference.getShopType(getApplicationContext());
        String shopPhoneNumber = ShopInfoSharedPreference.getShopPhoneNumber(getApplicationContext());
        String shopAddress = ShopInfoSharedPreference.getShopAddress(getApplicationContext());
        double shopLatitude = ShopInfoSharedPreference.getShopLatitude(getApplicationContext());
        double shopLongitude = ShopInfoSharedPreference.getShopLongitude(getApplicationContext());
        String shopStatus = ShopInfoSharedPreference.getStatus(getApplicationContext());

        ConnectedShop shop = new ConnectedShop(shopId, shopName, shopPhoneNumber, shopType,
                shopAddress, shopLatitude, shopLongitude);

        return new Conversation(shop, customer);

    }


    @Override
    public void onBackPressed() {
        hangUpWithoutConfirmation.show();
    }

    @Override
    protected void onDestroy() {
        conversationPresenter.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onCallConnectedUI() {

        // set phone volume up-down button to control call volume (default is media volume)
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        // set speaker on
        audioManager.setSpeakerphoneOn(true);

        setUpOnCallView();

        Toasty.success(this, "Call connected!").show();

    }

    private void setUpOnCallView() {
    // show call connected view

        setContentView(R.layout.activity_conversation_on_call_view);

        onCallCutomerNameTV = findViewById(R.id.onCallCustomerNameTV);
        onCallCutomerNameTV.setText(conversationPresenter.getConversation().getCustomer().getUsername());
    }

    @Override
    public void onCallDisconnectedUI() {

        if(conversationPresenter.getConversation().isShopConfirmed() && conversationPresenter.getConversation().isCustomerConfirmed()) {
            Toasty.success(this, "order placed!").show();

            removeThisToTrigger();
        }
        else
            Toasty.error(this, "call ended").show();

        // reset audio settings
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        audioManager.setSpeakerphoneOn(false);

        finish();

    }

    private void removeThisToTrigger() {
        //TODO: do this in trigger!!!! NOT HERE
        conversationPresenter.getConversationDatabaseHandler().removeNode();
        FirebaseDatabase.getInstance().getReference().child( "shops/"+ShopInfoSharedPreference.getUserID(getApplicationContext())+"/pendingOrders")
                .push().setValue(conversationPresenter.getConversation().getCustomer());
    }

    @Override
    public void onErrorUI(String message) {

        Toasty.error(this, message).show();
        conversationPresenter.hangUpCall();

    }

    public void endCallClick(View view) {

        if(conversationPresenter.getConversation().isShopConfirmed())
            hangUpWithConfirmation.show();
        else
            hangUpWithoutConfirmation.show();

    }

    public void confirmClick(View view) {

        boolean confirmation = ((CheckBox) view).isChecked();

        conversationPresenter.updateShopConfirmation(confirmation);

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
}