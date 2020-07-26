package com.mapbox.api.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SingleElementSafeListTypeAdapterTest {

  @Test
  public void parseEmptyJson() {
    final Gson gson = createGson();
    assertNull(gson.fromJson("", List.class));
  }

  @Test
  public void parseStringArrayWithMultipleElements() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("[\"a\", \"b\", \"c\"]", List.class);
    assertEquals(Arrays.asList("a", "b", "c"), parsed);
  }

  @Test
  public void parseStringArrayWithSingleElement() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("\"a\"", List.class);
    assertEquals(Collections.singletonList("a"), parsed);
  }

  @Test
  public void parseNullJson() {
    final Gson gson = createGson();
    assertNull(gson.fromJson("null", List.class));
  }

  @Test
  public void parseArrayOfNulls() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("[null, null, null]", List.class);
    assertEquals(Arrays.asList(null, null, null), parsed);
  }

  @Test
  public void parseBooleanArrayWithMultipleElements() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("[true, false]", List.class);
    assertEquals(Arrays.asList(true, false), parsed);
  }

  @Test
  public void parseBooleanArrayWithSingleElement() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("true", List.class);
    assertEquals(Collections.singletonList(true), parsed);
  }

  @Test
  public void parseNumberArrayWithMultipleElements() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("[1, 2, 3]", List.class);
    assertEquals(Arrays.asList(1.0, 2.0, 3.0), parsed);
  }

  @Test
  public void parseNumberArrayWithSingleElement() {
    final Gson gson = createGson();
    final List parsed = gson.fromJson("5", List.class);
    assertEquals(Collections.singletonList(5.0), parsed);
  }

  @Test
  public void parseCustomTypeArrayWithMultipleElements() {
    final Gson gson = createGson();

    final String inputJson = "[" +
        "{\"string_field\":\"abc\",\"boolean_field\":true,\"int_field\":1}," +
        "{\"string_field\":\"def\",\"boolean_field\":false,\"int_field\":11}" +
        "]";

    final TypeToken typeToken = TypeToken.getParameterized(List.class, TestType.class);
    final List parsed = gson.fromJson(inputJson, typeToken.getType());

    final List<TestType> expectedList = Arrays.asList(
        new TestType("abc", true, 1),
        new TestType("def", false, 11)
    );
    assertEquals(expectedList, parsed);
  }

  @Test
  public void parseCustomTypeArrayWithSingleElement() {
    final Gson gson = createGson();

    final String inputJson = "{\"string_field\":\"abc\",\"boolean_field\":true,\"int_field\":1}";

    final TypeToken typeToken = TypeToken.getParameterized(List.class, TestType.class);
    final List parsed = gson.fromJson(inputJson, typeToken.getType());

    final List<TestType> expectedList =
        Collections.singletonList(new TestType("abc", true, 1));
    assertEquals(expectedList, parsed);
  }

  @Test
  public void serializeCustomTypeArrayWithMultipleElements() {
    final Gson gson = createGson();

    final List<TestType> testData = Arrays.asList(
        new TestType("abc", true, 1),
        new TestType("def", false, 11)
    );

    final String serialized = gson.toJson(testData);

    final String expectedJson = "[" +
        "{\"string_field\":\"abc\",\"boolean_field\":true,\"int_field\":1}," +
        "{\"string_field\":\"def\",\"boolean_field\":false,\"int_field\":11}" +
        "]";

    assertEquals(expectedJson, serialized);
  }

  @Test
  public void serializeCustomTypeArrayWithSingleElement() {
    final Gson gson = createGson();

    final TestType testData = new TestType("abc", true, 1);
    final String serialized = gson.toJson(testData);

    final String expectedJson = "{\"string_field\":\"abc\",\"boolean_field\":true,\"int_field\":1}";
    assertEquals(expectedJson, serialized);
  }

  private static Gson createGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(SingleElementSafeListTypeAdapter.FACTORY)
        .create();
  }

  private static class TestType {

    @SerializedName("string_field")
    private final String stringField;

    @SerializedName("boolean_field")
    private final boolean booleanField;

    @SerializedName("int_field")
    private final int intField;

    TestType(String stringField, boolean booleanField, int intField) {
      this.stringField = stringField;
      this.booleanField = booleanField;
      this.intField = intField;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TestType testType = (TestType) o;

      if (booleanField != testType.booleanField) return false;
      if (intField != testType.intField) return false;
      return stringField != null ? stringField.equals(testType.stringField) : testType.stringField == null;
    }

    @Override
    public int hashCode() {
      int result = stringField != null ? stringField.hashCode() : 0;
      result = 31 * result + (booleanField ? 1 : 0);
      result = 31 * result + intField;
      return result;
    }
  }
}
