package com.mapbox.geojson.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Required to handle the special case where the altitude might be a Double.NaN, which isn't a valid
 * double value as per JSON specification.
 *
 * @since 1.0.0
 */
public class PointSerializer implements JsonSerializer<Point> {

  /**
   * Empty constructor to prevent relying on the default one.
   *
   * @since 3.0.0
   */
  public PointSerializer() {
    // Empty Constructor
  }

  /**
   * Required to handle the special case where the altitude might be a Double.NaN, which isn't a
   * valid double value as per JSON specification.
   *
   * @param src       A {@link Point} defined by a longitude, latitude, and optionally, an
   *                  altitude.
   * @param typeOfSrc Common superinterface for all types in the Java.
   * @param context   Context for deserialization that is passed to a custom deserializer during
   *                  invocation of its {@link com.google.gson.JsonDeserializationContext} method.
   * @return a JsonArray containing the raw coordinates.
   * @since 1.0.0
   */
  @Override
  public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray rawCoordinates = new JsonArray();

    BigDecimal lat = BigDecimal.valueOf(src.latitude());
    String latString = lat.setScale(7, RoundingMode.HALF_UP)
      .stripTrailingZeros().toPlainString();

    BigDecimal lon = BigDecimal.valueOf(src.longitude());
    String lonString = lon.setScale(7, RoundingMode.HALF_UP)
      .stripTrailingZeros().toPlainString();

    rawCoordinates.add(new JsonPrimitive(Double.valueOf(lonString)));
    rawCoordinates.add(new JsonPrimitive(Double.valueOf(latString)));

    // Includes altitude
    if (src.hasAltitude()) {
      rawCoordinates.add(new JsonPrimitive(src.altitude()));
    }

    return rawCoordinates;
  }
}
