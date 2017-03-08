package com.mapbox.services.android.testapp.staticimage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;

public class StaticImageActivity extends AppCompatActivity {

  // All default Mapbox styles
  public static final String[] MAPBOX_STYLES = {
    Constants.MAPBOX_STYLE_STREETS, Constants.MAPBOX_STYLE_LIGHT,
    Constants.MAPBOX_STYLE_DARK, Constants.MAPBOX_STYLE_OUTDOORS,
    Constants.MAPBOX_STYLE_SATELLITE, Constants.MAPBOX_STYLE_SATELLITE_HYBRID};

  // The ten venues for Copa America Centenario
  public static final double[][] PLACES = {
    {47.5952, -122.3316}, // CenturyLink Field
    {41.8625, -87.616667}, // Soldier Field
    {42.090944, -71.264344}, // Gillette Stadium
    {40.813611, -74.074444}, // MetLife Stadium
    {37.403, -121.97}, // Levi's Stadium
    {39.900833, -75.1675}, // Lincoln Financial Field
    {28.539167, -81.402778}, // Orlando Citrus Bowl
    {29.684722, -95.410833}, // NRG Stadium
    {33.5275, -112.2625}, // University of Phoenix Stadium
    {34.161389, -118.1675}}; // Rose Bowl

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_static_image);

    // Build the dataset using the MapboxStaticImage API
    String[] dataset = buildDataset();

    // Recycler view
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery_view);

    // Use a GridLayoutManager with 2/3 columns
    int cols = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, cols);
    recyclerView.setLayoutManager(layoutManager);

    // Specify an adapter
    RecyclerView.Adapter adapter = new GalleryAdapter(this, dataset);
    recyclerView.setAdapter(adapter);
  }

  private String[] buildDataset() {
    String[] dataset = new String[PLACES.length * MAPBOX_STYLES.length];

    // Set @2x when adequate based on pixel density
    boolean isRetina = (getResources().getDisplayMetrics().density >= 2);

    int i = 0;
    for (double[] place : PLACES) {
      for (String style : MAPBOX_STYLES) {
        String imageUrl = getImageUrl(style, place, isRetina);
        dataset[i] = imageUrl;
        i++;
      }
    }

    return dataset;
  }

  private String getImageUrl(String style, double[] place, boolean isRetina) {
    MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
      .setAccessToken(Utils.getMapboxAccessToken(this))
      .setUsername(Constants.MAPBOX_USER)
      .setStyleId(style)
      .setLon(place[1])
      .setLat(place[0])
      .setZoom(16)
      .setBearing(45)
      .setPitch(60)
      .setWidth(500)
      .setHeight(500)
      .setRetina(isRetina)
      .build();
    return staticImage.getUrl().toString();
  }
}
