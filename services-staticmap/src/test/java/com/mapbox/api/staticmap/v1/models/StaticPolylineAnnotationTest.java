package com.mapbox.api.staticmap.v1.models;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

public class StaticPolylineAnnotationTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  public void sanity() throws Exception {
    StaticPolylineAnnotation staticPolylineAnnotation = StaticPolylineAnnotation.builder()
      .polyline("test-polylineAnnotation")
      .build();

    System.err.print(">>>>>>>>  url=" +staticPolylineAnnotation.url());

    // path-strokeWidth+strokeColor-strokeOpacity+fillColor-fillOpacity(polylineName)
    // assertTrue(staticPolylineAnnotation.url().contains("path-10.0+646464-0.5+323232-0.75(test-polylineAnnotation)"));
  }

  @Test
  public void polyline_ispresent() throws Exception {
    exception.expect(IllegalStateException.class);
    StaticPolylineAnnotation staticPolylineAnnotation = StaticPolylineAnnotation.builder()
      .build();
  }

  @Test
  public void url_annotations() throws Exception {
    StaticPolylineAnnotation staticPolylineAnnotation = StaticPolylineAnnotation.builder()
      .polyline("test-polylineAnnotation")
      .strokeColor(100, 100, 100)
      .strokeWidth(10.0)
      .strokeOpacity(0.5f)
      .fillColor(50, 50, 50)
      .fillOpacity(0.75f)
      .build();

    // path-{strokeWidth}+{strokeColor}-{strokeOpacity}+{fillColor}-{fillOpacity}({polyline})
    assertTrue(staticPolylineAnnotation.url().contains("path-10.0+646464-0.5+323232-0.75(test-polylineAnnotation)"));
  }

  @Test
  public void url_annotations_stroke() throws Exception {
    StaticPolylineAnnotation staticPolylineAnnotation = StaticPolylineAnnotation.builder()
      .polyline("test-polylineAnnotation")
      .strokeColor(100, 100, 100)
      .strokeWidth(10.0)
      .strokeOpacity(0.5f)
      .build();

    // path-{strokeWidth}+{strokeColor}-{strokeOpacity}+{fillColor}-{fillOpacity}({polyline})
    assertTrue(staticPolylineAnnotation.url().contains("path-10.0+646464-0.5(test-polylineAnnotation)"));
  }

  @Test
  public void url_annotations_fill() throws Exception {
    StaticPolylineAnnotation staticPolylineAnnotation = StaticPolylineAnnotation.builder()
      .polyline("test-polylineAnnotation")
      .fillColor(50, 50, 50)
      .fillOpacity(0.75f)
      .build();

    // path-{strokeWidth}+{strokeColor}-{strokeOpacity}+{fillColor}-{fillOpacity}({polyline})
    assertTrue(staticPolylineAnnotation.url().contains("path+323232-0.75(test-polylineAnnotation)"));
  }
}
