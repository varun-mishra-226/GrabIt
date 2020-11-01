package com.example.grabit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YourBroadcastReceiver extends BroadcastReceiver {

    DatabaseReference mDatabaseCustomer;
    FirebaseDatabase database;

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .remove("stepCount").commit();
//        MediaPlayer mediaPlayer = MediaPlayer.create(context,
//                Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
    }
}
