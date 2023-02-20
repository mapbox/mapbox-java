package com.mapbox.api.geocoding.v5;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Similar to {@link com.google.gson.internal.bind.CollectionTypeAdapterFactory},
 * safely adapts single element list represented as Json object or primitive.
 *
 * Note: unlike {@link com.google.gson.internal.bind.CollectionTypeAdapterFactory},
 * this adapter does not perform advanced type analyse and always returns instance of ArrayList
 * which may not work if it is used to deserialize JSON elements into another subtype of List
 * (LinkedList for example).
 *
 * @param <E> collection element type
 *
 * @since 5.4.0
 */
public class SingleElementSafeListTypeAdapter<E> extends TypeAdapter<List<E>> {

  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
      final Class<? super T> rawType = typeToken.getRawType();
      if (!List.class.isAssignableFrom(rawType)) {
        return null;
      }

      final Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), rawType);
      final TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));

      @SuppressWarnings("unchecked")
      final TypeAdapter<T> adapter =
          (TypeAdapter<T>) new SingleElementSafeListTypeAdapter<>(elementTypeAdapter);
      return adapter;
    }
  };

  private final TypeAdapter<E> elementTypeAdapter;

  private SingleElementSafeListTypeAdapter(final TypeAdapter<E> elementTypeAdapter) {
    this.elementTypeAdapter = elementTypeAdapter;
  }

  @Override
  public List<E> read(final JsonReader in) throws IOException {
    final JsonToken token = in.peek();
    final List<E> elements = new ArrayList<>();
    switch (token) {
      case BEGIN_ARRAY:
        in.beginArray();
        while (in.hasNext()) {
          elements.add(elementTypeAdapter.read(in));
        }
        in.endArray();
        return elements;
      case BEGIN_OBJECT:
      case STRING:
      case NUMBER:
      case BOOLEAN:
        elements.add(elementTypeAdapter.read(in));
        return elements;
      case NULL:
        in.nextNull();
        return null;
      case NAME:
      case END_ARRAY:
      case END_OBJECT:
      case END_DOCUMENT:
        throw new MalformedJsonException("Unexpected token: " + token);
      default:
        throw new IllegalStateException("Unprocessed token: " + token);
    }
  }

  @Override
  public void write(final JsonWriter out, final List<E> value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    out.beginArray();
    for (E element : value) {
      elementTypeAdapter.write(out, element);
    }
    out.endArray();
  }
}
