package com.authentication.android;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    public GCMIntentService() {
       super();
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        Intent in = new Intent();
        in.setAction("com.Authentication.BroadCast");
        in.putExtra("device_token", registrationId);
        sendBroadcast(in);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.e("", "unregistered");
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

//        Intent in = new Intent();
//        in.setAction("com.Authentication.BroadCast");
//        in.putExtra("onmessage",intent);
//        sendBroadcast(in);

        Intent notification = new Intent();
        notification.setAction("com.Authentication.Notification");
        notification.putExtra("onmessage",intent);
        sendBroadcast(notification);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {

        Intent in = new Intent();
        in.setAction("com.Authentication.BroadCast");
        in.putExtra("ondeletemessage","message deleted");
        sendBroadcast(in);
    }

    @Override
    public void onError(Context context, String errorId) {

        Intent in = new Intent();
        in.setAction("com.Authentication.BroadCast");
        sendBroadcast(in);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

        Intent in = new Intent();
        in.setAction("com.Authentication.BroadCast");
        sendBroadcast(in);
        return super.onRecoverableError(context, errorId);
    }
}
