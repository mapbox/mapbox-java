package com.mapbox.api.isochrone;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.core.utils.MapboxUtils;
import com.mapbox.core.utils.TextUtils;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.GeometryAdapterFactory;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;

/**
 * An isochrone, from the Greek root words iso (equal) and chrone (time), is a line that connects
 * points of equal travel time around a given location. The Mapbox Isochrone API computes areas
 * that are reachable within a specified amount of time from a location, and returns the reachable
 * regions as contours of polygons or lines that you can display on a map.
 * <p>
 * Given a location and a routing profile, retrieve up to four isochrone contours. The contours
 * are calculated using rasters and are returned as either polygon or line features, depending
 * on your input setting for the polygons parameter.
 * <p>
 * The Isochrone API is limited to 300 requests per minute. The Isochrone API supports 1 coordinate
 * per request. The Isochrone API can support a maximum of 4 isochrone contours per request.
 * The maximum time that can be specified for an isochrone contour is 60 minutes. Results must be
 * displayed on a Mapbox map using one of the Mapbox libraries or SDKs. If you require a higher
 * rate limit, contact us.
 * <p>
 *
 * @see <a href="https://docs.mapbox.com/api/navigation/#isochrone">API documentation</a>
 * @since 4.6.0
 */
@AutoValue
public abstract class MapboxIsochrone extends MapboxService<FeatureCollection, IsochroneService> {

  protected MapboxIsochrone() {
    super(IsochroneService.class);
  }

  @Override
  protected GsonBuilder getGsonBuilder() {
    MapboxIsochrone.Builder b = MapboxIsochrone.builder();

    return new GsonBuilder()
      .registerTypeAdapterFactory(GeoJsonAdapterFactory.create())
      .registerTypeAdapterFactory(GeometryAdapterFactory.create());
  }

  @Override
  protected Call<FeatureCollection> initializeCall() {
    return getService().getCall(
      user(),
      profile(),
      coordinates(),
      contoursMinutes(),
      accessToken(),
      contoursColors(),
      polygons(),
      denoise(),
      generalize(),
      contoursMeters(),
      exclusions(),
      departAt()
    );
  }

  /**
   * Build a new {@link MapboxIsochrone} object with the initial value set for
   * {@link #baseUrl()}.
   *
   * @return a {@link MapboxIsochrone.Builder} object for creating this object
   * @since 4.6.0
   */
  public static Builder builder() {
    return new AutoValue_MapboxIsochrone.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .contoursMinutes("")
      .contoursMeters("")
      .user(IsochroneCriteria.PROFILE_DEFAULT_USER);
  }

  @NonNull
  @Override
  protected abstract String baseUrl();

  @NonNull
  abstract String accessToken();

  @NonNull
  abstract String user();

  @NonNull
  abstract String profile();

  @NonNull
  abstract String coordinates();

  @NonNull
  abstract String contoursMinutes();

  @Nullable
  abstract String contoursColors();

  @Nullable
  abstract Boolean polygons();

  @Nullable
  abstract Float denoise();

  @Nullable
  abstract Float generalize();

  @Nullable
  abstract String contoursMeters();

  @Nullable
  abstract String exclusions();

  @Nullable
  abstract String departAt();

  /**
   * This builder is used to create a new request to the Mapbox Isochrone API. At a bare minimum,
   * your request must include an access token, a directions routing profile (driving, walking,
   * or cycling),and a comma separated list of time for the contours. All other fields can be
   * left alone in order to use the default behaviour of the API.
   * <p>
   * Note to contributors: All optional booleans in this builder use the object {@code Boolean}
   * rather than the primitive to allow for unset (null) values.
   * </p>
   *
   * @since 4.6.0
   */
  @AutoValue.Builder
  public abstract static class Builder {

    private Integer[] contoursMinutes;
    private String[] contoursColors;
    private Integer[] contoursMeters;
    private Set<String> exclusions = new HashSet<>();

