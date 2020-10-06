package com.example.grabit;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class YourBroadcastReceiver {
    public void onReceive (Context context, Intent intent) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("stepCount").commit();
    }
}
