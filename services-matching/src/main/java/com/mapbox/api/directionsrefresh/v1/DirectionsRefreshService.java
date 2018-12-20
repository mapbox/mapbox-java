package com.mapbox.api.directionsrefresh.v1;

import com.mapbox.api.matching.v5.models.MapMatchingResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DirectionsRefreshService {

  @POST("directions-refresh/v1/{user}/{profile}")
  Call<MapMatchingResponse> getCall(
    @Header("User-Agent") String userAgent,
    @Path("user") String user,
    @Path("profile") String profile,
    @Query("coordinates") String coordinates,
    @Query("access_token") String accessToken,
    @Query("geometries") String geometries,
    @Query("radiuses") String radiuses,
    @Query("steps") Boolean steps,
    @Query("overview") String overview,
    @Query("timestamps") String timestamps,
    @Query("annotations") String annotations,
    @Query("language") String language,
    @Query("tidy") Boolean tidy,
    @Query("roundabout_exits") Boolean roundaboutExits,
    @Query("banner_instructions") Boolean bannerInstructions,
    @Query("voice_instructions") Boolean voiceInstructions,
    @Query("voice_units") String voiceUnits,
    @Query("waypoints") String waypoints,
    @Query("waypoint_names") String waypointNames,
    @Query("approaches") String approaches);
}
