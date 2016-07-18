package com.mapbox.services.android.testapp.turf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;

public class TurfDistanceActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;
    private Marker distanceMarker;

    private Position cadillacHotelPosition = Position.fromCoordinates(-118.479852, 33.993898);
    private String units = TurfConstants.UNIT_MILES;

    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_distance);


        container = findViewById(R.id.turf_distance_map_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                map = mapboxMap;

                distance();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_turf_units, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_miles:
                units = TurfConstants.UNIT_MILES;
                return true;
            case R.id.action_nauticalmiles:
                units = TurfConstants.UNIT_NAUTICAL_MILES;
                return true;
            case R.id.action_kilometers:
                units = TurfConstants.UNIT_KILOMETERS;
                return true;
            case R.id.action_radians:
                units = TurfConstants.UNIT_RADIANS;
                return true;
            case R.id.action_degrees:
                units = TurfConstants.UNIT_DEGREES;
                return true;
            case R.id.action_inches:
                units = TurfConstants.UNIT_INCHES;
                return true;
            case R.id.action_yards:
                units = TurfConstants.UNIT_YARDS;
                return true;
            case R.id.action_meters:
                units = TurfConstants.UNIT_METERS;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void distance() {

        map.addMarker(new MarkerViewOptions()
                .position(new LatLng(cadillacHotelPosition.getLatitude(), cadillacHotelPosition.getLongitude()))
                .title("point 1"));

        Snackbar.make(container, "Click map anywhere to calculate distance", Snackbar.LENGTH_INDEFINITE).show();

        map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                if (distanceMarker != null) map.removeMarker(distanceMarker);

                distanceMarker = map.addMarker(new MarkerViewOptions()
                        .position(point)
                        .title("point 2"));

                double distance = 0;
                try {
                    distance = TurfMeasurement.distance(Point.fromCoordinates(cadillacHotelPosition), Point.fromCoordinates(Position.fromCoordinates(point.getLongitude(), point.getLatitude())), units);
                } catch (TurfException e) {
                    e.printStackTrace();
                }
                Snackbar.make(container, "Distance = " + distance + " " + units, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }
}