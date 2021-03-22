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
import com.mapbox.api.directions.v5.WalkingOptions;
import com.mapbox.api.directions.v5.WalkingOptionsAdapterFactory;
import com.mapbox.api.directions.v5.utils.FormatUtils;
import com.mapbox.api.directions.v5.utils.ParseUtils;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.PointAsCoordinatesTypeAdapter;
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
   * The routing profile to use. Possible values are
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}, {@link DirectionsCriteria#PROFILE_DRIVING},
   * {@link DirectionsCriteria#PROFILE_WALKING}, or {@link DirectionsCriteria#PROFILE_CYCLING}.
   * The same profile which was used during the request that resulted in this root directions
   * response. <tt>MapboxDirections.Builder</tt> ensures that a profile is always set even if the
   * <tt>MapboxDirections</tt> requesting object doesn't specifically set a profile.
   *
   * @return string value representing the profile defined in
   *         {@link DirectionsCriteria.ProfileCriteria}
   * @since 3.0.0
   */
  @NonNull
  public abstract String profile();

  /**
   * A list of Points to visit in order.
   * There can be between two and 25 coordinates for most requests, or up to three coordinates for
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
   * Note that these coordinates are different than the direction responses
   * {@link DirectionsWaypoint}s that these are the non-snapped coordinates.
   *
   * @return a list of {@link Point}s which represent the route origin, destination,
   *   and optionally, waypoints
   * @since 3.0.0
   */
  @NonNull
  public abstract List<Point> coordinates();

  /**
   * Whether to try to return alternative routes (true) or not (false, default). An alternative
   * route is a route that is significantly different than the fastest route, but also still
   * reasonably fast. Such a route does not exist in all circumstances. Up to two alternatives may
   * be returned. This is available for {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
   * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_CYCLING}.
   *
   * @return boolean object representing the setting for alternatives
   * @since 3.0.0
   */
  @Nullable
  public abstract Boolean alternatives();

  /**
   * The language of returned turn-by-turn text instructions. The default is en (English).
   * Must be used in conjunction with {@link RouteOptions.Builder#steps(Boolean)}.
   *
   * @return the language as a string used during the request,
   *   if english, this will most likely be empty
   * @since 3.0.0
   */
  @Nullable
  public abstract String language();

  /**
   * The maximum distance a coordinate can be moved to snap to the road network in meters. There
   * must be as many radiuses as there are coordinates in the request, each separated by ;.
   * Values can be any number greater than 0, the string unlimited or empty string.
   *
   * @return a string representing the radiuses separated by ;.
   * @since 3.0.0
   */
  @Nullable
  public abstract String radiuses();

  /**
   * The maximum distance a coordinate can be moved to snap to the road network in meters. There
   * must be as many radiuses as there are coordinates in the request.
   * Values can be any number greater than 0, the string unlimited, or null.
   *
   * @return a list of radiuses
   */
  @Nullable
  public List<Double> radiusesList() {
    return ParseUtils.parseToDoubles(radiuses());
  }

  /**
   * Influences the direction in which a route starts from a waypoint. Used to filter the road
   * segment the waypoint will be placed on by direction. This is useful for making sure the new
   * routes of rerouted vehicles continue traveling in their current direction. A request that does
   * this would provide bearing and radius values for the first waypoint and leave the remaining
   * values empty. Returns two comma-separated values per waypoint: an angle clockwise from true
   * north between 0 and 360, and the range of degrees by which the angle can deviate (recommended
   * value is 45° or 90°), formatted as {angle, degrees}. If provided, the list of bearings must be
   * the same length as the list of coordinates.
   *
   * @return a string representing the bearings with the ; separator. Angle and degrees for every
   *   bearing value are comma-separated.
   * @since 3.0.0
   */
  @Nullable
  public abstract String bearings();

  /**
   * Influences the direction in which a route starts from a waypoint. Used to filter the road
   * segment the waypoint will be placed on by direction. This is useful for making sure the new
   * routes of rerouted vehicles continue traveling in their current direction. A request that does
   * this would provide bearing and radius values for the first waypoint and leave the remaining
   * values empty. Returns a list of values, each value is a list of an angle clockwise from true
   * north between 0 and 360, and the range of degrees by which the angle can deviate (recommended
   * value is 45° or 90°).
   * If provided, the list of bearings must be the same length as the list of coordinates.
   *
   * @return a List of list of doubles representing the bearings used in the original request.
   *         The first value in the list is the angle, the second one is the degrees.
   */
  @Nullable
  public List<List<Double>> bearingsList() {
    return ParseUtils.parseToListOfListOfDoubles(bearings());
  }

  /**
   * The allowed direction of travel when departing intermediate waypoints. If true, the route
   * will continue in the same direction of travel. If false, the route may continue in the opposite
   * direction of travel. Defaults to true for {@link DirectionsCriteria#PROFILE_DRIVING} and false
   * for {@link DirectionsCriteria#PROFILE_WALKING} and {@link DirectionsCriteria#PROFILE_CYCLING}.
   *
   * @return a boolean value representing whether or not continueStraight was enabled or
   *   not during the initial request
   * @since 3.0.0
   */
  @SerializedName("continue_straight")
  @Nullable
  public abstract Boolean continueStraight();

  /**
   * Whether to emit instructions at roundabout exits (true) or not (false, default). Without
   * this parameter, roundabout maneuvers are given as a single instruction that includes both
   * entering and exiting the roundabout. With roundabout_exits=true, this maneuver becomes two
   * instructions, one for entering the roundabout and one for exiting it. Must be used in
   * conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return a boolean value representing whether or not roundaboutExits was enabled or disabled
   *   during the initial route request
   * @since 3.1.0
   */
  @SerializedName("roundabout_exits")
  @Nullable
  public abstract Boolean roundaboutExits();

  /**
   * The format of the returned geometry. Allowed values are:
   * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (default, a polyline with a precision of five
   * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6} (a polyline with a precision
   * of six decimal places).
   *
   * @return String geometry type from {@link DirectionsCriteria.GeometriesCriteria}.
   * @since 3.1.0
   */
  @Nullable
  public abstract String geometries();

  /**
   * Displays the requested type of overview geometry. Can be
   * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
   * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default, a simplified version of
   * the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
   *
   * @return null or one of the options found in {@link DirectionsCriteria.OverviewCriteria}
   * @since 3.1.0
   */
  @Nullable
  public abstract String overview();

  /**
   * Whether to return steps and turn-by-turn instructions (true) or not (false, default).
   * If steps is set to true, the following guidance-related parameters will be available:
   * {@link RouteOptions#bannerInstructions()}, {@link RouteOptions#language()},
   * {@link RouteOptions#roundaboutExits()}, {@link RouteOptions#voiceInstructions()},
   * {@link RouteOptions#voiceUnits()}, {@link RouteOptions#waypointNamesList()},
   * {@link RouteOptions#waypointTargetsList()}, waypoints from {@link RouteOptions#coordinates()}
   *
   * @return true if you'd like step information, false otherwise
   * @since 3.1.0
   */
  @Nullable
  public abstract Boolean steps();

  /**
   * A comma-separated list of annotations. Defines whether to return additional metadata along the
   * route. Possible values are:
   * {@link DirectionsCriteria#ANNOTATION_DURATION}
   * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
   * {@link DirectionsCriteria#ANNOTATION_SPEED}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
   * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
   * See the {@link RouteLeg} object for more details on what is included with annotations.
   * Must be used in conjunction with overview=full.
   *
   * @return a string containing any of the annotations that were used during the request
   * @since 3.0.0
   */
  @Nullable
  public abstract String annotations();

  /**
   * A list of annotations. Defines whether to return additional metadata along the
   * route. Possible values are:
   * {@link DirectionsCriteria#ANNOTATION_DURATION}
   * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
   * {@link DirectionsCriteria#ANNOTATION_SPEED}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
   * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
   * See the {@link RouteLeg} object for more details on what is included with annotations.
   * Must be used in conjunction with overview=full.
   *
   * @return a list of annotations that were used during the request
   */
  @Nullable
  public List<String> annotationsList() {
    return ParseUtils.parseToStrings(annotations(), ",");
  }

  /**
   * Exclude certain road types from routing. The default is to not exclude anything from the
   * profile selected. The following exclude flags are available for each profile:
   *
   * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
   * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
   *
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
   * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
   * {@link DirectionsCriteria#EXCLUDE_FERRY}.
   *
   * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
   *
   * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
   *
   * @return a string matching one of the {@link DirectionsCriteria.ExcludeCriteria} exclusions
   * @since 3.0.0
   */
  @Nullable
  public abstract String exclude();

  /**
   * Whether to return SSML marked-up text for voice guidance along the route (true) or not
   * (false, default).
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return true if the original request included voice instructions
   * @since 3.0.0
   */
  @SerializedName("voice_instructions")
  @Nullable
  public abstract Boolean voiceInstructions();

  /**
   * Whether to return banner objects associated with the route steps (true) or not
   * (false, default). Must be used in conjunction with {@link RouteOptions#steps()}=true
   *
   * @return true if the original request included banner instructions
   * @since 3.0.0
   */
  @SerializedName("banner_instructions")
  @Nullable
  public abstract Boolean bannerInstructions();

  /**
   * A type of units to return in the text for voice instructions.
   * Can be {@link DirectionsCriteria#IMPERIAL} (default) or {@link DirectionsCriteria#METRIC}.
   * Must be used in conjunction with {@link RouteOptions#steps()}=true and
   * {@link RouteOptions#voiceInstructions()} ()}=true.
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
   * in the future. <tt>MapboxDirections</tt> always waits for the response object which ensures
   * this value will never be null.
   *
   * @return a string containing the request UUID
   * @since 3.0.0
   */
  @SerializedName("uuid")
  @NonNull
  public abstract String requestUuid();

  /**
   * Indicates from which side of the road to approach a waypoint.
   * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
   * {@link DirectionsCriteria#APPROACH_CURB} .
   * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
   * from either side of the road.
   * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
   * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
   * region in which the returned route is located.
   * If provided, the list of approaches must be the same length as the list of waypoints.
   *
   * @return a string representing approaches for each waypoint
   * @since 3.2.0
   */
  @Nullable
  public abstract String approaches();

  /**
   * Indicates from which side of the road to approach a waypoint.
   * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
   * {@link DirectionsCriteria#APPROACH_CURB} .
   * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
   * from either side of the road.
   * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
   * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
   * region in which the returned route is located.
   * If provided, the list of approaches must be the same length as the list of waypoints.
   *
   * @return a list of strings representing approaches for each waypoint
   */
  @Nullable
  public List<String> approachesList() {
    return ParseUtils.parseToStrings(approaches());
  }

  /**
   * Indicates which input coordinates should be treated as waypoints.
   * <p>
   * Most useful in combination with  steps=true and requests based on traces
   * with high sample rates. Can be an index corresponding to any of the input coordinates,
   * but must contain the first ( 0 ) and last coordinates' index separated by  ; .
   * {@link #steps()}
   * </p>
   *
   * @return a string representing indices to be used as waypoints
   * @since 4.4.0
   */
  @SerializedName("waypoints")
  @Nullable
  public abstract String waypointIndices();

  /**
   * Indicates which input coordinates should be treated as waypoints.
   * <p>
   * Most useful in combination with  steps=true and requests based on traces
   * with high sample rates. Can be an index corresponding to any of the input coordinates,
   * but must contain the first ( 0 ) and last coordinates' index.
   * {@link #steps()}
   * </p>
   *
   * @return a List of Integers representing indices to be used as waypoints
   */
  @Nullable
  public List<Integer> waypointIndicesList() {
    return ParseUtils.parseToIntegers(waypointIndices());
  }

  /**
   * A semicolon-separated list of custom names for entries in the list of
   * {@link RouteOptions#coordinates()}, used for the arrival instruction in banners and voice
   * instructions. Values can be any string, and the total number of all characters cannot exceed
   * 500. If provided, the list of waypoint_names must be the same length as the list of
   * coordinates. The first value in the list corresponds to the route origin, not the first
   * destination.
   * Must be used in conjunction with {@link RouteOptions#steps()} = true.
   * @return  a string representing names for each waypoint
   * @since 3.3.0
   */
  @SerializedName("waypoint_names")
  @Nullable
  public abstract String waypointNames();

  /**
   * A semicolon-separated list of custom names for entries in the list of
   * {@link RouteOptions#coordinates()}, used for the arrival instruction in banners and voice
   * instructions. Values can be any string, and the total number of all characters cannot exceed
   * 500. If provided, the list of waypoint_names must be the same length as the list of
   * coordinates. The first value in the list corresponds to the route origin, not the first
   * destination.
   * Must be used in conjunction with {@link RouteOptions#steps()} = true.
   *
   * @return  a list of strings representing names for each waypoint
   */
  @Nullable
  public List<String> waypointNamesList() {
    return ParseUtils.parseToStrings(waypointNames());
  }

  /**
   * A semicolon-separated list of coordinate pairs used to specify drop-off
   * locations that are distinct from the locations specified in coordinates.
   * If this parameter is provided, the Directions API will compute the side of the street,
   * left or right, for each target based on the waypoint_targets and the driving direction.
   * The maneuver.modifier, banner and voice instructions will be updated with the computed
   * side of street. The number of waypoint targets must be the same as the number of coordinates.
   * Must be used with {@link RouteOptions#steps()} = true.
   * @return  a list of Points representing coordinate pairs for drop-off locations
   * @since 4.3.0
   */
  @SerializedName("waypoint_targets")
  @Nullable
  public abstract String waypointTargets();

  /**
   * A list of points used to specify drop-off
   * locations that are distinct from the locations specified in coordinates.
   * If this parameter is provided, the Directions API will compute the side of the street,
   * left or right, for each target based on the waypoint_targets and the driving direction.
   * The maneuver.modifier, banner and voice instructions will be updated with the computed
   * side of street. The number of waypoint targets must be the same as the number of coordinates.
   * Must be used with {@link RouteOptions#steps()} = true.
   * @return  a list of Points representing coordinate pairs for drop-off locations
   */
  @Nullable
  public List<Point> waypointTargetsList() {
    return ParseUtils.parseToPoints(waypointTargets());
  }

  /**
   * To be used to specify settings for use with the walking profile.
   *
   * @return options to use for walking profile
   * @since 4.8.0
   */
  @Nullable
  public abstract WalkingOptions walkingOptions();

  /**
   * A semicolon-separated list of booleans affecting snapping of waypoint locations to road
   * segments.
   * If true, road segments closed due to live-traffic closures will be considered for snapping.
   * If false, they will not be considered for snapping.
   * If provided, the number of snappingClosures must be the same as the number of
   * coordinates.
   * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
   *
   * @return a String representing a list of booleans
   */
  @SerializedName("snapping_closures")
  @Nullable
  public abstract String snappingClosures();

  /**
   * A list of booleans affecting snapping of waypoint locations to road segments.
   * If true, road segments closed due to live-traffic closures will be considered for snapping.
   * If false, they will not be considered for snapping.
   * If provided, the number of snappingClosures must be the same as the number of
   * coordinates.
   * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
   *
   * @return a list of booleans
   */
  @Nullable
  public List<Boolean> snappingClosuresList() {
    return ParseUtils.parseToBooleans(snappingClosures());
  }

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
  @NonNull
  public static RouteOptions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    gson.registerTypeAdapter(Point.class, new PointAsCoordinatesTypeAdapter());
    gson.registerTypeAdapterFactory(WalkingOptionsAdapterFactory.create());
    return gson.create().fromJson(json, RouteOptions.class);
  }

  /**
   * Convert the current {@link RouteOptions} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link RouteOptions}.
   *
   * @return a {@link RouteOptions.Builder} with the same values set to match the ones defined
   *   in this {@link RouteOptions}
   */
  @NonNull
  public abstract Builder toBuilder();

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
     * The routing profile to use. Possible values are
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_WALKING}, or
     * {@link DirectionsCriteria#PROFILE_CYCLING}.
     * The same profile which was used during the request that resulted in this root directions
     * response. <tt>MapboxDirections.Builder</tt> ensures that a profile is always set even if the
     * <tt>MapboxDirections</tt> requesting object doesn't specifically set a profile.
     *
     * @param profile One of the direction profiles defined in
     *                {@link DirectionsCriteria.ProfileCriteria}
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

    /**
     * A list of Points to visit in order.
     * There can be between two and 25 coordinates for most requests, or up to three coordinates for
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
     * Note that these coordinates are different than the direction responses
     * {@link DirectionsWaypoint}s that these are the non-snapped coordinates.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally, waypoints
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder coordinates(@NonNull List<Point> coordinates);

    /**
     * Whether to try to return alternative routes (true) or not (false, default). An alternative
     * route is a route that is significantly different than the fastest route, but also still
     * reasonably fast. Such a route does not exist in all circumstances. Up to two alternatives may
     * be returned. This is available for{@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param alternatives true if the request contained additional route request, otherwise false
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder alternatives(@NonNull Boolean alternatives);

    /**
     * The language of returned turn-by-turn text instructions. The default is en (English).
     * Must be used in conjunction with {@link RouteOptions#steps()} = true.
     *
     * @param language a string with the language which was requested in the url
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder language(@NonNull String language);

    /**
     * The maximum distance a coordinate can be moved to snap to the road network in meters. There
     * must be as many radiuses as there are coordinates in the request, each separated by ;.
     * Values can be any number greater than 0 or the string unlimited.
     *
     * @param radiuses a String of radius values, each separated by ;.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder radiuses(@NonNull String radiuses);

    /**
     * The maximum distance a coordinate can be moved to snap to the road network in meters. There
     * must be as many radiuses as there are coordinates in the request.
     * Values can be any number greater than 0 or {@link Double#POSITIVE_INFINITY}.
     *
     * @param radiuses a list of radius values
     * @return this builder for chaining options together
     */
    public Builder radiusesList(@NonNull List<Double> radiuses) {
      String result = FormatUtils.formatRadiuses(radiuses);
      if (result != null) {
        radiuses(result);
      }
      return this;
    }

    /**
     * Influences the direction in which a route starts from a waypoint. Used to filter the road
     * segment the waypoint will be placed on by direction. This is useful for making sure the new
     * routes of rerouted vehicles continue traveling in their current direction. A request that
     * does this would provide bearing and radius values for the first waypoint and leave the
     * remaining values empty. Takes two comma-separated values per waypoint: an angle clockwise
     * from true north between 0 and 360, and the range of degrees by which the angle can deviate
     * (recommended value is 45° or 90°), formatted as {angle, degrees}. If provided, the list of
     * bearings must be the same length as the list of coordinates. However, you can skip a
     * coordinate and show its position in the list with the ; separator.
     *
     * @param bearings a string representing the bearings with the ; separator. Angle and degrees
     *                 for every bearing value are comma-separated.
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bearings(@NonNull String bearings);

    /**
     * Influences the direction in which a route starts from a waypoint. Used to filter the road
     * segment the waypoint will be placed on by direction. This is useful for making sure the new
     * routes of rerouted vehicles continue traveling in their current direction. A request that
     * does this would provide bearing and radius values for the first waypoint and leave the
     * remaining values empty. Takes a List of list of doubles: the first value in the list is an
     * angle clockwise from true north between 0 and 360, the second value is the range of degrees
     * by which the angle can deviate (recommended value is 45° or 90°).
     * If provided, the list of bearings must be the same length as the list of coordinates.
     * However, you can skip a coordinate and show its position in the list with the null value.
     *
     * @param bearings a List of list of doubles representing the bearings used in the original
     *                 request. The first value in the list is the angle, the second one is the
     *                 degrees.
     * @return this builder for chaining options together
     */
    public Builder bearingsList(@NonNull List<List<Double>> bearings) {
      String result = FormatUtils.formatBearings(bearings);
      if (result != null) {
        bearings(result);
      }
      return this;
    }

    /**
     * Sets the allowed direction of travel when departing intermediate waypoints. If true, the
     * route will continue in the same direction of travel. If false, the route may continue in the
     * opposite direction of travel. Defaults to true for {@link DirectionsCriteria#PROFILE_DRIVING}
     * and false for {@link DirectionsCriteria#PROFILE_WALKING} and
     * {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param continueStraight true if you'd like the user to continue straight from the starting
     *                         point
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder continueStraight(@NonNull Boolean continueStraight);

    /**
     * Whether to emit instructions at roundabout exits (true) or not (false, default). Without
     * this parameter, roundabout maneuvers are given as a single instruction that includes both
     * entering and exiting the roundabout. With roundabout_exits=true, this maneuver becomes two
     * instructions, one for entering the roundabout and one for exiting it. Must be used in
     * conjunction with {@link RouteOptions.Builder#steps(Boolean)}
     *
     * @param roundaboutExits true if you'd like extra roundabout instructions
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder roundaboutExits(@NonNull Boolean roundaboutExits);

    /**
     * The format of the returned geometry. Allowed values are:
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (default, a polyline with a precision of five
     * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6} (a polyline with a precision
     * of six decimal places).
     * A null value will reset this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @param geometries one of the options found in {@link DirectionsCriteria.GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder geometries(
        @NonNull @DirectionsCriteria.GeometriesCriteria String geometries);

    /**
     * Displays the requested type of overview geometry. Can be
     * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
     * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default, a simplified version of
     * the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
     *
     * @param overview one of the options found in {@link DirectionsCriteria.OverviewCriteria}
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder overview(
      @NonNull @DirectionsCriteria.OverviewCriteria String overview
    );

    /**
     * Whether to return steps and turn-by-turn instructions (true) or not (false, default).
     * If steps is set to true, the following guidance-related parameters will be available:
     * {@link RouteOptions#bannerInstructions()}, {@link RouteOptions#language()},
     * {@link RouteOptions#roundaboutExits()}, {@link RouteOptions#voiceInstructions()},
     * {@link RouteOptions#voiceUnits()}, {@link RouteOptions#waypointNamesList()},
     * {@link RouteOptions#waypointTargetsList()}, waypoints from {@link RouteOptions#coordinates()}
     *
     * @param steps true if you'd like step information, false otherwise
     * @return this builder for chaining options together
     * @since 3.1.0
     */
    public abstract Builder steps(@NonNull Boolean steps);

    /**
     * Whether to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DURATION}
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
     * {@link DirectionsCriteria#ANNOTATION_SPEED}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
     * You can include several annotations as a comma-separated list. See the
     * {@link RouteLeg} object for more details on what is included with annotations.
     * Must be used in conjunction with overview=full.
     *
     * @param annotations in string format and separated by commas if more than one annotation was
     *                    requested
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder annotations(@NonNull String annotations);

    /**
     * Whether to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DURATION}
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
     * {@link DirectionsCriteria#ANNOTATION_SPEED}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
     *
     * See the {@link RouteLeg} object for more details on what is included with
     * annotations.
     * Must be used in conjunction with overview=full.
     *
     * @param annotations a list of annotations
     * @return this builder for chaining options together
     */
    public Builder annotationsList(@NonNull List<String> annotations) {
      String result = FormatUtils.join(",", annotations);
      if (result != null) {
        annotations(result);
      }
      return this;
    }

    /**
     * Whether to return SSML marked-up text for voice guidance along the route (true) or not
     * (false, default).
     * Must be used in conjunction with {@link RouteOptions.Builder#steps(Boolean)}.
     *
     * @param voiceInstructions true if the original request included voice instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceInstructions(@NonNull Boolean voiceInstructions);

    /**
     * Whether to return banner objects associated with the route steps (true) or not
     * (false, default). Must be used in conjunction with
     * {@link RouteOptions.Builder#steps(Boolean)}
     *
     * @param bannerInstructions true if the original request included banner instructions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder bannerInstructions(@NonNull Boolean bannerInstructions);

    /**
     * Specify which type of units to return in the text for voice instructions.
     * Can be {@link DirectionsCriteria#IMPERIAL} (default) or {@link DirectionsCriteria#METRIC}.
     * Must be used in conjunction with {@link RouteOptions.Builder#steps(Boolean)}=true and
     * {@link RouteOptions.Builder#voiceInstructions(Boolean)}=true.
     *
     * @param voiceUnits string matching either imperial or metric
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder voiceUnits(@NonNull String voiceUnits);

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
     * Exclude certain road types from routing. The default is to not exclude anything from the
     * profile selected. The following exclude flags are available for each profile:
     *
     * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
     * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
     *
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
     * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
     * {@link DirectionsCriteria#EXCLUDE_FERRY}.
     *
     * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
     *
     * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
     *
     * @param exclude a string matching one of the {@link DirectionsCriteria.ExcludeCriteria}
     *                exclusions
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public abstract Builder exclude(@NonNull String exclude);

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
     * {@link DirectionsCriteria#APPROACH_CURB} .
     * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
     * from either side of the road.
     * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
     * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
     * region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     * However, you can skip a coordinate and show its position in the list with the ; separator.
     * The same approaches the user originally made when the request was made.
     *
     * @param approaches unrestricted, curb or omitted (;)
     * @return this builder for chaining options together
     * @since 3.2.0
     */
    public abstract Builder approaches(@NonNull String approaches);

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
     * {@link DirectionsCriteria#APPROACH_CURB} .
     * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
     * from either side of the road.
     * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
     * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
     * region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     * However, you can skip a coordinate and show its position in the list with null value.
     * The same approaches the user originally made when the request was made.
     *
     * @param approaches a list of Strings
     * @return this builder for chaining options together
     */
    public Builder approachesList(@NonNull List<String> approaches) {
      String result = FormatUtils.formatApproaches(approaches);
      if (result != null) {
        approaches(result);
      }
      return this;
    }

    /**
     * Indicates which input coordinates should be treated as waypoints.
     * <p>
     * Most useful in combination with  steps=true and requests based on traces
     * with high sample rates. Can be an index corresponding to any of the input coordinates,
     * but must contain the first ( 0 ) and last coordinates' index separated by  ; .
     * {@link #steps()}
     * </p>
     * The same waypoint indices the user originally made when the request was made.
     *
     * @param waypointIndices to be used as waypoints
     * @return this builder for chaining options together
     * @since 4.4.0
     */
    public abstract Builder waypointIndices(@NonNull String waypointIndices);

    /**
     * Indicates which input coordinates should be treated as waypoints.
     * <p>
     * Most useful in combination with  steps=true and requests based on traces
     * with high sample rates. Can be an index corresponding to any of the input coordinates,
     * but must contain the first ( 0 ) and last coordinates'.
     * {@link #steps()}
     * </p>
     * The same waypoint indices the user originally made when the request was made.
     *
     * @param indices a list to be used as waypoints
     * @return this builder for chaining options together
     */
    public Builder waypointIndicesList(@NonNull List<Integer> indices) {
      String result = FormatUtils.join(";", indices);
      if (result != null) {
        waypointIndices(result);
      }
      return this;
    }

    /**
     * A semicolon-separated list of custom names for entries in the list of
     * {@link RouteOptions#coordinates()}, used for the arrival instruction in banners and voice
     * instructions. Values can be any string, and the total number of all characters cannot exceed
     * 500. If provided, the list of waypoint_names must be the same length as the list of
     * coordinates, but you can skip a coordinate pair and show its position in the list with the ;
     * separator. The first value in the list corresponds to the route origin, not the first
     * destination. To leave the origin unnamed, begin the list with a semicolon.
     * Must be used in conjunction with {@link RouteOptions.Builder#steps(Boolean)} = true.
     *
     * @param waypointNames unrestricted, curb or omitted (;)
     * @return this builder for chaining options together
     * @since 3.3.0
     */
    public abstract Builder waypointNames(@NonNull String waypointNames);

    /**
     * A semicolon-separated list of custom names for entries in the list of
     * {@link RouteOptions#coordinates()}, used for the arrival instruction in banners and voice
     * instructions. Values can be any string, and the total number of all characters cannot exceed
     * 500. If provided, the list of waypoint_names must be the same length as the list of
     * coordinates, but you can skip a coordinate pair and show its position in the list with the
     * null value. The first value in the list corresponds to the route origin, not the first
     * destination. To leave the origin unnamed, begin the list with a null value.
     * Must be used in conjunction with {@link RouteOptions.Builder#steps(Boolean)} = true.
     *
     * @param waypointNames a list of Strings
     * @return this builder for chaining options together
     */
    public Builder waypointNamesList(@NonNull List<String> waypointNames) {
      String result = FormatUtils.formatWaypointNames(waypointNames);
      if (result != null) {
        waypointNames(result);
      }
      return this;
    }

    /**
     * A semicolon-separated list of coordinate pairs used to specify drop-off
     * locations that are distinct from the locations specified in coordinates.
     * If this parameter is provided, the Directions API will compute the side of the street,
     * left or right, for each target based on the waypoint_targets and the driving direction.
     * The maneuver.modifier, banner and voice instructions will be updated with the computed
     * side of street. The number of waypoint targets must be the same as the number of coordinates,
     * but you can skip a coordinate pair and show its position in the list with the ; separator.
     * Must be used with {@link RouteOptions.Builder#steps(Boolean)} = true.
     * The same waypoint targets the user originally made when the request was made.
     *
     * @param waypointTargets list of coordinate pairs for drop-off locations (;)
     * @return this builder for chaining options together
     * @since 4.3.0
     */
    public abstract Builder waypointTargets(@NonNull String waypointTargets);

    /**
     * A list of coordinate pairs used to specify drop-off
     * locations that are distinct from the locations specified in coordinates.
     * If this parameter is provided, the Directions API will compute the side of the street,
     * left or right, for each target based on the waypoint_targets and the driving direction.
     * The maneuver.modifier, banner and voice instructions will be updated with the computed
     * side of street. The number of waypoint targets must be the same as the number of coordinates,
     * but you can skip a coordinate pair and show its position in the list with the null value.
     * Must be used with {@link RouteOptions.Builder#steps(Boolean)} = true.
     * The same waypoint targets the user originally made when the request was made.
     *
     * @param waypointTargets list of Points for drop-off locations
     * @return this builder for chaining options together
     */
    public Builder waypointTargetsList(@NonNull List<Point> waypointTargets) {
      waypointTargets(FormatUtils.formatPointsList(waypointTargets));
      return this;
    }

    /**
     * To be used to specify settings for use with the walking profile.
     *
     * @param walkingOptions options to use for walking profile
     * @return this builder for chaining options together
     * @since 4.8.0
     */
    public abstract Builder walkingOptions(@NonNull WalkingOptions walkingOptions);

    /**
     * A semicolon-separated list of booleans affecting snapping of waypoint locations to road
     * segments.
     * If true, road segments closed due to live-traffic closures will be considered for snapping.
     * If false, they will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * You can skip a coordinate and show its position in the list with the ; separator.
     * If unspecified, this parameter defaults to false.
     * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
     *
     * @param snappingClosures a semicolon-separated list of booleans
     * @return this builder for chaining options together
     */
    public abstract Builder snappingClosures(@NonNull String snappingClosures);

    /**
     * A list of booleans affecting snapping of waypoint locations to road segments.
     * If true, road segments closed due to live-traffic closures will be considered for snapping.
     * If false, they will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * You can skip a coordinate and show its position in the list with null value.
     * If unspecified, this parameter defaults to false.
     * Must be used with {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}
     *
     * @param snappingClosures a list of booleans
     * @return this builder for chaining options together
     */
    public Builder snappingClosures(@NonNull List<Boolean> snappingClosures) {
      String result = FormatUtils.join(";", snappingClosures);
      if (result != null) {
        snappingClosures(result);
      } else {
        snappingClosures("");
      }
      return this;
    }

    /**
     * Builds a new instance of the {@link RouteOptions} object.
     *
     * @return a new {@link RouteOptions} instance
     * @since 3.0.0
     */
    public abstract RouteOptions build();
  }
}
