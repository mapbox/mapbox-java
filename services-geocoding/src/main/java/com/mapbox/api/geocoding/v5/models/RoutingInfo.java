package com.mapbox.api.geocoding.v5.models;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.api.geocoding.v5.RoutingDestinationTypeAdapter;
import com.mapbox.api.geocoding.v5.RoutingInfoDeserializer;
import com.mapbox.api.geocoding.v5.RoutingInfoTypeAdapter;
import com.mapbox.geojson.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An object within the Geocoding API response's {@link CarmenFeature},
 * which holds information about the ideal location for navigation
 * routing. This routable point information is only provided by the
 * API if MapboxGeocoding#geocodingTypes() is set to
 * {@link com.mapbox.api.geocoding.v5.GeocodingCriteria#TYPE_ADDRESS}.
 * <p>
 * See <a href="https://docs.mapbox.com/api/search/#forward-geocoding">
 * https://docs.mapbox.com/api/search/#forward-geocoding</a> for
 * more information.
 *
 * @since 4.10.0
 */
public class RoutingInfo implements Serializable {

  private List<RoutableDestination> routableDestinations;

  RoutingInfo(List<RoutableDestination> routableDestinationList) {
    this.routableDestinations = routableDestinationList;
  }

  /**
   * Create a {@link RoutingInfo} object with a list of {@link Point}s.
   *
   * @param pointList list of {@link Point}s. They'll be used to create
   *                  {@link RoutableDestination} objects, which will then
   *                  be used to create a {@link RoutingInfo} object.
   * @return a RoutingInfo object.
   * @since 4.10.0
   */
  public static RoutingInfo fromPoints(@NonNull List<Point> pointList) {
    List<RoutableDestination> routableDestinationsList = new ArrayList<>(pointList.size());
    for (Point singlePoint : pointList) {
      routableDestinationsList.add(new RoutableDestination(
          Arrays.asList(singlePoint.longitude(), singlePoint.latitude())));
    }
    return new RoutingInfo(routableDestinationsList);
  }

  /**
   * Create a {@link RoutingInfo} object with a single {@link Point}.
   *
   * @param routablePoint the single {@link Point} that's best for
   *                      routablePoints to the
   *                      {@link com.mapbox.geojson.Feature}.
   * @since 4.10.0
   */
  public static RoutingInfo fromPoint(@NonNull Point routablePoint) {
    return new RoutingInfo(Arrays.asList(
        new RoutableDestination(
            Arrays.asList(routablePoint.longitude(), routablePoint.latitude()))));
  }

  /**
   * Convenience method for getting the list of {@link Point}s to use for
   * routablePoints to the specific {@link com.mapbox.geojson.Feature}.
   *
   * @return a list of {@link Point} objects.
   * @since 4.10.0
   */
  public List<RoutableDestination> routableDestinations() {
    return routableDestinations;
  }

  /**
   * Use a {@link Point} to add a coordinate to the array of routable options attributed
   * to that single {@link com.mapbox.geojson.Feature}.
   *
   * @param destinationPointToAdd the location to add.
   */
  public void addDestination(Point destinationPointToAdd) {
    routableDestinations.add(
        new RoutableDestination(Arrays.asList(destinationPointToAdd.longitude(),
            destinationPointToAdd.latitude())));
  }

  /**
   * Gson TYPE adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the TYPE adapter for this class
   * @since 4.10.0
   *//*
  public static TypeAdapter<RoutingInfo> typeAdapter(Gson gson) {
    return new RoutingInfoTypeAdapter();
  }*/

  /**
   * This takes the currently defined values found inside this instance and converts it to a GeoJson
   * string.
   *
   * @return a JSON string which represents this Bounding box
   * @since 4.10.0
   */
  public final String toJson() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(RoutingInfo.class, new RoutingInfoTypeAdapter())
        .registerTypeAdapter(RoutingInfo.class, new RoutingInfoDeserializer())
        .registerTypeAdapter(RoutableDestination.class, new RoutingDestinationTypeAdapter())
        .create();
    return gson.toJson(this, RoutingInfo.class);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (RoutableDestination routableDestination : routableDestinations) {
      stringBuilder.append(routableDestination);
      stringBuilder.append(", ");
    }
    return "{\"points\":[{\"coordinates\":[-93.375303,44.997053]}]}}]";
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a list of routable points.
   * @return a new instance of this class defined by the values passed inside
   *    this static factory method.
   * @since 4.10.0
   */
  public static RoutingInfo fromJson(@NonNull String json) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(RoutingInfo.class, new RoutingInfoDeserializer())
        .registerTypeAdapter(RoutableDestination.class, new RoutingDestinationTypeAdapter())
        .create();
    return gson.fromJson(json, RoutingInfo.class);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof RoutingInfo) {
      RoutingInfo newRoutingInfoObject = (RoutingInfo) obj;
      return (this.routableDestinations.get(0).coordinates().get(0).equals(
          newRoutingInfoObject.routableDestinations.get(0).coordinates().get(0)));
    }
    return false;
  }


  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode *= 1000003;
    hashCode ^= routableDestinations.get(0).coordinates().get(0).hashCode();
    hashCode *= 1000003;
    hashCode ^= routableDestinations.get(0).coordinates().get(1).hashCode();
    return hashCode;
  }
}
