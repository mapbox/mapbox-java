package com.mapbox.services.api.mapmatching.v5.models;

import java.util.List;

/**
 * Mapbox map matching API response and convenience getter methods for optional properties
 *
 * @see <a href="https://www.mapbox.com/api-documentation/#retrieve-a-match">Map Matching API Documentation</a>
 * @since 2.0.0
 */
public class MapMatchingResponse {

  private String code;
  private List<MapMatchingMatching> matchings;
  private List<MapMatchingTracepoint> tracepoints;

  public MapMatchingResponse() {
  }

  /**
   * A string depicting the state of the response.
   * <ul>
   * <li>"Ok" - Normal case</li>
   * <li>"NoMatch" - The input did not produce any matches. matchings will be an empty array.</li>
   * <li>"TooManyCoordinates" - There are more than 100 points in the request.</li>
   * <li>"InvalidInput" - message will hold an explanation of the invalid input.</li>
   * <li>"ProfileNotFound" - Profile should be {@code mapbox.driving}, {@code mapbox.walking},
   * or {@code mapbox.cycling}.</li>
   * </ul>
   *
   * @return String containing the code.
   * @since 2.0.0
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code String containing the code.
   * @since 2.0.0
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * An array of Match objects
   *
   * @return matchings list
   */
  public List<MapMatchingMatching> getMatchings() {
    return matchings;
  }

  /**
   * Set the matchings value
   *
   * @param matchings list
   */
  public void setMatchings(List<MapMatchingMatching> matchings) {
    this.matchings = matchings;
  }

  /**
   * An array of Tracepoint objects representing the location an input point was matched with.
   * Array of Waypoint objects representing all input points of the trace in the order they
   * were matched. If a trace point is omitted by map matching because it is an outlier,
   * the entry will be null.
   *
   * @return tracepoints list
   */
  public List<MapMatchingTracepoint> getTracepoints() {
    return tracepoints;
  }

  /**
   * Set the tracepoints value
   *
   * @param tracepoints list
   */
  public void setTracepoints(List<MapMatchingTracepoint> tracepoints) {
    this.tracepoints = tracepoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MapMatchingResponse that = (MapMatchingResponse) o;

    if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
      return false;
    }
    if (getMatchings() != null ? !getMatchings().equals(that.getMatchings()) : that.getMatchings() != null) {
      return false;
    }
    return getTracepoints() != null ? getTracepoints().equals(that.getTracepoints()) : that.getTracepoints() == null;

  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
    result = 31 * result + (getMatchings() != null ? getMatchings().hashCode() : 0);
    result = 31 * result + (getTracepoints() != null ? getTracepoints().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MapMatchingResponse{"
      + "code='" + code + '\''
      + ", matchings=" + matchings
      + ", tracepoints=" + tracepoints
      + '}';
  }
}
