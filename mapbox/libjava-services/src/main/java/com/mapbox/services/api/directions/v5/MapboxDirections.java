package com.mapbox.services.api.directions.v5;

import com.mapbox.services.api.MapboxBuilder;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.TextUtils;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;

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
 * The Directions API allows the calculation of routes between coordinates. The fastest route
 * is returned with geometries, and turn-by-turn instructions. The Mapbox Directions API supports
 * routing for driving cars, riding bicycles and walking.
 *
 * @since 1.0.0
 */
public class MapboxDirections extends MapboxService<DirectionsResponse> {

  protected Builder builder = null;
  private DirectionsService service = null;
  private Call<DirectionsResponse> call = null;

  protected MapboxDirections(Builder builder) {
    this.builder = builder;
  }

  private DirectionsService getService() {
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
    service = retrofitBuilder.build().create(DirectionsService.class);
    return service;
  }

  private Call<DirectionsResponse> getCall() {
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
      builder.isAlternatives(),
      builder.getGeometries(),
      builder.getOverview(),
      builder.getRadiuses(),
      builder.isSteps(),
      builder.getBearings(),
      builder.isContinueStraight(),
      builder.getAnnotation());

    // Done
    return call;
  }

  /**
   * Execute the call
   *
   * @return The Directions v5 response
   * @throws IOException Signals that an I/O exception of some sort has occurred.
   * @since 1.0.0
   */
  @Override
  public Response<DirectionsResponse> executeCall() throws IOException {
    return getCall().execute();
  }

  /**
   * Execute the call
   *
   * @param callback A Retrofit callback.
   * @since 1.0.0
   */
  @Override
  public void enqueueCall(Callback<DirectionsResponse> callback) {
    getCall().enqueue(callback);
  }

  /**
   * Cancel the call
   *
   * @since 1.0.0
   */
  @Override
  public void cancelCall() {
    getCall().cancel();
  }

  /**
   * clone the call
   *
   * @return cloned call
   * @since 1.0.0
   */
  @Override
  public Call<DirectionsResponse> cloneCall() {
    return getCall().clone();
  }

  /**
   * Directions v5 builder
   *
   * @since 1.0.0
   */
  public static class Builder<T extends Builder> extends MapboxBuilder {

    // We use `Boolean` instead of `boolean` to allow unset (null) values.
    private String user = null;
    private String profile = null;
    private List<Position> coordinates = null;
    private String accessToken = null;
    private Boolean alternatives = null;
    private String geometries = null;
    private String overview = null;
    private double[] radiuses = null;
    private double[][] bearings = null;
    private Boolean steps = null;
    private Boolean continueStraight = null;
    private Position origin = null;
    private Position destination = null;
    private String[] annotation = null;

    /**
     * Constructor
     *
     * @since 1.0.0
     */
    public Builder() {
      // Set defaults
      this.user = DirectionsCriteria.PROFILE_DEFAULT_USER;

      // by defauly the geometry is polyline with precision 6.
      this.geometries = DirectionsCriteria.GEOMETRY_POLYLINE6;
    }

    /**
     * The SDK currently only supports {@code Polyline} geometry (not GeoJSON) to simplify the response. You do have
     * the option on whether the precision is 5 or 6 using either {@link DirectionsCriteria#GEOMETRY_POLYLINE} or
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     *
     * @param geometries A {@code String} constant which equals either {@code "polyline"} or {@code "polyline6"}.
     * @return Builder
     * @since 2.0.0
     */
    public T setGeometry(String geometries) {
      this.geometries = geometries;
      return (T) this;
    }

    /*
     * Setters
     */

    /**
     * @param user User string
     * @return Builder
     * @since 1.0.0
     */
    public T setUser(String user) {
      this.user = user;
      return (T) this;
    }

    /**
     * @param profile Profile string
     * @return Builder
     * @since 1.0.0
     */
    public T setProfile(String profile) {
      this.profile = profile;
      return (T) this;
    }

    /**
     * Set the list of coordinates for the directions service. If you've previously set an
     * origin with setOrigin() or a destination with setDestination(), those will be
     * overridden.
     *
     * @param coordinates List of {@link Position} giving origin and destination(s) coordinates.
     * @return Builder
     * @since 1.0.0
     */
    public T setCoordinates(List<Position> coordinates) {
      this.coordinates = coordinates;
      return (T) this;
    }

    /**
     * Inserts the specified position at the beginning of the coordinates list. If you've
     * set other coordinates previously with setCoordinates() those elements are kept
     * and their index will be moved up by one (the coordinates are moved to the right).
     *
     * @param origin {@link Position} of route origin.
     * @return Builder
     * @since 1.0.0
     */
    public T setOrigin(Position origin) {
      this.origin = origin;
      return (T) this;
    }

    /**
     * Appends the specified destination to the end of the coordinates list. If you've
     * set other coordinates previously with setCoordinates() those elements are kept
     * and the destination is added at the end of the list.
     *
     * @param destination {@link Position} of route destination.
     * @return Builder
     * @since 1.0.0
     */
    public T setDestination(Position destination) {
      this.destination = destination;
      return (T) this;
    }

    /**
     * Required to call when building {@link Builder}.
     *
     * @param accessToken Mapbox access token, You must have a Mapbox account inorder to use
     *                    this library.
     * @return Builder
     * @since 1.0.0
     */
    @Override
    public T setAccessToken(String accessToken) {
      this.accessToken = accessToken;
      return (T) this;
    }

    /**
     * Optionally, call if you'd like to receive alternative routes besides just one.
     *
     * @param alternatives true if you'd like alternative routes, else false.
     * @return Builder
     * @since 1.0.0
     */
    public T setAlternatives(Boolean alternatives) {
      this.alternatives = alternatives;
      return (T) this;
    }

    /**
     * Optionally, set whether you want the route geometry to be full, simplified, etc.
     *
     * @param overview String defining type of overview you'd like the API to give. Use one of
     *                 the constants.
     * @return Builder
     * @since 1.0.0
     */
    public T setOverview(String overview) {
      this.overview = overview;
      return (T) this;
    }

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and dictates the angle of
     * approach. This option should always be used in conjunction with the
     * {@link MapboxDirections.Builder#setRadiuses(double[])} parameter. The parameter takes two values per waypoint:
     * the first is an angle clockwise from true north between 0 and 360. The second is the range of degrees the angle
     * can deviate by. We recommend a value of 45 degrees or 90 degrees for the range, as bearing measurements tend to
     * be inaccurate. This is useful for making sure we reroute vehicles on new routes that continue traveling in their
     * current direction. A request that does this would provide bearing and radius values for the first waypoint and
     * leave the remaining values empty. If provided, the list of bearings must be the same length as the list of
     * waypoints, but you can skip a coordinate and show its position by passing in a double array with no values
     * included like so: {@code new double[] {}}.
     *
     * @param bearings a double array with two values indicating the angle and the other value indicating the deviating
     *                 range.
     * @return Builder
     * @since 2.0.0
     */
    public T setBearings(double[]... bearings) {
      this.bearings = bearings;
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
     * @since 1.0.0
     */
    public T setRadiuses(double[] radiuses) {
      this.radiuses = radiuses;
      return (T) this;
    }

    /**
     * Optionally, call if you'd like to include step information within route.
     *
     * @param steps true if you'd like step information.
     * @return Builder
     * @since 1.0.0
     */
    public T setSteps(Boolean steps) {
      this.steps = steps;
      return (T) this;
    }

    /**
     * Toggle whether you want the route to always continue straight toward the next destination
     * or allow for backtracking.
     *
     * @param continueStraight boolean true if you want to always continue straight, else false.
     * @return Builder
     * @since 1.0.0
     */
    public T setContinueStraight(Boolean continueStraight) {
      this.continueStraight = continueStraight;
      return (T) this;
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE}, {@link DirectionsCriteria#ANNOTATION_DURATION}, and
     * {@link DirectionsCriteria#ANNOTATION_SPEED}. Several annotation can be used by separating them with {@code ,}.
     *
     * @param annotation String referencing one of the annotation direction criteria's.
     * @return Builder
     * @see <a href="https://www.mapbox.com/api-documentation/#routeleg-object">RouteLeg object documentation</a> for
     * more information.
     * @since 2.1.0
     */
    public T setAnnotation(String... annotation) {
      this.annotation = annotation;
      return (T) this;
    }

    /*
     * Getters, they return the value in a format ready for the API to consume
     */

    /**
     * @return the user as String
     * @since 1.0.0
     */
    public String getUser() {
      return user;
    }

    /**
     * @return {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_DRIVING_TRAFFIC},
     * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_DRIVING},
     * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_CYCLING},
     * or {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#PROFILE_WALKING}
     * @since 1.0.0
     */
    public String getProfile() {
      return profile;
    }

    /**
     * The coordinates parameter denotes between which points routing happens. The coordinates
     * must be in the format:
     * <p>
     * {longitude},{latitude};{longitude},{latitude}[;{longitude},{latitude} ...]
     * <p>
     * - Each coordinate is a pair of a longitude float and latitude float, which are separated by a ,
     * - Coordinates are separated by a ; from each other
     * - A query must at minimum have 2 coordinates and may at maximum have 25 coordinates
     *
     * @return String containing coordinates formatted.
     * @since 1.0.0
     */
    public String getCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      // Insert origin at beginning of list if one is provided.
      if (origin != null) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          origin.getLongitude(),
          origin.getLatitude()));
      }
      if (coordinates != null) {
        for (Position coordinate : coordinates) {
          coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
            coordinate.getLongitude(),
            coordinate.getLatitude()));
        }
      }
      // Insert destination at end of list if one is provided.
      if (destination != null) {
        coordinatesFormatted.add(String.format(Locale.US, "%f,%f",
          destination.getLongitude(),
          destination.getLatitude()));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    /**
     * @return your Mapbox access token.
     * @since 1.0.0
     */
    @Override
    public String getAccessToken() {
      return accessToken;
    }

    /**
     * @return true if you {@link #setAlternatives(Boolean)} to true.
     * @since 1.0.0
     */
    public Boolean isAlternatives() {
      return alternatives;
    }

    /**
     * @return {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#GEOMETRY_GEOJSON},
     * {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#GEOMETRY_POLYLINE},
     * or {@link com.mapbox.services.api.directions.v5.DirectionsCriteria#GEOMETRY_POLYLINE6}
     * @since 1.0.0
     */
    public String getGeometries() {
      return geometries;
    }

    /**
     * @return The overview, full, simplied, etc.
     * @since 1.0.0
     */
    public String getOverview() {
      return overview;
    }

    /**
     * @return The optional bearing values if given, otherwise null.
     * @since 2.0.0
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
          bearingFormatted[i] = String.format(Locale.US, "%f,%f", bearings[i][0], bearings[i][1]);
        }
      }
      return TextUtils.join(";", bearingFormatted);
    }

    /**
     * Radiuses indicate how far from a coordinate a routeable way is searched. They
     * are indicated like this:
     * <p>
     * ?radiuses={radius};{radius}}[;{radius} ...].
     * <p>
     * If no routeble way can be found within the serach radius, a NoRoute error will be returned.
     * - Radiuses are separated by a ,
     * - Each radius must be of a value {@code float >= 0} in meters or unlimited (default)
     * - There must be as many radiuses as there are coordinates
     * - It is possible to not specify radiuses via ;;, which result in the same behaviour as setting unlimited
     *
     * @return String containing formatted radiuses.
     * @since 1.0.0
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
          radiusesFormatted[i] = String.format(Locale.US, "%f", radiuses[i]);
        }
      }

      return TextUtils.join(";", radiusesFormatted);
    }

    /**
     * @return true if you requested step information in {@link #setSteps(Boolean)}.
     * @since 1.0.0
     */
    public Boolean isSteps() {
      return steps;
    }

    /**
     * Determine whether the route's been set to continue straight or not.
     *
     * @return true if continuing straight, otherwise false.
     * @since 1.0.0
     */
    public Boolean isContinueStraight() {
      return continueStraight;
    }

    /**
     * returns one or a combination of {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION}, or {@link DirectionsCriteria#ANNOTATION_SPEED}.
     *
     * @return String with 1 or several annotations that have been set.
     * @since 2.1.0
     */
    public String getAnnotation() {
      if (annotation == null || annotation.length == 0) {
        return null;
      }
      return TextUtils.join(",", annotation);
    }

    public T setClientAppName(String appName) {
      super.clientAppName = appName;
      return (T) this;
    }

    public T setBaseUrl(String baseUrl) {
      super.baseUrl = baseUrl;
      return (T) this;
    }

    /**
     * Build method
     *
     * @return MapboxDirections
     * @throws ServicesException Generic Exception for all things directions.
     * @since 1.0.0
     */
    @Override
    public MapboxDirections build() throws ServicesException {
      validateAccessToken(accessToken);

      // Get the total number of coordinates being passed to API
      int coordLength = 0;
      if (coordinates != null) {
        coordLength = coordinates.size();
      }
      if (origin != null) {
        coordLength += 1;
      }
      if (destination != null) {
        coordLength += 1;
      }

      if (profile == null) {
        throw new ServicesException(
          "A profile is required for the Directions API. Use one of the profiles found in the"
            + "DirectionsCriteria.java file.");
      }

      if (origin == null && destination == null) {
        if (coordinates == null || coordLength < 2) {
          throw new ServicesException(
            "You should provide at least two coordinates (from/to).");
        }
      }

      if (profile.equals(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
        && coordLength > 3) {
        throw new ServicesException(
          "Using the driving-traffic profile allows for maximum of 3 coordinates.");
      }


      if (coordLength > 25) {
        throw new ServicesException(
          "All profiles (except driving-traffic) allows for maximum of 25 coordinates.");
      }


      if (radiuses != null && radiuses.length != coordLength) {
        throw new ServicesException(
          "There must be as many radiuses as there are coordinates.");
      }
      
      if (radiuses != null) {
        for (double radius : radiuses) {
          if (radius < 0) {
            throw new ServicesException(
              "Radius values need to be greater than zero.");
          }
        }
      }

      if (bearings != null) {
        for (double[] bearing : bearings) {
          if (bearing.length != 2 && bearing.length != 0) {
            throw new ServicesException(
              "Requesting a route which includes bearings requires exactly 2 values in each double array.");
          }
        }
      }

      if (bearings != null && bearings.length != coordLength) {
        throw new ServicesException(
          "There must be as many bearings as there are coordinates.");
      }

      if (annotation != null) {
        if (annotation.length > 3) {
          throw new ServicesException(
            "Annotation request can only contain one of the three DirectionsCriteria constants.");
        }

        // Check that user isn't using incorrect annotation request.
        for (String annotationEntry : annotation) {
          if (!annotationEntry.equals(DirectionsCriteria.ANNOTATION_DISTANCE)
            && !annotationEntry.equals(DirectionsCriteria.ANNOTATION_DURATION)
            && !annotationEntry.equals(DirectionsCriteria.ANNOTATION_SPEED)) {
            throw new ServicesException(
              "Annotation value must be one of the constant values found inside DirectionsCriteria");
          }
        }
      }

      return new MapboxDirections(this);
    }
  }
}
