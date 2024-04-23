package com.mapbox.api.geocoding.v6.models;

import java.util.List;

public class ModelDataFactory {

  public static V6Coordinates createV6Coordinates(
    Double longitude,
    Double latitude,
    String accuracy,
    List<V6RoutablePoint> routablePoints
  ) {
    return new AutoValue_V6Coordinates(longitude, latitude, accuracy, routablePoints);
  }

  public static V6RoutablePoint createV6RoutablePoint(
          Double longitude,
          Double latitude,
          String name
  ) {
    return new AutoValue_V6RoutablePoint(longitude, latitude, name);
  }

  public static V6MatchCode createV6MatchCode(
    String addressNumber,
    String street,
    String locality,
    String place,
    String postcode,
    String region,
    String country,
    String confidence
  ) {
    return new AutoValue_V6MatchCode.Builder()
      .addressNumber(addressNumber)
      .street(street)
      .locality(locality)
      .place(place)
      .postcode(postcode)
      .region(region)
      .country(country)
      .confidence(confidence)
      .build();
  }
}
