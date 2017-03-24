package com.mapbox.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 * Port of Realms version checker
 */
public class MapboxVersionChecker {
  public static final String MAPBOX_ANDROID_DOWNLOAD_URL = "https://www.mapbox.com/android-docs/mapbox-services/";

  private static final String VERSION_URL = "http://static.realm.io/update/java?";
  private static final String MAPBOX_VERSION = Version.VERSION;
  private static final String MAPBOX_VERSION_PATTERN = "\\d+\\.\\d+\\.\\d+";
  private static final int READ_TIMEOUT = 2000;
  private static final int CONNECT_TIMEOUT = 4000;

  private static MapboxVersionChecker instance = null;

  private ProcessingEnvironment processingEnvironment;

  public static MapboxVersionChecker getInstance(ProcessingEnvironment processingEnvironment) {
    if (instance == null) {
      instance = new MapboxVersionChecker(processingEnvironment);
    }
    return instance;
  }

  private MapboxVersionChecker(ProcessingEnvironment processingEnvironment) {
    this.processingEnvironment = processingEnvironment;
  }

  public void executeMapboxVersionUpdate() {
    Thread backgroundThread = new Thread(new Runnable() {
      @Override
      public void run() {
        launchMapboxCheck();
      }
    });

    backgroundThread.start();

    try {
      backgroundThread.join(CONNECT_TIMEOUT + READ_TIMEOUT);
    } catch (InterruptedException ignore) {
      // We ignore this exception on purpose not to break the build system if this class fails
    }
  }

  private void launchMapboxCheck() {
    //Check Mapbox version server
    String latestVersionStr = checkLatestVersion();
    if (!latestVersionStr.equals(MAPBOX_VERSION)) {
      printMessage("Version " + latestVersionStr + " of Mapbox Services is now available: "
        + MAPBOX_ANDROID_DOWNLOAD_URL);
    }
  }

  private String checkLatestVersion() {
    String result = MAPBOX_VERSION;
    try {
      URL url = new URL(VERSION_URL + MAPBOX_VERSION);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT);
      conn.setReadTimeout(READ_TIMEOUT);
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String latestVersion = rd.readLine();
      // if the obtained string does not match the pattern, we are in a separate network.
      if (latestVersion.matches(MAPBOX_VERSION_PATTERN)) {
        result = latestVersion;
      }
      rd.close();
    } catch (IOException ioException) {
      // We ignore this exception on purpose not to break the build system if this class fails
    }
    return result;
  }

  private void printMessage(String message) {
    processingEnvironment.getMessager().printMessage(Diagnostic.Kind.OTHER, message);
  }
}
