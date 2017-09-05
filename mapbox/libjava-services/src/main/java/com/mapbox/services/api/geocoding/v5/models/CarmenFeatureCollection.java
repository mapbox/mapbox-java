package com.mapbox.services.api.geocoding.v5.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;

import java.util.Arrays;
import java.util.List;

/**
 * The Geocoding API hierarchy starts with this class.
 *
 * @since 1.0.0
 */
@AutoValue
public abstract class CarmenFeatureCollection {

  private static final String type = "FeatureCollection";

  public abstract List<String> query();

  public abstract String attribution();

  public abstract List<CarmenFeature> features();

  public static CarmenFeatureCollection fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    return gson.create().fromJson(json, CarmenFeatureCollection.class);
  }

  public static CarmenFeatureCollection fromFeatures(List<CarmenFeature> features) {
    return new AutoValue_CarmenFeatureCollection(features);
  }

  public static FeatureCollection fromFeatures(Feature[] features) {
    return new AutoValue_CarmenFeatureCollection(Arrays.asList(features));
  }

  public String type() {
    return type;
  }


  public String toJson() {
    return new Gson().toJson(this);
  }
}
