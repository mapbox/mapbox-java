package com.mapbox.services.android.testapp.icons;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mapbox.services.android.testapp.R;

public class DirectionsIconsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_directions_icons);

    // The recycler view set up
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.directions_icon_recycler_view);
    recyclerView.setHasFixedSize(true);

    // Use a grid layout manager
    int cols = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, cols);
    recyclerView.setLayoutManager(layoutManager);

    // Specify an adapter
    RecyclerView.Adapter adapter = new IconAdapter(DirectionsList.ICONS);
    recyclerView.setAdapter(adapter);
  }

}
