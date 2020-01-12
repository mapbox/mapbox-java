package com.mapbox.api.directions.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mapbox.core.TestUtils;
import org.junit.Test;

public class DirectionsErrorTest extends TestUtils {

  @Test
  public void sanity() throws Exception {
    DirectionsError error = DirectionsError.builder()
      .code("TEST")
      .message("This is a test")
      .build();
    assertNotNull(error);
  }

  @Test
  public void testSerializable() throws Exception {
    DirectionsError error = DirectionsError.builder()
      .code("TEST")
      .message("This is a test")
      .build();
    byte[] serialized = TestUtils.serialize(error);
    assertEquals(error, deserialize(serialized, DirectionsError.class));
  }
}
