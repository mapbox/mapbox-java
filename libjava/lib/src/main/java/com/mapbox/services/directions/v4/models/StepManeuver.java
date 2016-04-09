package com.mapbox.services.directions.v4.models;

/**
 * Used to describe a instruction step maneuver.
 */
@Deprecated
public class StepManeuver {

    private String type;
    private ManeuverPoint location;
    private String instruction;
    private String mode;

    /**
     * A specific string describing the type of maneuver. The following types are currently used:
     * continue, bear right, turn right, sharp right, u-turn, sharp left, turn left, bear left,
     * waypoint, depart, enter roundabout, and arrive. Warning, new maneuver types may be introduced
     * in the future however, so <a href="https://www.mapbox.com/developers/api/directions/">check here</a>
     * to ensure you don't miss any.
     *
     * @return string with possibly one of the maneuvers given above.
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * {@link ManeuverPoint} object containing type and the coordinate.
     *
     * @return {@link ManeuverPoint} object.
     */
    public ManeuverPoint getLocation() {
        return location;
    }

    public void setLocation(ManeuverPoint location) {
        this.location = location;
    }

    /**
     * Human-readable string describing the maneuver. typically combines {@link #getType()} and a
     * street name. if within {@code MapboxDirections.Builder} you setInstructions to HTML, you'll
     * relieve the instructions in HTML format.
     *
     * @return string typically the length of a sentence giving next maneuver instruction.
     */
    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    /**
     * A string signifying the mode of transportation. Possible values can be:
     * <p>For driving: driving, ferry, movable bridge, unaccessible.
     * <p>For walking: walking, ferry, unaccessible.
     * <p>For cycling: cycling, walking, ferry, train, movable bridge, unaccessible.
     *
     * @return string with possibly one of the values given above.
     */
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
