package com.mapbox.api.directions.v5.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UnrecognizedPropertiesUtilsTest {

  @Test
  public void fromSerializableProperties_nullProperties() {
    Map<String, JsonElement> actual = UnrecognizedPropertiesUtils.fromSerializableProperties(null);
    Assert.assertNull(actual);
  }

  @Test
  public void fromSerializableProperties_emptyProperties() {
    Map<String, JsonElement> actual = UnrecognizedPropertiesUtils.fromSerializableProperties(
      new HashMap<>()
    );
    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void fromSerializableProperties_hasProperties() {
    final JsonObject propertyValue = new JsonObject();
    propertyValue.add("key", new JsonPrimitive("value"));
    final Map<String, SerializableJsonElement> properties = new HashMap<>();
    properties.put("aaa", new SerializableJsonElement(propertyValue));
    properties.put("bbb", new SerializableJsonElement(new JsonObject()));
    Map<String, JsonElement> expected = new HashMap<>();
    expected.put("aaa", propertyValue);
    expected.put("bbb", new JsonObject());
    final Map<String, JsonElement> actual = UnrecognizedPropertiesUtils.fromSerializableProperties(
      properties
    );
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void toSerializableProperties_nullProperties() {
    Map<String, SerializableJsonElement> actual = UnrecognizedPropertiesUtils.toSerializableProperties(null);
    Assert.assertNull(actual);
  }

  @Test
  public void toSerializableProperties_emptyProperties() {
    Map<String, SerializableJsonElement> actual = UnrecognizedPropertiesUtils.toSerializableProperties(
      new HashMap<>()
    );
    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void toSerializableProperties_hasProperties() {
    final JsonObject propertyValue = new JsonObject();
    propertyValue.add("key", new JsonPrimitive("value"));
    final Map<String, JsonElement> properties = new HashMap<>();
    properties.put("aaa", propertyValue);
    properties.put("bbb", new JsonObject());
    Map<String, SerializableJsonElement> expected = new HashMap<>();
    expected.put("aaa", new SerializableJsonElement(propertyValue));
    expected.put("bbb", new SerializableJsonElement(new JsonObject()));
    final Map<String, SerializableJsonElement> actual = UnrecognizedPropertiesUtils.toSerializableProperties(
      properties
    );
    Assert.assertEquals(expected, actual);
  }

}