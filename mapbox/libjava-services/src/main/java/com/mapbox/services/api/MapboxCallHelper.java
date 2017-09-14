package com.mapbox.services.api;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// TODO move all methods to TextUtils
public class MapboxCallHelper {

  public static String formatCoordinates(List<Point> coordinates) {
    List<String> coordinatesFormatted = new ArrayList<>();
    for (Point point : coordinates) {
      coordinatesFormatted.add(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude())));
    }

    return TextUtils.join(";", coordinatesFormatted.toArray());
  }
}
