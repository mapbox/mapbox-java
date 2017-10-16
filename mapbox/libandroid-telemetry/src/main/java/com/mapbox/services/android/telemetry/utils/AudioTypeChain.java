package com.mapbox.services.android.telemetry.utils;


class AudioTypeChain {

  AudioTypeResolver setup() {
    AudioTypeResolver unknown = new UnknownAudioType();
    AudioTypeResolver headphones = new HeadphonesAudioType();
    headphones.nextChain(unknown);
    AudioTypeResolver bluetooth = new BluetoothAudioType();
    bluetooth.nextChain(headphones);
    AudioTypeResolver rootOfTheChain = new SpeakerAudioType();
    rootOfTheChain.nextChain(bluetooth);

    return rootOfTheChain;
  }
}
