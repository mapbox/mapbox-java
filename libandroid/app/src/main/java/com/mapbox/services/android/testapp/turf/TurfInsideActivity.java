package com.mapbox.services.android.testapp.turf;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.turf.TurfException;
import com.mapbox.services.commons.turf.TurfJoins;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TurfInsideActivity extends AppCompatActivity {

  private static final String TAG = "TurfInsideActivity";

  private MapView mapView;
  private MapboxMap map;
  private Marker withinMarker;
  private Polygon polygon;

  private View container;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_turf_inside);

    container = findViewById(R.id.turf_inside_map_container);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mapView = (MapView) findViewById(R.id.mapView);
    mapView.setAccessToken(Utils.getMapboxAccessToken(this));
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        new DrawGeoJson().execute();
        onMapClick();
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

  public void onMapClick() {

    map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull LatLng point) {
        if (withinMarker != null) {
          map.removeMarker(withinMarker);
        }

        if (polygon != null) {
          withinMarker = map.addMarker(new MarkerViewOptions().position(point));

          List<Position> polygonPositions = new ArrayList<>();
          for (LatLng latLng : polygon.getPoints()) {
            polygonPositions.add(Position.fromCoordinates(latLng.getLongitude(), latLng.getLatitude()));
          }

          try {
            boolean pointWithin = TurfJoins.inside(Position.fromCoordinates(
              withinMarker.getPosition().getLongitude(), withinMarker.getPosition().getLatitude()), polygonPositions);
            Snackbar.make(container, "Point lands within polygon = " + pointWithin, Snackbar.LENGTH_INDEFINITE).show();
          } catch (TurfException turfException) {
            Log.e(TAG, "onMapClick: " + turfException.getMessage());
            turfException.printStackTrace();
          }
        }
      }
    });

  }

  private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {
    @Override
    protected List<LatLng> doInBackground(Void... voids) {

      ArrayList<LatLng> points = new ArrayList<>();

      try {
        // Load GeoJSON file
        InputStream inputStream = getAssets().open("los_angeles_airport.geojson");
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }

        inputStream.close();

        // Parse JSON
        JSONObject json = new JSONObject(sb.toString());
        JSONArray features = json.getJSONArray("features");
        JSONObject feature = features.getJSONObject(0);
        JSONObject geometry = feature.getJSONObject("geometry");
        if (geometry != null) {
          String type = geometry.getString("type");

          // Our GeoJSON only has one feature: a line string
          if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

            // Get the Coordinates
            JSONArray coords = geometry.getJSONArray("coordinates");
            for (int lc = 0; lc < coords.length(); lc++) {
              JSONArray coord = coords.getJSONArray(lc);
              LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
              points.add(latLng);
            }
          }
        }
      } catch (Exception exception) {
        Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
      }

      return points;
    }

    @Override
    protected void onPostExecute(List<LatLng> points) {
      super.onPostExecute(points);

      if (points.size() > 0) {
        LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

        // Draw Points on MapView
        polygon = map.addPolygon(new PolygonOptions()
          .add(pointsArray)
          .fillColor(Color.parseColor("#e55e5e"))
          .alpha(0.50f));
      }
    }
  }
}
