package com.mapbox.services.api.utils;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PolylineUtilsTest {

  private final static double DELTA = 1e-15;

  private Position[] points = {
    Position.fromCoordinates(224.55, 250.15), Position.fromCoordinates(226.91, 244.19), Position.fromCoordinates(233.31, 241.45), Position.fromCoordinates(234.98, 236.06),
    Position.fromCoordinates(244.21, 232.76), Position.fromCoordinates(262.59, 215.31), Position.fromCoordinates(267.76, 213.81), Position.fromCoordinates(273.57, 201.84),
    Position.fromCoordinates(273.12, 192.16), Position.fromCoordinates(277.62, 189.03), Position.fromCoordinates(280.36, 181.41), Position.fromCoordinates(286.51, 177.74),
    Position.fromCoordinates(292.41, 159.37), Position.fromCoordinates(296.91, 155.64), Position.fromCoordinates(314.95, 151.37), Position.fromCoordinates(319.75, 145.16),
    Position.fromCoordinates(330.33, 137.57), Position.fromCoordinates(341.48, 139.96), Position.fromCoordinates(369.98, 137.89), Position.fromCoordinates(387.39, 142.51),
    Position.fromCoordinates(391.28, 139.39), Position.fromCoordinates(409.52, 141.14), Position.fromCoordinates(414.82, 139.75), Position.fromCoordinates(427.72, 127.30),
    Position.fromCoordinates(439.60, 119.74), Position.fromCoordinates(474.93, 107.87), Position.fromCoordinates(486.51, 106.75), Position.fromCoordinates(489.20, 109.45),
    Position.fromCoordinates(493.79, 108.63), Position.fromCoordinates(504.74, 119.66), Position.fromCoordinates(512.96, 122.35), Position.fromCoordinates(518.63, 120.89),
    Position.fromCoordinates(524.09, 126.88), Position.fromCoordinates(529.57, 127.86), Position.fromCoordinates(534.21, 140.93), Position.fromCoordinates(539.27, 147.24),
    Position.fromCoordinates(567.69, 148.91), Position.fromCoordinates(575.25, 157.26), Position.fromCoordinates(580.62, 158.15), Position.fromCoordinates(601.53, 156.85),
    Position.fromCoordinates(617.74, 159.86), Position.fromCoordinates(622.00, 167.04), Position.fromCoordinates(629.55, 194.60), Position.fromCoordinates(638.90, 195.61),
    Position.fromCoordinates(641.26, 200.81), Position.fromCoordinates(651.77, 204.56), Position.fromCoordinates(671.55, 222.55), Position.fromCoordinates(683.68, 217.45),
    Position.fromCoordinates(695.25, 219.15), Position.fromCoordinates(700.64, 217.98), Position.fromCoordinates(703.12, 214.36), Position.fromCoordinates(712.26, 215.87),
    Position.fromCoordinates(721.49, 212.81), Position.fromCoordinates(727.81, 213.36), Position.fromCoordinates(729.98, 208.73), Position.fromCoordinates(735.32, 208.20),
    Position.fromCoordinates(739.94, 204.77), Position.fromCoordinates(769.98, 208.42), Position.fromCoordinates(779.60, 216.87), Position.fromCoordinates(784.20, 218.16),
    Position.fromCoordinates(800.24, 214.62), Position.fromCoordinates(810.53, 219.73), Position.fromCoordinates(817.19, 226.82), Position.fromCoordinates(820.77, 236.17),
    Position.fromCoordinates(827.23, 236.16), Position.fromCoordinates(829.89, 239.89), Position.fromCoordinates(851.00, 248.94), Position.fromCoordinates(859.88, 255.49),
    Position.fromCoordinates(865.21, 268.53), Position.fromCoordinates(857.95, 280.30), Position.fromCoordinates(865.48, 291.45), Position.fromCoordinates(866.81, 298.66),
    Position.fromCoordinates(864.68, 302.71), Position.fromCoordinates(867.79, 306.17), Position.fromCoordinates(859.87, 311.37), Position.fromCoordinates(860.08, 314.35),
    Position.fromCoordinates(858.29, 314.94), Position.fromCoordinates(858.10, 327.60), Position.fromCoordinates(854.54, 335.40), Position.fromCoordinates(860.92, 343.00),
    Position.fromCoordinates(856.43, 350.15), Position.fromCoordinates(851.42, 352.96), Position.fromCoordinates(849.84, 359.59), Position.fromCoordinates(854.56, 365.53),
    Position.fromCoordinates(849.74, 370.38), Position.fromCoordinates(844.09, 371.89), Position.fromCoordinates(844.75, 380.44), Position.fromCoordinates(841.52, 383.67),
    Position.fromCoordinates(839.57, 390.40), Position.fromCoordinates(845.59, 399.05), Position.fromCoordinates(848.40, 407.55), Position.fromCoordinates(843.71, 411.30),
    Position.fromCoordinates(844.09, 419.88), Position.fromCoordinates(839.51, 432.76), Position.fromCoordinates(841.33, 441.04), Position.fromCoordinates(847.62, 449.22),
    Position.fromCoordinates(847.16, 458.44), Position.fromCoordinates(851.38, 462.79), Position.fromCoordinates(853.97, 471.15), Position.fromCoordinates(866.36, 480.77)
  };

  private Position[] simplified = {
    Position.fromCoordinates(224.55, 250.15), Position.fromCoordinates(267.76, 213.81), Position.fromCoordinates(296.91, 155.64), Position.fromCoordinates(330.33, 137.57),
    Position.fromCoordinates(409.52, 141.14), Position.fromCoordinates(439.60, 119.74), Position.fromCoordinates(486.51, 106.75), Position.fromCoordinates(529.57, 127.86),
    Position.fromCoordinates(539.27, 147.24), Position.fromCoordinates(617.74, 159.86), Position.fromCoordinates(629.55, 194.60), Position.fromCoordinates(671.55, 222.55),
    Position.fromCoordinates(727.81, 213.36), Position.fromCoordinates(739.94, 204.77), Position.fromCoordinates(769.98, 208.42), Position.fromCoordinates(779.60, 216.87),
    Position.fromCoordinates(800.24, 214.62), Position.fromCoordinates(820.77, 236.17), Position.fromCoordinates(859.88, 255.49), Position.fromCoordinates(865.21, 268.53),
    Position.fromCoordinates(857.95, 280.30), Position.fromCoordinates(867.79, 306.17), Position.fromCoordinates(859.87, 311.37), Position.fromCoordinates(854.54, 335.40),
    Position.fromCoordinates(860.92, 343.00), Position.fromCoordinates(849.84, 359.59), Position.fromCoordinates(854.56, 365.53), Position.fromCoordinates(844.09, 371.89),
    Position.fromCoordinates(839.57, 390.40), Position.fromCoordinates(848.40, 407.55), Position.fromCoordinates(839.51, 432.76), Position.fromCoordinates(853.97, 471.15),
    Position.fromCoordinates(866.36, 480.77)
  };

    /*
     * Sample values from
     * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
     */

  @Test
  public void testDecode() {
    // _p~iF~ps|U_ulLnnqC_mqNvxq`@
    List<Position> path = PolylineUtils.decode(
      "_p~iF~ps|U_ulLnnqC_mqNvxq`@", Constants.GOOGLE_PRECISION);

    // (38.5, -120.2), (40.7, -120.95), (43.252, -126.453)
    assertEquals(path.get(0).getLongitude(), -120.2, DELTA);
    assertEquals(path.get(0).getLatitude(), 38.5, DELTA);
    assertEquals(path.get(1).getLongitude(), -120.95, DELTA);
    assertEquals(path.get(1).getLatitude(), 40.7, DELTA);
    assertEquals(path.get(2).getLongitude(), -126.453, DELTA);
    assertEquals(path.get(2).getLatitude(), 43.252, DELTA);
  }

  @Test
  public void testEncode() {
    // (38.5, -120.2), (40.7, -120.95), (43.252, -126.453)
    List<Position> path = new ArrayList<>();
    path.add(Position.fromCoordinates(-120.2, 38.5));
    path.add(Position.fromCoordinates(-120.95, 40.7));
    path.add(Position.fromCoordinates(-126.453, 43.252));

    // _p~iF~ps|U_ulLnnqC_mqNvxq`@
    assertEquals(PolylineUtils.encode(
      path, Constants.GOOGLE_PRECISION), "_p~iF~ps|U_ulLnnqC_mqNvxq`@");
  }

    /*
     * Sample route:
     * https://api.mapbox.com/v4/directions/mapbox.walking/-77.043410,38.909620;-77.036500,38.897700.json?access_token=&alternatives=false&steps=false&geometry=polyline
     */

  private final static String POLYLINE = "ujzeiApjj}qCfj@}Wnv@ka@tYaPrVmMb]kN`GiCxEgDdr@}[dCsAjXoMhNkGjM_Hts@k^dG_CrAi@|GoDzl@mWdH}GzA{Bl@iFAge@vF?zB?pB@rt@B`E?fF@fEGxB[nC}@xNaHp`@iREsG~EiA`JIxAUvAkBNeDo@qBpFuH|BkD|A{CnAaDdAaEn@uDXiDPkFAiG|H@x@sABkMdVEhFE|GCCko@|GChDv@";

  private final static String LINESTRING = "{\"type\":\"LineString\",\"coordinates\":[[-77.043385,38.909627],[-77.042986,38.908935],[-77.042436,38.908047],[-77.042163,38.90762],[-77.041932,38.907242],[-77.041686,38.90676],[-77.041617,38.906631],[-77.041533,38.906522],[-77.04107,38.905703],[-77.041028,38.905636],[-77.040796,38.90523],[-77.040662,38.904985],[-77.040518,38.904755],[-77.040016,38.903912],[-77.039952,38.903781],[-77.039931,38.903739],[-77.039843,38.903596],[-77.039452,38.902862],[-77.039309,38.902715],[-77.039247,38.902669],[-77.03913,38.902646],[-77.038518,38.902647],[-77.038518,38.902523],[-77.038518,38.902461],[-77.038519,38.902404],[-77.038521,38.901546],[-77.038521,38.901449],[-77.038522,38.901333],[-77.038518,38.901233],[-77.038504,38.901172],[-77.038473,38.9011],[-77.038328,38.900847],[-77.038019,38.90031],[-77.037881,38.900313],[-77.037844,38.900201],[-77.037839,38.900024],[-77.037828,38.899979],[-77.037774,38.899935],[-77.037691,38.899927],[-77.037634,38.899951],[-77.037479,38.89983],[-77.037393,38.899767],[-77.037315,38.89972],[-77.037234,38.89968],[-77.037137,38.899645],[-77.037046,38.899621],[-77.036961,38.899608],[-77.036843,38.899599],[-77.03671,38.8996],[-77.036711,38.899441],[-77.036669,38.899412],[-77.036439,38.89941],[-77.036436,38.899039],[-77.036433,38.898922],[-77.036431,38.898779],[-77.035657,38.898781],[-77.035655,38.898638],[-77.035683,38.898553]]}";

  @Test
  public void testOsrmEncode() {
    LineString path = LineString.fromJson(LINESTRING);
    assertEquals(path.toPolyline(Constants.OSRM_PRECISION_V4), POLYLINE);
  }

  @Test
  public void testOsrmDecode() {
    LineString path = LineString.fromPolyline(POLYLINE, Constants.OSRM_PRECISION_V4);

    // Check a first, middle, last elements
    int middle = path.getCoordinates().size() / 2;
    int last = path.getCoordinates().size() - 1;
    assertEquals(path.getCoordinates().get(0).getLongitude(), -77.043385, DELTA);
    assertEquals(path.getCoordinates().get(0).getLatitude(), 38.909627, DELTA);
    assertEquals(path.getCoordinates().get(middle).getLongitude(), -77.038504, DELTA);
    assertEquals(path.getCoordinates().get(middle).getLatitude(), 38.901172, DELTA);
    assertEquals(path.getCoordinates().get(last).getLongitude(), -77.035683, DELTA);
    assertEquals(path.getCoordinates().get(last).getLatitude(), 38.898553, DELTA);
  }

  @Test
  public void testSimplify() {
    // Simplifies points correctly with the given tolerance
    Position[] result = PolylineUtils.simplify(points, 5);
    assertEquals(simplified.length, result.length);
    for (int i = 0; i < simplified.length; i++) {
      assertEquals(simplified[i].getLongitude(), result[i].getLongitude(), DELTA);
      assertEquals(simplified[i].getLatitude(), result[i].getLatitude(), DELTA);
    }

    // Just return the points if it has only one point
    Position[] onePoint = {Position.fromCoordinates(1, 2)};
    result = PolylineUtils.simplify(onePoint);
    assertEquals(result.length, 1);
    assertEquals(result[0].getLongitude(), 1, DELTA);
    assertEquals(result[0].getLatitude(), 2, DELTA);

    // Just return the points if it has no points
    Position[] emptyList = {};
    result = PolylineUtils.simplify(emptyList);
    assertEquals(result.length, 0);
  }

}
