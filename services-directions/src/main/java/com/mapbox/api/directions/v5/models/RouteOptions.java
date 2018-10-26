package com.mapbox.api.directions.v5.models;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.PointDeserializer;

import java.util.List;

/**
 * Provides information connected to your request that help when a new directions request is needing
 * using the identical parameters as the original request.
 * <p>
 * For example, if I request a driving (profile) with alternatives and continueStraight set to true.
 * I make the request but loose reference and information which built the original request. Thus, If
 * I only want to change a single variable such as the destination coordinate, i'd have to have all
 * the other route information stores so the request was made identical to the previous but only now
 * using this new destination point.
 * <p>
 * Using this class can provide you wth the information used when the {@link DirectionsRoute} was
 * made.
 *
 * @since 3.0.0
 */
@AutoValue
public abstract class RouteOptions extends DirectionsJsonObject {

  /**
   * Build a new instance of this RouteOptions class optionally settling values.
   *
   * @return {@link RouteOptions.Builder}
   * @since 3.0.0
   */
  public static Builder builder() {
    return new AutoValue_RouteOptions.Builder();
  }

  /**
   * The same base URL which was used during the request that resulted in this root directions
   * response.
   *
   * @return string value representing the base URL
   * @since 3.0.0
   */
  @NonNull
  public abstract String baseUrl();

  /**
   * The same user which was used during the request that resulted in this root directions response.
   *
   * @return string value representing the user
   * @since 3.0.0
   */
  @NonNull
  public abstract String user();

  /**
   * The same profile which was used during the request that resulted in this root directions
   * response. {@link MapboxDirections#builder()} ensures that a profile is always set even if the
   * {@link MapboxDirections} requesting object doesn't specifically set a profile.
   *
   * @return string value representing the profile
   * @since 3.0.0
   */
  @NonNull
  public abstract String profile();

  /**
   * The coordinates used for the routes origin, destination, and optionally, waypoints. Note that
   * these coordinates are different than the direction responses {@link DirectionsWaypoint}s in
   * that these are the non-snapped coordinates.
   *
   * @return a list of {@link Point}s which represent the route origin, destination,
   *   and optionally, waypoints
   * @since 3.0.0
   */
  @NonNull
  public abstract List<Point> coordinates();

  /**
   * The same alternative setting which were used during the request that resulted in this root
   * directions response.
   *
   * @return boolean object representing the setting for alternatives
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean alternatives();

  /**
   * The same language which was used during the request that resulted in this root directions
   * response.
   *
   * @return the language as a string used during the request,
   *   if english, this will most likely be empty
   * @since 3.0.0
   */
  @Nullable
  public abstract String language();

  /**
   * The same radiuses were used during the request that resulted in this root directions response.
   *
   * @return a string representing the radiuses
   * @since 3.0.0
   */
  @Nullable
  public abstract String radiuses();

  /**
   * The same bearings which were used during the request that resulted in this root directions
   * response. Note that even though these are saved. it's a good idea to recalculate any bearings
   * being used and use the newer values for the directions request.
   *
   * @return a string representing the bearings used in the original request
   * @since 3.0.0
   */
  @Nullable
  public abstract String bearings();

  /**
   * The same continueStraight setting which was used during the request that resulted in this root
   * directions response.
   *
   * @return a boolean value representing whether or not continueStraight was enabled or
   *   not during the initial request
   * @since 3.0.0
   */
  @SerializedName("continue_straight")
  @Nullable
  public abstract Boolean continueStraight();

  /**
   * This is set to true if you want to enable instructions while exiting roundabouts
   * and rotaries.
   *
   * @return a boolean value representing whether or not roundaboutExits was enabled or disabled
   *   during the initial route request
   * @since 3.1.0
   */
  @SerializedName("roundabout_exits")
  @Nullable
  public abstract Boolean roundaboutExits();

  /**
   * Geometry type used to make the initial directions request.
   *
   * @return String geometry type used to make the initial directions request.
   * @since 3.1.0
   */
  public abstract String geometries();

