package com.mapbox.api.tilequery;

import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

import org.hamcrest.core.StringStartsWith;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapboxTilequeryTest extends TilequeryTestUtils {

    /**
     * Test the most basic request (default response format)
     */
    @Test
    public void sanity() throws ServicesException, IOException {
        MapboxTilequery client = MapboxTilequery.builder()
          .accessToken(ACCESS_TOKEN)
          .query("-122.42901,37.80633")
          .mapIds("mapbox.mapbox-streets-v7")
          .baseUrl(mockUrl.toString())
          .build();
        Response<FeatureCollection> response = client.executeCall();
        assertEquals(200, response.code());
        assertNotNull(response.body());
    }

  @Test
  public void query_acceptsPointsCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query(Point.fromLngLat(-122.42901,37.80633))
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();

    String str = client.cloneCall().request().url().toString();

    assertTrue(client.cloneCall().request().url().toString()
      .contains("-122.42901,37.80633"));
  }

  @Test
  public void build_noAccessTokenExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: accessToken");
    MapboxTilequery.builder()
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_invalidAccessTokenExceptionThrown() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(StringStartsWith.startsWith("Using Mapbox Services requires setting a valid access token"));
    MapboxTilequery.builder()
      .accessToken("")
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_noQueryExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: query");
    MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_invalidQueryExceptionThrown() throws ServicesException {
    thrown.expect(ServicesException.class);
    thrown.expectMessage(StringStartsWith.startsWith("A query with latitude and longitude values is required"));
    MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_noMapIdExceptionThrown() throws Exception {
    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing required properties: mapIds");
    MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .baseUrl(mockUrl.toString())
      .build();
  }

  @Test
  public void build_optionalParameters() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .build();

    assertNull(client.limit());
    assertNull(client.radius());
    assertNull(client.dedupe());
    assertNull(client.geometry());
    assertNull(client.layers());

    Response<FeatureCollection> response = client.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());
  }

  @Test
  public void build_limitGetsAddedToListCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .limit(50)
      .build();
    assertTrue(client.cloneCall().request().url().toString()
      .contains("limit=50"));
  }

  @Test
  public void build_radiusGetsAddedToListCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .radius(200)
      .build();
    assertTrue(client.cloneCall().request().url().toString()
      .contains("radius=200"));
  }

  @Test
  public void build_geometryGetsAddedToListCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .geometry(TilequeryCriteria.TILEQUERY_GEOMETRY_LINESTRING)
      .build();

    assertTrue(client.cloneCall().request().url().toString()
      .contains("geometry=linestring"));
  }

  @Test
  public void build_dedupeGetsAddedToListCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .dedupe(true)
      .build();

    assertTrue(client.cloneCall().request().url().toString()
      .contains("dedupe=true"));
  }

  @Test
  public void build_layersGetAddedToListCorrectly() throws Exception {
    MapboxTilequery client = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .layers("poi_label")
      .build();
    assertTrue(client.cloneCall().request().url().toString()
      .contains("layers=poi_label"));
  }

  @Test
  public void executeCall_optionalParamLimitHonored() throws Exception {
    MapboxTilequery clientAppParams = MapboxTilequery.builder()
      .accessToken(ACCESS_TOKEN)
      .query("-122.42901,37.80633")
      .mapIds("mapbox.mapbox-streets-v7")
      .baseUrl(mockUrl.toString())
      .layers("poi_label")
      .geometry("point")
      .radius(500)
      .limit(2)
      .layers("poi_label")
      .build();

    Response<FeatureCollection> response = clientAppParams.executeCall();
    assertEquals(200, response.code());
    assertNotNull(response.body());

    FeatureCollection featureCollection = (FeatureCollection)response.body();
    assertEquals(2, featureCollection.features().size());
  }
}
