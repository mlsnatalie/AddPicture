package com.ytx.appframework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

/**
 * Created by fangcan on 2017/8/31.
 */

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private NetworkInfo networkInfo;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            networkInfo = null;
            onNetworkDisconnected();
            Log.d("network", " network disconnected");
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return ;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            int type = networkInfo.getType();
            if (networkInfo.isConnected()) {
                handler.removeCallbacks(runnable);
                if (this.networkInfo == null || this.networkInfo.getType() != networkInfo.getType()) {
                    this.networkInfo = networkInfo;
                    onNetworkConnected(type);
                    Log.d("network", " network connected: type  " + type);
                }
            } else {
                Log.d("network", " network connecting: type " + type);
            }
        } else {
            handler.postDelayed(runnable, 1000);
        }
    }

    protected void onNetworkConnected(int networkType) {

    }

    protected void onNetworkDisconnected() {

    }
}
