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

public class TollCostTest {

    @Test
    public void sanity() throws Exception {
        TollCost tollCost = TollCost.builder()
                .currency("USD")
                .paymentMethods(
                        PaymentMethods.builder()
                                .etc((CostPerVehicleSize.builder().jumbo(555.66).build()))
                                .build()
                )
                .build();
        assertNotNull(tollCost);
    }

    @Test
    public void serializable() throws Exception {
        TollCost tollCost = TollCost.builder()
                .currency("USD")
                .paymentMethods(
                        PaymentMethods.builder()
                                .etc((CostPerVehicleSize.builder().jumbo(555.66).build()))
                                .build()
                )
                .build();
        byte[] serialized = TestUtils.serialize(tollCost);
        assertEquals(tollCost, deserialize(serialized, TollCost.class));
    }

    @Test
    public void toFromJsonFilled() {
        PaymentMethods paymentMethods = PaymentMethods.builder()
                .etc((CostPerVehicleSize.builder().jumbo(555.66).build()))
                .build();
        TollCost original = TollCost.builder()
                .currency("USD")
                .paymentMethods(paymentMethods)
                .build();
        String json = original.toJson();
        TollCost deserialized = TollCost.fromJson(json);
        assertEquals(original, deserialized);
    }

    @Test
    public void toFromJsonDefault() {
        TollCost original = TollCost.builder().build();
        String json = original.toJson();
        TollCost deserialized = TollCost.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromJsonInvalid() {
        String invalidJson = "not a json";
        assertThrows(JsonSyntaxException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                TollCost.fromJson(invalidJson);
            }
        });
    }

    @Test
    public void fromEmptyJson() {
        String json = "{}";
        TollCost deserialized = TollCost.fromJson(json);
        checkDefaultObject(deserialized);
    }

    @Test
    public void fromFilledJson() {
        String expectedCurrency = "USD";
        PaymentMethods expectedPaymentMethods = PaymentMethods.builder()
                .etc((CostPerVehicleSize.builder().standard(444.33).build()))
                .build();
        String json = "{\"currency\": \"USD\", \"payment_methods\": {\"etc\": {\"standard\": 444.33}}}";
        TollCost deserialized = TollCost.fromJson(json);
        assertEquals(expectedCurrency, deserialized.currency());
        assertEquals(expectedPaymentMethods, deserialized.paymentMethods());
    }

    private void checkDefaultObject(TollCost actual) {
        assertNull(actual.currency());
        assertNull(actual.paymentMethods());
    }
}
