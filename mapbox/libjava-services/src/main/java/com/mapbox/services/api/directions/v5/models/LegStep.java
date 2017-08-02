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
  private double weight;
  private List<StepIntersection> intersections;
  private String exit;

  /**
   * Empty constructor
   *
   * @since 2.0.0
   */
  public LegStep() {
  }

  /**
   * Constructor taking in a list of {@link StepIntersection}s.
   *
   * @param intersections {@link StepIntersection}
   * @since 2.0.0
   */
  public LegStep(List<StepIntersection> intersections) {
    this.intersections = intersections;
  }

  /**
   * Constructor taking in a {@code String} name, a {@code String} rotaryName and a {@link StepManeuver}.
   *
   * @param name       String with the name of the way along which the travel proceeds.
   * @param rotaryName String indicating the name of the rotary.
   * @param maneuver   one {@link StepManeuver} object.
   * @since 2.0.0
   */
  public LegStep(String name, String rotaryName, StepManeuver maneuver) {
    this.name = name;
    this.rotaryName = rotaryName;
    this.maneuver = maneuver;
  }

  /**
   * Specifies a decimal precision of edge weights, default value 1.
   *
   * @return a decimal precision double value.
   * @since 2.1.0
   */
  public double getWeight() {
    return weight;
  }

  /**
   * Specifies a decimal precision of edge weights, default value 1.
   *
   * @param weight double value representing the edge weight.
   * @since 2.1.0
   */
  public void setWeight(double weight) {
    this.weight = weight;
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
   * The distance traveled from the maneuver to the next {@link LegStep}.
   *
   * @param distance a double number with unit meters.
   * @since 2.1.0
   */
  public void setDistance(double distance) {
    this.distance = distance;
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
   * The estimated travel time from the maneuver to the next {@link LegStep}.
   *
   * @param duration a double number with unit seconds.
   * @since 2.1.0
   */
  public void setDuration(double duration) {
    this.duration = duration;
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
   * Sets the geometry of the leg step.
   *
   * @param geometry an encoded polyline string.
   * @since 2.1.0
   */
  public void setGeometry(String geometry) {
    this.geometry = geometry;
  }

  /**
   * String with the name of the way along which the travel proceeds.
   *
   * @return a {@code String} representing the way along which the travel proceeds.
   * @since 1.0.0
   */
  public String getName() {
    return name;
  }

  /**
   * Sets String with the name of the way along which the travel proceeds.
   *
   * @param name a {@code String} representing the way along which the travel proceeds.
   * @since 2.0.0
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * String with reference number or code of the way along which the travel proceeds.
   *
   * @return String with reference number or code of the way along which the travel proceeds. Optionally included, if
   * data is available.
   * @since 2.0.0
   */
  public String getRef() {
    return ref;
  }

  /**
   * Sets String with reference number or code of the way along which the travel proceeds. Optionally included, if data
   * is available.
   *
   * @param ref a String representing the reference number or code of the way along which the travel proceeds.
   * @since 2.1.0
   */
  public void setRef(String ref) {
    this.ref = ref;
  }

  /**
   * String with the destinations of the way along which the travel proceeds.
   *
   * @return String with the destinations of the way along which the travel proceeds. Optionally included, if data
   * is available.
   * @since 2.0.0
   */
  public String getDestinations() {
    return destinations;
  }

  /**
   * String with the destinations of the way along which the travel proceeds.
   *
   * @param destinations a String with the destinations of the way along which the travel proceeds.
   *                     Optionally included, if data is available.
   * @since 2.1.0
   */

  public void setDestinations(String destinations) {
    this.destinations = destinations;
  }

  /**
   * indicates the mode of transportation in the step.
   *
   * @return String indicating the mode of transportation.
   * @since 1.0.0
   */
  public String getMode() {
    return mode;
  }

  /**
   * indicates the mode of transportation in the step.
   *
   * @param mode a String indicating the mode of transportation.
   * @since 2.1.0
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * A {@link StepManeuver} object that typically represents the first coordinate making up the
   * {@link LegStep#getGeometry()}.
   *
   * @return One {@link StepManeuver} object.
   * @since 1.0.0
   */
  public StepManeuver getManeuver() {
    return maneuver;
  }

  /**
   * Provide a {@link StepManeuver} object that typically represents the first coordinate making up the
   * {@link LegStep#getGeometry()}.
   *
   * @param maneuver a {@link StepManeuver} for the given step.
   * @since 2.1.0
   */
  public void setManeuver(StepManeuver maneuver) {
    this.maneuver = maneuver;
  }

  /**
   * Provides a list of all the intersections connected to the current way the user is traveling along.
   *
   * @return List of {@link StepIntersection} representing all intersections along the step.
   * @since 1.3.0
   */
  public List<StepIntersection> getIntersections() {
    return intersections;
  }

  /**
   * Provide a {@code List} of objects representing all intersections along the step.
   *
   * @param intersections List of {@link StepIntersection} representing all intersections along the step.
   * @since 2.1.0
   */
  public void setIntersections(List<StepIntersection> intersections) {
    this.intersections = intersections;
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
   * An optional string indicating the name of the rotary.
   *
   * @param rotaryName a String with the rotary name.
   * @since 2.1.0
   */
  public void setRotaryName(String rotaryName) {
    this.rotaryName = rotaryName;
  }

  /**
   * An optional string indicating the pronunciation of the name of the rotary.
   *
   * @return String in IPA with the rotary name's pronunciation.
   * @since 2.0.0
   */
  public String getRotaryPronunciation() {
    return rotaryPronunciation;
  }

  /**
   * An optional string indicating the pronunciation of the name of the rotary.
   *
   * @param rotaryPronunciation String in IPA with the rotary name's pronunciation.
   * @since 2.1.0
   */
  public void setRotaryPronunciation(String rotaryPronunciation) {
    this.rotaryPronunciation = rotaryPronunciation;
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

  /**
   * The pronunciation hint of the way name. Will be undefined if no pronunciation is hit.
   *
   * @param pronunciation provide a String with the pronunciation of the way name.
   * @since 2.1.0
   */
  public void setPronunciation(String pronunciation) {
    this.pronunciation = pronunciation;
  }

  /**
   * A String with the exit numbers or names of the way. Optionally included, if data is available
   * @return String depicting exit
   */
  public String getExit() {
    return exit;
  }

  /**
   * A String with the exit numbers or names of the way. Optionally included, if data is available
   */
  public void setExit(String exit) {
    this.exit = exit;
  }
}
