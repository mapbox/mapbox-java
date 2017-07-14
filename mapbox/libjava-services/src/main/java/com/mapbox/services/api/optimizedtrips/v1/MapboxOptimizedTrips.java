package com.mapbox.services.api.optimizedtrips.v1;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The Mapbox Optimized Trips API returns a duration-optimized trip between the input coordinates. This is also known
 * as solving the Traveling Salesperson Problem. A typical use case for this API is planning the route for deliveries
 * in a city. Optimized trips can be retrieved for car driving, bicycling and walking or hiking.
 * <p>
 * Under normal plans, a maximum of 12 coordinates can be passed in at once at a maximum 60 requests per minute. For
 * higher volumes, reach out through our contact page.
 * <p>
 * Note that for under 10 coordinates, the returned results will be optimal. For 10 and more coordinates, the results
 * will be optimized approximations.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">Traveling Salesperson Problem</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#optimized-trips">API documentation</a>
 * @since 2.1.0
 */
public class MapboxOptimizedTrips extends MapboxService<OptimizedTripsResponse> {

  protected Builder builder = null;
  private OptimizedTripsService service = null;
  private Call<OptimizedTripsResponse> call = null;

  protected MapboxOptimizedTrips(Builder builder) {
    this.builder = builder;
  }

  private OptimizedTripsService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(builder.getBaseUrl())
      .addConverterFactory(GsonConverterFactory.create());
    if (getCallFactory() != null) {
      retrofitBuilder.callFactory(getCallFactory());
    } else {
      retrofitBuilder.client(getOkHttpClient());
    }

