package com.mapbox.core.utils;

import static org.junit.Assert.assertTrue;

import com.mapbox.geojson.TestUtils;

import org.junit.Assert;
import org.junit.Test;

public class TextUtilsTest extends TestUtils {

  @Test
  public void isEmptyTest() throws Exception {
    String string = null;
    assertTrue(TextUtils.isEmpty(string));
    string = "";
    assertTrue(TextUtils.isEmpty(string));
  }

  @Test
  public void joinTest() throws Exception {
    Float[] numbers = new Float[4];
    for (int i = 0; i < 4; i++) {
      numbers[i] = 1.0f * i;
    }
    Assert.assertEquals("0.0-1.0-2.0-3.0", TextUtils.join("-", numbers));
  }
}



