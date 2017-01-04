package com.mapbox.services.android.testapp.utils;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimplifyPolylineActivity extends AppCompatActivity {

  private static final String TAG = "SimplifyLineActivity";

  private MapView mapView;
  private MapboxMap map;

  private List<Position> route;
  private Polyline simplifiedRoute;
  private boolean quality = false;

  // Progress bar goes from 0 to 100 (default is 50)
  private int toleranceValue = 50;
  private TextView toleranceTextView;
  private TextView pointValueTextView;

  private int totalRoutePoints;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_utils_simplify_polyline);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    toleranceTextView = (TextView) findViewById(R.id.tolerance_value);
    updateToleranceTextView(toleranceValue);

    pointValueTextView = (TextView) findViewById(R.id.point_value);

    SeekBar toleranceSeekBar = (SeekBar) findViewById(R.id.seekbar_tolerance);
    if (toleranceSeekBar != null) {
      toleranceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          toleranceValue = progress;
          updateToleranceTextView(toleranceValue);

          if (route != null) {
            drawSimplify(route, progress, quality);
          }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
      });
    }

    CheckBox qualityCheckBox = (CheckBox) findViewById(R.id.quality_toggle);
    if (qualityCheckBox != null) {
      qualityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          quality = isChecked;
          if (route != null) {
            drawSimplify(route, toleranceValue, quality);
          }
        }
      });
    }

    mapView = (MapView) findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        new DrawGeoJson().execute();

      }
    });
  }

  private void updateToleranceTextView(int progress) {
    toleranceTextView.setText(String.format(Locale.US, "±: %d", progress));
  }

  /*
   * Tolerance affects the amount of simplification, and it's specified in the same metric as the
   * point coordinates. For coordinates, that means multiplying the 0..100 range progress from the
   * SeekBar by 0.0001.
   */
  private static double getToleranceAdjusted(int progress) {
    return 0.0001 * progress;
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

  private class DrawGeoJson extends AsyncTask<Void, Void, List<Position>> {
    @Override
    protected List<Position> doInBackground(Void... voids) {

      List<Position> points = new ArrayList<>();

      try {
        // Load GeoJSON file
        InputStream inputStream = getAssets().open("matched_route.geojson");
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
              Position position = Position.fromCoordinates(coord.getDouble(0), coord.getDouble(1));
              points.add(position);
            }
          }
        }
      } catch (Exception exception) {
        Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
      }

      return points;
    }

    @Override
    protected void onPostExecute(List<Position> points) {
      super.onPostExecute(points);

      route = points;

      drawBeforeSimplify(points);
      drawSimplify(points, toleranceValue, false);
    }
  }

  private void drawBeforeSimplify(List<Position> points) {
    totalRoutePoints = points.size();

    LatLng[] pointsArray = new LatLng[points.size()];
    for (int i = 0; i < points.size(); i++) {
      pointsArray[i] = new LatLng(points.get(i).getLatitude(), points.get(i).getLongitude());
    }

    map.addPolyline(new PolylineOptions()
      .add(pointsArray)
      .color(Color.parseColor("#8a8acb"))
      .width(4));
  }

  private void drawSimplify(List<Position> points, int progress, boolean quality) {
    if (simplifiedRoute != null) {
      map.removeAnnotation(simplifiedRoute);
    }

    Position[] before = new Position[points.size()];
    for (int i = 0; i < points.size(); i++) {
      before[i] = points.get(i);
    }

    Log.v(TAG, String.format(Locale.US,
      "Tolerance value: %.4f; quality: %b", getToleranceAdjusted(progress), quality));
    Position[] after = PolylineUtils.simplify(before, getToleranceAdjusted(progress), quality);

    int simplifiedRoutePoints = after.length;

    LatLng[] result = new LatLng[after.length];
    for (int i = 0; i < after.length; i++) {
      result[i] = new LatLng(after[i].getLatitude(), after[i].getLongitude());
    }

    simplifiedRoute = map.addPolyline(new PolylineOptions()
      .add(result)
      .color(Color.parseColor("#3bb2d0"))
      .width(4));

    pointValueTextView.setText(
      String.format(Locale.US, "%s/%s", simplifiedRoutePoints, totalRoutePoints));
  }
}
