package com.mapbox.services.android.testapp.icons;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mapbox.services.android.testapp.R;

public class DirectionsIconsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_icons);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // The recycler view set up
        recyclerView = (RecyclerView) findViewById(R.id.directions_icon_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Use a grid layout manager
        int cols = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
        layoutManager = new GridLayoutManager(this, cols);
        recyclerView.setLayoutManager(layoutManager);

        // Specify an adapter
        adapter = new IconAdapter(DirectionsList.ICONS);
        recyclerView.setAdapter(adapter);
    }

}
