package com.mapbox.services.android.navigation.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class BaseNavigationTest {

  protected static final double DELTA = 1E-10;

  static String convertStreamToString(InputStream is) throws IOException {
    final int bufferSize = 1024;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(is, "UTF-8");
    for (; ; ) {
      int rsz = in.read(buffer, 0, buffer.length);
      if (rsz < 0) {
        break;
      }
      out.append(buffer, 0, rsz);
    }
    return out.toString();
  }

}