  /**
   * Type of returned overview geometry that was used to make the initial directions request.
   *
   * @return null or one of the options found in
   *   {@link DirectionsCriteria.OverviewCriteria}
   * @since 3.1.0
   */
  @Nullable
  public abstract String overview();

  /**
   * Boolean value used to determine whether to return steps and turn-by-turn instructions in the
   * initial directions request.
   *
   * @return true if you'd like step information, false otherwise
   * @since 3.1.0
   */
  @Nullable
  public abstract Boolean steps();

  /**
   * The same annotations in String format which were used during the request that resulted in this
   * root directions response.
   *
   * @return a string containing any of the annotations that were used during the request
   * @since 3.0.0
   */
  @Nullable
  public abstract String annotations();

  /**
   * The same exclusions the user originally made when the request was made.
   *
   * @return a string matching one of the {@link DirectionsCriteria} exclusions
   * @since 3.0.0
   */
  @Nullable
  public abstract String exclude();

  /**
   * Whether or not the request had voice instructions set to true or not.
   *
   * @return true if the original request included voice instructions
   * @since 3.0.0
   */
  @SerializedName("voice_instructions")
  @Nullable
  public abstract Boolean voiceInstructions();

  /**
   * Whether or not the request had banner instructions set to true or not.
   *
   * @return true if the original request included banner instructions
   * @since 3.0.0
   */
  @SerializedName("banner_instructions")
  @Nullable
  public abstract Boolean bannerInstructions();

  /**
   * Whether or not the units used inside the voice instruction's string are in imperial or metric.
   *
   * @return a string matching either imperial or metric
   * @since 3.0.0
   */
  @SerializedName("voice_units")
  @Nullable
  public abstract String voiceUnits();

  /**
   * A valid Mapbox access token used to making the request.
   *
   * @return a string representing the Mapbox access token
   * @since 3.0.0
   */
  @SerializedName("access_token")
  @NonNull
  public abstract String accessToken();

  /**
   * A universally unique identifier (UUID) for identifying and executing a similar specific route
   * in the future. {@link MapboxDirections} always waits for the response object which ensures this
   * value will never be null.
   *
   * @return a string containing the request UUID
   * @since 3.0.0
   */
  @SerializedName("uuid")
  @NonNull
  public abstract String requestUuid();

  /**
   * Indicates from which side of the road to approach a waypoint.
   * Accepts  unrestricted (default) or  curb . If set to  unrestricted ,
   * the route can approach waypoints from either side of the road.
   * If set to curb, the route will be returned so that on arrival,
   * the waypoint will be found on the side that corresponds with the  driving_side of the region
   * in which the returned route is located.
   * If provided, the list of approaches must be the same length as the list of waypoints.
   * However, you can skip a coordinate and show its position in the list with the  ; separator.
   *
   * @return a string representing approaches for each waypoint
   * @since 3.2.0
   */

  @Nullable
  public abstract String approaches();

  /**
   * Custom names for waypoints used for the arrival instruction in banners and voice instructions,
   * each separated by  ; . Values can be any string and total number of all characters cannot
   * exceed 500. If provided, the list of waypoint_names must be the same length as the list of
   * coordinates, but you can skip a coordinate and show its position with the  ; separator.
   *
   * @return a string representing names for each waypoint
   * @since 3.3.0
   */
  @SerializedName("waypoint_names")
  @Nullable
  public abstract String waypointNames();

  /**
   * The type of bicycle, either `Road`, `Hybrid`, `City`, `Cross`, `Mountain`. The default type is
   * Hybrid.
   *
   * @return the type of bicycle
   * @since 4.1.0
   */
  @SerializedName("bicycle_type")
  @Nullable
  public abstract String bicycleType();

  /**
   * Cycling speed is the average travel speed along smooth, flat roads. This is meant to be the
   * speed a rider can comfortably maintain over the desired distance of the route. It can be
   * modified (in the costing method) by surface type in conjunction with bicycle type and
   * (coming soon) by hilliness of the road section. When no speed is specifically provided, the
   * default speed is determined by the bicycle type and are as follows: Road = 25 KPH (15.5 MPH),
   * Cross = 20 KPH (13 MPH), Hybrid/City = 18 KPH (11.5 MPH), and Mountain = 16 KPH (10 MPH).
   *
   * @return the cycling speed in kmh
   * @since 4.1.0
   */
  @SerializedName("cycling_speed")
  @Nullable
  public abstract Float cyclingSpeed();

