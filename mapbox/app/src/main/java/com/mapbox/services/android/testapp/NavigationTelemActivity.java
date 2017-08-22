package com.mapbox.services.android.testapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mapbox.services.android.telemetry.MapboxTelemetry;
import com.mapbox.services.android.telemetry.navigation.MapboxNavigationEvent;

import java.util.Hashtable;

public class NavigationTelemActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation_telem);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Hashtable<String, Object> map = MapboxNavigationEvent.buildDepartEvent(
          "sdkIdentifier",
          "sdkVersion",
          "sessionIdentifier",
          0.0,
          0.0,
          "geometry",
          "profile",
          0,
          0,
          0,
          false,
          "originalRequestIdentifier",
          "requestIdentifier",
          "originalGeometry",
          0,
          0,
          "audioType",
          0,
          0
        );

        MapboxTelemetry.getInstance().pushEvent(map);
      }
    });
  }

}
