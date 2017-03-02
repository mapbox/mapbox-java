package com.mapbox.services.api.navigation.v5.osrm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.services.api.directions.v5.models.IntersectionLanes;
import com.mapbox.services.api.directions.v5.models.LegStep;

import java.io.InputStream;
import java.io.InputStreamReader;

public class TextInstructions {

  private JsonObject versionObject;

  public TextInstructions(String language, String version) {
    // Load the resource
    String localPath = String.format("translations/%s.json", language);
    InputStream resource = getClass().getClassLoader().getResourceAsStream(localPath);
    if (resource == null) {
      throw new RuntimeException("Translation not found for language: " + language);
    }

    // Parse the JSON content
    JsonObject rootObject = new JsonParser().parse(new InputStreamReader(resource)).getAsJsonObject();
    versionObject = rootObject.getAsJsonObject(version);
    if (versionObject == null) {
      throw new RuntimeException("Version not found for value: " + version);
    }
  }

  public JsonObject getVersionObject() {
    return versionObject;
  }

  public static String capitalizeFirstLetter(String text) {
    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }

  /**
   * Transform numbers to their translated ordinalized value
   *
   * @param number
   * @return
   */
  public String ordinalize(int number) {
    try {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("ordinalize")
        .getAsJsonPrimitive(String.valueOf(number)).getAsString();
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * Transform degrees to their translated compass direction
   *
   * @param degree
   * @return
   */
  public String directionFromDegree(Integer degree) {
    if (degree == null) {
      // step had no bearing_after degree, ignoring
      return "";
    } else if (degree >= 0 && degree <= 20) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("north").getAsString();
    } else if (degree > 20 && degree < 70) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("northeast").getAsString();
    } else if (degree >= 70 && degree <= 110) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("east").getAsString();
    } else if (degree > 110 && degree < 160) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("southeast").getAsString();
    } else if (degree >= 160 && degree <= 200) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("south").getAsString();
    } else if (degree > 200 && degree < 250) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("southwest").getAsString();
    } else if (degree >= 250 && degree <= 290) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("west").getAsString();
    } else if (degree > 290 && degree < 340) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("northwest").getAsString();
    } else if (degree >= 340 && degree <= 360) {
      return getVersionObject().getAsJsonObject("constants").getAsJsonObject("direction")
        .getAsJsonPrimitive("north").getAsString();
    } else {
      throw new RuntimeException("Degree is invalid: " + degree);
    }
  }

  /**
   * Reduce any lane combination down to a contracted lane diagram
   *
   * @param step
   */
  public String laneConfig(LegStep step) {
    if (step.getIntersections() == null
      || step.getIntersections().size() == 0
      || step.getIntersections().get(0).getLanes() == null
      || step.getIntersections().get(0).getLanes().length == 0) {
      throw new RuntimeException("No lanes object");
    }

    StringBuilder config = new StringBuilder();
    Boolean currentLaneValidity = null;
    for (IntersectionLanes lane : step.getIntersections().get(0).getLanes()) {
      if (currentLaneValidity == null || currentLaneValidity != lane.getValid()) {
        if (lane.getValid()) {
          config.append("o");
        } else {
          config.append("x");
        }
        currentLaneValidity = lane.getValid();
      }
    }

    return config.toString();
  }
}
