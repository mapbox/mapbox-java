package com.mapbox.services.android.testapp.distance;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.distance.v1.MapboxDistance;
import com.mapbox.services.api.distance.v1.models.DistanceResponse;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textFont;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textHaloColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textHaloWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textMaxWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textPadding;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textTranslate;

public class DistanceActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

  private static final String TAG = "DistanceActivity";
  private static final String KEY_DURATION = "duration";
  private static final String KEY_NAME = "name";

  private MapView mapView;
  private MapboxMap mapboxMap;
  private final List<Position> coordinates = new ArrayList<>();
  private final List<Feature> restaurants = new ArrayList<>();

  private final List<Feature> features = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_distance);

    addRestaurants();

    for (int i = 0; i < restaurants.size(); i++) {
      coordinates.add((Position) restaurants.get(i).getGeometry().getCoordinates());
    }

    mapView = (MapView) findViewById(R.id.mapview);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Override
  public void onMapReady(MapboxMap mapboxMap) {
    this.mapboxMap = mapboxMap;
    mapboxMap.setOnMapClickListener(this);

    for (int i = 0; i < restaurants.size(); i++) {
      FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[] {restaurants.get(i)});
      Source geoJsonSource = new GeoJsonSource(
        restaurants.get(i).getStringProperty(KEY_NAME) + "-source", featureCollection
      );
      mapboxMap.addSource(geoJsonSource);
    }

    addMarkers();
    updateLayers();
  }

  @Override
  public void onMapClick(@NonNull LatLng point) {

    features.clear();

    final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
    for (int i = 0; i < restaurants.size(); i++) {
      features.addAll(mapboxMap.queryRenderedFeatures(
        pixel,
        restaurants.get(i).getStringProperty(KEY_NAME) + "-layer"
      ));
    }

    features.addAll(mapboxMap.queryRenderedFeatures(pixel, "circle-layer"));

    if (features.size() > 0) {
      callDistanceApi();
    }
  }

  private void callDistanceApi() {

    try {
      MapboxDistance client = new MapboxDistance.Builder()
        .setAccessToken(Utils.getMapboxAccessToken(DistanceActivity.this))
        .setCoordinates(coordinates)
        .setProfile(DirectionsCriteria.PROFILE_WALKING)
        .build();

      client.enqueueCall(new Callback<DistanceResponse>() {
        @Override
        public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {

          // Check that the distance API response is "OK".
          if (response.code() == 200) {

            if (restaurants.size() <= 0) {
              return;
            }

            int feature = 0;
            for (int i = 0; i < restaurants.size(); i++) {
              if (restaurants.get(i).getProperty(KEY_NAME).getAsString().equals(
                features.get(0).getStringProperty(KEY_NAME))) {
                feature = i;
              }
            }

            for (int i = 0; i < restaurants.size(); i++) {
              // Get the json object and replace the duration property with the updated time. We convert the API's
              // second value to minutes.
              JsonObject jsonObject = restaurants.get(i).getProperties().getAsJsonObject();
              jsonObject.addProperty(KEY_DURATION, (response.body().getDurations()[feature][i] / 60));

              // It is necessary to update the geojson source now along with the layers.
              FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[] {restaurants.get(i)});
              GeoJsonSource source = mapboxMap.getSourceAs(restaurants.get(i).getStringProperty(KEY_NAME) + "-source");
              if (source != null) {
                source.setGeoJson(featureCollection);
              }
            }
            updateLayers();
          }
        }

        @Override
        public void onFailure(Call<DistanceResponse> call, Throwable throwable) {
          Log.e(TAG, "MapboxDistance error: " + throwable.getMessage());
        }
      });


    } catch (ServicesException servicesException) {
      Log.e(TAG, "MapboxDistance error: " + servicesException.getMessage());
      servicesException.printStackTrace();
    }

  }

  private void addMarkers() {
    for (int i = 0; i < restaurants.size(); i++) {
      CircleLayer circleLayer = new CircleLayer(
              restaurants.get(i).getStringProperty("name") + "-circle-layer",
        restaurants.get(i).getStringProperty("name") + "-source"
      ).withProperties(
        circleColor(Color.parseColor("#e55e5e")),
        circleRadius(10f)
      );

      mapboxMap.addLayer(circleLayer);

      SymbolLayer nameLayer = new SymbolLayer(
        restaurants.get(i).getStringProperty("name") + "-layer-name",
        restaurants.get(i).getStringProperty("name") + "-source"
      ).withProperties(
        textSize(14f),
        textFont(new String[] {"Open Sans Bold", "Arial Unicode MS Bold"}),
        textHaloColor(Color.WHITE),
        textHaloWidth(0.75f),
        textIgnorePlacement(true),
        textPadding(0f),
        textTranslate(new Float[] {14f, 0f}),
        textAnchor(Property.TEXT_ANCHOR_BOTTOM_LEFT),
        textField(restaurants.get(i).getStringProperty("name")),
        textMaxWidth(8f)
      );
      mapboxMap.addLayer(nameLayer);
    }
  }

  private void updateLayers() {

    for (int i = 0; i < restaurants.size(); i++) {

      if (mapboxMap.getLayer(restaurants.get(i).getStringProperty("name" + "-layer")) != null) {
        mapboxMap.removeLayer(restaurants.get(i).getStringProperty("name") + "-layer");
      }


      SymbolLayer marker = new SymbolLayer(
        restaurants.get(i).getStringProperty("name") + "-layer",
        restaurants.get(i).getStringProperty("name") + "-source"
      ).withProperties(
        textSize(12f),
        textHaloColor(Color.WHITE),
        textTranslate(new Float[] {14f, 0f}),
        textHaloWidth(0.75f),
        textIgnorePlacement(true),
        textPadding(0f),
        textAnchor(Property.TEXT_ANCHOR_TOP_LEFT),
        textField("{duration}" + " min"),
        textMaxWidth(8f)
      );
      mapboxMap.addLayer(marker);
    }
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
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
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
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  private void addRestaurants() {

    Feature franklinBarbecue = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.731250, 30.270168))
    );
    franklinBarbecue.addStringProperty(KEY_NAME, "Franklin Barbecue");
    franklinBarbecue.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(franklinBarbecue); // Franklin Barbecue

    Feature lamberts = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.747905, 30.265222))
    );
    lamberts.addStringProperty(KEY_NAME, "Lamberts");
    lamberts.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(lamberts); // Lamberts

    Feature stubbsBarbq = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.736278, 30.268506))
    );
    stubbsBarbq.addStringProperty(KEY_NAME, "Stubb's Bar-B-Q");
    stubbsBarbq.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(stubbsBarbq); // Stubb's Bar-B-Q

    Feature stilesSwitch = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.721459, 30.334567))
    );
    stilesSwitch.addStringProperty(KEY_NAME, "Stiles Switch");
    stilesSwitch.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(stilesSwitch); // Stiles Switch

    Feature laBarbecue = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.724013, 30.257052))
    );
    laBarbecue.addStringProperty(KEY_NAME, "La Barbecue");
    laBarbecue.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(laBarbecue); // La Barbecue

    Feature freedmens = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.747936, 30.288418))
    );
    freedmens.addStringProperty(KEY_NAME, "Freedmen's");
    freedmens.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(freedmens); // Freedmen's

    Feature micklethwait = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.725124, 30.268580))
    );
    micklethwait.addStringProperty(KEY_NAME, "Micklethwait Craft Meats");
    micklethwait.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(micklethwait); // Micklethwait Craft Meats

    Feature kerlinBbq = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.725714, 30.257843))
    );
    kerlinBbq.addStringProperty(KEY_NAME, "Kerlin BBQ");
    kerlinBbq.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(kerlinBbq); // Kerlin BBQ

    Feature countryLine = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.785535, 30.357089))
    );
    countryLine.addStringProperty(KEY_NAME, "County Line");
    countryLine.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(countryLine); // County Line

    Feature ironWorksBbq = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.739009, 30.262272))
    );
    ironWorksBbq.addStringProperty(KEY_NAME, "Iron Works BBQ");
    ironWorksBbq.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(ironWorksBbq); // Iron Works BBQ

    Feature houseParkBarbecue = Feature.fromGeometry(
      Point.fromCoordinates(Position.fromCoordinates(-97.750397, 30.276797))
    );
    houseParkBarbecue.addStringProperty(KEY_NAME, "House Park Barbecue");
    houseParkBarbecue.addNumberProperty(KEY_DURATION, 0);
    restaurants.add(houseParkBarbecue); // House Park Barbecue

  }
}
