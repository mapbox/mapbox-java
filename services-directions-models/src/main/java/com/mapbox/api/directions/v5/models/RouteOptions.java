package com.mapbox.api.directions.v5.models;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.utils.FormatUtils;
import com.mapbox.api.directions.v5.utils.ParseUtils;
import com.mapbox.geojson.Point;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * Defines route request parameters.
 * <p>
 * Refer to
 * <a href="https://www.mapbox.com/api-documentation/navigation/#directions">Directions API
 * documentation</a> for details and up-to-date documentation for each of the parameters.
 */
@AutoValue
public abstract class RouteOptions extends DirectionsJsonObject {

  /**
   * Build a new instance of this RouteOptions.
   *
   * @return {@link RouteOptions.Builder}
   */
  public static Builder builder() {
    return new AutoValue_RouteOptions.Builder()
      .baseUrl(DirectionsCriteria.BASE_API_URL)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6);
  }

  /**
   * Base URL for the request.
   *
   * @return string value representing the base URL
   */
  @NonNull
  public abstract String baseUrl();

  /**
   * The user parameter of the request, defaults to "mapbox".
   *
   * @return string with the user
   */
  @NonNull
  public abstract String user();

  /**
   * The routing profile to use. Possible values are
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}, {@link DirectionsCriteria#PROFILE_DRIVING},
   * {@link DirectionsCriteria#PROFILE_WALKING}, or {@link DirectionsCriteria#PROFILE_CYCLING}.
   *
   * @return string value representing the profile defined in
   *   {@link DirectionsCriteria.ProfileCriteria}
   */
  @NonNull
  @DirectionsCriteria.ProfileCriteria
  public abstract String profile();

  /**
   * A semicolon-separated list of {longitude},{latitude} coordinate pairs to visit in order.
   * There can be between two and 25 coordinates for most requests,
   * or up to three coordinates for {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
   * Contact Mapbox Support if you'd like to extend this limit.
   * <p>
   * Note that these coordinates are different than the {@link DirectionsWaypoint}s
   * found in the {@link DirectionsResponse} which are snapped to a road.
   *
   * @return a list of {@link Point}s which represent the route origin, destination,
   *   and optionally waypoints
   */
  @NonNull
  public abstract String coordinates();

  /**
   * A list of Points to visit in order.
   * There can be between two and 25 coordinates for most requests, or up to 3 coordinates for
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
   * Contact Mapbox Support if you'd like to extend this limit.
   * <p>
   * Note that these coordinates are different than the {@link DirectionsWaypoint}s
   * found in the {@link DirectionsResponse} which are snapped to a road.
   *
   * @return a list of {@link Point}s which represent the route origin, destination,
   *   and optionally waypoints
   */
  @NonNull
  public List<Point> coordinatesList() {
    return ParseUtils.parseToPoints(coordinates());
  }

  /**
   * Whether to try to return alternative routes (true) or not (false, default). An alternative
   * route is a route that is significantly different than the fastest route, but also still
   * reasonably fast. Such a route does not exist in all circumstances. Up to two alternatives may
   * be returned. This is available for {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
   * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_CYCLING}.
   * <p>
   * The order of the routes in the response is not sorted by duration, but by weight.
   * The first route in the list is not the most preferable because of the duration,
   * but also based on the type of maneuvers.
   * <p>
   * If null is provided, the Directions API defaults to false.
   *
   * @return boolean object representing the setting for alternatives
   */
  @Nullable
  public abstract Boolean alternatives();

  /**
   * The language of returned turn-by-turn text instructions.
   * Defaults to "en" (English) if null.
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   * <p>
   * Refer to
   * <a href="https://docs.mapbox.com/api/navigation/directions/#instructions-languages">
   * supported languages list</a> for details.
   *
   * @return the language as a string used during the request
   */
  @Nullable
  public abstract String language();

  /**
   * The maximum distance a coordinate can be moved to snap to the road network in meters. There
   * must be as many radiuses as there are coordinates in the request, each separated by ";".
   * Values can be any number greater than 0 or the string "unlimited".
   * <p>
   * A NoSegment error is returned if no routable road is found within the radius.
   *
   * @return a string representing the radiuses separated by ";".
   */
  @Nullable
  public abstract String radiuses();

  /**
   * The maximum distance a coordinate can be moved to snap to the road network in meters. There
   * must be as many radiuses as there are coordinates in the request.
   * Values can be any number greater than 0 or {@link Double#POSITIVE_INFINITY} for unlimited.
   * <p>
   * A NoSegment error is returned if no routable road is found within the radius.
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
   * However, you can skip a coordinate and show its position in the list with the ; separator.
   *
   * @return a string representing the bearings with the ; separator. Angle and degrees for every
   *   bearing value are comma-separated.
   */
  @Nullable
  public abstract String bearings();

  /**
   * Influences the direction in which a route starts from a waypoint. Used to filter the road
   * segment the waypoint will be placed on by direction. This is useful for making sure the new
   * routes of rerouted vehicles continue traveling in their current direction. A request that does
   * this would provide bearing and radius values for the first waypoint and leave the remaining
   * values empty. Returns a list of {@link Bearing} values.
   * <p>
   * If provided, the list of bearings must be the same length as the list of coordinates.
   *
   * @return a List of list of doubles representing the bearings used in the original request.
   *   The first value in the list is the angle, the second one is the degrees.
   */
  @Nullable
  public List<Bearing> bearingsList() {
    return ParseUtils.parseBearings(bearings());
  }

  /**
   * The allowed direction of travel when departing intermediate waypoints. If true, the route
   * will continue in the same direction of travel. If false, the route may continue in the opposite
   * direction of travel. Defaults to true for {@link DirectionsCriteria#PROFILE_DRIVING} and false
   * for {@link DirectionsCriteria#PROFILE_WALKING} and {@link DirectionsCriteria#PROFILE_CYCLING}.
   *
   * @return a boolean value representing whether or not continueStraight was enabled or
   *   not during the initial request
   */
  @SerializedName("continue_straight")
  @Nullable
  public abstract Boolean continueStraight();

  /**
   * Whether to emit instructions at roundabout exits (true) or not (false, default if null).
   * Without this parameter, roundabout maneuvers are given as a single instruction that
   * includes both entering and exiting the roundabout.
   * With roundabout_exits=true, this maneuver becomes two instructions,
   * one for entering the roundabout and one for exiting it. Must be used in
   * conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return a boolean value representing whether or not roundaboutExits is enabled or disabled
   */
  @SerializedName("roundabout_exits")
  @Nullable
  public abstract Boolean roundaboutExits();

  /**
   * The format of the returned geometry. Allowed values are:
   * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (a polyline with a precision of five
   * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6}
   * (default, a polyline with a precision of six decimal places).
   *
   * @return String geometry type from {@link DirectionsCriteria.GeometriesCriteria}.
   */
  @NonNull
  @DirectionsCriteria.GeometriesCriteria
  public abstract String geometries();

  /**
   * Displays the requested type of overview geometry. Can be
   * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
   * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default if null,
   * a simplified version of the full geometry),
   * or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
   *
   * @return null or one of the options found in {@link DirectionsCriteria.OverviewCriteria}
   */
  @Nullable
  @DirectionsCriteria.OverviewCriteria
  public abstract String overview();

  /**
   * Whether to return steps and turn-by-turn instructions (true)
   * or not (false if null, default).
   * If steps is set to true, the following guidance-related parameters will be available:
   * {@link RouteOptions#bannerInstructions()}, {@link RouteOptions#language()},
   * {@link RouteOptions#roundaboutExits()}, {@link RouteOptions#voiceInstructions()},
   * {@link RouteOptions#voiceUnits()}, {@link RouteOptions#waypointNames()},
   * {@link RouteOptions#waypointNamesList()}, {@link RouteOptions#waypointTargets()},
   * {@link RouteOptions#waypointTargetsList()}, {@link RouteOptions#waypointIndices()},
   * {@link RouteOptions#waypointIndicesList()}, {@link RouteOptions#alleyBias()}
   *
   * @return true if you'd like step information, false or null otherwise
   */
  @Nullable
  public abstract Boolean steps();

  /**
   * A comma-separated list of annotations. Defines whether to return additional metadata along the
   * route. Possible values are:
   * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
   * {@link DirectionsCriteria#ANNOTATION_DURATION}
   * {@link DirectionsCriteria#ANNOTATION_SPEED}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION_NUMERIC}
   * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
   * {@link DirectionsCriteria#ANNOTATION_CLOSURE}
   * See the {@link RouteLeg} object for more details on what is included with annotations.
   * <p>
   * Must be used in conjunction with {@link DirectionsCriteria#OVERVIEW_FULL}
   * in {@link RouteOptions#overview()}.
   *
   * @return a string containing requested annotations
   */
  @Nullable
  public abstract String annotations();

  /**
   * A list of annotations. Defines whether to return additional metadata along the
   * route. Possible values are:
   * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
   * {@link DirectionsCriteria#ANNOTATION_DURATION}
   * {@link DirectionsCriteria#ANNOTATION_SPEED}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
   * {@link DirectionsCriteria#ANNOTATION_CONGESTION_NUMERIC}
   * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
   * {@link DirectionsCriteria#ANNOTATION_CLOSURE}
   * See the {@link RouteLeg} object for more details on what is included with annotations.
   * <p>
   * Must be used in conjunction with {@link DirectionsCriteria#OVERVIEW_FULL}
   * in {@link RouteOptions#overview()}.
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
   * <p>
   * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
   * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
   * <p>
   * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
   * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
   * {@link DirectionsCriteria#EXCLUDE_FERRY}.
   * <p>
   * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
   * <p>
   * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
   *
   * @return a string matching one of the {@link DirectionsCriteria.ExcludeCriteria} exclusions
   */
  @Nullable
  public abstract String exclude();

  /**
   * Whether to return SSML marked-up text for voice guidance along the route (true) or not
   * (false, default if null).
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return true if the request included voice instructions
   */
  @SerializedName("voice_instructions")
  @Nullable
  public abstract Boolean voiceInstructions();

  /**
   * Whether to return banner objects associated with the route steps (true) or not
   * (false, default if null).
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return true if the request includes banner instructions
   */
  @SerializedName("banner_instructions")
  @Nullable
  public abstract Boolean bannerInstructions();

  /**
   * A type of units to return in the text for voice instructions.
   * Can be {@link DirectionsCriteria#IMPERIAL} (default) or {@link DirectionsCriteria#METRIC}.
   * Must be used in conjunction with {@link RouteOptions#steps()}=true and
   * {@link RouteOptions#steps()}=true
   * and {@link RouteOptions#voiceInstructions()}=true.
   *
   * @return a string matching either imperial or metric
   */
  @SerializedName("voice_units")
  @Nullable
  @DirectionsCriteria.VoiceUnitCriteria
  public abstract String voiceUnits();

  /**
   * A valid Mapbox access token used to making the request.
   *
   * @return a string representing the Mapbox access token
   */
  @SerializedName("access_token")
  @NonNull
  public abstract String accessToken();

  /**
   * A semicolon-separated list indicating from which side of the road
   * to approach a waypoint.
   * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
   * {@link DirectionsCriteria#APPROACH_CURB}.
   * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
   * from either side of the road.
   * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
   * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
   * region in which the returned route is located.
   * If provided, the list of approaches must be the same length as the list of waypoints.
   * However, you can skip a coordinate and show its position in the list with the ; separator.
   * Since the first value will not be evaluated, begin the list with a semicolon.
   * If the waypoint is within 1 meter of the road, this parameter is ignored.
   *
   * @return a string representing approaches for each waypoint
   */
  @Nullable
  public abstract String approaches();

  /**
   * Indicates from which side of the road to approach a waypoint.
   * Accepts {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
   * {@link DirectionsCriteria#APPROACH_CURB} .
   * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
   * from either side of the road.
   * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
   * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
   * region in which the returned route is located.
   * If provided, the list of approaches must be the same length as the list of waypoints.
   * However, you can skip a coordinate and show its position in the list with null.
   * Since the first value will not be evaluated, you can begin the list with a null.
   * If the waypoint is within 1 meter of the road, this parameter is ignored.
   *
   * @return a list of strings representing approaches for each waypoint
   */
  @Nullable
  public List<String> approachesList() {
    return ParseUtils.parseToStrings(approaches());
  }

  /**
   * A semicolon-separated list indicating which input coordinates
   * should be treated as waypoints.
   * <p>
   * Waypoints form the beginning and end of each leg in the returned route and correspond to
   * the depart and arrive steps.
   * If a list of waypoints is not provided, all coordinates are treated as waypoints.
   * Each item in the list must be the zero-based index of an input coordinate,
   * and the list must include 0 (the index of the first coordinate)
   * and the index of the last coordinate.
   * The waypoints parameter can be used to guide the path of the route without
   * introducing additional legs and arrive/depart instructions.
   * <p>
   * For example, if a coordinates list has 3 points,
   * origin, some middle point, and destination, we can have below combinations:
   * <p>
   * - waypointIndices are null, the route will have 2 legs
   * <p>
   * - waypointIndices are "0;1;2", the route will have 2 legs
   * <p>
   * - waypointIndices are "0;2", the route will have only one leg that goes via the middle point
   * <p>
   * Must be used with {@link RouteOptions#steps()}=true.
   *
   * @return a string representing indices to be used as waypoints
   */
  @SerializedName("waypoints")
  @Nullable
  public abstract String waypointIndices();

  /**
   * A list indicating which input coordinates should be treated as waypoints.
   * <p>
   * Waypoints form the beginning and end of each leg in the returned route and correspond to
   * the depart and arrive steps.
   * If a list of waypoints is not provided, all coordinates are treated as waypoints.
   * Each item in the list must be the zero-based index of an input coordinate,
   * and the list must include 0 (the index of the first coordinate)
   * and the index of the last coordinate.
   * The waypoints parameter can be used to guide the path of the route without
   * introducing additional legs and arrive/depart instructions.
   * <p>
   * For example, if a coordinates list has 3 points,
   * origin, some middle point, and destination, we can have below combinations:
   * <p>
   * - waypointIndices are null, the route will have 2 legs
   * <p>
   * - waypointIndices are [0,1,2], the route will have 2 legs
   * <p>
   * - waypointIndices are [0,2], the route will have only one leg that goes via the middle point
   * <p>
   * Must be used with {@link RouteOptions#steps()}=true.
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
   * To leave the origin unnamed, begin the list with a semicolon.
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return a string representing names for each waypoint
   */
  @SerializedName("waypoint_names")
  @Nullable
  public abstract String waypointNames();

  /**
   * A list of custom names for entries in the list of
   * {@link RouteOptions#coordinatesList()}, used for the arrival instruction in banners and voice
   * instructions. Values can be any string, and the total number of all characters cannot exceed
   * 500. If provided, the list of names must be the same length as the list of
   * coordinates. The first value in the list corresponds to the route origin, not the first
   * destination.
   * <p>
   * Must be used in conjunction with {@link RouteOptions#steps()}=true.
   *
   * @return a list of strings representing names for each waypoint
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
   * side of street. The number of waypoint targets must be the same as the number of coordinates,
   * but you can skip a coordinate pair and show its position in the list with the ; separator.
   * Since the first value will not be evaluated, begin the list with a semicolon.
   * <p>
   * Must be used with {@link RouteOptions#steps()}=true.
   *
   * @return a list of Points representing coordinate pairs for drop-off locations
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
   * You can skip a coordinate pair and show its position in the list with the "null".
   * Since the first value will not be evaluated, you can begin the list with "null".
   * Must be used with {@link RouteOptions#steps()}=true.
   *
   * @return a list of Points representing coordinate pairs for drop-off locations
   */
  @Nullable
  public List<Point> waypointTargetsList() {
    return ParseUtils.parseToPoints(waypointTargets());
  }

  /**
   * A scale from -1 to 1, where -1 biases the route against alleys
   * and 1 biases the route toward alleys. If null, default is 0, which is neutral.
   *
   * @return bias towards alleys
   */
  @SerializedName("alley_bias")
  @Nullable
  public abstract Double alleyBias();

  /**
   * The walking speed in meters per second with a minimum of 0.14 m/s (or 0.5 km/h)
   * and a maximum of 6.94 m/s (or 25.0 km/h).
   * If null, defaults to 1.42 m/s (5.1 km/h).
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_WALKING}.
   *
   * @return walkingSpeed in meters per second
   */
  @SerializedName("walking_speed")
  @Nullable
  public abstract Double walkingSpeed();

  /**
   * A bias which determines whether the route should prefer or avoid the use of roads or paths
   * that are set aside for pedestrian-only use (walkways). The allowed range of values is from
   * -1 to 1, where -1 indicates indicates preference to avoid walkways, 1 indicates preference
   * to favor walkways, and 0 indicates no preference (the default, if null).
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_WALKING}.
   *
   * @return walkwayBias bias to prefer or avoid walkways
   */
  @SerializedName("walkway_bias")
  @Nullable
  public abstract Double walkwayBias();

  /**
   * A semicolon-separated list of booleans affecting snapping of waypoint locations to road
   * segments.
   * If true, road segments closed due to live-traffic closures will be considered for snapping.
   * If false, they will not be considered for snapping.
   * If provided, the number of snappingClosures must be the same as the number of
   * coordinates.
   * However, you can skip a coordinate and show its position in the list with the ; separator.
   * If null, this parameter defaults to false.
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}.
   *
   * @return a String representing a list of booleans
   */
  @SerializedName("snapping_include_closures")
  @Nullable
  public abstract String snappingIncludeClosures();

  /**
   * A list of booleans affecting snapping of waypoint locations to road segments.
   * If true, road segments closed due to live-traffic closures will be considered for snapping.
   * If false, they will not be considered for snapping.
   * If provided, the number of snappingClosures must be the same as the number of
   * coordinates.
   * However, you can skip a coordinate and show its position in the list with the null.
   * If the list is null, this parameter defaults to false.
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}.
   *
   * @return a list of booleans
   */
  @Nullable
  public List<Boolean> snappingIncludeClosuresList() {
    return ParseUtils.parseToBooleans(snappingIncludeClosures());
  }

  /**
   * The desired arrival time, formatted as a timestamp in ISO-8601 format
   * in the local time at the route destination.
   * The travel time returned in duration is a prediction for travel time based
   * on historical travel data. The route is calculated in a time-dependent manner.
   * For example, a trip that takes two hours will consider changing historic traffic
   * conditions across the two-hour window.
   * The route takes timed turn restrictions and conditional access restrictions into account
   * based on the requested arrival time.
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING}.
   */
  @SerializedName("arrive_by")
  @Nullable
  public abstract String arriveBy();

  /**
   * The departure time, formatted as a timestamp in ISO-8601 format in the local time
   * at the route origin. The travel time returned in duration is a prediction for travel time
   * based on historical travel data. The route is calculated in a time-dependent manner.
   * For example, a trip that takes two hours will consider changing historic traffic conditions
   * across the two-hour window, instead of only at the specified depart_at time.
   * The route takes timed turn restrictions and conditional access restrictions into account based
   * on the requested departure time.
   * <p>
   * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING}.
   */
  @SerializedName("depart_at")
  @Nullable
  public abstract String departAt();

  /**
   * Whether the routes should be refreshable via the directions refresh API.
   * <p>
   * If false, the refresh requests will fail. Defaults to false if null.
   *
   * @return whether the routes should be refreshable
   */
  @SerializedName("enable_refresh")
  @Nullable
  public abstract Boolean enableRefresh();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
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
   * @see #fromUrl(URL)
   */
  @NonNull
  public static RouteOptions fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, RouteOptions.class);
  }

  /**
   * Create a new instance of this class by passing a get request URL.
   *
   * @param url request URL
   * @return a new instance of this class defined by the values passed inside this static factory
   *   method
   * @see #fromJson(String)
   */
  @NonNull
  public static RouteOptions fromUrl(@NonNull URL url) {
    JsonObject optionsJson = new JsonObject();

    optionsJson.addProperty("baseUrl", url.getProtocol() + "://" + url.getHost());

    String[] pathElements = url.getPath().split("/");
    optionsJson.addProperty("user", pathElements[3]);
    optionsJson.addProperty("profile", pathElements[4]);
    optionsJson.addProperty("coordinates", pathElements[5]);

    String[] queryElements = url.getQuery().split("&");
    for (String query : queryElements) {
      int idx = query.indexOf("=");
      try {
        optionsJson.addProperty(
          URLDecoder.decode(query.substring(0, idx), "UTF-8"),
          URLDecoder.decode(query.substring(idx + 1), "UTF-8")
        );
      } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException(ex);
      }
    }

    return fromJson(optionsJson.toString());
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
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Base URL for the request.
     *
     * @param baseUrl string value representing the base URL
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * The user parameter of the request, defaults to "mapbox".
     *
     * @param user string with the user
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder user(@NonNull String user);

    /**
     * The routing profile to use. Possible values are
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link DirectionsCriteria#PROFILE_DRIVING},
     * {@link DirectionsCriteria#PROFILE_WALKING}, or {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param profile string value representing the profile defined in
     *                {@link DirectionsCriteria.ProfileCriteria}
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder profile(@NonNull @DirectionsCriteria.ProfileCriteria String profile);

    /**
     * A semicolon-separated list of {longitude},{latitude} coordinate pairs to visit in order.
     * There can be between two and 25 coordinates for most requests,
     * or up to three coordinates for {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
     * Contact Mapbox Support if you'd like to extend this limit.
     * <p>
     * Note that these coordinates are different than the {@link DirectionsWaypoint}s
     * found in the {@link DirectionsResponse} which are snapped to a road.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally waypoints
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder coordinates(@NonNull String coordinates);

    /**
     * A list of Points to visit in order.
     * There can be between two and 25 coordinates for most requests, or up to 3 coordinates for
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} requests.
     * Contact Mapbox Support if you'd like to extend this limit.
     * <p>
     * Note that these coordinates are different than the {@link DirectionsWaypoint}s
     * found in the {@link DirectionsResponse} which are snapped to a road.
     *
     * @param coordinates a list of {@link Point}s which represent the route origin, destination,
     *                    and optionally waypoints
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder coordinatesList(@NonNull List<Point> coordinates) {
      String result = FormatUtils.formatPointsList(coordinates);
      if (result != null) {
        coordinates(result);
      }
      return this;
    }

    /**
     * Whether to try to return alternative routes (true) or not (false, default). An alternative
     * route is a route that is significantly different than the fastest route, but also still
     * reasonably fast. Such a route does not exist in all circumstances. Up to two alternatives may
     * be returned. This is available for {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link DirectionsCriteria#PROFILE_DRIVING}, {@link DirectionsCriteria#PROFILE_CYCLING}.
     * <p>
     * The order of the routes in the response is not sorted by duration, but by weight.
     * The first route in the list is not the most preferable because of the duration,
     * but also based on the type of maneuvers.
     * <p>
     * If null is provided, the Directions API defaults to false.
     *
     * @param alternatives boolean object representing the setting for alternatives
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder alternatives(@Nullable Boolean alternatives);

    /**
     * The language of returned turn-by-turn text instructions.
     * Defaults to "en" (English) if null.
     * Must be used in conjunction with {@link RouteOptions#steps()}=true.
     * <p>
     * Refer to
     * <a href="https://docs.mapbox.com/api/navigation/directions/#instructions-languages">
     * supported languages list</a> for details.
     *
     * @param language the language as a string used during the request
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder language(@Nullable String language);

    /**
     * The maximum distance a coordinate can be moved to snap to the road network in meters. There
     * must be as many radiuses as there are coordinates in the request, each separated by ";".
     * Values can be any number greater than 0 or the string "unlimited".
     * <p>
     * A NoSegment error is returned if no routable road is found within the radius.
     *
     * @param radiuses a string representing the radiuses separated by ";".
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder radiuses(@Nullable String radiuses);

    /**
     * The maximum distance a coordinate can be moved to snap to the road network in meters. There
     * must be as many radiuses as there are coordinates in the request.
     * Values can be any number greater than 0 or {@link Double#POSITIVE_INFINITY} for unlimited.
     * <p>
     * A NoSegment error is returned if no routable road is found within the radius.
     *
     * @param radiuses a list of radiuses
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder radiusesList(@Nullable List<Double> radiuses) {
      String result = FormatUtils.formatRadiuses(radiuses);
      if (result != null) {
        radiuses(result);
      }
      return this;
    }

    /**
     * Influences the direction in which a route starts from a waypoint. Used to filter the road
     * segment the waypoint will be placed on by direction. This is useful for making sure the new
     * routes of rerouted vehicles continue traveling in their current direction.
     * A request that does
     * this would provide bearing and radius values for the first waypoint and leave the remaining
     * values empty. Returns two comma-separated values per waypoint: an angle clockwise from true
     * north between 0 and 360, and the range of degrees by which the angle can deviate (recommended
     * value is 45° or 90°), formatted as {angle, degrees}. If provided, the list of bearings must
     * be the same length as the list of coordinates.
     * However, you can skip a coordinate and show its position in the list with the ; separator.
     *
     * @param bearings a string representing the bearings with the ; separator.
     *                 Angle and degrees for every bearing value are comma-separated.
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder bearings(@Nullable String bearings);

    /**
     * Influences the direction in which a route starts from a waypoint. Used to filter the road
     * segment the waypoint will be placed on by direction. This is useful for making sure the new
     * routes of rerouted vehicles continue traveling in their current direction.
     * A request that does
     * this would provide bearing and radius values for the first waypoint and leave the remaining
     * values empty. Returns a list of values, each value is a list of {@link Bearing} objects.
     * <p>
     * If provided, the list of bearings must be the same length as the list of coordinates.
     * However, you can skip a coordinate and show its position in the list with null.
     *
     * @param bearings a List of list of doubles representing the bearings used in the original
     *                 request.
     *                 The first value in the list is the angle, the second one is the degrees.
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder bearingsList(@Nullable List<Bearing> bearings) {
      String result = FormatUtils.formatBearings(bearings);
      if (result != null) {
        bearings(result);
      }
      return this;
    }

    /**
     * The allowed direction of travel when departing intermediate waypoints. If true, the route
     * will continue in the same direction of travel. If false, the route may continue in
     * the opposite
     * direction of travel. Defaults to true for {@link DirectionsCriteria#PROFILE_DRIVING}
     * and false
     * for {@link DirectionsCriteria#PROFILE_WALKING} and
     * {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param continueStraight a boolean value representing whether or not continueStraight
     *                         was enabled or during the initial request
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder continueStraight(@Nullable Boolean continueStraight);

    /**
     * Whether to emit instructions at roundabout exits (true) or not (false, default if null).
     * Without this parameter, roundabout maneuvers are given as a single instruction that
     * includes both entering and exiting the roundabout.
     * With roundabout_exits=true, this maneuver becomes two instructions,
     * one for entering the roundabout and one for exiting it. Must be used in
     * conjunction with {@link RouteOptions#steps()}=true.
     *
     * @param roundaboutExits a boolean value representing whether or not roundaboutExits
     *                        is enabled or disabled
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder roundaboutExits(@Nullable Boolean roundaboutExits);

    /**
     * The format of the returned geometry. Allowed values are:
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE} (a polyline with a precision of five
     * decimal places), {@link DirectionsCriteria#GEOMETRY_POLYLINE6}
     * (default, a polyline with a precision of six decimal places).
     *
     * @param geometries String geometry type from {@link DirectionsCriteria.GeometriesCriteria}.
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder geometries(
      @NonNull @DirectionsCriteria.GeometriesCriteria String geometries);

    /**
     * Displays the requested type of overview geometry. Can be
     * {@link DirectionsCriteria#OVERVIEW_FULL} (the most detailed geometry
     * available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (default if null,
     * a simplified version of the full geometry),
     * or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview geometry).
     *
     * @param overview null or one of the options found in
     *                 {@link DirectionsCriteria.OverviewCriteria}
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder overview(
      @Nullable @DirectionsCriteria.OverviewCriteria String overview
    );

    /**
     * Whether to return steps and turn-by-turn instructions (true)
     * or not (false if null, default).
     * If steps is set to true, the following guidance-related parameters will be available:
     * {@link RouteOptions#bannerInstructions()}, {@link RouteOptions#language()},
     * {@link RouteOptions#roundaboutExits()}, {@link RouteOptions#voiceInstructions()},
     * {@link RouteOptions#voiceUnits()}, {@link RouteOptions#waypointNames()},
     * {@link RouteOptions#waypointNamesList()}, {@link RouteOptions#waypointTargets()},
     * {@link RouteOptions#waypointTargetsList()}, {@link RouteOptions#waypointIndices()},
     * {@link RouteOptions#waypointIndicesList()}, {@link RouteOptions#alleyBias()}
     *
     * @param steps true if you'd like step information, false or null otherwise
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder steps(@Nullable Boolean steps);

    /**
     * A comma-separated list of annotations. Defines whether to return additional metadata along
     * the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
     * {@link DirectionsCriteria#ANNOTATION_DURATION}
     * {@link DirectionsCriteria#ANNOTATION_SPEED}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION_NUMERIC}
     * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
     * {@link DirectionsCriteria#ANNOTATION_CLOSURE}
     * See the {@link RouteLeg} object for more details on what is included with annotations.
     * <p>
     * Must be used in conjunction with {@link DirectionsCriteria#OVERVIEW_FULL}
     * in {@link RouteOptions#overview()}.
     *
     * @param annotations a string containing requested annotations
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder annotations(@Nullable String annotations);

    /**
     * A list of annotations. Defines whether to return additional metadata along the
     * route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}
     * {@link DirectionsCriteria#ANNOTATION_DURATION}
     * {@link DirectionsCriteria#ANNOTATION_SPEED}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION_NUMERIC}
     * {@link DirectionsCriteria#ANNOTATION_MAXSPEED}
     * {@link DirectionsCriteria#ANNOTATION_CLOSURE}
     * See the {@link RouteLeg} object for more details on what is included with annotations.
     * <p>
     * Must be used in conjunction with {@link DirectionsCriteria#OVERVIEW_FULL}
     * in {@link RouteOptions#overview()}.
     *
     * @param annotations a list of annotations that were used during the request
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder annotationsList(@Nullable List<String> annotations) {
      String result = FormatUtils.join(",", annotations);
      if (result != null) {
        annotations(result);
      }
      return this;
    }

    /**
     * Whether to return SSML marked-up text for voice guidance along the route (true) or not
     * (false, default if null).
     * Must be used in conjunction with {@link RouteOptions#steps()}=true.
     *
     * @param voiceInstructions true if the request included voice instructions
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder voiceInstructions(@Nullable Boolean voiceInstructions);

    /**
     * Whether to return banner objects associated with the route steps (true) or not
     * (false, default if null).
     * Must be used in conjunction with {@link RouteOptions#steps()}=true.
     *
     * @param bannerInstructions true if the request includes banner instructions
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder bannerInstructions(@Nullable Boolean bannerInstructions);

    /**
     * A type of units to return in the text for voice instructions.
     * Can be {@link DirectionsCriteria#IMPERIAL} (default) or {@link DirectionsCriteria#METRIC}.
     * Must be used in conjunction with {@link RouteOptions#steps()}=true and
     * {@link RouteOptions#steps()}=true
     * and {@link RouteOptions#voiceInstructions()}=true.
     *
     * @param voiceUnits a string matching either imperial or metric
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder voiceUnits(@Nullable String voiceUnits);

    /**
     * A valid Mapbox access token used to making the request.
     *
     * @param accessToken a string containing a valid Mapbox access token
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * Exclude certain road types from routing. The default is to not exclude anything from the
     * profile selected. The following exclude flags are available for each profile:
     * <p>
     * {@link DirectionsCriteria#PROFILE_DRIVING}: One of {@link DirectionsCriteria#EXCLUDE_TOLL},
     * {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or {@link DirectionsCriteria#EXCLUDE_FERRY}.
     * <p>
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}: One of
     * {@link DirectionsCriteria#EXCLUDE_TOLL}, {@link DirectionsCriteria#EXCLUDE_MOTORWAY}, or
     * {@link DirectionsCriteria#EXCLUDE_FERRY}.
     * <p>
     * {@link DirectionsCriteria#PROFILE_WALKING}: No excludes supported
     * <p>
     * {@link DirectionsCriteria#PROFILE_CYCLING}: {@link DirectionsCriteria#EXCLUDE_FERRY}
     *
     * @param exclude a string matching one of the {@link DirectionsCriteria.ExcludeCriteria}
     *                exclusions
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder exclude(@Nullable @DirectionsCriteria.ExcludeCriteria String exclude);

    /**
     * A semicolon-separated list indicating from which side of the road
     * to approach a waypoint.
     * Accepts  {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
     * {@link DirectionsCriteria#APPROACH_CURB}.
     * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
     * from either side of the road.
     * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
     * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
     * region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     * However, you can skip a coordinate and show its position in the list with the ; separator.
     * Since the first value will not be evaluated, begin the list with a semicolon.
     * If the waypoint is within 1 meter of the road, this parameter is ignored.
     *
     * @param approaches a string representing approaches for each waypoint
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder approaches(@Nullable String approaches);

    /**
     * Indicates from which side of the road to approach a waypoint.
     * Accepts {@link DirectionsCriteria#APPROACH_UNRESTRICTED} (default) or
     * {@link DirectionsCriteria#APPROACH_CURB} .
     * If set to {@link DirectionsCriteria#APPROACH_UNRESTRICTED}, the route can approach waypoints
     * from either side of the road.
     * If set to {@link DirectionsCriteria#APPROACH_CURB}, the route will be returned so that on
     * arrival, the waypoint will be found on the side that corresponds with the driving_side of the
     * region in which the returned route is located.
     * If provided, the list of approaches must be the same length as the list of waypoints.
     * However, you can skip a coordinate and show its position in the list with null.
     * Since the first value will not be evaluated, you can begin the list with a null.
     * If the waypoint is within 1 meter of the road, this parameter is ignored.
     *
     * @param approaches a list of strings representing approaches for each waypoint
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder approachesList(@Nullable List<String> approaches) {
      String result = FormatUtils.formatApproaches(approaches);
      if (result != null) {
        approaches(result);
      }
      return this;
    }

    /**
     * A semicolon-separated list indicating which input coordinates
     * should be treated as waypoints.
     * <p>
     * Waypoints form the beginning and end of each leg in the returned route and correspond to
     * the depart and arrive steps.
     * If a list of waypoints is not provided, all coordinates are treated as waypoints.
     * Each item in the list must be the zero-based index of an input coordinate,
     * and the list must include 0 (the index of the first coordinate)
     * and the index of the last coordinate.
     * The waypoints parameter can be used to guide the path of the route without
     * introducing additional legs and arrive/depart instructions.
     * <p>
     * For example, if a coordinates list has 3 points,
     * origin, some middle point, and destination, we can have below combinations:
     * <p>
     * - waypointIndices are null, the route will have 2 legs
     * <p>
     * - waypointIndices are "0;1;2", the route will have 2 legs
     * <p>
     * - waypointIndices are "0;2", the route will have only one leg that goes via the middle point
     * <p>
     * Must be used with {@link RouteOptions#steps()}=true.
     *
     * @param waypointIndices a string representing indices to be used as waypoints
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder waypointIndices(@Nullable String waypointIndices);

    /**
     * A list indicating which input coordinates should be treated as waypoints.
     * <p>
     * Waypoints form the beginning and end of each leg in the returned route and correspond to
     * the depart and arrive steps.
     * If a list of waypoints is not provided, all coordinates are treated as waypoints.
     * Each item in the list must be the zero-based index of an input coordinate,
     * and the list must include 0 (the index of the first coordinate)
     * and the index of the last coordinate.
     * The waypoints parameter can be used to guide the path of the route without
     * introducing additional legs and arrive/depart instructions.
     * <p>
     * For example, if a coordinates list has 3 points,
     * origin, some middle point, and destination, we can have below combinations:
     * <p>
     * - waypointIndices are null, the route will have 2 legs
     * <p>
     * - waypointIndices are [0,1,2], the route will have 2 legs
     * <p>
     * - waypointIndices are [0,2], the route will have only one leg that goes via the middle point
     * <p>
     * Must be used with {@link RouteOptions#steps()}=true.
     *
     * @param indices a List of Integers representing indices to be used as waypoints
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder waypointIndicesList(@Nullable List<Integer> indices) {
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
     * coordinates. The first value in the list corresponds to the route origin, not the first
     * destination.
     * To leave the origin unnamed, begin the list with a semicolon.
     * Must be used in conjunction with {@link RouteOptions#steps()}=true.
     *
     * @param waypointNames a string representing names for each waypoint
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder waypointNames(@Nullable String waypointNames);

    /**
     * A list of custom names for entries in the list of
     * {@link RouteOptions#coordinatesList()}, used for the arrival instruction in banners and voice
     * instructions. Values can be any string, and the total number of all characters cannot exceed
     * 500. If provided, the list of names must be the same length as the list of
     * coordinates. The first value in the list corresponds to the route origin, not the first
     * destination.
     * <p>
     * Must be used in conjunction with {@link RouteOptions#steps()}=true.
     *
     * @param waypointNames a list of strings representing names for each waypoint
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder waypointNamesList(@Nullable List<String> waypointNames) {
      String result = FormatUtils.join(";", waypointNames);
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
     * Since the first value will not be evaluated, begin the list with a semicolon.
     * <p>
     * Must be used with {@link RouteOptions#steps()}=true.
     *
     * @param waypointTargets a list of Points representing coordinate pairs for drop-off locations
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder waypointTargets(@Nullable String waypointTargets);

    /**
     * A list of points used to specify drop-off
     * locations that are distinct from the locations specified in coordinates.
     * If this parameter is provided, the Directions API will compute the side of the street,
     * left or right, for each target based on the waypoint_targets and the driving direction.
     * The maneuver.modifier, banner and voice instructions will be updated with the computed
     * side of street. The number of waypoint targets must be the same as the number of coordinates.
     * You can skip a coordinate pair and show its position in the list with the "null".
     * Since the first value will not be evaluated, you can begin the list with "null".
     * Must be used with {@link RouteOptions#steps()}=true.
     *
     * @param waypointTargets a list of Points representing coordinate pairs for drop-off locations
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder waypointTargetsList(@Nullable List<Point> waypointTargets) {
      waypointTargets(FormatUtils.formatPointsList(waypointTargets));
      return this;
    }

    /**
     * A scale from -1 to 1, where -1 biases the route against alleys
     * and 1 biases the route toward alleys. If null, default is 0, which is neutral.
     *
     * @param alleyBias bias towards alleys
     * @return this builder
     */
    @NonNull
    public abstract Builder alleyBias(
      @Nullable @FloatRange(from = -1, to = 1) Double alleyBias);

    /**
     * The walking speed in meters per second with a minimum of 0.14 m/s (or 0.5 km/h)
     * and a maximum of 6.94 m/s (or 25.0 km/h).
     * If null, defaults to 1.42 m/s (5.1 km/h).
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_WALKING}.
     *
     * @param walkingSpeed in meters per second
     * @return this builder
     */
    @NonNull
    public abstract Builder walkingSpeed(
      @Nullable @FloatRange(from = 0.14, to = 6.94) Double walkingSpeed);

    /**
     * A bias which determines whether the route should prefer or avoid the use of roads or paths
     * that are set aside for pedestrian-only use (walkways). The allowed range of values is from
     * -1 to 1, where -1 indicates indicates preference to avoid walkways, 1 indicates preference
     * to favor walkways, and 0 indicates no preference (the default, if null).
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_WALKING}.
     *
     * @param walkwayBias bias to prefer or avoid walkways
     * @return this builder
     */
    @NonNull
    public abstract Builder walkwayBias(
      @Nullable @FloatRange(from = -1, to = 1) Double walkwayBias);

    /**
     * A semicolon-separated list of booleans affecting snapping of waypoint locations to road
     * segments.
     * If true, road segments closed due to live-traffic closures will be considered for snapping.
     * If false, they will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * However, you can skip a coordinate and show its position in the list with the ; separator.
     * If null, this parameter defaults to false.
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}.
     *
     * @param snappingClosures a String representing a list of booleans
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder snappingIncludeClosures(@Nullable String snappingClosures);

    /**
     * A list of booleans affecting snapping of waypoint locations to road segments.
     * If true, road segments closed due to live-traffic closures will be considered for snapping.
     * If false, they will not be considered for snapping.
     * If provided, the number of snappingClosures must be the same as the number of
     * coordinates.
     * However, you can skip a coordinate and show its position in the list with the null.
     * If the list is null, this parameter defaults to false.
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC}.
     *
     * @param snappingClosures a list of booleans
     * @return this builder for chaining options together
     */
    @NonNull
    public Builder snappingIncludeClosuresList(@Nullable List<Boolean> snappingClosures) {
      String result = FormatUtils.join(";", snappingClosures);
      if (result != null) {
        snappingIncludeClosures(result);
      }
      return this;
    }

    /**
     * The desired arrival time, formatted as a timestamp in ISO-8601 format
     * in the local time at the route destination.
     * The travel time returned in duration is a prediction for travel time based
     * on historical travel data. The route is calculated in a time-dependent manner.
     * For example, a trip that takes two hours will consider changing historic traffic
     * conditions across the two-hour window.
     * The route takes timed turn restrictions and conditional access restrictions into account
     * based on the requested arrival time.
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING}.
     *
     * @param arriveBy arrive time ISO8601, see {@link FormatUtils#ISO_8601_PATTERN}
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder arriveBy(@Nullable String arriveBy);

    /**
     * The departure time, formatted as a timestamp in ISO-8601 format in the local time
     * at the route origin. The travel time returned in duration is a prediction for travel time
     * based on historical travel data. The route is calculated in a time-dependent manner.
     * For example, a trip that takes two hours will consider changing historic traffic conditions
     * across the two-hour window, instead of only at the specified depart_at time.
     * The route takes timed turn restrictions and conditional access restrictions into account
     * based on the requested departure time.
     * <p>
     * Only available with the {@link DirectionsCriteria#PROFILE_DRIVING}.
     *
     * @param departAt departure time ISO8601, see {@link FormatUtils#ISO_8601_PATTERN}
     * @return this builder for chaining options together
     */
    @NonNull
    public abstract Builder departAt(@Nullable String departAt);

    /**
     * Whether the routes should be refreshable via the directions refresh API.
     * <p>
     * If false, the refresh requests will fail. Defaults to false if null.
     *
     * @param enableRefresh whether the routes should be refreshable
     * @return this builder
     */
    @NonNull
    public abstract Builder enableRefresh(@Nullable Boolean enableRefresh);

    /**
     * Package private used to build the object and verify.
     */
    @NonNull
    abstract RouteOptions autoBuild();

    /**
     * This uses the provided parameters set using the {@link Builder} and first checks that all
     * values are valid, and creates a new {@link RouteOptions} object with the values provided.
     *
     * @return a new instance of {@link RouteOptions}
     */
    @NonNull
    public RouteOptions build() {
      RouteOptions routeOptions = autoBuild();

      List<Point> coordinates = routeOptions.coordinatesList();
      if (coordinates.size() < 2) {
        throw new RuntimeException(
          "An origin and destination are required before making the directions API request."
        );
      }

      List<Integer> waypointIndices = routeOptions.waypointIndicesList();
      if (waypointIndices != null && !waypointIndices.isEmpty()) {
        if (waypointIndices.size() < 2) {
          throw new RuntimeException(
            "Waypoints indices must be a list of at least two indexes, origin and destination."
          );
        }
        if (waypointIndices.get(0) != 0 || waypointIndices.get(waypointIndices.size() - 1)
          != coordinates.size() - 1) {
          throw new RuntimeException(
            "First and last waypoints indices must match the origin and final destination."
          );
        }
        for (int i = 1; i < waypointIndices.size() - 1; i++) {
          if (waypointIndices.get(i) < 0
            || waypointIndices.get(i) >= coordinates.size()) {
            throw new RuntimeException(
              "Waypoints index out of bounds (no corresponding coordinate).");
          }
        }
      }

      checkSizeMatchingWaypoints(
        "waypointTargets",
        true,
        routeOptions.waypointTargetsList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      checkSizeMatchingWaypoints(
        "approaches",
        false,
        routeOptions.approachesList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      checkSizeMatchingWaypoints(
        "snappingIncludeClosures",
        false,
        routeOptions.snappingIncludeClosuresList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      checkSizeMatchingWaypoints(
        "bearings",
        false,
        routeOptions.bearingsList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      checkSizeMatchingWaypoints(
        "radiuses",
        false,
        routeOptions.radiusesList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      checkSizeMatchingWaypoints(
        "waypointNames",
        true,
        routeOptions.waypointNamesList(),
        coordinates,
        routeOptions.waypointIndicesList()
      );

      return routeOptions;
    }

    private void checkSizeMatchingWaypoints(
      String paramName,
      boolean waypointIndicesDependent,
      List<?> testedParam,
      List<Point> coordinates,
      List<Integer> waypointIndices
    ) {
      if (testedParam != null && !testedParam.isEmpty()) {
        boolean error = false;
        if (waypointIndicesDependent && waypointIndices != null && !waypointIndices.isEmpty()) {
          if (testedParam.size() != waypointIndices.size()) {
            error = true;
          }
        } else if (testedParam.size() != coordinates.size()) {
          error = true;
        }
        if (error) {
          StringBuilder builder = new StringBuilder();
          builder.append("Number of ");
          builder.append(paramName);
          builder.append(" must match the number of coordinates");
          if (waypointIndicesDependent) {
            builder.append(" or waypoints indices (if present)");
          }
          builder.append(".");

          throw new RuntimeException(builder.toString());
        }
      }
    }
  }
}
