package com.mapbox.api.directionsrefresh.v1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshAdapterFactory;
import com.mapbox.api.directionsrefresh.v1.models.DirectionsRefreshResponse;
import com.mapbox.core.MapboxService;
import com.mapbox.core.constants.Constants;
import com.mapbox.core.utils.ApiCallHelper;

import retrofit2.Call;

@AutoValue
public abstract class MapboxDirectionsRefresh extends MapboxService<DirectionsRefreshResponse,
  DirectionsRefreshService> {

  protected MapboxDirectionsRefresh() {
    super(DirectionsRefreshService.class);
  }

  @Override
  protected Call<DirectionsRefreshResponse> initializeCall() {
    return getService().getCall(
      ApiCallHelper.getHeaderUserAgent(clientAppName()),
      requestId(),
      routeIndex(),
      legIndex(),
      accessToken()
    );
  }

  abstract String requestId();

  abstract String routeIndex();

  abstract String legIndex();

  abstract String accessToken();

  @Nullable
  abstract String clientAppName();

  @Override
  protected GsonBuilder getGsonBuilder() {
    return super.getGsonBuilder()
      .registerTypeAdapterFactory(DirectionsRefreshAdapterFactory.create())
      .registerTypeAdapterFactory(DirectionsAdapterFactory.create());
  }

  @NonNull
  @Override
  protected abstract String baseUrl();

  public static Builder builder() {
    return new AutoValue_MapboxDirectionsRefresh.Builder()
      .baseUrl(Constants.BASE_API_URL)
      .routeIndex(0)
      .legIndex(0);
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    private String[] annotations;

    public abstract Builder requestId(String requestId);

    // todo nullable?
    public Builder annotations(@Nullable @DirectionsCriteria.AnnotationCriteria String... annotations) {
      this.annotations = annotations;
      return this;
    }

    public abstract Builder routeIndex(@NonNull String routeIndex);

    public abstract Builder legIndex(@NonNull String legIndex);

    public abstract Builder accessToken(@NonNull String accessToken);

    public abstract Builder clientAppName(@NonNull String clientAppName);

    public abstract Builder baseUrl(String baseUrl);

    public abstract MapboxDirectionsRefresh build();
  }
}
