package com.mapbox.api.geocoding.v6;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.geojson.Geometry;

import java.util.List;

@AutoValue
public abstract class V6Feature {

    /**
     * The geometry which makes up this feature. A Geometry object represents points, curves, and
     * surfaces in coordinate space. One of the seven geometries provided inside this library can be
     * passed in through one of the static factory methods.
     *
     * @return a single defined {@link Geometry} which makes this feature spatially aware
     */
    @Nullable
    public abstract Geometry geometry();

    @Nullable
    @SerializedName("properties")
    public abstract V6Properties properties();

    /**
     * Gson type adapter for parsing Gson to this class.
     *
     * @param gson the built {@link Gson} object
     * @return the type adapter for this class
     */
    public static TypeAdapter<V6Feature> typeAdapter(Gson gson) {
        return new AutoValue_V6Feature.GsonTypeAdapter(gson);
    }
}
