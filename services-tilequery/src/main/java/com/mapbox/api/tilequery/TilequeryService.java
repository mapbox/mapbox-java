package com.mapbox.api.tilequery;

import com.mapbox.geojson.FeatureCollection;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TilequeryService {

  @GET("/v4/{mapIds}/tilequery/{query}.json")
  Call<FeatureCollection> getCall(
    @Path("mapIds") String mapIds,
    @Path("query") String query,
    @Query("access_token") String accessToken,
    @Query("radius") Integer radius,
    @Query("limit") Integer limit,
    @Query("dedupe") Boolean dedupe,
    @Query("geometry") String geometry,
    @Query("textType") String layers);

  @GET("/v4/{mapIds}/tilequery/{query}.json")
  Call<List<FeatureCollection>> getBatchCall(
    @Path("mapIds") String mapIds,
    @Path("query") String query,
    @Query("access_token") String accessToken,
    @Query("radius") Integer radius,
    @Query("limit") Integer limit,
    @Query("dedupe") Boolean dedupe,
    @Query("geometry") String geometry,
    @Query("textType") String layers);
}
