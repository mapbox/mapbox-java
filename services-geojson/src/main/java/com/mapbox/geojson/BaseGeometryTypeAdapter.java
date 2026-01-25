package com.mapbox.geojson;

import androidx.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.geojson.gson.BoundingBoxTypeAdapter;

import java.io.IOException;

/**
 * Base class for converting {@code Geometry} instances to JSON and
 * JSON to instances of {@code Geometry}.
 *
 * @param <G> Geometry
 * @param <T> Type of coordinates
 * @since 4.6.0
 */
@Keep
abstract class BaseGeometryTypeAdapter<G, T, A> extends TypeAdapter<G> {

  private volatile TypeAdapter<String> stringAdapter;
  private volatile TypeAdapter<BoundingBox> boundingBoxAdapter;
  private volatile TypeAdapter<A> coordinatesAdapter;

  private final Gson gson;

  BaseGeometryTypeAdapter(Gson gson, TypeAdapter<A> coordinatesAdapter) {
    this.gson = gson;
    this.coordinatesAdapter = coordinatesAdapter;
    this.boundingBoxAdapter = new BoundingBoxTypeAdapter();
  }

  public void writeCoordinateContainerPrimitive(JsonWriter jsonWriter, FlattenedCoordinateContainer<T, A> object) throws IOException {
    if (object == null) {
      jsonWriter.nullValue();
      return;
    }
    writeCommon(jsonWriter, object);
    jsonWriter.name("coordinates");
    if (object.coordinates() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<A> coordinatesAdapter = this.coordinatesAdapter;
      if (coordinatesAdapter == null) {
        throw new GeoJsonException("Coordinates type adapter is null");
      }
      coordinatesAdapter.write(jsonWriter, object.flattenCoordinates());
    }
    jsonWriter.endObject();
  }
  public void writeCoordinateContainer(JsonWriter jsonWriter, CoordinateContainer<A> object)
          throws IOException {
    if (object == null) {
      jsonWriter.nullValue();
      return;
    }

    writeCommon(jsonWriter, object);

    jsonWriter.name("coordinates");
    if (object.coordinates() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<A> coordinatesAdapter = this.coordinatesAdapter;
      if (coordinatesAdapter == null) {
        throw new GeoJsonException("Coordinates type adapter is null");
      }
      coordinatesAdapter.write(jsonWriter, object.coordinates());
    }

    jsonWriter.endObject();
  }

  private void writeCommon(JsonWriter jsonWriter, CoordinateContainer object) throws IOException {
    jsonWriter.beginObject();
    jsonWriter.name("type");
    if (object.type() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<String> stringAdapter = this.stringAdapter;
      if (stringAdapter == null) {
        stringAdapter = gson.getAdapter(String.class);
        this.stringAdapter = stringAdapter;
      }
      stringAdapter.write(jsonWriter, object.type());
    }
    jsonWriter.name("bbox");
    if (object.bbox() == null) {
      jsonWriter.nullValue();
    } else {
      TypeAdapter<BoundingBox> boundingBoxAdapter = this.boundingBoxAdapter;
      if (boundingBoxAdapter == null) {
        boundingBoxAdapter = gson.getAdapter(BoundingBox.class);
        this.boundingBoxAdapter = boundingBoxAdapter;
      }
      boundingBoxAdapter.write(jsonWriter, object.bbox());
    }
  }

  public CoordinateContainer<T> readCoordinateContainer(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() == JsonToken.NULL) {
      jsonReader.nextNull();
      return null;
    }

    jsonReader.beginObject();
    String type = null;
    BoundingBox bbox = null;
    A coordinates = null;

    while (jsonReader.hasNext()) {
      String name = jsonReader.nextName();
      if (jsonReader.peek() == JsonToken.NULL) {
        jsonReader.nextNull();
        continue;
      }
      switch (name) {
        case "type":
          TypeAdapter<String> stringAdapter = this.stringAdapter;
          if (stringAdapter == null) {
            stringAdapter = gson.getAdapter(String.class);
            this.stringAdapter = stringAdapter;
          }
          type = stringAdapter.read(jsonReader);
          break;

        case "bbox":
          TypeAdapter<BoundingBox> boundingBoxAdapter = this.boundingBoxAdapter;
          if (boundingBoxAdapter == null) {
            boundingBoxAdapter = gson.getAdapter(BoundingBox.class);
            this.boundingBoxAdapter = boundingBoxAdapter;
          }
          bbox = boundingBoxAdapter.read(jsonReader);
          break;

        case "coordinates":
          TypeAdapter<A> coordinatesAdapter = this.coordinatesAdapter;
          if (coordinatesAdapter == null) {
            throw new GeoJsonException("Coordinates type adapter is null");
          }
          coordinates = coordinatesAdapter.read(jsonReader);
          break;

        default:
          jsonReader.skipValue();

      }
    }
    jsonReader.endObject();

    return createCoordinateContainer(type, bbox, coordinates);
  }

  abstract CoordinateContainer<T> createCoordinateContainer(String type,
                                                            BoundingBox bbox,
                                                            A coordinates);
}
