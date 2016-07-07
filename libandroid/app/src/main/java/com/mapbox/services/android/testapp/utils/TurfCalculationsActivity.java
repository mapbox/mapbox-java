package com.mapbox.services.android.testapp.utils;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.geojson.Geometry;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfConstants;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.commons.turf.TurfMisc;

import java.util.ArrayList;
import java.util.List;

public class TurfCalculationsActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;

    private Marker cadillacHotel;
    private Marker horizonAve;
    private Marker westminsterAve;

    private Position cadillacHotelPosition = Position.fromCoordinates(-118.479852, 33.993898);
    private Position horizionAvePosition = Position.fromCoordinates(-118.474681, 33.988071);
    private Position westminsterAvePosition = Position.fromCoordinates(-118.470658, 33.991861);

    private View container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_turf_calculations);

        container = findViewById(R.id.turf_calculation_map_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> turfCalculationsAdapter = ArrayAdapter.createFromResource(TurfCalculationsActivity.this, R.array.turf_calculation_items, android.R.layout.simple_spinner_item);
        turfCalculationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner turfCalculation = (Spinner) findViewById(R.id.turf_calculation_spinner);
        turfCalculation.setAdapter(turfCalculationsAdapter);
        turfCalculation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // select one
                        break;
                    case 1:
                        double bearing = TurfMeasurement.bearing(Point.fromCoordinates(cadillacHotelPosition), Point.fromCoordinates(horizionAvePosition));
                        Snackbar.make(container, "Bearing = " + bearing, Snackbar.LENGTH_INDEFINITE).show();
                        break;
                    case 2:
                        try {
                            Point result = TurfMeasurement.destination(Point.fromCoordinates(cadillacHotelPosition), 1, 45, TurfConstants.UNIT_MILES);
                            Position resultPosition = result.getCoordinates();
                            Snackbar.make(container, "Destination = " + resultPosition.getLatitude() + ", " + resultPosition.getLongitude(), Snackbar.LENGTH_INDEFINITE).show();
                        } catch (TurfException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            double distance = TurfMeasurement.distance(Point.fromCoordinates(cadillacHotelPosition), Point.fromCoordinates(horizionAvePosition), TurfConstants.UNIT_MILES);
                            Snackbar.make(container, "Distance in miles= " + distance, Snackbar.LENGTH_INDEFINITE).show();
                        } catch (TurfException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        // TODO
                        // TurfMeasurement.lineDistance()
                        break;
                    case 5:
                        List<Position> linePoints = new ArrayList<>();
                        linePoints.add(cadillacHotelPosition);
                        linePoints.add(horizionAvePosition);
                        linePoints.add(westminsterAvePosition);
                        linePoints.add(cadillacHotelPosition);

                        LineString lineString = LineString.fromCoordinates(linePoints);

                        try {
                            LineString slicedLine = TurfMisc.lineSlice(Point.fromCoordinates(horizionAvePosition), Point.fromCoordinates(westminsterAvePosition), lineString);
                            //List<Position> slicedLinePoints = slicedLine.getCoordinates();

                            //for (int i = 0; i < slicedLinePoints.size(); i++) {
                            //    map.addMarker(new MarkerViewOptions().position(new LatLng(slicedLinePoints.get(i).getLatitude(), slicedLinePoints.get(i).getLongitude())));
                            //}
                        } catch (TurfException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setAccessToken(Utils.getMapboxAccessToken(this));
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                cadillacHotel = mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(33.993898, -118.479852)));

                horizonAve = mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(33.988071, -118.474681)));

                westminsterAve = mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(33.991861, -118.470658)));
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
}