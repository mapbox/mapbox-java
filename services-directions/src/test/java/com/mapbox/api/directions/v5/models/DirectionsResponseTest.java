package com.mapbox.api.directions.v5.models;

import com.mapbox.core.TestUtils;
import com.mapbox.core.utils.TextUtils;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DirectionsResponseTest extends TestUtils {

  private static final String DIRECTIONS_V5_PRECISION6_FIXTURE = "directions_v5_precision_6.json";

  @Test
  public void sanity() throws Exception {
    DirectionsResponse response = DirectionsResponse.builder()
      .code("100")
      .routes(new ArrayList<DirectionsRoute>())
      .build();
    assertNotNull(response);
  }

  private final List<String> EXPECTED_DEFAULT_METHODS = new ArrayList<String>() {{
    add("equals");
    add("toString");
    add("hashCode");
    add("toBuilder");
    add("typeAdapter");
    add("fromJson");
    add("wait");
    add("getClass");
    add("notify");
    add("notifyAll");
    add("builder");
    add("compareTo");
  }};

  private final List<String> OPTIONAL_METHODS = new ArrayList<String>() {{
    add("message");
    add("annotation");
    add("ref");
    add("destinations");
    add("pronunciation");
    add("rotaryName");
    add("exits");
    add("rotaryPronunciation");
    add("exit");
    add("voiceInstructions");
    add("bannerInstructions");
    add("classes");
    add("in");
    add("out");
    add("name");
    add("routeOptions");
    add("lanes");
  }};

  @Test
  public void fromJson_correctlyBuildsFromJson() throws Exception {
    String json = loadJsonFixture(DIRECTIONS_V5_PRECISION6_FIXTURE);
    DirectionsResponse response = DirectionsResponse.fromJson(json);
    assertNotNull(response);
    assertEquals(1, response.routes().size());

    System.out.println("Start test");
    recursiveMethodIteration(response);
    System.out.println("End test");
  }

  private void recursiveMethodIteration(Object base) throws Exception {
    System.out.println("----------------------------------------------------------------------------");
    Class baseClass = base.getClass();
    System.out.println("Test output for " + baseClass.getSimpleName());
    Method[] methods = baseClass.getMethods();

    Object object;
    Class subClass;
    String methodName;
    String packageName;
    for (Method method : methods) {
      methodName = method.getName();
      if (!EXPECTED_DEFAULT_METHODS.contains(methodName) && !OPTIONAL_METHODS.contains(methodName)) {
        System.out.println("Testing out method " + methodName);
        try {
          object = method.invoke(base);
          subClass = object.getClass();
          packageName = subClass.getPackage().getName();
          System.out.println("Found " + object.getClass().getSimpleName());

          if (packageName.contains("com.mapbox.geojson")) {
            System.out.println("Skipping object -> geojson package");
            return;
          }

          if (subClass.isPrimitive()) {
            // todo add primitive validation
            System.out.println("Test OK!");
          } else if (object instanceof Number) {
            Number number = (Number) object;
            assertFalse(number.equals(0));
            System.out.println("Test OK!");
          } else if (object instanceof String) {
            assertNotNull(object);
            assertFalse(TextUtils.isEmpty((String) object));
            System.out.println("Test OK!");
          } else if (object instanceof Boolean) {
            // todo add boolean validation
            System.out.println("Test OK!");
          } else if (object instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) object;
            for (Object o : arrayList) {
              if(!(o instanceof String || o instanceof Number || o instanceof Boolean)) {
                recursiveMethodIteration(o);
              }
            }
          } else {
            recursiveMethodIteration(object);
          }
        } catch (IllegalAccessException exception) {
          System.out.println("IllegalAccessException, skipping test");
        }
      }
    }
  }

}
