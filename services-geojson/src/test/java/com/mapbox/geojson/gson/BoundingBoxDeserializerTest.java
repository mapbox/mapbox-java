package com.mapbox.geojson.gson;


import com.google.gson.Gson;
import com.mapbox.geojson.BoundingBox;

import org.junit.Test;

public class BoundingBoxDeserializerTest {

  @Test
  public void bboxDeserializer_handlesOnlyOneAltitudeCorrectly() throws Exception {
    Gson gson = new Gson();
    BoundingBox bbox = gson.fromJson("bbox:[1.0,2.0,3.0,4.0,5.0]", BoundingBox.class);
    // TODO

  }
}
