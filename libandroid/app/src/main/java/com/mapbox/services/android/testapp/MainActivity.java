package com.mapbox.services.android.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mapbox.services.android.BuildConfig;
import com.mapbox.services.android.testapp.directions.DirectionsV4Activity;
import com.mapbox.services.android.testapp.directions.DirectionsV5Activity;
import com.mapbox.services.android.testapp.geocoding.GeocodingReverseActivity;
import com.mapbox.services.android.testapp.geocoding.GeocodingServiceActivity;
import com.mapbox.services.android.testapp.geocoding.GeocodingWidgetActivity;
import com.mapbox.services.android.testapp.icons.DirectionsIconsActivity;
import com.mapbox.services.android.testapp.icons.MakiIconsActivity;
import com.mapbox.services.android.testapp.staticimage.StaticImageActivity;
import com.mapbox.services.android.testapp.utils.MapMatchingActivity;
import com.mapbox.services.android.testapp.utils.SimplifyPolylineActivity;
import com.mapbox.services.android.testapp.turf.TurfBearingActivity;
import com.mapbox.services.android.testapp.turf.TurfDestinationActivity;
import com.mapbox.services.android.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This activity shows how to use PermissionsUtils to request location permissions
 * from the user. It loads all the sample activities using a RecyclerView.
 */
public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final static List<SampleItem> samples = new ArrayList<>(Arrays.asList(
            new SampleItem("Directions v5", "", DirectionsV5Activity.class),
            new SampleItem("Directions v4", "", DirectionsV4Activity.class),
            new SampleItem("Directions icons", "", DirectionsIconsActivity.class),
            new SampleItem("Reverse geocoding", "", GeocodingReverseActivity.class),
            new SampleItem("Geocoding widget", "", GeocodingWidgetActivity.class),
            new SampleItem("Geocoding service", "", GeocodingServiceActivity.class),
            new SampleItem("Maki icons", "", MakiIconsActivity.class),
            new SampleItem("Static image", "", StaticImageActivity.class),
            new SampleItem("Simplify polyline", "", SimplifyPolylineActivity.class),
            new SampleItem("Map matching", "", MapMatchingActivity.class),
            new SampleItem("Turf bearing", "", TurfBearingActivity.class),
            new SampleItem("Turf destination", "", TurfDestinationActivity.class)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Debug information
        Log.d(LOG_TAG, "MAS version name: " + BuildConfig.VERSION_NAME);
        Log.d(LOG_TAG, "MAS version code: " + BuildConfig.VERSION_CODE);

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter
        mAdapter = new MainAdapter(samples);
        mRecyclerView.setAdapter(mAdapter);

        // Check for location permission
        if (!PermissionsUtils.isLocationGranted(this)) {
            mRecyclerView.setEnabled(false);
            PermissionsUtils.startPermissionFlow(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (PermissionsUtils.isRequestSuccessful(requestCode, permissions, grantResults)) {
            mRecyclerView.setEnabled(true);
        } else {
            PermissionsUtils.explainFallback(this);
        }
    }

    /*
     * Recycler view
     */

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

        private List<SampleItem> samples;

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView nameView;
            private TextView descriptionView;

            public ViewHolder(View v) {
                super(v);
                nameView = (TextView) v.findViewById(R.id.nameView);
                descriptionView = (TextView) v.findViewById(R.id.descriptionView);
            }
        }

        public MainAdapter(List<SampleItem> samples) {
            this.samples = samples;
        }

        @Override
        public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_main_feature, parent, false);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildLayoutPosition(v);
                    Intent intent = new Intent(v.getContext(), samples.get(position).getActivity());
                    startActivity(intent);
                }
            });

            return new ViewHolder(v);
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
