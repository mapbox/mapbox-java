package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CongestionTest extends TestUtils {

  @Test
  public void sanity() {
    assertNotNull(getDefault());
  }

  @Test
  public void testSerializable() throws Exception {
    Congestion congestion = getDefault();
    byte[] serialized = TestUtils.serialize(congestion);

    assertEquals(congestion, deserialize(serialized, Congestion.class));
  }

  @Test
  public void jsonComparingDefaultCongestion() {
    Congestion congestion = getDefault();
    String json = congestion.toJson();

    Congestion fromJson = Congestion.fromJson(json);

    assertEquals(congestion, fromJson);
  }

  @Test
  public void jsonComparingCustomCongestion() {
    Congestion congestion = Congestion.builder().value(10).build();
    String json = congestion.toJson();

    Congestion fromJson = Congestion.fromJson(json);

    assertEquals(congestion, fromJson);
  }

  private Congestion getDefault() {
    return Congestion.builder()
      .value(5)
      .build();
  }
}
