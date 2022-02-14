package com.mapbox.api.directions.v5.models;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.geojson.Point;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RouteOptionsExcludeTest {
  @Test
  public void parseTwoPointsWithTwoCriteriaMixed() {
    String excludeQueryParameter = "point(4234.23423 -8482.49523445),motorway,point(-444.0 999.111111),ferry";
    Exclude exclude = Exclude.fromUrlQueryParameter(excludeQueryParameter);
    assertEquals(
        Arrays.asList(
            Point.fromLngLat(4234.23423, -8482.49523445),
            Point.fromLngLat(-444.0, 999.111111)
        ),
        exclude.points()
    );
    assertEquals(
        Arrays.asList(
            DirectionsCriteria.EXCLUDE_MOTORWAY,
            DirectionsCriteria.EXCLUDE_FERRY
        ),
        exclude.criteria()
    );
  }

  @Test
  public void parseNull() {
    Exclude exclude = Exclude.fromUrlQueryParameter(null);
    assertNull(exclude);
  }

  @Test
  public void parseEmptyParameter() {
    Exclude exclude = Exclude.fromUrlQueryParameter("");
    assertNull(exclude);
  }

  @Test
  public void parseOnePoint() {
    String excludeQueryParameter = "point(555.5 2222.0)";
    Exclude exclude = Exclude.fromUrlQueryParameter(excludeQueryParameter);
    assertEquals(
        Arrays.asList(
            Point.fromLngLat(555.5, 2222.0)
        ),
        exclude.points()
    );
    assertNull(exclude.criteria());
  }

  @Test
  public void parseOneCriteria() {
    String excludeQueryParameter = DirectionsCriteria.EXCLUDE_TUNNEL;
    Exclude exclude = Exclude.fromUrlQueryParameter(excludeQueryParameter);
    assertEquals(
        Arrays.asList(
            DirectionsCriteria.EXCLUDE_TUNNEL
        ),
        exclude.criteria()
    );
    assertNull(exclude.points());
  }

  @Test
  public void parsePointWithAFewSpaces() {
    String invalidPoint = "point(3.0      4.0)";
    Exclude exclude = Exclude.fromUrlQueryParameter(invalidPoint);
    assertEquals(
        Arrays.asList(
            Point.fromLngLat(3.0, 4.0)
        ),
        exclude.points()
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePointWithTextInsteadOfNumbers() {
    String invalidPoint = "point(one two)";
    Exclude.fromUrlQueryParameter(invalidPoint);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePointWithSemicolonDelimiter() {
    String invalidPoint = "point(5.0;7.0)";
    Exclude.fromUrlQueryParameter(invalidPoint);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsePointWithCommaDelimiter() {
    String invalidPoint = "point(5.0,7.0)";
    Exclude.fromUrlQueryParameter(invalidPoint);
  }

  @Test
  public void singleCriteriaToQueryUrl() {
    String queryParameter = Exclude.builder()
        .criteria(Arrays.asList(DirectionsCriteria.EXCLUDE_FERRY))
        .build()
        .toUrlQueryParameter();
    assertEquals(DirectionsCriteria.EXCLUDE_FERRY, queryParameter);
  }

  @Test
  public void criteriaListToQueryUrl() {
    String queryParameter = Exclude.builder()
        .criteria(Arrays.asList(DirectionsCriteria.EXCLUDE_FERRY, DirectionsCriteria.EXCLUDE_TOLL))
        .build()
        .toUrlQueryParameter();
    assertEquals("ferry,toll", queryParameter);
  }

  @Test
  public void parseUnexpectedTypeOfParameter() {
    Exclude exclude = Exclude.fromUrlQueryParameter("new(test)");
    assertNull(exclude);
  }

  @Test
  public void parseUnexpectedTypeOfParameterMixedWithPointAndCriteria() {
    Exclude exclude = Exclude.fromUrlQueryParameter("ferry,new(test),point(0.5 -0.5)");
    assertNotNull(exclude);
    assertEquals(1, exclude.criteria().size());
    assertEquals(1, exclude.points().size());
  }
}
