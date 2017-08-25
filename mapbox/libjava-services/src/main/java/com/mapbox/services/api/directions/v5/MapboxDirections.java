package com.mapbox.services.api.directions.v5;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.services.Constants;
import com.mapbox.services.api.MapboxService;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.AnnotationCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.GeometriesCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.OverviewCriteria;
import com.mapbox.services.api.directions.v5.DirectionsCriteria.ProfileCriteria;
import com.mapbox.services.api.directions.v5.gson.DirectionsAdapterFactory;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.utils.TextUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The Directions API allows the calculation of routes between coordinates. The fastest route can be
 * returned with geometries, turn-by-turn instructions, and much more. The Mapbox Directions API
 * supports routing for driving cars (including live traffic), riding bicycles and walking.
 * Requested routes can include as much as 25 coordinates anywhere on earth (except the traffic
 * profile).
 * <p>
 * Requesting a route at a bare minimal must include, a Mapbox access token, destination, and an
 * origin.
 *
 * @see <a href="https://www.mapbox.com/android-docs/mapbox-services/overview/directions/">Android
 * Directions documentation</a>
 * @see <a href="https://www.mapbox.com/api-documentation/#directions">Directions API
 * documentation</a>
 * @since 1.0.0
 */
@AutoValue
public abstract class MapboxDirections extends MapboxService<DirectionsResponse> {

  protected Builder builder;
  private DirectionsService service;
  private Call<DirectionsResponse> call;
  private Gson gson;


  protected Gson getGson() {
    // Gson instance with type adapters
    if (gson == null) {
      gson = new GsonBuilder()
        .registerTypeAdapterFactory(DirectionsAdapterFactory.create())
        .create();
    }
    return gson;
  }

  private DirectionsService getService() {
    // No need to recreate it
    if (service != null) {
      return service;
    }

    // Retrofit instance
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(baseUrl())
      .addConverterFactory(GsonConverterFactory.create(getGson()));
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
      getHeaderUserAgent(clientAppName()),
      user(),
      profile(),
      coordinates(),
      accessToken(),
      alternatives(),
      geometries(),
      overview(),
      radiuses(),
      steps(),
      bearings(),
      continueStraight(),
      annotations(),
      language());

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

  abstract String user();

  abstract String profile();

  abstract String coordinates();

  abstract String accessToken();

  abstract String baseUrl();

  @Nullable
  abstract Boolean alternatives();

  @Nullable
  abstract String geometries();

  @Nullable
  abstract String overview();

  @Nullable
  abstract String radiuses();

  @Nullable
  abstract String bearings();

  @Nullable
  abstract Boolean steps();

  @Nullable
  abstract Boolean continueStraight();

  @Nullable
  abstract String annotations();

  @Nullable
  abstract String language();

  @Nullable
  abstract String clientAppName();

  public static Builder builder() {
    return new AutoValue_MapboxDirections.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .profile(DirectionsCriteria.PROFILE_DRIVING)
      .user(DirectionsCriteria.PROFILE_DEFAULT_USER)
      .geometries(DirectionsCriteria.GEOMETRY_POLYLINE6);
  }

  /**
   * This builder is used to create a new request to the Mapbox Directions API. At a bare minimum,
   * your request must include an access token, an origin, and a destination. All other fields can
   * be left alone inorder to use the default behaviour of the API.
   * <p>
   * By default, the directions profile is set to driving (without traffic) but can be changed to
   * reflect your users use-case.
   * </p><p>
   * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
   * rather than the primitive to allow for unset (null) values.
   * </p>
   *
   * @since 1.0.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    List<Double[]> bearings = new ArrayList<>();
    List<Point> coordinates = new ArrayList<>();
    String[] annotations;
    double[] radiuses;
    Point destination;
    Point origin;

    /**
     * The username for the account that the directions engine runs on. In most cases, this should
     * always remain the default value of {@link DirectionsCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the directions
     *             request
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * This selects which mode of transportation the user will be using while navigating from the
     * origin to the final destination. The options include driving, driving considering traffic,
     * walking, and cycling. Using each of these profiles will result in different routing biases.
     *
     * @param profile required to be one of the String values found in the {@link ProfileCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder profile(@NonNull @ProfileCriteria String profile);

    /**
     * This sets the starting point on the map where the route will begin. It is one of the
     * required parameters which must be set for a successful directions response.
     *
     * @param origin a GeoJSON {@link Point} object representing the starting location for the route
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder origin(@NonNull Point origin) {
      this.origin = origin;
      return this;
    }

    /**
     * This sets the ending point on the map where the route will end. It is one of the required
     * parameters which must be set for a successful directions response.
     *
     * @param destination a GeoJSON {@link Point} object representing the starting location for the
     *                    route
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder destination(@NonNull Point destination) {
      this.destination = destination;
      return this;
    }

    /**
     * This can be used to set up to 23 additional in-between points which will act as pit-stops
     * along the users route. Note that if you are using the
     * {@link DirectionsCriteria#PROFILE_DRIVING_TRAFFIC} that the max number of waypoints allowed
     * in the request is currently limited to 1.
     *
     * @param waypoint a {@link Point} which represents the pit-stop or waypoint where you'd like
     *                 one of the {@link com.mapbox.services.api.directions.v5.models.RouteLeg} to
     *                 navigate the user to
     * @return this builder for chaining options together
     * @since 3.0.0
     */
    public Builder addWaypoint(@NonNull Point waypoint) {
      coordinates.add(waypoint);
      return this;
    }

