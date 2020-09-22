package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Point;

import java.util.List;

/**
 * Object representing an intersection along the step.
 *
 * @since 1.3.0
 */
@AutoValue
public abstract class StepIntersection extends DirectionsJsonObject {

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
  @NonNull
  @SerializedName("location")
  @SuppressWarnings( {"mutable", "WeakerAccess"})
  protected abstract double[] rawLocation();

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
   * <li><strong>tunnel</strong>: the road continues on a tunnel</li>
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
  @Nullable
  public abstract Integer in();

  /**
   * Index out of the bearings/entry array. Used to extract the bearing after the turn. Namely, The
   * clockwise angle from true north to the direction of travel after the maneuver/passing the
   * intersection. The value is not supplied for arrive maneuvers.
   *
   * @return index out of the bearings/entry array
   * @since 1.3.0
   */
  @Nullable
  public abstract Integer out();

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
   * The zero-based index for the intersection.
   * This value can be used to apply the duration annotation that corresponds with the intersection.
   * Only available on the driving profile.
   *
   * @return index for the intersection
   */
  @Nullable
  @SerializedName("geometry_index")
  public abstract Integer geometryIndex();

  /**
   * A boolean indicating whether the road exiting the intersection is considered to be in an urban
   * area. This value is determined by the density of the surrounding road network.
   * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
   *
   * @return a value indicating whether the road exiting the intersection is in an urban area
   */
  @Nullable
  @SerializedName("is_urban")
  public abstract Boolean isUrban();

  /**
   * The zero-based index into the admin list on the route leg for this intersection.
   * Use this field to look up the ISO-3166-1 country code for this point on the route.
   * Only available on the `driving` profile.
   *
   * @return a zero-based index into the admin list on the route leg.
   * @see RouteLeg#admins()
   */
  @Nullable
  @SerializedName("admin_index")
  public abstract Integer adminIndex();

  /**
   * An object containing information about passing rest stops along the route.
   * Only available on the `driving` profile.
   *
   * @return an object containing information about passing rest stops along the route.
   */
  @Nullable
  @SerializedName("rest_stop")
  public abstract RestStop restStop();

  /**
   * An object containing detailed information about the road exiting the intersection along the
   * route. Properties in this object correspond to properties in the {@link #classes()}
   * specification. Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
   *
   * @return an object containing detailed road information.
   */
  @Nullable
  @SerializedName("mapbox_streets_v8")
  public abstract MapboxStreetsV8 mapboxStreetsV8();

  /**
   * Convert the current {@link StepIntersection} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link StepIntersection}.
   *
   * @return a {@link StepIntersection.Builder} with the same values set to match the ones defined
   *   in this {@link StepIntersection}
   * @since 3.1.0
   */
  public abstract Builder toBuilder();

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
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a StepIntersection
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static StepIntersection fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, StepIntersection.class);
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
    public abstract Builder in(@Nullable Integer in);

    /**
     * Index out of the bearings/entry array. Used to extract the bearing after the turn. Namely,
     * The clockwise angle from true north to the direction of travel after the maneuver/passing the
     * intersection. The value is not supplied for arrive maneuvers.
     *
     * @param out index out of the bearings/entry array
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder out(@Nullable Integer out);

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
    public abstract Builder lanes(@Nullable List<IntersectionLanes> lanes);

    /**
     * The zero-based index for the intersection.
     * This value can be used to apply the duration annotation
     * that corresponds with the intersection.
     * Only available on the driving profile.
     *
     * @param geometryIndex index for the intersection
     * @return this builder for chaining options together
     */
    public abstract Builder geometryIndex(@Nullable Integer geometryIndex);

    /**
     * A boolean indicating whether the road exiting the intersection is considered to be in an
     * urban area. This value is determined by the density of the surrounding road network.
     * Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
     *
     * @param isUrban indicating whether the road exiting the intersection is in an urban area
     * @return this builder for chaining options together
     */
    @Nullable
    public abstract Builder isUrban(@Nullable Boolean isUrban);

    /**
     * The zero-based index into the admin list on the route leg for this intersection.
     * Use this field to look up the ISO-3166-1 country code for this point on the route.
     * Only available on the `driving` profile.
     *
     * @param adminIndex zero-based index into the admin list on the route leg for this intersection
     * @return this builder for chaining options together
     */
    @Nullable
    public abstract Builder adminIndex(@Nullable Integer adminIndex);

    /**
     * An object containing information about passing rest stops along the route.
     * Only available on the `driving` profile.
     *
     * @param restStop object containing information about passing rest stops along the route.
     * @return this builder for chaining options together
     */
    @Nullable
    public abstract Builder restStop(@Nullable RestStop restStop);

    /**
     * An object containing detailed information about the road exiting the intersection along the
     * route. Properties in this object correspond to properties in the {@link #classes()}
     * specification. Only available on the {@link DirectionsCriteria#PROFILE_DRIVING} profile.
     *
     * @param street an object containing detailed road information.
     * @return this builder for chaining options together
     */
    @Nullable
    public abstract Builder mapboxStreetsV8(@Nullable MapboxStreetsV8 street);

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
    public abstract Builder rawLocation(@NonNull double[] rawLocation);

    /**
     * Build a new {@link StepIntersection} object.
     *
     * @return a new {@link StepIntersection} using the provided values in this builder
     * @since 3.0.0
     */
    public abstract StepIntersection build();
  }
}