  /**
   * A cyclist's propensity to use roads alongside other vehicles. This is a range of values from 0
   * to 1, where 0 attempts to avoid roads and stay on cycleways and paths, and 1 indicates the
   * rider is more comfortable riding on roads. Based on the use_roads factor, roads with certain
   * classifications and higher speeds are penalized in an attempt to avoid them when finding the
   * best path. The default value is 0.5.
   *
   * @return a cyclist's propensity to use roads alongside other vehicles
   * @since 4.1.0
   */
  @SerializedName("use_roads")
  @Nullable
  public abstract Float useRoads();

  /**
   * A cyclist's desire to tackle hills in their routes. This is a range of values from 0 to 1,
   * where 0 attempts to avoid hills and steep grades even if it means a longer (time and
   * distance) path, while 1 indicates the rider does not fear hills and steeper grades. Based on
   * the use_hills factor, penalties are applied to roads based on elevation change and grade.
   * These penalties help the path avoid hilly roads in favor of flatter roads or less steep
   * grades where available. Note that it is not always possible to find alternate paths to avoid
   * hills (for example when route locations are in mountainous areas). The default value is 0.5.
   *
   * @return a cyclist's desire to tackle hills in their routes
   * @since 4.1.0
   */
  @SerializedName("use_hills")
  @Nullable
  public abstract Float useHills();

  /**
   * This value indicates the willingness to take ferries. This is a range of values between 0 and
   * 1. Values near 0 attempt to avoid ferries and values near 1 will favor ferries. Note that
   * sometimes ferries are required to complete a route so values of 0 are not guaranteed to avoid
   * ferries entirely.
   * The default value is 0.5.
   *
   * @return the willingness to take ferries
   * @since 4.1.0
   */
  @SerializedName("use_ferry")
  @Nullable
  public abstract Float useFerry();

  /**
   * This value is meant to represent how much a cyclist wants to avoid roads with poor surfaces
   * relative to the bicycle type being used. This is a range of values between 0 and 1. When the
   * value is 0, there is no penalization of roads with different surface types; only bicycle
   * speed on each surface is taken into account. As the value approaches 1, roads with poor
   * surfaces for the bike are penalized heavier so that they are only taken if they
   * significantly improve travel time. When the value is equal to 1, all bad surfaces are
   * completely disallowed from routing, including start and end points. The default value is 0.25.
   *
   * @return how much a cyclist wants to avoid roads with poor surfaces
   * @since 4.1.0
   */
  @SerializedName("avoid_bad_surfaces")
  @Nullable
  public abstract Float avoidBadSurfaces();

