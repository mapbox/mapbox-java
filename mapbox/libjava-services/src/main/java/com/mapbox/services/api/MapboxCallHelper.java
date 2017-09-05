package com.mapbox.services.api;

import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.utils.MapboxUtils;
import com.mapbox.services.commons.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapboxCallHelper {

  public static String formatIntArray(int[] array) {
    if (array == null || array.length == 0) {
      return null;
    }

    String[] sourcesFormatted = new String[array.length];
    return TextUtils.join(";", sourcesFormatted);
  }

  public static String formatStringArray(String[] array) {
    if (array == null || array.length == 0) {
      return null;
    }
    return TextUtils.join(",", array);
  }

  public static String formatRadiuses(double[] radiuses) {
    if (radiuses == null || radiuses.length == 0) {
      return null;
    }

    String[] radiusesFormatted = new String[radiuses.length];
    for (int i = 0; i < radiuses.length; i++) {
      if (radiuses[i] == Double.POSITIVE_INFINITY) {
        radiusesFormatted[i] = "unlimited";
      } else {
        radiusesFormatted[i] = String.format(Locale.US, "%s", TextUtils.formatCoordinate(radiuses[i]));
      }
    }
    return TextUtils.join(";", radiusesFormatted);
  }


  public static String formatCoordinates(List<Point> coordinates) {
    List<String> coordinatesFormatted = new ArrayList<>();
    for (Point point : coordinates) {
      coordinatesFormatted.add(String.format(Locale.US, "%s,%s",
        TextUtils.formatCoordinate(point.longitude()),
        TextUtils.formatCoordinate(point.latitude())));
    }

    return TextUtils.join(";", coordinatesFormatted.toArray());
  }

  public static String formatBearing(List<Double[]> bearings) {
    if (bearings.isEmpty()) {
      return null;
    }

    String[] bearingFormatted = new String[bearings.size()];
    for (int i = 0; i < bearings.size(); i++) {
      if (bearings.get(i).length == 0) {
        bearingFormatted[i] = "";
      } else {
        bearingFormatted[i] = String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(bearings.get(i)[0]),
          TextUtils.formatCoordinate(bearings.get(i)[1]));
      }
    }
    return TextUtils.join(";", bearingFormatted);
  }

  public static String formatDistributions(List<Integer[]> distributions) {
    if (distributions.isEmpty()) {
      return null;
    }

    String[] distributionsFormatted = new String[distributions.size()];
    for (int i = 0; i < distributions.size(); i++) {
      if (distributions.get(i).length == 0) {
        distributionsFormatted[i] = "";
      } else {
        distributionsFormatted[i] = String.format(Locale.US, "%s,%s",
          TextUtils.formatCoordinate(distributions.get(i)[0]),
          TextUtils.formatCoordinate(distributions.get(i)[1]));
      }
    }
    return TextUtils.join(";", distributionsFormatted);
  }
}
