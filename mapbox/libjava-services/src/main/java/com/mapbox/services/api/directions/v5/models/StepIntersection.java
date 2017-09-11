package com.mapbox.services.api.directions.v5.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.services.commons.geojson.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Object representing an intersection along the step.
 *
 * @since 1.3.0
 */
@AutoValue
public abstract class StepIntersection implements Serializable {

  /**
   * Create a new instance of this class by using the {@link Builder} class.
   *
   * @return this classes {@link Builder} for creating a new instance
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_StepIntersection.Builder();
  }

  /**
   * A {@link Point} representing this intersection location.
   *
   * @return GeoJson Point representing this intersection location
   * @since 3.0.0
   */
  @NonNull
  public Point location() {
    return Point.fromLngLat(rawLocation()[0], rawLocation()[1]);
  }

  /**
   * A {@link Point} representing this intersection location. Since the rawLocation isn't public,
   * it's okay to be mutable as long as nothing in this SDK changes values.
   *
   * @return GeoJson Point representing this intersection location
   * @since 3.0.0
   */
  @SerializedName("location")
  @SuppressWarnings("mutable")
  abstract double[] rawLocation();

  /**
   * An integer list of bearing values available at the step intersection.
   *
   * @return An array of bearing values (for example [0,90,180,270]) that are available at the
   *   intersection. The bearings describe all available roads at the intersection.
   * @since 1.3.0
   */
  @Nullable
  public abstract List<Integer> bearings();

  /**
   * A list of strings signifying the classes of the road exiting the intersection. Possible
   * values:
   * <ul>
   * <li><strong>toll</strong>: the road continues on a toll road</li>
   * <li><strong>ferry</strong>: the road continues on a ferry</li>
   * <li><strong>restricted</strong>: the road continues on with access restrictions</li>
   * <li><strong>motorway</strong>: the road continues on a motorway</li>
   * </ul>
   *
   * @return a string list containing the classes of the road exiting the intersection
   * @since 3.0.0
   */
  @Nullable
  public abstract List<String> classes();

  /**
   * A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value of true
   * indicates that the respective road could be entered on a valid route. false indicates that the
   * turn onto the respective road would violate a restriction.
   *
   * @return a list of entry flags, corresponding in a 1:1 relationship to the bearings
   * @since 1.3.0
   */
  @Nullable
  public abstract List<Boolean> entry();

  /**
   * Index into bearings/entry array. Used to calculate the bearing before the turn. Namely, the
   * clockwise angle from true north to the direction of travel before the maneuver/passing the
   * intersection. To get the bearing in the direction of driving, the bearing has to be rotated by
   * a value of 180. The value is not supplied for departure
   * maneuvers.
   *
   * @return index into bearings/entry array
   * @since 1.3.0
   */
  public abstract int in();

  /**
   * Index out of the bearings/entry array. Used to extract the bearing after the turn. Namely, The
   * clockwise angle from true north to the direction of travel after the maneuver/passing the
   * intersection. The value is not supplied for arrive maneuvers.
   *
   * @return index out of the bearings/entry array
   * @since 1.3.0
   */
  public abstract int out();

  /**
   * Array of lane objects that represent the available turn lanes at the intersection. If no lane
   * information is available for an intersection, the lanes property will not be present. Lanes are
   * provided in their order on the street, from left to right.
   *
   * @return array of lane objects that represent the available turn lanes at the intersection
   * @since 2.0.0
   */
  @Nullable
  public abstract List<IntersectionLanes> lanes();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<StepIntersection> typeAdapter(Gson gson) {
    return new AutoValue_StepIntersection.GsonTypeAdapter(gson);
  }

  /**
   * This builder can be used to set the values describing the {@link StepIntersection}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * An integer array of bearing values available at the step intersection.
     *
     * @param bearing An array of bearing values (for example [0,90,180,270]) that are available at
     *                the intersection. The bearings describe all available roads at the
     *                intersection.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearings(@Nullable List<Integer> bearing);

    /**
     * A list of strings signifying the classes of the road exiting the intersection. Possible
     * values:
     * <ul>
     * <li><strong>toll</strong>: the road continues on a toll road</li>
     * <li><strong>ferry</strong>: the road continues on a ferry</li>
     * <li><strong>restricted</strong>: the road continues on with access restrictions</li>
     * <li><strong>motorway</strong>: the road continues on a motorway</li>
     * </ul>
     *
     * @param classes a list of strings containing the classes of the road exiting the intersection
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder classes(@Nullable List<String> classes);

    /**
     * A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value of true
     * indicates that the respective road could be entered on a valid route. false indicates that
     * the turn onto the respective road would violate a restriction.
     *
     * @param entry a {@link Boolean} list of entry flags, corresponding in a 1:1 relationship to
     *              the bearings
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder entry(@Nullable List<Boolean> entry);

    /**
     * Index into bearings/entry array. Used to calculate the bearing before the turn. Namely, the
     * clockwise angle from true north to the direction of travel before the maneuver/passing the
     * intersection. To get the bearing in the direction of driving, the bearing has to be rotated
     * by a value of 180. The value is not supplied for departure
     * maneuvers.
     *
     * @param in index into bearings/entry array
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder in(int in);

    /**
     * Index out of the bearings/entry array. Used to extract the bearing after the turn. Namely,
     * The clockwise angle from true north to the direction of travel after the maneuver/passing the
     * intersection. The value is not supplied for arrive maneuvers.
     *
     * @param out index out of the bearings/entry array
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder out(int out);

    /**
     * Array of lane objects that represent the available turn lanes at the intersection. If no lane
     * information is available for an intersection, the lanes property will not be present. Lanes
     * are provided in their order on the street, from left to right.
     *
     * @param lanes array of lane objects that represent the available turn lanes at the
     *              intersection
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder lanes(List<IntersectionLanes> lanes);

    /**
     * The rawLocation as a double array. Once the {@link StepIntersection} object's created,
     * this raw location gets converted into a {@link Point} object and is public exposed as such.
     * The double array should have a length of two, index 0 being the longitude and index 1 being
     * latitude.
     *
     * @param rawLocation a double array with a length of two, index 0 being the longitude and
     *                    index 1 being latitude.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder rawLocation(double[] rawLocation);

    /**
     * Build a new {@link StepIntersection} object.
     *
     * @return a new {@link StepIntersection} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract StepIntersection build();
  }
}
