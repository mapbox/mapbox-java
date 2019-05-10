package com.mapbox.api.directions.v5.models;

  import com.mapbox.api.directions.v5.WalkingOptions;
  import com.mapbox.core.TestUtils;

  import org.junit.Test;

  import static org.junit.Assert.assertEquals;

public class WalkingOptionsTest extends TestUtils {
 private static final String JSON = "{\"walkingSpeed\":1.0,\"walkwayBias\":0.6,\"alleyBias\":0" +
   ".7}";

 @Test
 public void toJson_fromJson() {
  WalkingOptions walkingOptions = walkingOptions();

  String jsonString = walkingOptions.toJson();
  WalkingOptions walkingOptionsFromJson = WalkingOptions.fromJson(jsonString);

  assertEquals(walkingOptions, walkingOptionsFromJson);
 }

 @Test
 public void toJson() {
  WalkingOptions walkingOptions = walkingOptions();

  compareJson(JSON, walkingOptions.toJson());
 }

 @Test
 public void fromJson() {
  WalkingOptions walkingOptions = WalkingOptions.fromJson(JSON);

  assertEquals(Double.valueOf(1.0), walkingOptions.walkingSpeed());
  assertEquals(Double.valueOf(0.6), walkingOptions.walkwayBias());
  assertEquals(Double.valueOf(0.7), walkingOptions.alleyBias());
 }

 private WalkingOptions walkingOptions() {
  return WalkingOptions.builder()
    .walkingSpeed(1.0)
    .walkwayBias(0.6)
    .alleyBias(0.7)
    .build();
 }
}
