package com.android.davidlin.rentbike.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.davidlin.rentbike.RentBike;
import com.android.davidlin.rentbike.utils.ConnectionUtils;

/**
 * Receive the network changing broadcast
 * Created by David Lin on 2015/11/23.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        RentBike.networkState = ConnectionUtils.getNetworkState(context);
        Log.d(TAG, "Network state has changed");
    }
}
