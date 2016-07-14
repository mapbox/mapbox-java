package com.mapbox.services.android.testapp.turf;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfMisc;
import com.mapbox.services.commons.utils.PolylineUtils;

import java.util.ArrayList;
import java.util.List;

public class TurfLineSliceActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;

    List<Position> routePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turf_line_slice);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Full route
        routePoints = PolylineUtils.decode("ovnnEzvcrUt@o@z@u@_DkHu@cBM]Wi@q@aB[u@_BwDkBiEcB_EQ_@g@iAk@sAc@cAkA{CqAuCkAqCk@mAWm@S]]y@qBwEYq@]{@Z{@Xw@LYTm@Tm@Rk@Vq@JUJWNc@L]N_@Pc@HQPc@DKBKFO`@gARg@Tg@L[J[Rg@Tm@Nc@Pe@Zw@N_@JUNc@Z{@DIL[Tm@FDFF", Constants.OSRM_PRECISION_V5);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                drawFullRoute();
                drawSlicedRoute();

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

    private void drawFullRoute() {
        List<LatLng> routeLatLngs = new ArrayList<>();

        for (Position routePoint : routePoints) {
            routeLatLngs.add(new LatLng(routePoint.getLatitude(), routePoint.getLongitude()));
        }

        map.addPolyline(new PolylineOptions()
                .addAll(routeLatLngs)
                .color(Color.parseColor("#3bb2d0"))
                .alpha(0.5f)
                .width(5));
    }

    private void drawSlicedRoute() {
        try {
            LineString lineString = TurfMisc.lineSlice(Point.fromCoordinates(routePoints.get(5)), Point.fromCoordinates(routePoints.get(20)), LineString.fromCoordinates(routePoints));

            List<LatLng> routeSliceLatLngs = new ArrayList<>();
            for (Position routePoint : lineString.getCoordinates()) {
                routeSliceLatLngs.add(new LatLng(routePoint.getLatitude(), routePoint.getLongitude()));
            }

            map.addPolyline(new PolylineOptions()
                    .addAll(routeSliceLatLngs)
                    .color(Color.parseColor("#f9886c"))
                    .width(5));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}