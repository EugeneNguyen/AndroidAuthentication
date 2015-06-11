package com.authentication.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by thanhhaitran on 6/3/15.
 */

public class RegisterBroadCast extends BroadcastReceiver {

    public static BroadCastCallBack callBack;

    public void setOnEventListener(BroadCastCallBack listener) {
        callBack = listener;
    }

    public interface BroadCastCallBack
    {
        public void didReicive(Intent result);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
           if (callBack != null) {
               callBack.didReicive(intent);
           }
    }
}
