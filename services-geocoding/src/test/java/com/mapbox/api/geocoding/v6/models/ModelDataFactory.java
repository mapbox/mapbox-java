package com.mapbox.api.geocoding.v6.models;

public class ModelDataFactory {

  public static V6Coordinates createV6Coordinates(
    Double longitude,
    Double latitude,
    String accuracy
  ) {
    return new AutoValue_V6Coordinates(longitude, latitude, accuracy);
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
