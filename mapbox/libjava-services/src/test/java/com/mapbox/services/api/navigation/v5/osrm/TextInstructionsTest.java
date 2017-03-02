package com.mapbox.services.api.navigation.v5.osrm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TextInstructionsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSanity() {
    TextInstructions textInstructions = new TextInstructions("en", "v5");
    assertNotNull(textInstructions.getVersionObject());
  }

  @Test
  public void testBadLanguage() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(startsWith("Translation not found for language: xxx"));
    new TextInstructions("xxx", "v5");
  }

  @Test
  public void testBadVersion() {
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(startsWith("Version not found for value: yyy"));
    new TextInstructions("en", "yyy");
  }

  @Test
  public void testCapitalizeFirstLetter() {
    assertEquals("Mapbox", TextInstructions.capitalizeFirstLetter("mapbox"));
  }

  @Test
  public void testOrdinalize() {
    TextInstructions textInstructions = new TextInstructions("en", "v5");
    assertEquals("1st", textInstructions.ordinalize(1));
    assertEquals("", textInstructions.ordinalize(999));
  }

  @Test
  public void testValidDirectionFromDegree() {
    TextInstructions textInstructions = new TextInstructions("en", "v5");
    assertEquals("", textInstructions.directionFromDegree(null));
    assertEquals("east", textInstructions.directionFromDegree(100));
    assertEquals("west", textInstructions.directionFromDegree(250));
  }

  @Test
  public void testInvalidDirectionFromDegree() {
    TextInstructions textInstructions = new TextInstructions("en", "v5");
    thrown.expect(RuntimeException.class);
    thrown.expectMessage(startsWith("Degree is invalid: 999"));
    assertEquals("", textInstructions.directionFromDegree(999));
  }
}
