package com.mapbox.services.api.geocoding.v5.models;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.api.geocoding.v5.gson.CarmenGeometryDeserializer;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.custom.GeometryDeserializer;
import com.mapbox.services.commons.geojson.custom.PositionDeserializer;
import com.mapbox.services.commons.models.Position;

import java.util.Arrays;
import java.util.List;

/**
 * The Features key in the geocoding API response contains the majority of information you'll want
 * to use.
 *
 * @see <a href="https://github.com/mapbox/carmen/blob/master/carmen-geojson.md">Carmen Geojson information</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#geocoding">Mapbox geocoder documentation</a>
 * @see <a href='geojson.org/geojson-spec.html#feature-objects'>Official GeoJSON Feature Specifications</a>
 * @since 1.0.0
 */
public class CarmenFeature extends Feature {

  private String text;
  @SerializedName("place_name")
  private String placeName;
  private double[] bbox;
  private String address;
  private double[] center;
  private List<CarmenContext> context;
  private double relevance;

  /**
   * Private constructor.
   */
  private CarmenFeature(Geometry geometry, JsonObject properties, String id) {
    super(geometry, properties, id);
  }

  public CarmenFeature() {
    super(null, null, null);
  }

  /**
   * @return Text representing the feature (e.g. "Austin").
   * @since 1.0.0
   */
  public String getText() {
    return text;
  }

  /**
   * @return Human-readable text representing the full result hierarchy (e.g. "Austin, Texas, United States").
   * @since 1.0.0
   */
  public String getPlaceName() {
    return placeName;
  }

  /**
   * @return Array bounding box of the form [minx, miny, maxx, maxy].
   * @since 1.0.0
   */
  public double[] getBbox() {
    return bbox;
  }

  /**
   * @return Where applicable. While the string content isn't guaranteed, in many cases, this will be the house number.
   * If the response doesn't contain an address this will be null.
   * @since 1.0.0
   */
  public String getAddress() {
    return address;
  }

  /**
   * @return Array of the form [lon, lat].
   * @since 1.0.0
   */
  public double[] getCenter() {
    return center;
  }

  /**
   * @return Array representing a hierarchy of parents. Each parent includes id, text keys.
   * @since 1.0.0
   */
  public List<CarmenContext> getContext() {
    return context;
  }

  /**
   * You can use the relevance property to remove rough results if you require a response that
   * matches your whole query.
   *
   * @return double value between 0 and 1.
   * @since 1.0.0
   */
  public double getRelevance() {
    return relevance;
  }


  public void setText(String text) {
    this.text = text;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public void setBbox(double[] bbox) {
    this.bbox = bbox;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCenter(double[] center) {
    this.center = center;
  }

  public void setContext(List<CarmenContext> context) {
    this.context = context;
  }

  public void setRelevance(double relevance) {
    this.relevance = relevance;
  }

  /**
   * Create a CarmenFeature object from JSON.
   *
   * @param json String of JSON making up a carmen feature.
   * @return Carmen Feature
   * @since 2.0.0
   */
  public static CarmenFeature fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapter(Position.class, new PositionDeserializer());
    gson.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
    gson.registerTypeAdapter(Geometry.class, new CarmenGeometryDeserializer());
    return gson.create().fromJson(json, CarmenFeature.class);
  }

  /**
   * Util to transform center into a Position object
   *
   * @return a {@link Position} representing the center.
   * @since 1.0.0
   */
  public Position asPosition() {
    return Position.fromCoordinates(center[0], center[1]);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CarmenFeature that = (CarmenFeature) o;

    if (Double.compare(that.getRelevance(), getRelevance()) != 0) {
      return false;
    }
    if (getText() != null ? !getText().equals(that.getText()) : that.getText() != null) {
      return false;
    }
    if (getPlaceName() != null ? !getPlaceName().equals(that.getPlaceName()) : that.getPlaceName() != null) {
      return false;
    }
    if (!Arrays.equals(getBbox(), that.getBbox())) {
      return false;
    }
    if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null) {
      return false;
    }
    if (!Arrays.equals(getCenter(), that.getCenter())) {
      return false;
    }
    return getContext() != null ? getContext().equals(that.getContext()) : that.getContext() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getText() != null ? getText().hashCode() : 0);
    result = 31 * result + (getPlaceName() != null ? getPlaceName().hashCode() : 0);
    result = 31 * result + Arrays.hashCode(getBbox());
    result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
    result = 31 * result + Arrays.hashCode(getCenter());
    result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
    result = 31 * result + Double.valueOf(getRelevance()).hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "CarmenFeature{"
      + "text='" + text + '\''
      + ", placeName='" + placeName + '\''
      + ", bbox=" + Arrays.toString(bbox)
      + ", address='" + address + '\''
      + ", center=" + Arrays.toString(center)
      + ", context=" + context
      + ", relevance=" + relevance
      + '}';
  }
}
