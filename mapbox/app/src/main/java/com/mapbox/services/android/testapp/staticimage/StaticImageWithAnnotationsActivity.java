package com.mapbox.services.android.testapp.staticimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.Constants;
import com.mapbox.services.android.testapp.R;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.api.staticimage.v1.models.StaticMarkerAnnotation;
import com.mapbox.services.api.staticimage.v1.models.StaticPolylineAnnotation;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.squareup.picasso.Picasso;

public class StaticImageWithAnnotationsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_static_image_annotations);

    ImageView veniceImageView = (ImageView) findViewById(R.id.veniceImageView);
    ImageView parisImageView = (ImageView) findViewById(R.id.parisImageView);
    ImageView londonImageView = (ImageView) findViewById(R.id.londonImageView);

    StaticMarkerAnnotation firstStaticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_SMALL)
      .setPosition(Position.fromCoordinates(-122.46589, 37.77343))
      .setColor("9ed4bd")
      .setLabel("a")
      .build();

    StaticMarkerAnnotation secondStaticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setName(Constants.PIN_SMALL)
      .setPosition(Position.fromCoordinates(-122.42816, 37.75965))
      .setColor("000")
      .setLabel("b")
      .build();

    StaticPolylineAnnotation staticPolylineAnnotation = new StaticPolylineAnnotation.Builder()
      .setPolyline("%7DrpeFxbnjVsFwdAvr@cHgFor@jEmAlFmEMwM_FuItCkOi@wc@bg@wBSgM")
      .setStrokeOpacity(0.5f)
      .setStrokeColor("f44")
      .setStrokeWidth(5)
      .build();

    MapboxStaticImage veniceStaticImage = new MapboxStaticImage.Builder()
      .setAccessToken(Mapbox.getAccessToken())
      .setStyleId("streets-v10")
      .setStaticMarkerAnnotations(firstStaticMarkerAnnotation, secondStaticMarkerAnnotation)
      .setStaticPolylineAnnotations(staticPolylineAnnotation)
      .setAuto(true)
      .setWidth(320)
      .setHeight(320)
      .setRetina(true)
      .build();

    Picasso.with(this).load(veniceStaticImage.getUrl().toString()).into(veniceImageView);

    StaticMarkerAnnotation customStaticMarkerAnnotation = new StaticMarkerAnnotation.Builder()
      .setUrl("https://www.mapbox.com/img/rocket.png")
      .setPosition(Position.fromCoordinates(2.29450, 48.85826))
      .build();

    MapboxStaticImage parisStaticImage = new MapboxStaticImage.Builder()
      .setAccessToken(Mapbox.getAccessToken())
      .setStyleId(Constants.MAPBOX_STYLE_OUTDOORS)
      .setStaticMarkerAnnotations(customStaticMarkerAnnotation)
      .setLat(48.85826)
      .setLon(2.29450)
      .setZoom(16)
      .setPitch(20)
      .setBearing(60)
      .setWidth(320)
      .setHeight(320)
      .setRetina(true)
      .build();

    Picasso.with(this).load(parisStaticImage.getUrl().toString()).into(parisImageView);

    MapboxStaticImage londonStaticImage = new MapboxStaticImage.Builder()
      .setAccessToken(Mapbox.getAccessToken())
      .setStyleId(Constants.MAPBOX_STYLE_STREETS)
      .setLat(51.5062)
      .setLon(-0.0756)
      .setZoom(14)
      .setGeoJson(Feature.fromGeometry(Point.fromCoordinates(Position.fromCoordinates(-0.0756, 51.5062))))
      .setWidth(320)
      .setHeight(320)
      .setRetina(true)
      .build();

    Picasso.with(this).load(londonStaticImage.getUrl().toString()).into(londonImageView);
  }
}
