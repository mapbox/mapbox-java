package com.mapbox.services.android.testapp.nav;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.navigation.v5.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.NavigationEventListener;
import com.mapbox.services.android.navigation.v5.ProgressChangeListener;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.navigation.v5.RouteProgress;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.maps.MapboxMap.OnMapClickListener;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapClickListener,
  ProgressChangeListener, NavigationEventListener {

  private static final String TAG = "NavigationActivity";

  // Map variables
  private MapView mapView;
  private MapboxMap mapboxMap;
  private Marker destinationMarker;

  private MapboxNavigation navigation;
  private LocationEngine locationEngine;
  private DirectionsRoute route;
  private Button startRouteButton;
  private Polyline routeLine;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation_activity);

    startRouteButton = (Button) findViewById(R.id.startRouteButton);
    startRouteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (navigation != null && route != null) {
          navigation.setNavigationEventListener(NavigationActivity.this);
          navigation.setProgressChangeListener(NavigationActivity.this);

          // Adjust location engine to force a gps reading every second. This isn't required but gives an overall
          // better navigation experience for users.
          locationEngine.setInterval(0);
          locationEngine.setSmallestDisplacement(0f);
          locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
          locationEngine.setFastestInterval(1000);

          navigation.setLocationEngine(locationEngine);
          navigation.startNavigation(route);
        }
      }
    });

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);


    locationEngine = LocationSource.getLocationEngine(this);

    navigation = new MapboxNavigation(this, Mapbox.getAccessToken());

    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setOnMapClickListener(this);
    Snackbar.make(mapView, "Tap map to place destination", BaseTransientBottomBar.LENGTH_LONG).show();

    mapboxMap.moveCamera(CameraUpdateFactory.zoomBy(12));

    if (PermissionsManager.areLocationPermissionsGranted(this)) {
      mapboxMap.setMyLocationEnabled(true);
      mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
    }
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    if (destinationMarker != null) {
      mapboxMap.removeMarker(destinationMarker);
    }
    destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(point));

    startRouteButton.setVisibility(View.VISIBLE);
    calculateRoute(Position.fromCoordinates(point.getLongitude(), point.getLatitude()));
  }

  private void drawRouteLine(DirectionsRoute route) {
    List<Position> positions = LineString.fromPolyline(route.getGeometry(), Constants.PRECISION_6).getCoordinates();
    List<LatLng> latLngs = new ArrayList<>();
    for (Position position : positions) {
      latLngs.add(new LatLng(position.getLatitude(), position.getLongitude()));
    }

    // Remove old route if currently being shown on map.
    if (routeLine != null) {
      mapboxMap.removePolyline(routeLine);
    }

    routeLine = mapboxMap.addPolyline(new PolylineOptions()
      .addAll(latLngs)
      .color(Color.parseColor("#56b881"))
      .width(5f));
  }

  private void calculateRoute(Position destination) {
    Location userLocation = mapboxMap.getMyLocation();
    if (userLocation == null) {
      Log.d(TAG, "calculateRoute: User location is null, therefore, origin can't be set.");
      return;
    }

    navigation.setOrigin(Position.fromCoordinates(userLocation.getLongitude(), userLocation.getLatitude()));
    navigation.setDestination(destination);
    navigation.getRoute(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        DirectionsRoute route = response.body().getRoutes().get(0);
        NavigationActivity.this.route = route;
        drawRouteLine(route);

      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        Log.e(TAG, "onFailure: navigation.getRoute()", throwable);
      }
    });
  }

  /*
   * Navigation listeners
   */

  @Override
  public void onRunning(boolean running) {
    if (running) {
      Log.d(TAG, "onRunning: Started");
    } else {
      Log.d(TAG, "onRunning: Stopped");
    }
  }

  @Override
  public void onProgressChange(Location location, RouteProgress routeProgress) {
    Log.d(TAG, "onProgressChange: called");
  }

  /*
   * Activity lifecycle methods
   */

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStart() {
    super.onStart();
    navigation.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    navigation.onStop();
    mapView.onStop();
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
