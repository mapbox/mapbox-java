package com.mapbox.services.android.testapp.directions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionsV5Activity extends AppCompatActivity {

    private final static String LOG_TAG = "DirectionsV5Activity";

    private MapView mapView = null;
    private MapboxMap mapboxMap = null;

    private DirectionsRoute currentRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_v5);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                        .zoom(8)
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

    private void getRoute(Position origin, Position destination) throws ServicesException {
        Position[] positions = new Position[2];
        positions[0] = origin;
        positions[1] = destination;

        MapboxDirections.Builder builder = new MapboxDirections.Builder()
                .setCoordinates(positions)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING);

        // Check for a custom user
        int v5MapboxUser = getResources().getIdentifier("v5_mapbox_user", "string", getPackageName());
        if (!TextUtils.isEmpty(getString(v5MapboxUser))) {
            Log.d(LOG_TAG, "Custom user set.");
            builder.setUser(getString(v5MapboxUser));
        }

        // Check for a custom access token
        int v5MapboxAccessToken = getResources().getIdentifier("v5_mapbox_access_token", "string", getPackageName());
        if (!TextUtils.isEmpty(getString(v5MapboxAccessToken))) {
            Log.d(LOG_TAG, "Custom access token set.");
            builder.setAccessToken(getString(v5MapboxAccessToken));
        } else {
            builder.setAccessToken(Utils.getMapboxAccessToken(this));
        }

        MapboxDirections client = builder.build();

        /*
         * Note that we also support RxJava + RxAndroid to consume our APIs. For example, the
         * code below could be done like this with observables, in a way that creates a new thread
         * but the result can be used to change the views without hitting the dreaded
         * android.os.NetworkOnMainThreadException.
         *
         * client.getObservable()
         *                 .subscribeOn(Schedulers.newThread())
         *                 .observeOn(AndroidSchedulers.mainThread())
         *                 .subscribe(new Action1<DirectionsResponse>() {
         *                     @Override
         *                     public void call(DirectionsResponse response) {
         *                         DirectionsRoute currentRoute = response.getRoutes().get(0);
         *                         Log.d(LOG_TAG, "Response code: " + response.getCode());
         *                         Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
         *                     }
         *         });
         */

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get generic HTTP info about the response
                Log.d(LOG_TAG, "Response code: " + response.code());

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(LOG_TAG, "Distance: " + currentRoute.getDistance());
                showMessage(String.format("Route is %f meters long.", currentRoute.getDistance()));

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
