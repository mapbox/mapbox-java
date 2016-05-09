package com.mapbox.services.android.testapp.icons;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mapbox.services.android.testapp.R;

public class MakiIconsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maki_icons);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // The recycler view set up
        mRecyclerView = (RecyclerView) findViewById(R.id.maki_icon_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // Use a grid layout manager
        int cols = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3);
        mLayoutManager = new GridLayoutManager(this, cols);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter
        mAdapter = new IconAdapter(MakiList.ICONS_15);
        mRecyclerView.setAdapter(mAdapter);
    }

}
