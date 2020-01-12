package com.mapbox.api.directions.models;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DirectionsRouteTest extends TestUtils {

  private static final String DIRECTIONS_V5_VOICE_BANNER_FIXTURE = "directions_v5_voice_banner.json";
  private static final String DIRECTIONS_V5_VOICE_INVALID_FIXTURE = "directions_v5_voice_invalid.json";
  private static final int FIRST_ROUTE = 0;

  @Test
  public void sanity() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder()
      .distance(100d)
      .build();
    assertNotNull(route);
  }

  @Test
  public void testSerializable() throws Exception {
    DirectionsRoute route = DirectionsRoute.builder().distance(100d).build();
    byte[] serialized = TestUtils.serialize(route);
    assertEquals(route, deserialize(serialized, DirectionsRoute.class));
  }

  @Test
  public void directionsRoute_doesReturnVoiceLocale() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_VOICE_BANNER_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    DirectionsRoute route = response.routes().get(FIRST_ROUTE);

    String voiceLanguage = route.voiceLanguage();

    assertEquals("en-US", voiceLanguage);
  }

  @Test
  public void directionsRouteWithInvalidLanguage_doesReturnNullVoiceLanguage() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_VOICE_INVALID_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    DirectionsRoute route = response.routes().get(FIRST_ROUTE);

    String voiceLanguage = route.voiceLanguage();

    assertNull(voiceLanguage);
  }
}
