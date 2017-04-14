package com.mapbox.services.android.testapp.optimizedtrip;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.optimizedtrips.v1.MapboxOptimizedTrips;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.api.utils.turf.TurfConstants;
import com.mapbox.services.api.utils.turf.TurfMeasurement;
import com.mapbox.services.api.utils.turf.TurfMisc;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class OptimizedTripActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String LINE_SOURCE = "line-source";
  private static final String LINE_LAYER = "line-layer";

  private MapView mapView;
  private MapboxMap mapboxMap;
  private List<MarkerOptions> tripStops;
  private ValueAnimator valueAnimator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_optimized_trip);

    tripStops = new ArrayList<>();
    tripStops.add(new MarkerOptions().position(new LatLng(40.74302, -73.99322)));
    tripStops.add(new MarkerOptions().position(new LatLng(40.74451, -73.97920)));
    tripStops.add(new MarkerOptions().position(new LatLng(40.75979, -73.99179)));
    tripStops.add(new MarkerOptions().position(new LatLng(40.76369, -73.97144)));
    tripStops.add(new MarkerOptions().position(new LatLng(40.75906, -73.98812)));

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);

  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.addMarkers(tripStops);

    MapboxOptimizedTrips.Builder builder = new MapboxOptimizedTrips.Builder()
      .setAccessToken(Mapbox.getAccessToken())
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      // .setRoundTrip(false)
      .setSource(DirectionsCriteria.SOURCE_FIRST)
      // .setDestination(DirectionsCriteria.SOURCE_LAST)
      .setOverview(DirectionsCriteria.OVERVIEW_FULL);

    List<Position> coords = new ArrayList<>();
    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
    for (MarkerOptions markerOptions : tripStops) {
      coords.add(Position.fromCoordinates(
        markerOptions.getPosition().getLongitude(),
        markerOptions.getPosition().getLatitude())
      );

      boundsBuilder.include(markerOptions.getPosition());
    }

    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 150));

    MapboxOptimizedTrips client = builder.setCoordinates(coords).build();
    client.enqueueCall(new Callback<OptimizedTripsResponse>() {
      @Override
      public void onResponse(Call<OptimizedTripsResponse> call, Response<OptimizedTripsResponse> response) {
        Timber.v("Call Url: %s", call.request().url().toString());

        drawLine(response.body().getTrips().get(0).getGeometry());
      }

      @Override
      public void onFailure(Call<OptimizedTripsResponse> call, Throwable throwable) {
        Timber.e("Calling optimize trips failed: ", throwable);
      }
    });
  }

  private void drawLine(String geometry) {

    LineString linestring = LineString.fromPolyline(geometry, Constants.PRECISION_6);
    FeatureCollection featureCollection
      = FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(linestring)});

    GeoJsonSource source = new GeoJsonSource(LINE_SOURCE, featureCollection);
    mapboxMap.addSource(source);

    LineLayer layer = new LineLayer(LINE_LAYER, LINE_SOURCE).withProperties(
      PropertyFactory.lineWidth(5f),
      PropertyFactory.lineColor(Color.parseColor("#009DF9"))
    );

    mapboxMap.addLayerBelow(layer, "poi-parks-scalerank2");

    startAnimation(
      Position.fromCoordinates(
        tripStops.get(0).getPosition().getLongitude(),
        tripStops.get(0).getPosition().getLatitude()),
      linestring);

  }

  private void startAnimation(final Position startPosition, final LineString route) {

    final double routeDistance = TurfMeasurement.lineDistance(route, TurfConstants.UNIT_METERS);

    valueAnimator = ValueAnimator.ofFloat(0, 1);
    valueAnimator.setDuration(30000);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        Point currentPoint = TurfMeasurement.along(
          route,
          routeDistance * (double) animation.getAnimatedFraction(),
          TurfConstants.UNIT_METERS
        );
        LineString newLine = TurfMisc.lineSlice(Point.fromCoordinates(startPosition), currentPoint, route);

        GeoJsonSource source = mapboxMap.getSourceAs(LINE_SOURCE);
        if (source != null) {
          FeatureCollection newRouteFeature
            = FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(newLine)});
          source.setGeoJson(newRouteFeature);

        }
      }
    });
    valueAnimator.start();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
    if (valueAnimator != null) {
      valueAnimator.removeAllUpdateListeners();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}