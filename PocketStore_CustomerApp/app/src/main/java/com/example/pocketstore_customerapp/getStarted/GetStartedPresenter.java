package com.example.pocketstore_customerapp.getStarted;

import com.google.firebase.auth.FirebaseAuth;

public class GetStartedPresenter {

    FirebaseAuth firebaseAuth;
    GetStartedView view;

    public GetStartedPresenter(GetStartedView view,FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.view = view;
    }

    public boolean doesAuthUserExist()
    {
        if(firebaseAuth.getCurrentUser()==null)
        {
            return false;
        }
        else
        {
            return true;

        }
    }

}
