package com.mapbox.api.tilequery;

import com.mapbox.geojson.FeatureCollection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the tilequery service.
 *
 * @since 3.5.0
 */
public interface TilequeryService {

  /**
   * Constructs the HTTP request for the specified parameters.
   *
   * @param tilesetIds tile set ID(s)
   * @param query query point
   * @param accessToken Mapbox access token
   * @param radius distance in meters to query for features
   * @param limit the number of features
   * @param dedupe whether results will be deduplicated
   * @param geometry polygon, linestring, or point
   * @param layers list of layers to query
   * @return A retrofit Call object
   * @since 3.5.0
   */
  @GET("/v4/{tilesetIds}/tilequery/{query}.json")
  Call<FeatureCollection> getCall(
    @Path("tilesetIds") String tilesetIds,
    @Path("query") String query,
    @Query("access_token") String accessToken,
    @Query("radius") Integer radius,
    @Query("limit") Integer limit,
    @Query("dedupe") Boolean dedupe,
    @Query("geometry") String geometry,
    @Query("layers") String layers);

  /**
   * Constructs the HTTP request for the specified parameters.
   *
   * @param tilesetIds tile set ID(s)
   * @param query query point
   * @param accessToken Mapbox access token
   * @param radius distance in meters to query for features
   * @param limit the number of features
   * @param dedupe whether results will be deduplicated
   * @param geometry polygon, linestring, or point
   * @param layers list of layers to query
   * @return A retrofit Call object
   * @since 3.5.0
   */
  @GET("/v4/{tilesetIds}/tilequery/{query}.json")
  Call<List<FeatureCollection>> getBatchCall(
    @Path("tilesetIds") String tilesetIds,
    @Path("query") String query,
    @Query("access_token") String accessToken,
    @Query("radius") Integer radius,
    @Query("limit") Integer limit,
    @Query("dedupe") Boolean dedupe,
    @Query("geometry") String geometry,
    @Query("layers") String layers);
}
