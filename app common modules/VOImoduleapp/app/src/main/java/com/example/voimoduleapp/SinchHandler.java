package com.example.voimoduleapp;

import android.content.Context;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class SinchHandler implements VoiHandler {

    private String TAG = "debug-client";

    // observe client setup completion
    private CallClientSetupObserver callClientSetupObserver;
    // observe call events
    private CallObserver callObserver;

    // activity/service etc
    private Context context;
    // my username
    private String myUsername;

    // api client
    private SinchClient sinchClient = null;
    private boolean isClientStarted = false;
    private SinchClientListener sinchClientListener = new SinchClientListener() {
        // save sinchClient state

        @Override
        public void onClientStarted(SinchClient sinchClient) {

            callClientSetupObserver.onClientSetupDone();

            isClientStarted = true;
            Log.d(TAG, "onClientStarted: client setup successful!");
        }

        @Override
        public void onClientStopped(SinchClient sinchClient) {

            callClientSetupObserver.onClientStopped();

            isClientStarted = false;
            Log.d(TAG, "onClientStopped: client stopped!");
        }

        @Override
        public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {

            callClientSetupObserver.onClientSetupFailed(sinchError.getMessage());

            Log.d(TAG, "onClientFailed: client setup failed = "+sinchError.getMessage());
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

            callClientSetupObserver.onClientSetupFailed("issue with client registration");

            Log.d(TAG, "onRegistrationCredentialsRequired: issue with client registration");
        }

        @Override
        public void onLogMessage(int i, String s, String s1) {

        }
    };

    // outgoing call
    private Call call = null;
    // incoming/outgoing call listener
    private CallListener callListener = new CallListener() {
        @Override
        public void onCallProgressing(Call call) {
            // Ringing... (called only from caller's end)
            callObserver.onCallerCalling();
        }

        @Override
        public void onCallEstablished(Call call) {
            // call connected
            callObserver.onSuccess("call successfully connected!"); // hudai

            callObserver.onCallConnected(call.getRemoteUserId());

        }

        @Override
        public void onCallEnded(Call call) {
            // call ended
            callObserver.onSuccess("call successfully disconnected.");

            callObserver.onCallDisconnected();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            // i don no
        }
    };
    // incoming call listener
    private CallClientListener callClientListener = new CallClientListener() {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {

            SinchHandler.this.call = incomingCall;
            callObserver.onCallIncoming(incomingCall.getRemoteUserId());
        }
    };


    public SinchHandler(String myUsername,Context context, CallClientSetupObserver callClientSetupObserver){
        // constructor has setup client observer only

        this.callClientSetupObserver = callClientSetupObserver;
        this.context = context;
        this.myUsername = myUsername;

    }

    @Override
    public void setUpClient(){

        if(sinchClient!=null && isClientStarted){
            Log.d(TAG, "setUpClient: client already exists");
            return; // already an instance exists
        }

        //TODO: hide API-key & Secret
        this.sinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(myUsername)
                .applicationKey("aa432eb4-bb59-48e8-8c18-206c01ec8a1e")
                .applicationSecret("whuDJZjaZk6/hiqxwnXR4A==")
                .environmentHost("clientapi.sinch.com")
                .build();

        // set up client listener
        sinchClient.addSinchClientListener(this.sinchClientListener);
        // allow sinchClient to make calls
        sinchClient.setSupportCalling(true);
        // start the client
        sinchClient.start();
        // allow sinchClient to receive incoming calls
        sinchClient.startListeningOnActiveConnection();
        // set listener for incoming calls
        sinchClient.getCallClient().addCallClientListener(this.callClientListener);
    }

    @Override
    public void terminateClient(){

        if(sinchClient==null || !isClientStarted) {
            Log.d(TAG, "terminateClient: no client started");
            return; // client not started
        }

        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
        sinchClient = null;

    }

    @Override
    public void callUser(String callReceipient) {
        if(!isClientStarted){
            callObserver.onFailed("call failed! client not started");
            Log.d(TAG, "callUser: client not started!");
            return;
        }

        call = sinchClient.getCallClient().callUser(callReceipient);
        call.addCallListener(this.callListener);
    }

    @Override
    public void answerIncomingCall() {
        if(call == null) {
            callObserver.onFailed("failed to answer incoming call! why?");
            Log.d(TAG, "answerIncomingCall: answering incoming call failed!");
            return;
        }

        call.answer();
        call.addCallListener(this.callListener);
    }

    @Override
    public void hangUpCall() {
        if(call==null)
            return;

        call.hangup();
        call = null;

    }

    @Override
    public void setCallClientSetupObserver(CallClientSetupObserver callClientSetupObserver) {
        this.callClientSetupObserver = callClientSetupObserver;
    }

    @Override
    public void setCallObserver(CallObserver callObserver) {
        this.callObserver = callObserver;
    }
}
