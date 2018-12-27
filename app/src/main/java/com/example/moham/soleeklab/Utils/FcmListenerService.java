package com.example.moham.soleeklab.Utils;

import com.google.firebase.messaging.FirebaseMessagingService;

public class FcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "FcmListenerService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
