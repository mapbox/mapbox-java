package com.mapbox.services.api.directions.v5.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Includes one {@link StepManeuver} object and travel to the following {@link LegStep}.
 *
 * @since 1.0.0
 */
public class LegStep {

  private double distance;
  private double duration;
  private String geometry;
  private String name;
  private String ref;
  private String destinations;
  private String mode;
  private String pronunciation;
  @SerializedName("rotary_name")
  private String rotaryName;
  @SerializedName("rotary_pronunciation")
  private String rotaryPronunciation;
  private StepManeuver maneuver;
  private List<StepIntersection> intersections;

  public LegStep() {
  }

  public LegStep(List<StepIntersection> intersections) {
    this.intersections = intersections;
  }

  public LegStep(String name, String rotaryName, StepManeuver maneuver) {
    this.name = name;
    this.rotaryName = rotaryName;
    this.maneuver = maneuver;
  }

  /**
   * The distance traveled from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit meters.
   * @since 1.0.0
   */
  public double getDistance() {
    return distance;
  }

  /**
   * The estimated travel time from the maneuver to the next {@link LegStep}.
   *
   * @return a double number with unit seconds.
   * @since 1.0.0
   */
  public double getDuration() {
    return duration;
  }

  /**
   * Gives the geometry of the leg step.
   *
   * @return An encoded polyline string.
   * @since 1.0.0
   */
  public String getGeometry() {
    return geometry;
  }

  /**
   * @return String with the name of the way along which the travel proceeds.
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * @return String with reference number or code of the way along which the travel proceeds.
   * Optionally included, if data is available.
   * @since 2.0.0
   */
  public String getRef() {
    return ref;
  }

  /**
   * @return String with the destinations of the way along which the travel proceeds.
   * Optionally included, if data is available.
   * @since 2.0.0
   */
  public String getDestinations() {
    return destinations;
  }

  /**
   * @return String indicating the mode of transportation.
   * @since 1.0.0
   */
  public String getMode() {
    return mode;
  }

  /**
   * @return One {@link StepManeuver} object.
   * @since 1.0.0
   */
  public StepManeuver getManeuver() {
    return maneuver;
  }

  /**
   * @return Array of objects representing all intersections along the step.
   * @since 1.3.0
   */
  public List<StepIntersection> getIntersections() {
    return intersections;
  }


  /**
   * An optional string indicating the name of the rotary.
   *
   * @return String with the rotary name
   * @since 2.0.0
   */
  public String getRotaryName() {
    return rotaryName;
  }

  /**
   * An optional string indicating the pronunciation of the name of the rotary.
   *
   * @return String in IPA with the rotary name's pronunciation
   * @since 2.0.0
   */
  public String getRotaryPronunciation() {
    return rotaryPronunciation;
  }

  /**
   * The pronunciation hint of the way name. Will be undefined if no pronunciation is hit.
   *
   * @return String with the pronunciation
   * @since 2.0.0
   */
  public String getPronunciation() {
    return pronunciation;
  }

}
