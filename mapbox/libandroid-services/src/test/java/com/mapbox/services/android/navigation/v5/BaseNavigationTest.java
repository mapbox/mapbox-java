package com.mapbox.services.android.navigation.v5;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;


public class BaseNavigationTest {

  public static String readFileFromAssets(String fileName, Context c) {
    try {
      InputStream is = c.getAssets().open(fileName);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      String text = new String(buffer);

      return text;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