    /**
     * Optionally set whether to try to return alternative routes. An alternative is classified as a
     * route that is significantly different then the fastest route, but also still reasonably fast.
     * Not in all circumstances such a route exists. At the moment at most one alternative can be
     * returned.
     *
     * @param alternatives true if you'd like to receive an alternative route, otherwise false or
     *                     null to use the APIs default value
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder alternatives(@Nullable Boolean alternatives);

    /**
     * alter the default geometry being returned for the directions route. A null value will reset
     * this field to the APIs default value vs this SDKs default value of
     * {@link DirectionsCriteria#GEOMETRY_POLYLINE6}.
     * <p>
     * Note that while the API supports GeoJSON as an option for geometry, this SDK intentionally
     * removes this as an option since an encoded string for the geometry significantly reduces
     * bandwidth on mobile devices and speeds up response time.
     * </p>
     *
     * @param geometries null if you'd like the default geometry, else one of the options found in
     *                   {@link GeometriesCriteria}.
     * @return this builder for chaining options together
     * @since 2.0.0
     */
    public abstract Builder geometries(@Nullable @GeometriesCriteria String geometries);

    /**
     * Type of returned overview geometry. Can be {@link DirectionsCriteria#OVERVIEW_FULL} (the most
     * detailed geometry available), {@link DirectionsCriteria#OVERVIEW_SIMPLIFIED} (a simplified
     * version of the full geometry), or {@link DirectionsCriteria#OVERVIEW_FALSE} (no overview
     * geometry). The default is simplified. Passing in null will use the APIs default setting for
     * the overview field.
     *
     * @param Overview null or one of the options found in {@link OverviewCriteria}
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder overview(@Nullable @OverviewCriteria String Overview);

    /**
     * Setting this will determine whether to return steps and turn-by-turn instructions. Can be
     * set to either true or false to enable or disable respectively. null can also optionally be
     * passed in to set the default behavior to match what the API does by default.
     *
     * @param steps true if you'd like step information
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder steps(@Nullable Boolean steps);

    /**
     * Sets allowed direction of travel when departing intermediate waypoints. If true the route
     * will continue in the same direction of travel. If false the route may continue in the
     * opposite direction of travel. API defaults to true for
     * {@link DirectionsCriteria#PROFILE_DRIVING} and false for
     * {@link DirectionsCriteria#PROFILE_WALKING} and {@link DirectionsCriteria#PROFILE_CYCLING}.
     *
     * @param continueStraight boolean true if you want to always continue straight, else false.
     *                         Null can also be passed in here to use the APIs default option
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder continueStraight(@Nullable Boolean continueStraight);

    /**
     * Set the instruction language for the directions request, the default is english. Only a
     * select number of languages are currently supported, reference the table provided in the see
     * link below.
     *
     * @param language a Locale value representing the language you'd like the instructions to be
     *                 written in when returned
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#instructions-languages">Supported
     * Languages</a>
     * @since 2.2.0
     */
    public Builder language(@Nullable Locale language) {
      if (language != null) {
        language(language.toString());
      }
      return this;
    }

    /**
     * Whether or not to return additional metadata along the route. Possible values are:
     * {@link DirectionsCriteria#ANNOTATION_DISTANCE},
     * {@link DirectionsCriteria#ANNOTATION_DURATION},
     * {@link DirectionsCriteria#ANNOTATION_DURATION} and
     * {@link DirectionsCriteria#ANNOTATION_CONGESTION}. Several annotation can be used by
     * separating them with {@code ,}.
     *
     * @param annotations string referencing one of the annotation direction criteria's. The strings
     *                    restricted to one or multiple values inside the {@link AnnotationCriteria}
     *                    or null which will result in no annotations being used
     * @return this builder for chaining options together
     * @see <a href="https://www.mapbox.com/api-documentation/#routeleg-object">RouteLeg object
     * documentation</a>
     * @since 2.1.0
     */
    public Builder annotations(@Nullable @AnnotationCriteria String... annotations) {
      this.annotations = annotations;
      return this;
    }

