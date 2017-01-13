package com.mapbox.services.android.telemetry.connectivity;

/**
 * Callback to use with the ConnectivityReceiver
 */

public interface ConnectivityListener {

  void onConnectivityChanged(boolean connected);

}
