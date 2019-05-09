package com.mapbox.api.directions.v5.models;

  import com.mapbox.api.directions.v5.WalkingOptions;
  import com.mapbox.core.TestUtils;

  import org.junit.Test;

  import static org.junit.Assert.assertEquals;

public class WalkingOptionsTest extends TestUtils {
 private static final String JSON = "{\"walkingSpeed\":1.0,\"walkwayBias\":2.0,\"alleyBias\":3.0}";

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
  assertEquals(Double.valueOf(2.0), walkingOptions.walkwayBias());
  assertEquals(Double.valueOf(3.0), walkingOptions.alleyBias());
 }

 private WalkingOptions walkingOptions() {
  return WalkingOptions.builder()
    .walkingSpeed(1.0)
    .walkwayBias(2.0)
    .alleyBias(3.0)
    .build();
 }
}
