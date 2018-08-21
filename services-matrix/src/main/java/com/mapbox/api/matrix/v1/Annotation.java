package com.mapbox.api.matrix.v1;

public enum Annotation {
    DURATION("duration"),
    DISTANCE("distance");

    private final String value;

    Annotation(final String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
