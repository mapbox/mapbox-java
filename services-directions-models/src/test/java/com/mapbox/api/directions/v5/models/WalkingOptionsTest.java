package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.WalkingOptions;
import com.mapbox.core.TestUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WalkingOptionsTest extends TestUtils {
    private static final String JSON = "{\"walking_speed\":1.0,\"walkway_bias\":0.6,\"alley_bias\":0" +
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

    @Test
    public void toBuilder() {
        WalkingOptions walkingOptions = walkingOptions();

        double delta = 0;
        double newSpeed = 2.0;

        WalkingOptions updatedOptions = walkingOptions.toBuilder()
                .walkingSpeed(newSpeed)
                .build();

        assertEquals(newSpeed, updatedOptions.walkingSpeed(), delta);
        assertEquals(walkingOptions.walkwayBias(), updatedOptions.walkwayBias(), delta);
        assertEquals(walkingOptions.alleyBias(), updatedOptions.alleyBias(), delta);
    }

    private WalkingOptions walkingOptions() {
        return WalkingOptions.builder()
                .walkingSpeed(1.0)
                .walkwayBias(0.6)
                .alleyBias(0.7)
                .build();
    }
}