    /**
     * Optionally change the APIs base URL to something other than the default Mapbox one.
     *
     * @param baseUrl base url used as end point
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder baseUrl(@NonNull String baseUrl);

    /**
     * A valid Mapbox access token.
     *
     * @param accessToken the Mapbox access token to use for the Isochrone API call
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder accessToken(@NonNull String accessToken);

    /**
     * The username for the account that the Isochrone engine runs on. In most cases, this should
     * always remain the default value of {@link IsochroneCriteria#PROFILE_DEFAULT_USER}.
     *
     * @param user a non-null string which will replace the default user used in the Isochrone
     *             request
     * @return this builder for chaining options together
     * @since 4.7.0
     */
    public abstract Builder user(@NonNull String user);

    /**
     * A Mapbox Directions routing profile ID. Options are
     * {@link IsochroneCriteria#PROFILE_DRIVING} for travel times by car,
     * {@link IsochroneCriteria#PROFILE_DRIVING_TRAFFIC} for fastest travel by car using
     * current and historic traffic,
     * {@link IsochroneCriteria#PROFILE_WALKING} for pedestrian and hiking travel times,
     * and {@link IsochroneCriteria#PROFILE_CYCLING} for travel times by bicycle.
     *
     * @param profile a Mapbox Directions profile
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder profile(@Nullable @IsochroneCriteria.IsochroneProfile String profile);

    /**
     * A {@link Point} object which represents a {longitude,latitude} coordinate
     * pair around which to center the isochrone lines.
     *
     * @param queryPoint center query point for the isochrone calculation
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public Builder coordinates(@NonNull Point queryPoint) {
      coordinates(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(queryPoint.longitude()),
        TextUtils.formatCoordinate(queryPoint.latitude())));
      return this;
    }

    /**
     * A string which represents a {longitude,latitude} coordinate pair
     * around which to center the isochrone lines. The String should be
     * "longitude,latitude".
     *
     * @param queryPoint center query point for the isochrone calculation
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder coordinates(@NonNull String queryPoint);

    /**
     * An integer list of minute values to use for each isochrone contour.
     * You must pass in at least one minute amount and you can specify
     * up to four contours. Times must be in increasing order. For example,
     * "5,20,40,50" and not "20,10,40,55". The maximum time that can be
     * specified is 60 minutes.
     *
     * @param listOfMinuteValues an integer list with at least one number
     *                           for the minutes which represent each contour
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public Builder addContoursMinutes(@NonNull @IntRange(from = 0, to = 60)
                                        Integer... listOfMinuteValues) {
      this.contoursMinutes = listOfMinuteValues;
      return this;
    }

    /**
     * An integer list of meter values to use for each isochrone contour.
     * The distances, in meters, to use for each isochrone contour. You
     * must be in increasing order. The default maximum distance that can be specified
     * is 100000 meters (100km), if you need a bigger range contact support.
     *
     * @param listOfMeterValues an integer list with at least one number
     *                           for the meters which represent each contour
     * @return this builder for chaining options together
     * @since 7.3.0
     */
    public Builder addContoursMeters(Integer... listOfMeterValues) {
      this.contoursMeters = listOfMeterValues;
      return this;
    }

    /**
     * A single String which is a comma-separated list of time(s) in minutes
     * to use for each isochrone contour. You must pass in at least one minute
     * amount and you can specify up to four contours. Times must be in increasing order.
     * For example "5,20,40,50" is valid and "20,10,40,55" is invalid.
     * The maximum time that can be specified is 60 minutes.
     *
     * @param stringListOfMinuteValues a String of at least one number for the
     *                                 minutes which represent each contour
     * @return this builder for chaining options together
     */
    // Required for matching with MapboxIsochrone addContoursMinutes() method.
    @SuppressWarnings("WeakerAccess")
    abstract Builder contoursMinutes(@NonNull String stringListOfMinuteValues);

    /**
     * A single String which is a comma-separated list of values(s) in meters
     * to use for each isochrone contour. The distances, in meters, to use for each
     * isochrone contour. You can specify up to four contours. Distances must be in
     * increasing order. The default maximum distance that can be specified
     * is 100000 meters (100km), if you need a bigger range contact support.
     *
     * @param stringListOfMeterValues a String of at least one number for the
     *                                 meters which represent each contour
     * @return this builder for chaining options together
     */
    abstract Builder contoursMeters(@NonNull String stringListOfMeterValues);

