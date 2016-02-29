package com.android.davidlin.rentbike.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Check network connection state
 * Created by David Lin on 2015/11/23.
 */
public class ConnectionUtils {
    public static final int CELLULAR_CONNECT = 0;
    public static final int CELLULAR_DISCONNECT = 1;
    public static final int WIFI_CONNECT = 2;
    public static final int WIFI_DISCONNECT = 3;
    public static final int NO_NETWORK = 4;

    public static int getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        int networkType, typeWifi = WIFI_DISCONNECT, typeCellular = CELLULAR_DISCONNECT;
        String networkState;
        if (cm.getActiveNetworkInfo() == null) return NO_NETWORK;
        networkType = cm.getActiveNetworkInfo().getType();
        networkState = cm.getActiveNetworkInfo().getState().toString();

        if (networkType == ConnectivityManager.TYPE_WIFI) {
            if (!"DISCONNECTED".equals(networkState))
                typeWifi = WIFI_CONNECT;
            else
                typeWifi = WIFI_DISCONNECT;
        } else {
            if (!"DISCONNECTED".equals(networkState))
                typeCellular = CELLULAR_CONNECT;
            else
                typeCellular = CELLULAR_DISCONNECT;
        }

        if (typeWifi == WIFI_CONNECT) return typeWifi;
        if (typeCellular == CELLULAR_CONNECT) return typeCellular;
        return NO_NETWORK;
    }
}
