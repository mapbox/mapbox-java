package com.mapbox.services.android.navigation.v5;

import android.os.Handler;

import org.junit.Test;

import static org.junit.Assert.*;




public class LocationUpdatedThreadTest {



  @Test
  public void checksAlertMediumLevel() throws Exception {
    Handler aHandler = mock(Handler.class);
    LocationUpdatedThread locationUpdatedThread = new LocationUpdatedThread(aHandler);

    assertEquals(3, locationUpdatedThread.monitorStepProgress());
  }


}