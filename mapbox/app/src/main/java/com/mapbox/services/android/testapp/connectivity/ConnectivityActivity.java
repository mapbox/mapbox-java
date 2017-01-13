package com.mapbox.services.android.testapp.connectivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.services.android.telemetry.connectivity.ConnectivityListener;
import com.mapbox.services.android.telemetry.connectivity.ConnectivityReceiver;
import com.mapbox.services.android.testapp.R;

public class ConnectivityActivity extends AppCompatActivity
  implements ConnectivityListener{

  private static final String LOG_TAG = ConnectivityActivity.class.getSimpleName();
  private ConnectivityReceiver connectivityReceiver;

  private Button buttonAuto;
  private Button buttonYes;
  private Button buttonNo;
  private TextView textUpdate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_connectivity);
    textUpdate = (TextView) findViewById(R.id.text_update);
    setupButtons();

    // Set up the ConnectivityReceiver
    Log.d(LOG_TAG, "Setting up ConnectivityReceiver.");
    connectivityReceiver = new ConnectivityReceiver(getApplicationContext());
    connectivityReceiver.addConnectivityListener(this);
    setInitialState();
  }

  private void setupButtons() {
    buttonAuto = (Button) findViewById(R.id.button_auto);
    buttonAuto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setInitialState();
      }
    });

    buttonYes = (Button) findViewById(R.id.button_yes);
    buttonYes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonAuto.setEnabled(true);
        buttonYes.setEnabled(false);
        buttonNo.setEnabled(true);
        connectivityReceiver.setConnectedFlag(true);
      }
    });

    buttonNo = (Button) findViewById(R.id.button_no);
    buttonNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonAuto.setEnabled(true);
        buttonYes.setEnabled(true);
        buttonNo.setEnabled(false);
        connectivityReceiver.setConnectedFlag(false);
      }
    });
  }

  private void setInitialState() {
    buttonAuto.setEnabled(false);
    buttonYes.setEnabled(true);
    buttonNo.setEnabled(true);
    connectivityReceiver.setConnectedFlag(null);
  }

  @Override
  protected void onStart() {
    super.onStart();

    Log.d(LOG_TAG, "Requesting connectivity updates.");
    connectivityReceiver.requestConnectivityUpdates();
  }

  @Override
  protected void onStop() {
    super.onStop();

    Log.d(LOG_TAG, "Removing connectivity updates.");
    connectivityReceiver.removeConnectivityUpdates();
  }

  @Override
  public void onConnectivityChanged(boolean connected) {
    textUpdate.setText(String.format("Connectivity changed to %b.", connected));
  }
}