  /**
   * Type of location, accepts  break (default), through or null. A break is a stop,
   * so the first and last locations must be of type break. A through location is one
   * that the route path travels through, and is useful to force a route to go through location.
   * The path is not allowed to reverse direction at the through locations.
   * If no type is provided, the type is assumed to be a break.
   * If provided, the list of waypoint types must be the same length as the list of waypoints.
   * However, you can skip a coordinate and show its position in the list with the ; separator.
   *
   * @return a string representing waypoint types for each waypoint
   */
  @SerializedName("waypoint_types")
  @Nullable
  public abstract String waypointTypes();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   * @since 3.0.0
   */
  public static TypeAdapter<RouteOptions> typeAdapter(Gson gson) {
    return new AutoValue_RouteOptions.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining a RouteOptions
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @since 3.4.0
   */
  public static RouteOptions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointDeserializer());
    return gson.create().fromJson(json, RouteOptions.class);
  }

  /**
   * This builder can be used to set the values describing the {@link RouteOptions}.
   *
   * @since 3.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * The base URL that was used during the request time and resulted in this responses
     * result.
     *
     * @param baseUrl base URL used for original request
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * The user value that was used during the request.
     *
     * @param user string representing the user field in the calling url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * The directions profile that was used during the request time and resulted in this responses
     * result.
     *
     * @param profile One of the direction profiles defined in
     *                {@link DirectionsCriteria#DirectionsCriteria()}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

    /**
     * The coordinates used for the routes origin, destination, and optionally, waypoints. Note that
     * these coordinates are different than the direction responses {@link DirectionsWaypoint}s in
     * that these are the non-snapped coordinates.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally, waypoints
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder coordinates(@NonNull List<Point> coordinates);

    /**
     * Whether the alternatives value was set to true or not.
     *
     * @param alternatives true if the request contained additional route request, otherwise false
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder alternatives(@Nullable Boolean alternatives);

    /**
     * The language for instructions to be in when the response is given.
     *
     * @param language a string with the language which was requested in the url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder language(String language);

    /**
     * The radiuses in string format that were used during the original request.
     *
     * @param radiuses radiuses values separated by comma
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder radiuses(String radiuses);

    /**
     * The bearing values the user used for the original request which resulted in this response.
     * It is best to recalculate these values since they are probably outdated already.
     *
     * @param bearings number values representing the bearings separated by commas
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearings(String bearings);

    /**
     * Whether the original request wanted continueStraight enabled or not.
     *
     * @param continueStraight true if you'd like the user to continue straight from the starting
     *                         point
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder continueStraight(Boolean continueStraight);

    /**
     * This is set to true if you want to enable instructions while exiting roundabouts
     * and rotaries.
     *
     * @param roundaboutExits true if you'd like extra roundabout instructions
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder roundaboutExits(@Nullable Boolean roundaboutExits);

    /**
     * alter the default geometry being returned for the directions route. A null value will reset
     * this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @param geometries null if you'd like the default geometry, else one of the options found in
     *                   {@link DirectionsCriteria.GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder geometries(@DirectionsCriteria.GeometriesCriteria String geometries);

    /**
     * Type of returned overview geometry that was used to make the initial directions request.
     *
     * @param overview null or one of the options found in
     *                 {@link DirectionsCriteria.OverviewCriteria}
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder overview(
      @Nullable @DirectionsCriteria.OverviewCriteria String overview
    );

    /**
     * Boolean value used to determine whether to return steps and turn-by-turn instructions in the
     * initial directions request.
     *
     * @param steps true if you'd like step information, false otherwise
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder steps(@Nullable Boolean steps);

    /**
     * The annotation which were used during the request process.
     *
     * @param annotations in string format and separated by commas if more than one annotation was
     *                    requested
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder annotations(String annotations);

    /**
     * Whether or not the request had voice instructions set to true or not.
     *
     * @param voiceInstructions true if the original request included voice instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(Boolean voiceInstructions);

    /**
     * Whether or not the request had banner instructions set to true or not.
     *
     * @param bannerInstructions true if the original request included banner instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bannerInstructions(Boolean bannerInstructions);

    /**
     * Whether or not the units used inside the voice instruction's string are
     * in imperial or metric.
     *
     * @param voiceUnits string matching either imperial or metric
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceUnits(@Nullable String voiceUnits);

    /**
     * A valid Mapbox access token used to making the request.
     *
     * @param accessToken a string containing a valid Mapbox access token
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * A universally unique identifier (UUID) for identifying and executing a similar specific route
     * in the future.
     *
     * @param requestUuid a string containing the request UUID
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder requestUuid(@NonNull String requestUuid);

    /**
     * The same exclusions the user originally made when the request was made.
     *
     * @param exclude a string matching one of the {@link DirectionsCriteria} exclusions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    @Nullable
    public abstract Builder exclude(@NonNull String exclude);

    /**
     * The same approaches the user originally made when the request was made.
     *
     * @param approaches unrestricted, curb or omitted (;)
     * @return this builder for chaining options together
     * @since 3.2.0
     */
    @Nullable
    public abstract Builder approaches(String approaches);

    /**
     * The same approaches the user originally made when the request was made.
     *
     * @param waypointNames unrestricted, curb or omitted (;)
     * @return this builder for chaining options together
     * @since 3.3.0
     */
    @Nullable
    public abstract Builder waypointNames(@Nullable String waypointNames);

    /**
     * The type of bicycle, either `Road`, `Hybrid`, `City`, `Cross`, `Mountain`. The default type
     * is Hybrid.
     *
     * @param bicycleType the type of bicycle
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder bicycleType(@Nullable String bicycleType);

    /**
     * Cycling speed is the average travel speed along smooth, flat roads. This is meant to be the
     * speed a rider can comfortably maintain over the desired distance of the route. It can be
     * modified (in the costing method) by surface type in conjunction with bicycle type and
     * (coming soon) by hilliness of the road section. When no speed is specifically provided,
     * the default speed is determined by the bicycle type and are as follows: Road = 25 KPH (15
     * .5 MPH), Cross = 20 KPH (13 MPH), Hybrid/City = 18 KPH (11.5 MPH), and Mountain = 16 KPH
     * (10 MPH).
     *
     * @param cyclingSpeed in kmh
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder cyclingSpeed(
            @Nullable @FloatRange(from = 5, to = 60) Float cyclingSpeed);

    /**
     * A cyclist's propensity to use roads alongside other vehicles. This is a range of values from
     * 0 to 1, where 0 attempts to avoid roads and stay on cycleways and paths, and 1 indicates
     * the rider is more comfortable riding on roads. Based on the use_roads factor, roads with
     * certain classifications and higher speeds are penalized in an attempt to avoid them when
     * finding the best path. The default value is 0.5.
     *
     * @param useRoads a cyclist's propensity to use roads alongside other vehicles
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder useRoads(@Nullable @FloatRange(from = 0.0, to = 1.0) Float useRoads);

    /**
     * A cyclist's desire to tackle hills in their routes. This is a range of values from 0 to 1,
     * where 0 attempts to avoid hills and steep grades even if it means a longer (time and
     * distance) path, while 1 indicates the rider does not fear hills and steeper grades. Based
     * on the use_hills factor, penalties are applied to roads based on elevation change and
     * grade. These penalties help the path avoid hilly roads in favor of flatter roads or less
     * steep grades where available. Note that it is not always possible to find alternate paths
     * to avoid hills (for example when route locations are in mountainous areas). The default
     * value is 0.5.
     *
     * @param useHills a cyclist's desire to tackle hills in their routes
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder useHills(@Nullable @FloatRange(from = 0.0, to = 1.0) Float useHills);

    /**
     * This value indicates the willingness to take ferries. This is a range of values between 0 and
     * 1. Values near 0 attempt to avoid ferries and values near 1 will favor ferries. Note that
     * sometimes ferries are required to complete a route so values of 0 are not guaranteed to
     * avoid ferries entirely. The default value is 0.5.
     *
     * @param useFerry the willingness to take ferries
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder useFerry(@Nullable @FloatRange(from = 0.0, to = 1.0) Float useFerry);

    /**
     * This value is meant to represent how much a cyclist wants to avoid roads with poor surfaces
     * relative to the bicycle type being used. This is a range of values between 0 and 1. When
     * the value is 0, there is no penalization of roads with different surface types; only
     * bicycle speed on each surface is taken into account. As the value approaches 1, roads with
     * poor surfaces for the bike are penalized heavier so that they are only taken if they
     * significantly improve travel time. When the value is equal to 1, all bad surfaces are
     * completely disallowed from routing, including start and end points. The default value is 0
     * .25.
     *
     * @param avoidBadSurfaces how much a cyclist wants to avoid roads with poor surfaces
     * @return this builder for chaining options together
     * @since 4.1.0
     */
    @Nullable
    public abstract Builder avoidBadSurfaces(
            @Nullable @FloatRange(from = 0.0, to = 1.0) Float avoidBadSurfaces);

    /**
     * The same waypoint types the user originally made when the request was made.
     *
     * @param waypointTypes break, through or omitted (;)
     * @return this builder for chaining options together
     */
    @Nullable
    public abstract Builder waypointTypes(@Nullable String waypointTypes);

    /**
     * Builds a new instance of the {@link RouteOptions} object.
     *
     * @return a new {@link RouteOptions} instance
     * @since 3.0.0
     */
    public abstract RouteOptions build();
  }
}
