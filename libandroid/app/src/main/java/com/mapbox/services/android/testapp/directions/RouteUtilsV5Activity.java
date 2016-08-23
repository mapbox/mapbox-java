package com.mapbox.services.android.testapp.directions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.directions.v5.models.RouteLeg;
import com.mapbox.services.navigation.v5.RouteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteUtilsV5Activity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String LOG_TAG = "RouteUtilsV5Activity";

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;

    private LatLng from = null;
    private LatLng to = null;
    private DirectionsRoute currentRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_utils_v5);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up a standard Mapbox map
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

        // Dupont Circle
        LatLng target = new LatLng(38.90962, -77.04341);

        // Move map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(target)
                .zoom(14)
                .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                if (from == null) {
                    setFrom(point);
                } else if (to == null) {
                    setTo(point);
                } else {
                    try {
                        doUtils(point);
                    } catch (ServicesException e) {
                        Log.e(LOG_TAG, "Services exception: " + e.getMessage());
                        e.printStackTrace();
                    } catch (TurfException e) {
                        Log.e(LOG_TAG, "Turf exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setFrom(LatLng point) {
        from = point;
        mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                .title("From"));
    }

    private void setTo(LatLng point) {
        to = point;
        mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                .title("To"));

        try {
            getRoute(Position.fromCoordinates(from.getLongitude(), from.getLatitude()), Position.fromCoordinates(to.getLongitude(), to.getLatitude()));
        } catch (ServicesException e) {
            showMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    private void doUtils(LatLng point) throws ServicesException, TurfException {
        RouteUtils routeUtils = new RouteUtils();
        RouteLeg route = currentRoute.getLegs().get(0);
        Position position = Position.fromCoordinates(point.getLongitude(), point.getLatitude());

        Log.d(LOG_TAG, String.format(Locale.US, "- isOffRoute = %b", routeUtils.isOffRoute(position, route)));
        Log.d(LOG_TAG, String.format(Locale.US, "- getClosestStep = %d", routeUtils.getClosestStep(position, route)));

        for (int stepIndex = 0; stepIndex < route.getSteps().size(); stepIndex++) {
            Log.d(LOG_TAG, String.format("- Step index: %d", stepIndex));
            Log.d(LOG_TAG, String.format(Locale.US, "  - isInStep = %b", routeUtils.isInStep(position, route, stepIndex)));
            Log.d(LOG_TAG, String.format(Locale.US, "  - getDistanceToStep = %f", routeUtils.getDistanceToStep(position, route, stepIndex)));
            Log.d(LOG_TAG, String.format(Locale.US, "  - getSnapToRoute = %s", routeUtils.getSnapToRoute(position, route, stepIndex).toString()));
        }
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
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get generic HTTP info about the response
                Log.d(LOG_TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(LOG_TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                }

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
                showMessage(String.format(Locale.US, "Route is %f meters long.", currentRoute.getDistance()));

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Error: " + t.getMessage());
                showMessage("Error: " + t.getMessage());
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
                .color(Color.parseColor("#3887be"))
                .width(5));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
