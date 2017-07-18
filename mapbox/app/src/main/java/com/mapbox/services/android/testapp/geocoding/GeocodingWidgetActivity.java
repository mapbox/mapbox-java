package com.mapbox.services.android.testapp.geocoding;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.AndroidLocationEngineChain;
import com.mapbox.services.android.telemetry.location.ClasspathChecker;
import com.mapbox.services.android.telemetry.location.GoogleLocationEngineChain;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEngineChain;
import com.mapbox.services.android.telemetry.location.LocationEngineChainSupplier;
import com.mapbox.services.android.telemetry.location.LostLocationEngineChain;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

public class GeocodingWidgetActivity extends AppCompatActivity implements LocationEngineListener {

  private static final String LOG_TAG = "GeocodingWidgetActivity";

  private MapView mapView;
  private MapboxMap mapboxMap;

  private GeocoderAutoCompleteView autocomplete;
  private LocationEngine locationEngine;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geocoding_widget);

    // Set up autocomplete widget
    autocomplete = (GeocoderAutoCompleteView) findViewById(R.id.query);
    autocomplete.setAccessToken(Utils.getMapboxAccessToken(this));
    autocomplete.setType(GeocodingCriteria.TYPE_POI);
    autocomplete.setLimit(10);
    autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
      @Override
      public void onFeatureClick(CarmenFeature feature) {
        Position position = feature.asPosition();
        updateMap(position.getLatitude(), position.getLongitude());
      }
    });

    // Set up map
    mapView = (MapView) findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMapReady) {
        mapboxMap = mapboxMapReady;
        mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);
      }
    });

    // Set up location services to improve accuracy
    List<LocationEngineChain> locationSources = initLocationSources();
    LocationEngineChainSupplier locationSourceSupplier = new LocationEngineChainSupplier(locationSources);
    locationEngine = locationSourceSupplier.supply(this);
    locationEngine.addLocationEngineListener(this);
    locationEngine.activate();
  }

  @Override
  public void onConnected() {
    Log.d(LOG_TAG, "Connected to engine, we can now request updates.");
    locationEngine.requestLocationUpdates();
  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      Log.d(LOG_TAG, "New location: " + location.toString());
      autocomplete.setProximity(Position.fromCoordinates(
        location.getLongitude(), location.getLatitude()));
    }
  }

  private void updateMap(double latitude, double longitude) {
    // Marker
    mapboxMap.addMarker(new MarkerOptions()
      .position(new LatLng(latitude, longitude))
      .title("Geocoder result"));

    // Animate map
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(new LatLng(latitude, longitude))
      .zoom(15)
      .build();
    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null);
  }

  private List<LocationEngineChain> initLocationSources() {
    ClasspathChecker classpathChecker = new ClasspathChecker();
    List<LocationEngineChain> locationSources = new ArrayList<>();
    locationSources.add(new GoogleLocationEngineChain(classpathChecker));
    locationSources.add(new LostLocationEngineChain(classpathChecker));
    locationSources.add(new AndroidLocationEngineChain());

    return locationSources;
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
    if (locationEngine != null && locationEngine.isConnected()) {
      locationEngine.requestLocationUpdates();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
    if (locationEngine != null) {
      locationEngine.removeLocationUpdates();
      locationEngine.removeLocationEngineListener(this);
      locationEngine.deactivate();
    }

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    autocomplete.cancelApiCall();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
}