    // Directions service
    service = retrofitBuilder.build().create(OptimizedTripsService.class);
    return service;
  }

  private Call<OptimizedTripsResponse> getCall() {
    // No need to recreate it
    if (call != null) {
      return call;
    }

    call = getService().getCall(
      getHeaderUserAgent(builder.getClientAppName()),
      builder.getUser(),
      builder.getProfile(),
      builder.getCoordinates(),
      builder.getAccessToken(),
      builder.getRoundTrip(),
      builder.getRadiuses(),
      builder.getBearings(),
      builder.getSteps(),
      builder.getOverview(),
      builder.getGeometries(),
      builder.getAnnotation(),
      builder.getDestination(),
      builder.getSource(),
      builder.getLanguage(),
      builder.getDistributions());

    // Done
    return call;
  }

  /**
   * Execute the call
   *
   * @return The Directions Matrix v1 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 2.1.0
   */
  @Override
  public Response<OptimizedTripsResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 2.1.0
   */
  @Override
  public void enqueueCall(Callback<OptimizedTripsResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 2.1.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 2.1.0
   */
  @Override
  public Call<OptimizedTripsResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Optimized Trips v1 builder
   *
   * @since 2.1.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    private String user;
    private String accessToken;
    private String profile;
    private List<Position> coordinates;
    private Boolean roundTrip;
    private String source;
    private String destination;
    private String geometries;
    private double[] radiuses;
    private Boolean steps;
    private double[][] bearings;
    private String[] annotations;
    private String overview;
    private String language;
    private double[][] distributions;

    /**
     * Constructor
     *
     * @since 2.1.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;

      // by defauly the geometry is polyline with precision 6.
      this.geometries = DirectionsCriteria.GEOMETRY_POLYLINE6;
    }

    /*
     * Setters
     */

    /**
     * Required to call when building {@link Builder}.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    the Optimized Trip API.
     * @return Builder
     * @since 2.1.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    /**
     * @param user User string
     * @return Builder
     * @since 2.1.0
     */
    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    /**
     * @param profile Profile string
     * @return Builder
     * @since 2.1.0
     */
    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    /**
     * Set the list of coordinates for the Optimized Trips service. If you've previously set an
     * origin with setOrigin() or a destination with setDestination(), those will be
     * overridden.
     *
     * @param coordinates List of {@link Position} giving origin and destination(s) coordinates.
     * @return Builder
     * @since 2.1.0
     */
    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    /**
     * set whether the route is a roundtrip (route returns to first location). This defaults to true, but must be
     * carefully set depending on whether you have used {@link MapboxOptimizedTrips.Builder#setSource(String)} and
     * {@link MapboxOptimizedTrips.Builder#setDestination(String)}.
     *
     * @param roundTrip boolean true if you'd like the route to return to original position, else false.
     * @return Builder
     * @since 2.1.0
     */
    public T setRoundTrip(Boolean roundTrip) {
      this.roundTrip = roundTrip;
      return (T) this;
    }

    /**
     * Returned route starts at {@link DirectionsCriteria#SOURCE_ANY} (default )or
     * {@link DirectionsCriteria#SOURCE_FIRST} coordinate.
     *
     * @param source either {@code "any" or "first"}.
     * @return Builder
     * @since 2.1.0
     */
    public T setSource(String source) {
      this.source = source;
      return (T) this;
    }

    /**
     * Returned route ends at {@link DirectionsCriteria#SOURCE_ANY} or {@link DirectionsCriteria#SOURCE_LAST}
     * coordinate.
     *
     * @param destination either {@code "any" or "last"}.
     * @return Builder
     * @since 2.1.0
     */
    public T setDestination(String destination) {
      this.destination = destination;
      return (T) this;
    }

    /**
     * The SDK currently only supports {@code Polyline} geometry (not GeoJSON) to simplify the response. You do have
     * the option on whether the precision is 5 or 6 using either {@link DirectionsCriteria#GEOMETRY_POLYLINE} or
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @param geometries A {@code String} constant which equals either {@code "polyline"} or {@code "polyline6"}.
     * @return Builder
     * @since 2.1.0
     */
    public T setGeometries(String geometries) {
      this.geometries = geometries;
      return (T) this;
    }

    /**
     * Optionally, set the maximum distance in meters that each coordinate is allowed to move when snapped to a nearby
     * road segment. There must be as many radiuses as there are coordinates in the request. Values can be any number
     * greater than 0 or they can be unlimited simply by passing {@link Double#POSITIVE_INFINITY}. If no routable road
     * is found within the radius, a {@code NoSegment} error is returned.
     *
     * @param radiuses double array containing the radiuses defined in unit meters.
     * @return Builder
     * @since 2.1.0
     */
    public T setRadiuses(double[] radiuses) {
      this.radiuses = radiuses;
      return (T) this;
    }

    /**
     * Optionally, set whether you want the route geometry to be full, simplified, etc.
     *
     * @param overview String defining type of overview you'd like the API to give. Use one of
     *                 the constants.
     * @return Builder
     * @since 2.1.0
     */
    public T setOverview(String overview) {
      this.overview = overview;
      return (T) this;
    }

    /**
     * Optionally, call if you'd like to include step information within route.
     *
     * @param steps true if you'd like step information.
     * @return Builder
     * @since 2.1.0
     */
    public T setSteps(Boolean steps) {
      this.steps = steps;
      return (T) this;
    }

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and dictates the angle of
     * approach. This option should always be used in conjunction with the
     * {@link MapboxOptimizedTrips.Builder#setRadiuses(double[])} parameter. The parameter takes two values per
     * waypoint: the first is an angle clockwise from true north between 0 and 360. The second is the range of degrees
     * the angle can deviate by. We recommend a value of 45 degrees or 90 degrees for the range, as bearing measurements
     * tend to be inaccurate. This is useful for making sure we reroute vehicles on new routes that continue traveling
     * in their current direction. A request that does this would provide bearing and radius values for the first
     * waypoint and leave the remaining values empty. If provided, the list of bearings must be the same length as the
     * list of waypoints, but you can skip a coordinate and show its position by passing in a double array with no
     * values included like so: {@code new double[] {}}.
     *
     * @param bearings a double array with two values indicating the angle and the other value indicating the deviating
     *                 range.
     * @return Builder
     * @since 2.1.0
     */
    public T setBearings(double[]... bearings) {
      this.bearings = bearings;
      return (T) this;
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}, {@link DirectionsCriteria#ANNOTATION_DURATION}, and
     * {@link DirectionsCriteria#ANNOTATION_SPEED}. Several annotations can be used by separating them with {@code ,}.
     *
     * @param annotation String referencing one of the annotations direction criteria's.
     * @return Builder
     * @see <a href="https://www.mapbox.com/api-documentation/#routeleg-object">RouteLeg object documentation</a>
     * @since 2.1.0
     * @deprecated use {@link Builder#getAnnotations()} instead
     */
    @Deprecated
    public T setAnnotation(String... annotation) {
      this.annotations = annotation;
      return (T) this;
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}, {@link DirectionsCriteria#ANNOTATION_DURATION}, and
     * {@link DirectionsCriteria#ANNOTATION_SPEED}. Several annotations can be used by separating them with {@code ,}.
     *
     * @param annotation String referencing one of the annotations direction criteria's.
     * @return Builder
     * @see <a href="https://www.mapbox.com/api-documentation/#routeleg-object">RouteLeg object documentation</a>
     * @since 2.1.0
     */
    public T setAnnotations(String... annotation) {
      this.annotations = annotation;
      return (T) this;
    }

    /**
     * Optionally change the APIs base URL to something other then the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return Builder
     * @since 2.1.0
     */
    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    /**
     * Base package name or other simple string identifier
     *
     * @param appName base package name or other simple string identifier
     * @return Builder
     * @since 2.1.0
     */
    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    /**
     * Optionally set the language of returned turn-by-turn text instructions. The default is {@code en} for English.
     *
     * @param language The locale in which results should be returned.
     * @return Builder
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported languages</a>
     * @since 2.2.0
     */
    public T setLanguage(String language) {
      this.language = language;
      return (T) this;
    }

    /**
     * Specify pick-up and drop-off locations for a trip by providing a {@code double[]} each with a number pair that
     * correspond with the coordinates list. The first number indicates what place the coordinate of the pick-up
     * location is in the coordinates list, and the second number indicates what place the coordinate of the drop-off
     * location is in the coordinates list. Each pair must contain exactly two numbers. Pick-up and drop-off locations
     * in one pair cannot be the same. The returned solution will visit pick-up locations before visiting drop-off
     * locations. The depot (first location) can only be a pick-up location but not a drop-off location.
     *
     * @param distributions {@code double[]} with two values, first being the pickup coordinate in the coordinates list
     *                      and the second number being the coordinate in the coordinates list which should be the drop
     *                      off location.
     * @return Builder
     * @since 2.2.0
     */
    public T setDistributions(double[]... distributions) {
      this.distributions = distributions;
      return (T) this;
    }

    /*
     * Getters
     */

    /**
     * The current profile set for usage with the Optimized Trips API.
     *
     * @return {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_DRIVING},
     * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_CYCLING},
     * or {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_WALKING}
     * @since 2.1.0
     */
    public String getProfile() {
      return profile;
    }

    /**
     * set whether the route is a roundtrip (route returns to first location). This defaults to true, but must be
     * carefully set depending on whether you have used {@link MapboxOptimizedTrips.Builder#setSource(String)} and
     * {@link MapboxOptimizedTrips.Builder#setDestination(String)}.
     *
     * @return boolean true if {@link MapboxOptimizedTrips} has been set to have the route return to original position,
     * else false.
     * @since 2.1.0
     */
    public Boolean getRoundTrip() {
      return roundTrip;
    }

    /**
     * Returned route starts at {@link DirectionsCriteria#SOURCE_ANY} (default )or
     * {@link DirectionsCriteria#SOURCE_FIRST} coordinate.
     *
     * @return either {@code "any" or "first"}.
     * @since 2.1.0
     */
    public String getSource() {
      return source;
    }

    /**
     * Returned route ends at {@link DirectionsCriteria#SOURCE_ANY} or {@link DirectionsCriteria#SOURCE_LAST}
     * coordinate.
     *
     * @return either {@code "any" or "last"}.
     * @since 2.1.0
     */
    public String getDestination() {
      return destination;
    }

    /**
     * Set the list of coordinates for the Optimized Trips service. If you've previously set an
     * origin with setOrigin() or a destination with setDestination(), those will be
     * overridden.
     *
     * @return List of {@link Position} giving origin and destination(s) coordinates.
     * @since 2.1.0
     */
    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Position coordinate : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          coordinate.getLongitude(),
          coordinate.getLatitude()));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    /**
     * The SDK currently only supports {@code Polyline} geometry (not GeoJSON) to simplify the response. You do have
     * the option on whether the precision is 5 or 6 using either {@link DirectionsCriteria#GEOMETRY_POLYLINE} or
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @return a {@code String} constant which equals either {@code "polyline"} or {@code "polyline6"}.
     * @since 2.1.0
     */
    public String getGeometries() {
      return geometries;
    }

    /**
     * Get the maximum distance in meters you have set that each coordinate is allowed to move when snapped to a nearby
     * road segment.
     *
     * @return String containing the radiuses defined in unit meters.
     * @since 2.1.0
     */
    public String getRadiuses() {
      if (radiuses == null || radiuses.length == 0) {
        return null;
      }

      String[] radiusesFormatted = new String[radiuses.length];
      for (int i = 0; i < radiuses.length; i++) {
        if (radiuses[i] == Double.POSITIVE_INFINITY) {
          radiusesFormatted[i] = "unlimited";
        } else {
          radiusesFormatted[i] = String.format(Locale.US, "%s", TextUtils.formatCoordinate(radiuses[i]));
        }
      }

      return TextUtils.join(";", radiusesFormatted);
    }

    /**
     * @return User string
     * @since 2.1.0
     */
    public String getUser() {
      return user;
    }

    /**
     * Determine whether you want the route geometry to be full, simplified, etc.
     *
     * @return String defining type of overview you'd like the API to give. Use one of
     * the constants.
     * @since 2.1.0
     */
    public String getOverview() {
      return overview;
    }

    /**
     * Determine whether or not you are requesting for the steps information or not in your request.
     *
     * @return true if you'd like step information.
     * @since 2.1.0
     */
    public Boolean getSteps() {
      return steps;
    }

    /**
     * Determine whether you are filtering the road segment the waypoint will be placed on by direction and dictating
     * the angle of approach.
     *
     * @return String with two values indicating the angle and the other value indicating the deviating
     * range.
     * @since 2.1.0
     */
    public String getBearings() {
      if (bearings == null || bearings.length == 0) {
        return null;
      }

      String[] bearingFormatted = new String[bearings.length];
      for (int i = 0; i < bearings.length; i++) {
        if (bearings[i].length == 0) {
          bearingFormatted[i] = "";
        } else {
          bearingFormatted[i] = String.format(Locale.US, "%s,%s",
            TextUtils.formatCoordinate(bearings[i][0]),
            TextUtils.formatCoordinate(bearings[i][1]));
        }
      }
      return TextUtils.join(";", bearingFormatted);
    }

    /**
     * Determine whether or not you are currently requesting additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}, {@link DirectionsCriteria#ANNOTATION_DURATION}, and
     * {@link DirectionsCriteria#ANNOTATION_SPEED}. Several annotations can be used by separating them with {@code ,}.
     *
     * @return String referencing one of the annotations direction criteria's.
     * @since 2.1.0
     * @deprecated Use {@link Builder#getAnnotations()}
     */
    @Deprecated
    public String[] getAnnotation() {
      return annotations;
    }

    /**
     * Determine whether or not you are currently requesting additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}, {@link DirectionsCriteria#ANNOTATION_DURATION}, and
     * {@link DirectionsCriteria#ANNOTATION_SPEED}. Several annotations can be used by separating them with {@code ,}.
     *
     * @return String referencing one of the annotations direction criteria's.
     * @since 2.1.0
     */
    public String[] getAnnotations() {
      return annotations;
    }

    /**
     * @return your Mapbox access token.
     * @since 2.1.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    /**
     * @return The language the turn-by-turn directions will be in.
     * @since 2.2.0
     */
    public String getLanguage() {
      return language;
    }

    /**
     * If distribution values are set inside your request, this will return a String containing the given values.
     *
     * @return String containing the provided distribution values.
     * @since 2.2.0
     */
    public String getDistributions() {
      if (distributions == null || distributions.length == 0) {
        return null;
      }

      String[] distributionsFormatted = new String[distributions.length];
      for (int i = 0; i < distributions.length; i++) {
        if (distributions[i].length == 0) {
          distributionsFormatted[i] = "";
        } else {
          distributionsFormatted[i] = String.format(Locale.US, "%s,%s",
            TextUtils.formatCoordinate(distributions[i][0]),
            TextUtils.formatCoordinate(distributions[i][1]));
        }
      }
      return TextUtils.join(";", distributionsFormatted);
    }

    /**
     * Build method
     *
     * @return {@link MapboxOptimizedTrips}
     * @throws ServicesException occurs if your request you've built isn't valid and/or contains errors.
     * @since 2.1.0
     */
    @Override
    public MapboxOptimizedTrips build() throws ServicesException {
      validateAccessToken(accessToken);

      if (profile == null) {
        throw new ServicesException("A profile is required for the Optimized Trips API.");
      } else if (profile.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)) {
        throw new ServicesException("The driving traffic profile does not work with this API.");
      } else if (!profile.equals(DirectionsCriteria.PROFILE_CYCLING)
        && !profile.equals(DirectionsCriteria.PROFILE_WALKING)
        && !profile.equals(DirectionsCriteria.PROFILE_DRIVING)) {
        throw new ServicesException("A valid profile must be used with the Optimized Trips API.");
      }

      if (coordinates == null) {
        throw new ServicesException("At least two coordinates must be provided with your API request.");
      } else if (coordinates.size() < 2) {
        throw new ServicesException("At least two coordinates must be provided with your API request.");
      } else if (coordinates.size() > 12) {
        throw new ServicesException("Maximum of 12 coordinates are allowed for this API.");
      }

      return new MapboxOptimizedTrips(this);
    }
  }
}
