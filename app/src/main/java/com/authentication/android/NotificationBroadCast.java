package com.authentication.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by thanhhaitran on 6/6/15.
 */

public class NotificationBroadCast extends BroadcastReceiver {

    public static NotificationCallBack notification;

    public void setOnEventListener(NotificationCallBack listener) {
        notification = listener;
    }

    public interface NotificationCallBack
    {
        public void didGetNotification(Intent result, Context ctx);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (notification != null) {
            notification.didGetNotification(intent,context);
        }
    }
}
