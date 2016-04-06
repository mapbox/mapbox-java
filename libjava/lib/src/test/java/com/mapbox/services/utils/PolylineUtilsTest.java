package com.mapbox.services.utils;

import com.mapbox.services.Constants;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by antonio on 1/30/16.
 */
public class PolylineUtilsTest {

    /*
     * Sample values from
     * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
     */

    private final static double DELTA = 1e-15;

    @Test
    public void testDecode() {
        // _p~iF~ps|U_ulLnnqC_mqNvxq`@
        List<Position> path = com.mapbox.services.commons.utils.PolylineUtils.decode(
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
        assertEquals(com.mapbox.services.commons.utils.PolylineUtils.encode(
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
        assertEquals(path.toPolyline(Constants.OSRM_PRECISION), POLYLINE);
    }

    @Test
    public void testOsrmDecode() {
        LineString path = LineString.fromPolyline(POLYLINE, Constants.OSRM_PRECISION);

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

}
