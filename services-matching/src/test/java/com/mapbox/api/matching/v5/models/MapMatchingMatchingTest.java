package com.mapbox.api.matching.v5.models;

import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapMatchingMatchingTest extends TestUtils {

  private static final String MAP_MATCHING_V5_ROUTE_INDEX_JSON = "map_matching_v5_post.json";
  private static final String MAP_MATCHING_V5_VOICE_LANGUAGE_JSON = "map_matching_v5_voice_language.json";
  private static final String MAP_MATCHING_V5_INVALID_VOICE_LANGUAGE_JSON = "map_matching_v5_invalid_voice_language.json";
  private static final int FIRST_ROUTE = 0;

  @Test
  public void directionsRoute_doesReturnVoiceLocale() throws Exception {
    String json = loadJsonFixture(MAP_MATCHING_V5_VOICE_LANGUAGE_JSON);
    MapMatchingResponse response = MapMatchingResponse.fromJson(json);
    MapMatchingMatching matching = response.matchings().get(FIRST_ROUTE);

    String voiceLanguage = matching.voiceLanguage();

    assertEquals("en-US", voiceLanguage);
  }

  @Test
  public void directionsRouteWithInvalidLanguage_doesReturnNullVoiceLanguage() throws Exception {
    String json = loadJsonFixture(MAP_MATCHING_V5_INVALID_VOICE_LANGUAGE_JSON);
    MapMatchingResponse response = MapMatchingResponse.fromJson(json);
    MapMatchingMatching matching = response.matchings().get(FIRST_ROUTE);

    String voiceLanguage = matching.voiceLanguage();

    assertNull(voiceLanguage);
  }

  @Test
  public void directionsRoute_includesRouteIndex() throws Exception {
    String json = loadJsonFixture(MAP_MATCHING_V5_ROUTE_INDEX_JSON);

    MapMatchingResponse response = MapMatchingResponse.fromJson(json);

    assertEquals("0", response.matchings().get(0).routeIndex());
  }
}
