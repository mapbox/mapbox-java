package com.mapbox.services.android.testapp.directions;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v4.DirectionsCriteria;
import com.mapbox.services.directions.v4.MapboxDirections;
import com.mapbox.services.directions.v4.models.DirectionsResponse;
import com.mapbox.services.directions.v4.models.DirectionsRoute;
import com.mapbox.services.directions.v4.models.Waypoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionsV4Activity extends AppCompatActivity {

    private final static String LOG_TAG = "DirectionsV4Activity";

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;

    private DirectionsRoute currentRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_v4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Dupont Circle (Washington, DC)
        final Waypoint origin = new Waypoint(-77.04341, 38.90962);

        // The White House (Washington, DC)
        final Waypoint destination = new Waypoint(-77.0365, 38.8977);

        // Centroid
        final LatLng centroid = new LatLng(
                (origin.getLatitude() + destination.getLatitude()) / 2,
                (origin.getLongitude() + destination.getLongitude()) / 2);

        // Set up a standard Mapbox map
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setAccessToken(Utils.getMapboxAccessToken(this));
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMapReady) {
                mapboxMap = mapboxMapReady;

                mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

                // Move map
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(centroid)
                        .zoom(14)
                        .build();
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Add origin and destination to the map
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
                        .title("Origin")
                        .snippet("Dupont Circle"));
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(destination.getLatitude(), destination.getLongitude()))
                        .title("Destination")
                        .snippet("The White House"));

                // Get route from API
                try {
                    getRoute(origin, destination);
                } catch (ServicesException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getRoute(Waypoint origin, Waypoint destination) throws ServicesException {
        MapboxDirections client = new MapboxDirections.Builder()
                .setAccessToken(Utils.getMapboxAccessToken(this))
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
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
                showMessage(String.format("Route is %d meters long.", currentRoute.getDistance()));

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
        LineString lineString = route.asLineString(Constants.OSRM_PRECISION_V4);
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