    /**
     * A list of separate String which has a list of comma-separated
     * HEX color values to use for each isochrone contour.
     * <p>
     * For example, .contoursColors("6706ce","04e813","4286f4")
     * <p>
     * The colors should be specified as hex values without a
     * leading # (for example, ff0000 for red). If this parameter is
     * used, there must be the same number of colors as there are entries in
     * contours_minutes. If no colors are specified, the Isochrone API will
     * assign a default rainbow color scheme to the output.
     *
     * @param contoursColors the list of at least one color value to use for
     *                       the polygon fill areas in the API response
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public Builder addContoursColors(@Nullable String... contoursColors) {
      this.contoursColors = contoursColors;
      return this;
    }

    // Required for matching with MapboxIsochrone addContoursColors() method.
    abstract Builder contoursColors(@Nullable String countoursColorList);

    /**
     * Specify whether to return the contours as GeoJSON
     * {@link com.mapbox.geojson.Polygon} (true) or {@link com.mapbox.geojson.LineString} (false).
     * If no boolean is set, false is the default, which results in
     * {@link com.mapbox.geojson.LineString} being delivered.
     * <p>
     * When polygons=true, any contour that forms a ring is returned as
     * a {@link com.mapbox.geojson.Polygon}.
     *
     * @param polygons a boolean whether you want the API response to include
     *                 {@link com.mapbox.geojson.Polygon} geometries to represent the
     *                 various contours.
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder polygons(@Nullable Boolean polygons);

    /**
     * A floating point value from 0.0 to 1.0 that can be used to remove smaller contours.
     * The default is 1.0. A value of 1.0 will only return the largest contour for a given time
     * value. A value of 0.5 drops any contours that are less than half the area of the
     * largest contour in the set of contours for that same time value.
     *
     * @param denoise an optional number to determine the shape of small contours
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder denoise(@Nullable @FloatRange(from = 0.0, to = 1.0) Float denoise);

    /**
     * A positive floating point value in meters used as the tolerance for Douglas-Peucker
     * generalization. There is no upper bound. If no value is specified in the request,
     * the Isochrone API will choose the most optimized generalization to use for the request.
     * Note that the generalization of contours can lead to self-intersections, as well as
     * intersections of adjacent contours.
     *
     * @param generalize an optional number to determine how smooth or jagged the contour
     *                   lines/polygons contours are
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public abstract Builder generalize(@Nullable @FloatRange(from = 0.0) Float generalize);

    abstract Builder exclusions(@Nullable String exclusions);

    /**
     * Exclude highways or motorways. Available in mapbox/driving and
     * mapbox/driving-traffic profiles.
     * @param exclude indicates whether motorways should be excluded
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public Builder excludeMotorways(Boolean exclude) {
      if (exclude != null && exclude) {
        exclusions.add("motorway");
      } else {
        exclusions.remove("motorway");
      }
      return this;
    }

    /**
     * Exclude tolls. Available in mapbox/driving and
     * mapbox/driving-traffic profiles.
     * @param exclude indicates whether tolls should be excluded
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public Builder excludeTolls(Boolean exclude) {
      if (exclude != null && exclude) {
        exclusions.add("toll");
      } else {
        exclusions.remove("toll");
      }
      return this;
    }

    /**
     * Exclude ferries. Available in mapbox/driving and
     * mapbox/driving-traffic profiles.
     * @param exclude indicates whether ferries should be excluded
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public Builder excludeFerries(Boolean exclude) {
      if (exclude != null && exclude) {
        exclusions.add("ferry");
      } else {
        exclusions.remove("ferry");
      }
      return this;
    }

    /**
     * Exclude unpaved roads. Available in mapbox/driving and
     * mapbox/driving-traffic profiles.
     * @param exclude indicates whether unpaved roads should be excluded
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public Builder excludeUnpavedRoads(Boolean exclude) {
      if (exclude != null && exclude) {
        exclusions.add("unpaved");
      } else {
        exclusions.remove("unpaved");
      }
      return this;
    }

    /**
     * Exclude cash only toll roads. Available in mapbox/driving and
     * mapbox/driving-traffic profiles.
     * @param exclude indicates whether cash only toll roads should be excluded
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public Builder excludeCashOnlyTollRoads(Boolean exclude) {
      if (exclude != null && exclude) {
        exclusions.add("cash_only_tolls");
      } else {
        exclusions.remove("cash_only_tolls");
      }
      return this;
    }

    /**
     * The departure time from the given coordinates.
     * <a href="https://docs.mapbox.com/api/navigation/isochrone/">One of three formats.</a>.
     * If not provided then 'depart at' is considered to be the present time in the local
     * timezone of the coordinates. The isochrone contours will reflect traffic
     * conditions at the time provided.
     *
     * @param depart the departure date/time
     * @return this builder for chaining options together
     *
     * @since 7.3.0
     */
    public abstract Builder departAt(@NonNull String depart);