    /**
     * Optionally, Use to filter the road segment the waypoint will be placed on by direction and
     * dictates the angle of approach. This option should always be used in conjunction with the
     * {@link #radiuses(double...)} parameter.
     * <p>
     * The parameter takes two values per waypoint: the first is an angle clockwise from true north
     * between 0 and 360. The second is the range of degrees the angle can deviate by. We recommend
     * a value of 45 degrees or 90 degrees for the range, as bearing measurements tend to be
     * inaccurate. This is useful for making sure we reroute vehicles on new routes that continue
     * traveling in their current direction. A request that does this would provide bearing and
     * radius values for the first waypoint and leave the remaining values empty. If provided, the
     * list of bearings must be the same length as the list of waypoints, but you can skip a
     * coordinate and show its position by passing in null value for both the angle and tolerance
     * values.
     * </p><p>
     * Each bearing value gets associated with the same order which coordinates are arranged in this
     * builder. For example, the first bearing added in this builder will be associated with the
     * origin {@code Point}, the nth bearing being associated with the nth waypoint added (if added)
     * and the last bearing being added will be associated with the destination.
     * </p>
     *
     * @param angle     double value used for setting the corresponding coordinate's angle of travel
     *                  when determining the route
     * @param tolerance the deviation the bearing angle can vary while determining the route,
     *                  recommended to be either 45 or 90 degree tolerance
     * @return this builder for chaining options together
     * @since 2.0.0
     */
    public Builder addBearing(@Nullable @FloatRange(from = 0, to = 360) Double angle,
                              @Nullable @FloatRange(from = 0, to = 360) Double tolerance) {
      bearings.add(new Double[] {angle, tolerance});
      return this;
    }

    /**
     * Optionally, set the maximum distance in meters that each coordinate is allowed to move when
     * snapped to a nearby road segment. There must be as many radiuses as there are coordinates in
     * the request. Values can be any number greater than 0 or they can be unlimited simply by
     * passing {@link Double#POSITIVE_INFINITY}.
     * <p>
     * If no routable road is found within the radius, a {@code NoSegment} error is returned.
     * </p>
     *
     * @param radiuses double array containing the radiuses defined in unit meters.
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public Builder radiuses(@FloatRange(from = 0) double... radiuses) {
      this.radiuses = radiuses;
      return this;
    }

    /**
     * Base package name or other simple string identifier. Used inside the calls user agent header.
     *
     * @param clientAppName base package name or other simple string identifier
     * @return this builder for chaining options together
     * @since 1.0.0
     */
    public abstract Builder clientAppName(@NonNull String clientAppName);


    public abstract Builder accessToken(@NonNull String accessToken);

    public abstract Builder baseUrl(String baseUrl);

    // Private methods for matching MapboxDirections methods.

    abstract Builder language(@Nullable String language);

    abstract Builder radiuses(@Nullable String radiuses);

    abstract Builder bearings(@Nullable String bearings);

    abstract Builder coordinates(@NonNull String coordinates);

    abstract Builder annotations(@Nullable String annotations);

    abstract MapboxDirections autoBuild(); // not public

    public MapboxDirections build() {
      if (origin != null) {
        coordinates.add(0, origin);
      }
      if (destination != null) {
        coordinates.add(destination);
      }

      coordinates(formatCoordinates());
      bearings(formatBearing());
      annotations(formatAnnotations());
      radiuses(formatRadiuses());
      return autoBuild();
    }

    private String formatAnnotations() {
      if (annotations == null || annotations.length == 0) {
        return null;
      }
      return TextUtils.join(",", annotations);
    }

    private String formatRadiuses() {
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

    private String formatCoordinates() {
      List<String> coordinatesFormatted = new ArrayList<>();
      for (Point point : coordinates) {
        coordinatesFormatted.add(String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(point.getCoordinates().getLongitude()),
          TextUtils.formatCoordinate(point.getCoordinates().getLatitude())));
      }

      return TextUtils.join(";", coordinatesFormatted.toArray());
    }

    private String formatBearing() {
      if (bearings.isEmpty()) {
        return null;
      }

      String[] bearingFormatted = new String[bearings.size()];
      for (int i = 0; i < bearings.size(); i++) {
        if (bearings.get(i).length == 0) {
          bearingFormatted[i] = "";
        } else {
          bearingFormatted[i] = String.format(Locale.US, "%s,%s",
            TextUtils.formatCoordinate(bearings.get(i)[0]),
            TextUtils.formatCoordinate(bearings.get(i)[1]));
        }
      }
      return TextUtils.join(";", bearingFormatted);
    }
  }
}
