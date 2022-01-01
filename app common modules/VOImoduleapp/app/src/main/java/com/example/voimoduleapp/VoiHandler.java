package com.example.voimoduleapp;

import com.sinch.android.rtc.calling.Call;

import java.io.Serializable;

public interface VoiHandler {

    void setUpClient();
    void terminateClient();
    void callUser(String callRecipient);
    void hangUpCall();
    void answerIncomingCall();

    void setCallClientSetupObserver(CallClientSetupObserver callClientSetupObserver);
    void setCallObserver(CallObserver callObserver);

    // interface for class/presenter/activity who instantiated VoiHandler
    interface CallClientSetupObserver {

        void onClientSetupDone();
        void onClientStopped();
        void onClientSetupFailed(String errorMessage);

    }

    interface CallObserver {

        void onCallerCalling();
        void onCallConnected(String callerId);
        void onCallDisconnected();
        void onCallIncoming(String callerId);

        void onSuccess(String message);
        void onFailed(String message);

    }



}
