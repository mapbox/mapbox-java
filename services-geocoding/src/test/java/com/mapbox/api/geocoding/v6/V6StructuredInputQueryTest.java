package com.mapbox.api.geocoding.v6;

import com.mapbox.core.exceptions.ServicesException;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class V6StructuredInputQueryTest {

  @Test
  public void testEmptyStructuredInput() {
    final Exception e = assertThrows(ServicesException.class, new ThrowingRunnable() {
      @Override
      public void run() {
        V6StructuredInputQuery.builder().build();
      }
    });

    assertTrue(e.getMessage().contains("At least one component must be non null"));
  }

  @Test
  public void testStructuredInputWithAllValuesSet() {
    final V6StructuredInputQuery query = V6StructuredInputQuery.builder()
      .addressNumber("test-address-number")
      .street("test-street")
      .block("test-block")
      .place("test-place")
      .region("test-region")
      .postcode("test-postcode")
      .locality("test-locality")
      .neighborhood("test-neighborhood")
      .build();

    assertEquals("test-address-number", query.addressNumber());
    assertEquals("test-street", query.street());
    assertEquals("test-block", query.block());
    assertEquals("test-place", query.place());
    assertEquals("test-region", query.region());
    assertEquals("test-postcode", query.postcode());
    assertEquals("test-locality", query.locality());
    assertEquals("test-neighborhood", query.neighborhood());
  }

  @Test
  public void testUnspecifiedValuesAreNull() {
    final V6StructuredInputQuery queryWithAddress = V6StructuredInputQuery.builder()
      .addressNumber("test-address-number")
      .build();

    assertEquals("test-address-number", queryWithAddress.addressNumber());
    assertNull(queryWithAddress.street());
    assertNull(queryWithAddress.block());
    assertNull(queryWithAddress.place());
    assertNull(queryWithAddress.region());
    assertNull(queryWithAddress.postcode());
    assertNull(queryWithAddress.locality());
    assertNull(queryWithAddress.neighborhood());

    final V6StructuredInputQuery queryWithStreet = V6StructuredInputQuery.builder()
      .street("test-street")
      .build();

    assertNull(queryWithStreet.addressNumber());
  }
}
