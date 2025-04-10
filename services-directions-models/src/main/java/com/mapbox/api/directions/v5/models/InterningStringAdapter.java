package com.mapbox.api.directions.v5.models;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class InterningStringAdapter extends TypeAdapter<String> {

  @Override
  public void write(JsonWriter out, String value) throws IOException {
    out.value(value);
  }

  @Override
  public String read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return in.nextString().intern();
  }
}
