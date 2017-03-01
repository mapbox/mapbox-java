package com.mapbox.services.android.telemetry.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.UiThread;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ConnectivityReceiver is a BroadcastReceiver that helps you keep track of the connectivity
 * status. When used statically (getSystemConnectivity) the ConnectivityReceiver will always
 * return the connectivity status as reported by the Android system.
 *
 * When instantiating ConnectivityReceiver, you have the option to set a connectedFlag. You can
 * override the connectivity value reported by the system by setting this flag to true or false. If
 * left in its default value (null), ConnectivityReceiver will report the system value.
 *
 * ConnectivityReceiver also lets you subscribe to connecitivity changes using a
 * ConnectivityListener.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

  private Context context;
  private CopyOnWriteArrayList<ConnectivityListener> connectivityListeners;
  private Boolean connectedFlag;
  private int activationCounter;

  /**
   * ConnectivityReceiver constructor
   *
   * @param context Android context. To avoid memory leaks, you might want to pass the application
   *                context and make sure you call removeConnectivityUpdates() when you don't need
   *                further updates (https://github.com/mapbox/mapbox-gl-native/issues/7176)
   */
  public ConnectivityReceiver(Context context) {
    this.context = context;
    connectivityListeners = new CopyOnWriteArrayList<>();
    connectedFlag = null;
  }

  /**
   * Get the connectivity state as reported by the Android system
   *
   * @param context Android context
   * @return the connectivity state as reported by the Android system
   */
  private static boolean getSystemConnectivity(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  /**
   * Get the connectivity state. This can be overriden using the connectedFlag.
   *
   * @return the connectivity state
   */
  private boolean getManagedConnectivity() {
    if (connectedFlag == null) {
      return getSystemConnectivity(context);
    }

    return connectedFlag;
  }

  /**
   * Get the connectivity state as reported by the Android system
   *
   * @param context Android context
   * @return the connectivity state as reported by the Android system
   */
  public static boolean isConnected(Context context) {
    return getSystemConnectivity(context);
  }

  /**
   * Get the connectivity state. This can be overriden using the connectedFlag.
   *
   * @return the connectivity state
   */
  public boolean isConnected() {
    return getManagedConnectivity();
  }

  /**
   * Get the connectedFlag value
   *
   * @return the connectedFlag value, true/false if the connectivity state has ben overriden,
   * null otherwise.
   */
  public Boolean getConnectedFlag() {
    return connectedFlag;
  }

  /**
   * Set the connectedFlag value
   *
   * @param connectedFlag Set it to true/false to override the connectivity state
   */
  public void setConnectedFlag(Boolean connectedFlag) {
    this.connectedFlag = connectedFlag;
  }

  public void addConnectivityListener(ConnectivityListener listener) {
    if (!connectivityListeners.contains(listener)) {
      connectivityListeners.add(listener);
    }
  }

  public boolean removeConnectivityListener(ConnectivityListener listener) {
    return connectivityListeners.remove(listener);
  }

  @UiThread
  public void requestConnectivityUpdates() {
    if (activationCounter == 0) {
      context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    activationCounter++;
  }

  @UiThread
  public void removeConnectivityUpdates() {
    activationCounter--;
    if (activationCounter == 0) {
      context.unregisterReceiver(this);
    }
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    boolean connected = getManagedConnectivity();
    for (ConnectivityListener listener : connectivityListeners) {
      listener.onConnectivityChanged(connected);
    }
  }
}
