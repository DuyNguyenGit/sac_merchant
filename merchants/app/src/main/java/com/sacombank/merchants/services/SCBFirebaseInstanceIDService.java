package com.sacombank.merchants.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sacombank.merchants.SacombankApp;

/**
 * Created by telo.nguyen on 8/15/17.
 */

public class SCBFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String TAG = SCBFirebaseInstanceIDService.class.getSimpleName();


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeRegIdInPref(refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }

    private void storeRegIdInPref(String token) {
        ((SacombankApp) getApplication()).firebaseTokenPref.setValue(token);
    }
}
