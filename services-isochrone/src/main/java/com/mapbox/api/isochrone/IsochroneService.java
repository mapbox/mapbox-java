package com.mapbox.api.isochrone;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the Isochrone API service.
 *
 * @since 4.6.0
 */
public interface IsochroneService {

  /**
   * Constructs the HTTP request for the specified parameters.
   *
   * @param profile         A Mapbox Directions routing profile ID. Options are
   *                        {@link IsochroneCriteria#PROFILE_DRIVING} for travel
   *                        times by car, {@link IsochroneCriteria#PROFILE_WALKING}
   *                        for pedestrian and hiking travel times,and
   *                        {@link IsochroneCriteria#PROFILE_CYCLING} for travel times
   *                        by bicycle.
   * @param coordinates     A {@link Point} object which represents a
   *                        {longitude,latitude} coordinate pair around which to
   *                        center the isochrone lines.
   * @param contoursMinutes A single String which has a comma-separated time in minutes to use for
   *                        each isochrone contour.
   * @param accessToken     A valid Mapbox access token.
   * @param contoursColors  The colors to use for each isochrone contour, specified as hex values
   *                        without a leading # (for example, ff0000 for red). If this parameter is
   *                        used, there must be the same number of colors as there are entries in
   *                        contours_minutes. If no colors are specified, the Isochrone API will
   *                        assign a default rainbow color scheme to the output.
   * @param polygons        Specify whether to return the contours as GeoJSON polygons (true) or
   *                        linestrings (false, default). When polygons=true, any contour that
   *                        forms a ring is returned as a polygon.
   * @param denoise         A floating point value from 0.0 to 1.0 that can be used to remove
   *                        smaller contours. The default is 1.0. A value of 1.0 will only
   *                        return the largest contour for a given time value. A value of 0.5
   *                        drops any contours that are less than half the area of the largest
   *                        contour in the set of contours for that same time value.
   * @param generalize      A positive floating point value in meters used as the tolerance for
   *                        Douglas-Peucker generalization. There is no upper bound. If no value is
   *                        specified in the request, the Isochrone API will choose the most
   *                        optimized generalization to use for the request. Note that the
   *                        generalization of contours can lead to self-intersections, as well
   *                        as intersections of adjacent contours.
   * @return a {@link FeatureCollection} in a Call wrapper
   * @since 4.6.0
   * @deprecated this method was missing the user parameter. Use the getCall() method below instead.
   */
  @Deprecated
  @GET("/isochrone/v1/{profile}/{coordinates}")
  Call<FeatureCollection> getCall(
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("contours_minutes") String contoursMinutes,
    @Query("access_token") String accessToken,
    @Query("contours_colors") String contoursColors,
    @Query("polygons") Boolean polygons,
    @Query("denoise") Float denoise,
    @Query("generalize") Float generalize);


  /**
   * Constructs the HTTP request for the specified parameters.
   *
   * @param user            The username for the account that the Isochrone engine runs on.
   *                        In most cases, this should always remain the default value of
   *                        {@link IsochroneCriteria#PROFILE_DEFAULT_USER}.
   * @param profile         A Mapbox Directions routing profile ID. Options are
   *                        {@link IsochroneCriteria#PROFILE_DRIVING} for travel times
   *                        by car, {@link IsochroneCriteria#PROFILE_WALKING} for pedestrian
   *                        and hiking travel times,and {@link IsochroneCriteria#PROFILE_CYCLING}
   *                        for travel times by bicycle.
   * @param coordinates     A {@link Point} object which represents a
   *                        {longitude,latitude} coordinate pair around which to center
   *                        the isochrone lines.
   * @param contoursMinutes A single String which has a comma-separated time in minutes to use for
   *                        each isochrone contour.
   * @param accessToken     A valid Mapbox access token.
   * @param contoursColors  The colors to use for each isochrone contour, specified as hex values
   *                        without a leading # (for example, ff0000 for red). If this parameter is
   *                        used, there must be the same number of colors as there are entries in
   *                        contours_minutes. If no colors are specified, the Isochrone API will
   *                        assign a default rainbow color scheme to the output.
   * @param polygons        Specify whether to return the contours as GeoJSON polygons (true) or
   *                        linestrings (false, default). When polygons=true, any contour that
   *                        forms a ring is returned as a polygon.
   * @param denoise         A floating point value from 0.0 to 1.0 that can be used to remove
   *                        smaller contours. The default is 1.0. A value of 1.0 will only
   *                        return the largest contour for a given time value. A value of 0.5
   *                        drops any contours that are less than half the area of the largest
   *                        contour in the set of contours for that same time value.
   * @param generalize      A positive floating point value in meters used as the tolerance for
   *                        Douglas-Peucker generalization. There is no upper bound. If no value is
   *                        specified in the request, the Isochrone API will choose the most
   *                        optimized generalization to use for the request. Note that the
   *                        generalization of contours can lead to self-intersections, as well
   *                        as intersections of adjacent contours.
   * @return a {@link FeatureCollection} in a Call wrapper
   * @since 4.7.0
   */
  @GET("/isochrone/v1/{user}/{profile}/{coordinates}")
  Call<FeatureCollection> getCall(
    @Path("user") String user,
    @Path("profile") String profile,
    @Path("coordinates") String coordinates,
    @Query("contours_minutes") String contoursMinutes,
    @Query("access_token") String accessToken,
    @Query("contours_colors") String contoursColors,
    @Query("polygons") Boolean polygons,
    @Query("denoise") Float denoise,
    @Query("generalize") Float generalize);
}
