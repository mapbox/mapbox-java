package com.mapbox.services.android.testapp.nav;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.TrackingSettings;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.commons.turf.TurfMisc;
import com.mapbox.services.commons.utils.PolylineUtils;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.navigation.v5.RouteUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnapToRouteActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

  private static final String TAG = "SnapToRouteActivity";
  private static final Position origin = Position.fromCoordinates(-95.75188, 29.78533);
  //  private static final Position destination = Position.fromCoordinates(-95.71896, 29.78518);
  private static final Position destination = Position.fromCoordinates(-95.71892, 29.77516);

  private MapView mapView;
  private MapboxMap mapboxMap;
  private DirectionsRoute currentRoute;

  private Marker userLocation;
  private Marker snappedLocation;
  private FloatingActionButton backStepFab;
  private FloatingActionButton forwardStepFab;
  private int stepCount = 0;
  private Polyline stepPolyline;
  private Polyline distancePolyline;
  private Polyline distanceRoutePolyline;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_snap_to_route);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    forwardStepFab = (FloatingActionButton) findViewById(R.id.fab_forward_a_step);
    backStepFab = (FloatingActionButton) findViewById(R.id.fab_back_a_step);

    backStepFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          if (currentRoute == null || mapboxMap == null) {
            return;
          }

          if (stepCount <= 0) {
            Toast.makeText(SnapToRouteActivity.this, "On first step already", Toast.LENGTH_SHORT).show();
            return;
          }

          stepCount--;

          drawStepPolyline();
      }
    });

    forwardStepFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (currentRoute == null || mapboxMap == null) {
          return;
        }

        if (currentRoute.getLegs().get(0).getSteps().size() - 1 <= stepCount) {
          Toast.makeText(SnapToRouteActivity.this, "On last step already", Toast.LENGTH_SHORT).show();
          return;
        }

        stepCount++;

        drawStepPolyline();
      }
    });

    mapView = (MapView) findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {
    if (userLocation != null) {
      mapboxMap.removeMarker(userLocation);
    }
    if (snappedLocation != null) {
      mapboxMap.removeMarker(snappedLocation);
    }

    userLocation = mapboxMap.addMarker(new MarkerOptions()
      .position(point)
    );

    RouteUtils routeUtils = new RouteUtils();
    Position snappedPosition = null;

    try {
      snappedPosition = routeUtils.getSnapToRoute(
        Position.fromCoordinates(point.getLongitude(), point.getLatitude()),
        currentRoute.getLegs().get(0),
        stepCount
      );
    } catch (ServicesException | TurfException exception) {
      exception.printStackTrace();
      Log.e(TAG, "snap to route util: " + exception.getMessage());
    }

    if (snappedPosition == null) {
      Log.i(TAG, "snapPosition is null");
      return;
    }

    snappedLocation = mapboxMap.addMarker(new MarkerOptions()
      .position(new LatLng(snappedPosition.getLatitude(), snappedPosition.getLongitude()))
    );

    double stepDistance = 0;
    try {
      stepDistance = routeUtils.getDistanceToNextStep(snappedPosition, currentRoute.getLegs().get(0), stepCount);
    } catch (ServicesException | TurfException e) {
      e.printStackTrace();
    }

    double routeDistance = 0;
    try {
      routeDistance = routeUtils.getDistanceToEndOfRoute(snappedPosition, currentRoute);
    } catch (TurfException e) {
      e.printStackTrace();
    }

    drawDistanceRoutePolyline(snappedPosition);


    // Decode the geometry and draw the route from current position to start of next step.
    List<Position> coords = PolylineUtils.decode(currentRoute.getLegs().get(0).getSteps().get(stepCount).getGeometry(), Constants.OSRM_PRECISION_V5);

    // remove old line
    if (distancePolyline != null) {
      mapboxMap.removePolyline(distancePolyline);
    }

    try {
      LineString slicedLine = TurfMisc.lineSlice(
        Point.fromCoordinates(snappedPosition),
        Point.fromCoordinates(coords.get(coords.size() - 1)),
        LineString.fromCoordinates(coords)
      );

      List<Position> linePositions = slicedLine.getCoordinates();
      List<LatLng> lineLatLng = new ArrayList<>();
      for (Position pos : linePositions) {
        lineLatLng.add(new LatLng(pos.getLatitude(), pos.getLongitude()));
      }

      distancePolyline = mapboxMap.addPolyline(new PolylineOptions()
        .addAll(lineLatLng)
        .color(Color.parseColor("#f1f075"))
        .width(5f)
      );
    } catch (TurfException e) {
      e.printStackTrace();
    }

    Log.i(TAG, "distance in kilometers to end of route: " + routeDistance);
    Log.i(TAG, "distance in kilometers to next step: " + stepDistance);

  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    SnapToRouteActivity.this.mapboxMap = mapboxMap;
    mapboxMap.setOnMapClickListener(this);

    try {
      getRoute(origin, destination);
    } catch (ServicesException servicesException) {
      servicesException.printStackTrace();
    }
  }

  private void drawDistanceRoutePolyline(Position snappedPosition) {
    // Decode the geometry and draw the route from current position to end of route
    List<Position> routeCoords = PolylineUtils.decode(currentRoute.getGeometry(), Constants.OSRM_PRECISION_V5);

    // remove old line
    if (distanceRoutePolyline != null) {
      mapboxMap.removePolyline(distanceRoutePolyline);
    }

    try {
      LineString slicedRouteLine = TurfMisc.lineSlice(
        Point.fromCoordinates(snappedPosition),
        Point.fromCoordinates(routeCoords.get(routeCoords.size() - 1)),
        LineString.fromCoordinates(routeCoords)
      );

      List<Position> linePositions = slicedRouteLine.getCoordinates();
      List<LatLng> lineLatLng = new ArrayList<>();
      for (Position pos : linePositions) {
        lineLatLng.add(new LatLng(pos.getLatitude(), pos.getLongitude()));
      }

      distanceRoutePolyline = mapboxMap.addPolyline(new PolylineOptions()
        .addAll(lineLatLng)
        .color(Color.parseColor("#3887be"))
        .width(5f)
      );
    } catch (TurfException e) {
      e.printStackTrace();
    }
  }

  private void getRoute(Position origin, Position destination) throws ServicesException {

    MapboxDirections client = new MapboxDirections.Builder()
      .setOrigin(origin)
      .setDestination(destination)
      .setOverview(DirectionsCriteria.OVERVIEW_FULL)
      .setSteps(true)
      .setProfile(DirectionsCriteria.PROFILE_DRIVING)
      .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
      .build();

    Log.i(TAG, "Request: " + client.cloneCall().request());

    client.enqueueCall(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        // You can get the generic HTTP info about the response
        Log.d(TAG, "Response code: " + response.code());
        if (response.body() == null) {
          Log.e(TAG, "No routes found, make sure you set the right user and access token.");
          return;
        } else if (response.body().getRoutes().size() < 1) {
          Log.e(TAG, "No routes found");
          return;
        }

        // Print some info about the route
        currentRoute = response.body().getRoutes().get(0);
        Log.d(TAG, "Distance: " + currentRoute.getDistance());
        Toast.makeText(
          SnapToRouteActivity.this,
          "Route is " + currentRoute.getDistance() + " meters long.",
          Toast.LENGTH_SHORT).show();

        // Draw the route on the map
        drawRoute(currentRoute);
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        Log.e(TAG, "Error: " + throwable.getMessage());
        Toast.makeText(SnapToRouteActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void drawRoute(DirectionsRoute route) {
    // Convert LineString coordinates into LatLng[]
    LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
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
      .color(Color.parseColor("#009688"))
      .width(5));
  }

  private void drawStepPolyline() {
    LineString lineString = LineString.fromPolyline(
      currentRoute.getLegs().get(0).getSteps().get(stepCount).getGeometry(),
      Constants.OSRM_PRECISION_V5
    );

    List<Position> coordinates = lineString.getCoordinates();

    List<LatLng> points = new ArrayList<>();
    for (int i = 0; i < coordinates.size(); i++) {
      points.add(
        new LatLng(coordinates.get(i).getLatitude(), coordinates.get(i).getLongitude())
      );
    }

    if (stepPolyline != null) {
      mapboxMap.removePolyline(stepPolyline);
    }

    stepPolyline = mapboxMap.addPolyline(new PolylineOptions()
      .addAll(points)
      .color(Color.parseColor("#e55e5e"))
      .width(5));
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
