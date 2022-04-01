package com.mapbox.api.directions.v5.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MutateJsonUtil {

  /***
   * Adds a set of artificial fields of different types to every json object inside the passed json.
   * See an example in {@link MutateJsonTest#mutateJson}
   */
  public static void mutateJson(JsonElement jsonToMutate) {
    if (jsonToMutate.isJsonPrimitive() || jsonToMutate.isJsonNull()) {
    } else if (jsonToMutate.isJsonArray()) {
      JsonArray array = (JsonArray) jsonToMutate;
      for (JsonElement item : array) {
        mutateJson(item);
      }
    } else if (jsonToMutate.isJsonObject()) {
      JsonObject object = (JsonObject) jsonToMutate;
      for (Map.Entry<String, JsonElement> field : object.entrySet()) {
        mutateJson(field.getValue());
      }
      addPrimitiveValues(object);
      JsonObject testJsonObject = new JsonObject();
      addPrimitiveValues(testJsonObject);
      object.add("testingObject", testJsonObject);
    }
  }

  private static void addPrimitiveValues(JsonObject object) {
    object.add("testingStringValue", new JsonPrimitive("test"));
    object.add("testingBooleanValue", new JsonPrimitive(true));
    object.add("testingNumberValue", new JsonPrimitive(5));
  }

  public static class MutateJsonTest {
    @Test public void mutateJson() {
      String sourceJson = "{'a':'a','b':{},'c':{'c':{}}}";
      Gson gson = new GsonBuilder().create();

      JsonObject jsonToMutate = gson.fromJson(sourceJson, JsonObject.class);
      MutateJsonUtil.mutateJson(jsonToMutate);

      String mutatedJson = gson.toJson(jsonToMutate);
      assertEquals(
        "{\"a\":\"a\",\"b\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5,\"testingObject\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5}},\"c\":{\"c\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5,\"testingObject\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5}},\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5,\"testingObject\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5}},\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5,\"testingObject\":{\"testingStringValue\":\"test\",\"testingBooleanValue\":true,\"testingNumberValue\":5}}",
        mutatedJson
      );
    }
  }
}
