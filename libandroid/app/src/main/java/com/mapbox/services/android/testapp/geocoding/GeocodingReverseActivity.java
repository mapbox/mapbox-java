package com.mapbox.services.android.testapp.geocoding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class GeocodingReverseActivity extends AppCompatActivity {

    private static final String LOG_TAG = "GeocodingReverse";

    private MapView mapView;
    private MapboxMap mapboxMap;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding_reverse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.message);
        setMessage("Tap the map to trigger the reverse geocoder.");

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                // Click listener
                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {
                        setMessage("Geocoding...");
                        mapboxMap.removeAnnotations();
                        mapboxMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Your finger is here"));
                        try {
                            geocode(point);
                        } catch (ServicesException exception) {
                            setMessage("Geocoding failed: " + exception.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /*
     * Forward geocoding
     */

    private void geocode(LatLng point) throws ServicesException {
        Position position = Position.fromCoordinates(point.getLongitude(), point.getLatitude());
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken(Utils.getMapboxAccessToken(this))
                .setCoordinates(position)
                .setGeocodingType(GeocodingCriteria.TYPE_POI)
                .build();

        client.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().getFeatures();
                if (results.size() > 0) {
                    String placeName = results.get(0).getPlaceName();
                    setSuccess(placeName);
                } else {
                    setMessage("No results");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                setError(throwable.getMessage());
            }
        });
    }

    /*
     * Update text view
     */

    private void setMessage(String message) {
        Log.d(LOG_TAG, "Message: " + message);
        textView.setText(message);
    }

    private void setSuccess(String placeName) {
        Log.d(LOG_TAG, "Place name: " + placeName);
        textView.setText(placeName);
    }

    private void setError(String message) {
        Log.e(LOG_TAG, "Error: " + message);
        textView.setText("Error: " + message);
    }

}
