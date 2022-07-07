package com.mapbox.api.directions.v5.models;

import com.google.gson.JsonSyntaxException;
import com.mapbox.core.TestUtils;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static com.mapbox.core.TestUtils.deserialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CostPerVehicleSizeTest {

    @Test
    public void sanity() throws Exception {
        CostPerVehicleSize costPerVehicleSize = CostPerVehicleSize.builder().small(5566.0).large(3344.5).build();
        assertNotNull(costPerVehicleSize);
    }

    @Test
    public void serializable() throws Exception {
        CostPerVehicleSize costPerVehicleSize = CostPerVehicleSize.builder().small(5566.0).large(3344.5).build();
        byte[] serialized = TestUtils.serialize(costPerVehicleSize);
        assertEquals(costPerVehicleSize, deserialize(serialized, CostPerVehicleSize.class));
    }

    @Test
    public void toFromJsonFilled() {
        CostPerVehicleSize original = CostPerVehicleSize.builder()
                .small(1.2)
                .middle(3.44)
                .standard(9.87)
                .large(5.46)
                .jumbo(88.12777)
                .build();
        String json = original.toJson();
        CostPerVehicleSize deserialized = CostPerVehicleSize.fromJson(json);
        assertEquals(original, deserialized);
    }

    @Test
    public void toFromJsonDefault() {
        CostPerVehicleSize original = CostPerVehicleSize.builder().build();
        String json = original.toJson();
        CostPerVehicleSize deserialized = CostPerVehicleSize.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromJsonInvalid() {
        String invalidJson = "not a json";
        assertThrows(JsonSyntaxException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                CostPerVehicleSize.fromJson(invalidJson);
            }
        });
    }

    @Test
    public void fromEmptyJson() {
        String json = "{}";
        CostPerVehicleSize deserialized = CostPerVehicleSize.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromFilledJson() {
        String json = "{\"small\": 34.12, \"middle\": 77.54, \"large\": 99.65465, \"standard\": 443.12, \"jumbo\": 883.55}";
        CostPerVehicleSize deserialized = CostPerVehicleSize.fromJson(json);
        double delta = 0.0000000001;
        assertEquals(443.12, deserialized.standard(), delta);
        assertEquals(34.12, deserialized.small(), delta);
        assertEquals(99.65465, deserialized.large(), delta);
        assertEquals(77.54, deserialized.middle(), delta);
        assertEquals(883.55, deserialized.jumbo(), delta);
    }

    private void checkDefaultObject(CostPerVehicleSize actual) {
        assertNull(actual.standard());
        assertNull(actual.small());
        assertNull(actual.middle());
        assertNull(actual.large());
        assertNull(actual.jumbo());
    }
}
