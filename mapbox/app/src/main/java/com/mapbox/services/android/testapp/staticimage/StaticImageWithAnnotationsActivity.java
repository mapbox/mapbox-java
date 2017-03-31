package com.mapbox.services.android.testapp.staticimage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.android.testapp.Utils;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.api.staticimage.v1.models.Marker;

import java.util.ArrayList;

public class StaticImageWithAnnotationsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "StaticImageAnnotations";

    // All default Mapbox styles
    public static final String[] MAPBOX_STYLES = {
            Constants.MAPBOX_STYLE_STREETS, Constants.MAPBOX_STYLE_LIGHT,
            Constants.MAPBOX_STYLE_DARK};

    // The ten venues for Copa America Centenario
    public static final double[][] PLACES = {
            {38.9126115, -77.0316434}, // logan circle
            {38.9136582, -77.0294591}, // u street music hall
            {38.9131907, -77.0326778} // mapbox
            };
    // shape and size of markers
    public static final String[] MARKER_NAMES = {
            "pin-s","pin-m","pin-l"
    };

    // few sample labels for markers
    public static final String[] MARKER_LABELS={
            "a","b","d","z","9","0","3"
    };

    // few sample colors for markers
    public static final String[] MARKER_COLORS={
            "ff6666","4d4dff","666600","0099cc"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_image_with_annotations);

        // Build the dataset using the MapboxStaticImage API
        String[] dataset = buildDataset();

        // Recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // Specify an adapter
        RecyclerView.Adapter adapter = new GalleryAdapter(this, dataset);
        recyclerView.setAdapter(adapter);

    }

    private String[] buildDataset() {
        String[] dataset = new String[PLACES.length];

        // Set @2x when adequate based on pixel density
        boolean isRetina = (getResources().getDisplayMetrics().density >= 2);

        int i = 0;
        for (double[] place : PLACES) {

                String imageUrl = getImageUrl(MAPBOX_STYLES[i], place, isRetina);
                Log.d(LOG_TAG, "image url: " + imageUrl);
                dataset[i] = imageUrl;
                i++;

        }

        return dataset;
    }

    private String getImageUrl(String style, double[] place, boolean isRetina) {
        //create markers with different combination of name, size, labels and color.
        ArrayList<Marker> markers = createMarker();

        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
                .setAccessToken(Utils.getMapboxAccessToken(this))
                .setUsername(Constants.MAPBOX_USER)
                .setStyleId(style)
                .setMarker(markers)
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

    private ArrayList<Marker> createMarker(){

         ArrayList<Marker> markers = new ArrayList<>();
        for(int i=0; i<3;i++){
            Marker marker = new Marker();
            marker.setName(MARKER_NAMES[i]);
            marker.setLabel(MARKER_LABELS[i]);
            marker.setColor(MARKER_COLORS[i]);
            marker.setLon(PLACES[i][1]);
            marker.setLat(PLACES[i][0]);

            markers.add(marker);
        }
        return markers;

    }

}
