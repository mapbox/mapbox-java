package com.mapbox.services.android.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.android.testapp.connectivity.ConnectivityActivity;
import com.mapbox.services.android.testapp.directions.DirectionsV5Activity;
import com.mapbox.services.android.testapp.geocoding.GeocodingReverseActivity;
import com.mapbox.services.android.testapp.geocoding.GeocodingServiceActivity;
import com.mapbox.services.android.testapp.geocoding.GeocodingWidgetActivity;
import com.mapbox.services.android.testapp.location.LocationEngineActivity;
import com.mapbox.services.android.testapp.optimizedtrip.OptimizedTripActivity;

import com.mapbox.services.android.testapp.staticimage.StaticImageActivity;
import com.mapbox.services.android.testapp.staticimage.StaticImageWithAnnotationsActivity;
import com.mapbox.services.android.testapp.turf.TurfBearingActivity;
import com.mapbox.services.android.testapp.turf.TurfDestinationActivity;
import com.mapbox.services.android.testapp.turf.TurfDistanceActivity;
import com.mapbox.services.android.testapp.turf.TurfInsideActivity;
import com.mapbox.services.android.testapp.turf.TurfLineSliceActivity;
import com.mapbox.services.android.testapp.turf.TurfMidpointActivity;
import com.mapbox.services.android.testapp.utils.MapMatchingActivity;
import com.mapbox.services.android.testapp.utils.SimplifyPolylineActivity;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This activity shows how to use PermissionsManager to request location permissions
 * from the user. It loads all the sample activities using a RecyclerView.
 */
public class MainActivity extends AppCompatActivity implements PermissionsListener {

  private static final String LOG_TAG = "MainActivity";

  private RecyclerView recyclerView;
  private PermissionsManager permissionsManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final List<SampleItem> samples = new ArrayList<>(Arrays.asList(
      new SampleItem(
        getString(R.string.title_optimized_trip),
        getString(R.string.description_optimized_trip),
        OptimizedTripActivity.class),
      new SampleItem(
        getString(R.string.title_location),
        getString(R.string.description_location),
        LocationEngineActivity.class),
      new SampleItem(
        getString(R.string.title_directions_v5),
        getString(R.string.description_directions_v5),
        DirectionsV5Activity.class
      ),
      new SampleItem(
        getString(R.string.title_reverse_geocoding),
        getString(R.string.description_reverse_geocoding),
        GeocodingReverseActivity.class
      ),
      new SampleItem(
        getString(R.string.title_geocoding_widget),
        getString(R.string.description_geocoding_widget),
        GeocodingWidgetActivity.class
      ),
      new SampleItem(
        getString(R.string.title_geocoding_service),
        getString(R.string.description_geocoding_service),
        GeocodingServiceActivity.class
      ), // TODO fix activity memory leak
      new SampleItem(
        getString(R.string.title_static_image),
        getString(R.string.description_static_image),
        StaticImageActivity.class
      ),
            new SampleItem(
                    getString(R.string.title_static_image_with_annotations),
                    getString(R.string.description_static_image_with_annotations),
                    StaticImageWithAnnotationsActivity.class
            ),
      new SampleItem(
        getString(R.string.title_simplify_polyline),
        getString(R.string.description_simplify_polyline),
        SimplifyPolylineActivity.class
      ),
      new SampleItem(
        getString(R.string.title_map_matching),
        getString(R.string.description_map_matching),
        MapMatchingActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_bearing),
        getString(R.string.description_turf_bearing),
        TurfBearingActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_destination),
        getString(R.string.description_turf_destination),
        TurfDestinationActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_distance),
        getString(R.string.description_turf_distance),
        TurfDistanceActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_line_slice),
        getString(R.string.description_turf_line_slice),
        TurfLineSliceActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_inside),
        getString(R.string.description_turf_inside),
        TurfInsideActivity.class
      ),
      new SampleItem(
        getString(R.string.title_turf_midpoint),
        getString(R.string.description_turf_midpoint),
        TurfMidpointActivity.class
      ),
      new SampleItem(
        getString(R.string.title_connectivity),
        getString(R.string.description_connectivity),
        ConnectivityActivity.class
      )
    ));

    // Debug information
    Log.d(LOG_TAG, "MAS version name: " + BuildConfig.VERSION_NAME);
    Log.d(LOG_TAG, "MAS version code: " + BuildConfig.VERSION_CODE);

    // Check Position warnings are working
    Log.d(LOG_TAG, "The following Position warnings are intentional:");
    Position.fromLngLat(185, 95);

    // RecyclerView
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);

    // Use a linear layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    // Specify an adapter
    RecyclerView.Adapter adapter = new MainAdapter(samples);
    recyclerView.setAdapter(adapter);

    // Check for location permission
    permissionsManager = new PermissionsManager(this);
    if (!PermissionsManager.areLocationPermissionsGranted(this)) {
      recyclerView.setEnabled(false);
      permissionsManager.requestLocationPermissions(this);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {
    Toast.makeText(this, "This app needs location permissions in order to show its functionality.",
      Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      recyclerView.setEnabled(true);
    } else {
      Toast.makeText(this, "You didn't grant location permissions.",
        Toast.LENGTH_LONG).show();
    }
  }

  /*
   * Recycler view
   */

  private class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<SampleItem> samples;

    class ViewHolder extends RecyclerView.ViewHolder {

      private TextView nameView;
      private TextView descriptionView;

      ViewHolder(View view) {
        super(view);
        nameView = (TextView) view.findViewById(R.id.nameView);
        descriptionView = (TextView) view.findViewById(R.id.descriptionView);
      }
    }

    MainAdapter(List<SampleItem> samples) {
      this.samples = samples;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.item_main_feature, parent, false);

      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int position = recyclerView.getChildLayoutPosition(view);
          Intent intent = new Intent(view.getContext(), samples.get(position).getActivity());
          startActivity(intent);
        }
      });

      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
      holder.nameView.setText(samples.get(position).getName());
      holder.descriptionView.setText(samples.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
      return samples.size();
    }
  }
}
