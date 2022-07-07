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

public class PaymentMethodsTest {

    @Test
    public void sanity() throws Exception {
        PaymentMethods paymentMethods = PaymentMethods.builder().etc(CostPerVehicleSize.builder().small(555.6).build()).build();
        assertNotNull(paymentMethods);
    }

    @Test
    public void serializable() throws Exception {
        PaymentMethods paymentMethods = PaymentMethods.builder()
                .etc(CostPerVehicleSize.builder().small(555.6).build())
                .cash(CostPerVehicleSize.builder().standard(777.0).build())
                .build();
        byte[] serialized = TestUtils.serialize(paymentMethods);
        assertEquals(paymentMethods, deserialize(serialized, PaymentMethods.class));
    }

    @Test
    public void toFromJsonFilled() {
        CostPerVehicleSize etc = CostPerVehicleSize.builder().small(555.6).build();
        CostPerVehicleSize cash = CostPerVehicleSize.builder().standard(777.0).build();
        PaymentMethods original = PaymentMethods.builder()
                .etc(etc)
                .cash(cash)
                .build();
        String json = original.toJson();
        PaymentMethods deserialized = PaymentMethods.fromJson(json);
        assertEquals(original, deserialized);
    }

    @Test
    public void toFromJsonDefault() {
        PaymentMethods original = PaymentMethods.builder().build();
        String json = original.toJson();
        PaymentMethods deserialized = PaymentMethods.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromEmptyJson() {
        String json = "{}";
        PaymentMethods deserialized = PaymentMethods.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromFilledJson() {
        CostPerVehicleSize expectedEtc = CostPerVehicleSize.builder().small(555.44).build();
        CostPerVehicleSize expectedCash = CostPerVehicleSize.builder().standard(999.9).build();
        String json = "{\"etc\": {\"small\": 555.44}, \"cash\": {\"standard\": 999.9}}";
        PaymentMethods deserialized = PaymentMethods.fromJson(json);
        assertEquals(expectedEtc, deserialized.etc());
        assertEquals(expectedCash, deserialized.cash());
    }

    @Test
    public void fromInvalidJson() {
        String invalidJson = "not a json";
        assertThrows(JsonSyntaxException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                PaymentMethods.fromJson(invalidJson);
            }
        });
    }

    private void checkDefaultObject(PaymentMethods actual) {
        assertNull(actual.etc());
        assertNull(actual.cash());
    }
}
