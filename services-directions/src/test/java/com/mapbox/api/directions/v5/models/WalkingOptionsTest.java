package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.WalkingOptions;
import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WalkingOptionsTest extends TestUtils {
  
 @Test
  public void toJson_fromJson() throws Exception {
   WalkingOptions walkingOptions = WalkingOptions.builder()
     .walkingSpeed(1.0)
     .walkwayBias(2.0)
     .alleyBias(3.0)
     .ferryBias(4.0)
     .stepPenalty(5)
     .maxHikingDifficulty(6)
     .build();

   String jsonString = walkingOptions.toJson();
   WalkingOptions walkingOptionsFromJson = WalkingOptions.fromJson(jsonString);

   assertEquals(walkingOptions, walkingOptionsFromJson);
 }
}