    /**
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    abstract MapboxIsochrone autoBuild();

    /**
     * Build a new {@link MapboxIsochrone} object.
     *
     * @return this builder for chaining options together
     * @since 4.6.0
     */
    public MapboxIsochrone build() {

      exclusions(TextUtils.join(",", exclusions.toArray()));

      if (contoursMinutes != null) {
        if (contoursMinutes.length < 1) {
          throw new ServicesException("A query with at least one specified "
            + "minute amount is required.");
        }

        if (contoursMinutes.length >= 2) {
          for (int x = 0; x < contoursMinutes.length - 1; x++) {
            if (contoursMinutes[x] > contoursMinutes[x + 1]) {
              throw new ServicesException("The minutes must be listed"
                + " in order from the lowest number to the highest number.");
            }
          }
        }
        contoursMinutes(TextUtils.join(",", contoursMinutes));
      }

      if (contoursMeters != null) {
        if (contoursMeters.length < 1) {
          throw new ServicesException("A query with at least one specified "
                  + "meter value is required.");
        }

        if (contoursMeters.length >= 2) {
          for (int x = 0; x < contoursMeters.length - 1; x++) {
            if (contoursMeters[x] > contoursMeters[x + 1]) {
              throw new ServicesException("The meters must be listed"
                      + " in order from the lowest number to the highest number.");
            }
          }
        }
        contoursMeters(TextUtils.join(",", contoursMeters));
      }

      if (contoursColors != null) {
        contoursColors(TextUtils.join(",", contoursColors));
      }

      if (contoursColors != null
        && contoursMinutes != null
        && contoursColors.length != contoursMinutes.length) {
        throw new ServicesException("Number of color elements "
          + "must match number of minute elements provided.");
      }

      if (contoursColors != null
        && contoursMeters != null
        && contoursColors.length != contoursMeters.length) {
        throw new ServicesException("Number of color elements "
                + "must match number of meter elements provided.");
      }

      if (contoursMinutes != null && contoursMeters != null) {
        throw new ServicesException("Cannot specify both contoursMinutes and contoursMeters.");
      }

      MapboxIsochrone isochrone = autoBuild();

      if (!MapboxUtils.isAccessTokenValid(isochrone.accessToken())) {
        throw new ServicesException("Using the Mapbox Isochrone API requires setting "
          + "a valid access token.");
      }

      if (TextUtils.isEmpty(isochrone.coordinates())) {
        throw new ServicesException("A query with longitude and latitude values is "
          + "required.");
      }

      if (TextUtils.isEmpty(isochrone.profile())) {
        throw new ServicesException("A query with a set Directions profile (cycling,"
          + " walking, or driving) is required.");
      }

      if (TextUtils.isEmpty(isochrone.contoursMinutes())
        && TextUtils.isEmpty(isochrone.contoursMeters())) {
        throw new ServicesException("A query with at least one specified minute amount"
        + " or meter value is required.");
      }

      if (isochrone.contoursColors() != null) {
        if (isochrone.contoursColors().contains("#")) {
          throw new ServicesException("Make sure that none of the contour color HEX"
            + " values have a # in front of it. Provide a list of the HEX values "
            + "without any # symbols.");
        }
      }
      return isochrone;
    }
  }
}
