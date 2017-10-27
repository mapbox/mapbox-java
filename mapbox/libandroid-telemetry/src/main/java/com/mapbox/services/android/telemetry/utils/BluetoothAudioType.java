package com.mapbox.services.android.telemetry.utils;


import android.content.Context;
import android.media.AudioManager;

class BluetoothAudioType implements AudioTypeResolver {
  private static final String BLUETOOTH = "bluetooth";
  private AudioTypeResolver chain;

  @Override
  public void nextChain(AudioTypeResolver chain) {
    this.chain = chain;
  }

  @Override
  public String obtainAudioType(Context context) {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (audioManager.isBluetoothScoOn()) {
      return BLUETOOTH;
    } else {
      return chain.obtainAudioType(context);
    }
  }
}
