package com.example.findus.findme.notifications;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by nombu on 4/27/17.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyInstanceIDService";

    @Override
    public void onTokenRefresh() {

        Log.d(TAG, "Refreshing GCM Registration Token");

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
};