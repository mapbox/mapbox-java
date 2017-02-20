package com.mapbox.services.android.testapp.directions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.rx.directions.v5.MapboxDirectionsRx;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DirectionsV5Activity extends AppCompatActivity {

  private static final String LOG_TAG = "DirectionsV5Activity";

  private MapView mapView = null;
  private MapboxMap mapboxMap = null;

  private DirectionsRoute currentRoute = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_directions_v5);

    // San Francisco
    final Position origin = Position.fromCoordinates(-122.416667, 37.783333);

    // San Jose
    final Position destination = Position.fromCoordinates(-121.9, 37.333333);

    // Centroid
    final LatLng centroid = new LatLng(
      (origin.getLatitude() + destination.getLatitude()) / 2,
      (origin.getLongitude() + destination.getLongitude()) / 2);

    // Set up a standard Mapbox map
    mapView = (MapView) findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMapReady) {
        mapboxMap = mapboxMapReady;

        mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

        // Move map
        CameraPosition cameraPosition = new CameraPosition.Builder()
          .target(centroid)
          .zoom(8)
          .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Add origin and destination to the map
        mapboxMap.addMarker(new MarkerOptions()
          .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
          .title("Origin")
          .snippet("San Francisco"));
        mapboxMap.addMarker(new MarkerOptions()
          .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
          .title("Destination")
          .snippet("San Jose"));

        // Get route from API
        try {
          getRoute(origin, destination);
        } catch (ServicesException servicesException) {
          servicesException.printStackTrace();
        }
      }
    });
  }

  private void getRoute(Position origin, Position destination) throws ServicesException {
    ArrayList<Position> positions = new ArrayList<>();
    positions.add(origin);
    positions.add(destination);

    MapboxDirections client = new MapboxDirections.Builder()
      .setAccessToken(Utils.getMapboxAccessToken(this))
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setSteps(true)
      .setOverview(DirectionsCriteria.OVERVIEW_FULL)
      .setBearings(new double[] {60, 45}, new double[] {45, 45})
      .build();

    MapboxDirectionsRx clientRx = new MapboxDirectionsRx.Builder()
      .setAccessToken(Utils.getMapboxAccessToken(this))
      .setCoordinates(positions)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setSteps(true)
      .setOverview(DirectionsCriteria.OVERVIEW_FULL)
      .build();
    clientRx.getObservable()
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<DirectionsResponse>() {
        @Override
        public void call(DirectionsResponse response) {
          DirectionsRoute currentRoute = response.getRoutes().get(0);
          Log.d(LOG_TAG, "Response code: " + response.getCode());
          Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
        }
      });

    client.enqueueCall(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        System.out.println(call.request().url().toString());

        // You can get generic HTTP info about the response
        Log.d(LOG_TAG, "Response code: " + response.code());
        if (response.body() == null) {
          Log.e(LOG_TAG, "No routes found, make sure you set the right user and access token.");
          return;
        }

        // Print some info about the route
        currentRoute = response.body().getRoutes().get(0);
        Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
        showMessage(String.format(Locale.US, "Route is %.1f meters long.", currentRoute.getDistance()));

        // Draw the route on the map
        drawRoute(currentRoute);
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        Log.e(LOG_TAG, "Error: " + throwable.getMessage());
        showMessage("Error: " + throwable.getMessage());
      }
    });
  }

  private void drawRoute(DirectionsRoute route) {
    // Convert LineString coordinates into LatLng[]
    LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.PRECISION_6);
    List<Position> coordinates = lineString.getCoordinates();
    LatLng[] points = new LatLng[coordinates.size()];
    for (int i = 0; i < coordinates.size(); i++) {
      points[i] = new LatLng(
        coordinates.get(i).getLatitude(),
        coordinates.get(i).getLongitude());
    }

    // Draw Points on MapView
    mapboxMap.addPolyline(new PolylineOptions()
      .add(points)
      .color(Color.parseColor("#3887be"))
      .width(5));
  }

  private void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
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
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

}
