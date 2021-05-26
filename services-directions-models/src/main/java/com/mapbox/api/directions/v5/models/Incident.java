package com.mapbox.api.directions.v5.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Reproduces one of road incidents type ({@link IncidentType}) that might be on the way.
 */
@AutoValue
public abstract class Incident extends DirectionsJsonObject {

  /**
   * {@link IncidentType} accident.
   */
  public static final String INCIDENT_ACCIDENT = "accident";

  /**
   * {@link IncidentType} congestion.
   */
  public static final String INCIDENT_CONGESTION = "congestion";

  /**
   * {@link IncidentType} construction.
   */
  public static final String INCIDENT_CONSTRUCTION = "construction";

  /**
   * {@link IncidentType} disabled vehicle. ?
   */
  public static final String INCIDENT_DISABLED_VEHICLE = "disabled_vehicle";

  /**
   * {@link IncidentType} lane restriction.
   */
  public static final String INCIDENT_LANE_RESTRICTION = "lane_restriction";

  /**
   * {@link IncidentType} mass transit.
   */
  public static final String INCIDENT_MASS_TRANSIT = "mass_transit";

  /**
   * {@link IncidentType} miscellaneous.
   */
  public static final String INCIDENT_MISCELLANEOUS = "miscellaneous";

  /**
   * {@link IncidentType} other news.
   */
  public static final String INCIDENT_OTHER_NEWS = "other_news";

  /**
   * {@link IncidentType} planned event.
   */
  public static final String INCIDENT_PLANNED_EVENT = "planned_event";

  /**
   * {@link IncidentType} road closure.
   */
  public static final String INCIDENT_ROAD_CLOSURE = "road_closure";

  /**
   * {@link IncidentType} road hazard.
   */
  public static final String INCIDENT_ROAD_HAZARD = "road_hazard";

  /**
   * {@link IncidentType} weather.
   */
  public static final String INCIDENT_WEATHER = "weather";

  /**
   * Incident type.
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef({
    INCIDENT_ACCIDENT,
    INCIDENT_CONGESTION,
    INCIDENT_CONSTRUCTION,
    INCIDENT_DISABLED_VEHICLE,
    INCIDENT_LANE_RESTRICTION,
    INCIDENT_MASS_TRANSIT,
    INCIDENT_MISCELLANEOUS,
    INCIDENT_OTHER_NEWS,
    INCIDENT_PLANNED_EVENT,
    INCIDENT_ROAD_CLOSURE,
    INCIDENT_ROAD_HAZARD,
    INCIDENT_WEATHER
  })
  public @interface IncidentType {
  }

  /**
   * {@link ImpactType} unknown.
   */
  public static final String IMPACT_UNKNOWN = "unknown";

  /**
   * {@link ImpactType} critical.
   */
  public static final String IMPACT_CRITICAL = "critical";

  /**
   * {@link ImpactType} major.
   */
  public static final String IMPACT_MAJOR = "major";

  /**
   * {@link ImpactType} minor.
   */
  public static final String IMPACT_MINOR = "minor";

  /**
   * {@link ImpactType} low.
   */
  public static final String IMPACT_LOW = "low";

  /**
   * Impact type.
   */
  @Retention(RetentionPolicy.CLASS)
  @StringDef({
    IMPACT_UNKNOWN,
    IMPACT_CRITICAL,
    IMPACT_MAJOR,
    IMPACT_MINOR,
    IMPACT_LOW
  })
  public @interface ImpactType {
  }

  /**
   * Unique identifier for incident. It might be the only one <b>non-null</b> filed which meant
   * that incident started on previous leg and one has an incident with the same <b>id</b>.
   */
  @NonNull
  public abstract String id();

  /**
   * One of incident types.
   *
   * @see IncidentType
   */
  @Nullable
  @IncidentType
  public abstract String type();

  /**
   * <b>True</b> if road is closed and no possibility to pass through there. <b>False</b>
   * otherwise.
   */
  @Nullable
  public abstract Boolean closed();

  /**
   * Quantitative descriptor of congestion.
   */
  @Nullable
  public abstract Congestion congestion();

  /**
   * Human-readable description of the incident suitable for displaying to the users.
   */
  @Nullable
  public abstract String description();

  /**
   * Human-readable long description of the incident suitable for displaying to the users.
   */
  @Nullable
  @SerializedName("long_description")
  public abstract String longDescription();

  /**
   * Severity level of incident.
   *
   * @see ImpactType
   */
  @Nullable
  @ImpactType
  public abstract String impact();

  /**
   * Sub-type of the incident.
   */
  @Nullable
  @SerializedName("sub_type")
  public abstract String subType();

  /**
   * Sub-type-specific description.
   */
  @Nullable
  @SerializedName("sub_type_description")
  public abstract String subTypeDescription();

  /**
   * AlertC codes.
   *
   * @see <a href="https://www.iso.org/standard/59231.html">AlertC</a>
   */
  @Nullable
  @SerializedName("alertc_codes")
  public abstract List<Integer> alertcCodes();

  /**
   * Incident's geometry index start point.
   */
  @Nullable
  @SerializedName("geometry_index_start")
  public abstract Integer geometryIndexStart();

  /**
   * Incident's geometry index end point.
   */
  @Nullable
  @SerializedName("geometry_index_end")
  public abstract Integer geometryIndexEnd();

  /**
   * Time the incident was created/updated in ISO8601 format. Not the same
   * {@link #startTime()}/{@link #endTime()}, incident can be created/updated before the incident.
   */
  @Nullable
  @SerializedName("creation_time")
  public abstract String creationTime();

  /**
   * Start time of the incident in ISO8601 format.
   */
  @Nullable
  @SerializedName("start_time")
  public abstract String startTime();

  /**
   * End time of the incident in ISO8601 format.
   */
  @Nullable
  @SerializedName("end_time")
  public abstract String endTime();

  /**
   * Create a new instance of this class by using the {@link Incident.Builder} class.
   *
   * @return this classes {@link Incident.Builder} for creating a new instance
   */
  public static Builder builder() {
    return new AutoValue_Incident.Builder();
  }

  /**
   * Convert the current {@link Incident} to its builder holding the currently assigned
   * values. This allows you to modify a single property and then rebuild the object resulting in
   * an updated and modified {@link Incident}.
   *
   * @return a {@link Builder} with the same values set to match the ones defined in this
   * {@link Incident}
   */
  public abstract Builder toBuilder();

  /**
   * Gson type adapter for parsing Gson to this class.
   *
   * @param gson the built {@link Gson} object
   * @return the type adapter for this class
   */
  public static TypeAdapter<Incident> typeAdapter(Gson gson) {
    return new AutoValue_Incident.GsonTypeAdapter(gson);
  }

  /**
   * Create a new instance of this class by passing in a formatted valid JSON String.
   *
   * @param json a formatted valid JSON string defining an Incident
   * @return a new instance of this class defined by the values passed in the method
   */
  public static Incident fromJson(String json) {
    GsonBuilder gson = new GsonBuilder();
    gson.registerTypeAdapterFactory(DirectionsAdapterFactory.create());
    return gson.create().fromJson(json, Incident.class);
  }

  /**
   * This builder can be used to set the values describing the {@link Incident}.
   */
  @AutoValue.Builder
  public abstract static class Builder {

    /**
     * Unique identifier for incident. It might be the only one <b>non-null</b> filed which meant
     * that incident started on previous leg and one has an incident with the same <b>id</b>.
     *
     * @param id String
     */
    public abstract Builder id(@NonNull String id);

    /**
     * One of incident types.
     *
     * @param type incident type
     * @see IncidentType
     */
    public abstract Builder type(@Nullable @IncidentType String type);

    /**
     * <b>True</b> if road is closed and no possibility to pass through there. <b>False</b>
     * otherwise.
     *
     * @param closed is way closed
     */
    public abstract Builder closed(@Nullable Boolean closed);

    /**
     * Quantitative descriptor of congestion.
     *
     * @param congestion congestion
     */
    public abstract Builder congestion(@Nullable Congestion congestion);

    /**
     * Human-readable description of the incident suitable for displaying to the users.
     *
     * @param description incident description
     */
    public abstract Builder description(@Nullable String description);

    /**
     * Human-readable long description of the incident suitable for displaying to the users.
     *
     * @param longDescription incident long description
     */
    public abstract Builder longDescription(@Nullable String longDescription);

    /**
     * Severity level of incident.
     *
     * @param impact impact type
     * @see ImpactType
     */
    public abstract Builder impact(@Nullable @ImpactType String impact);

    /**
     * Sub-type of the incident.
     *
     * @param subType syp-type
     */
    public abstract Builder subType(@Nullable String subType);

    /**
     * Sub-type-specific description.
     *
     * @param subTypeDescription sub-type description
     */
    public abstract Builder subTypeDescription(@Nullable String subTypeDescription);

    /**
     * AlertC codes.
     *
     * @param alertcCodes list of alert codes
     * @see <a href="https://www.iso.org/standard/59231.html">AlertC</a>
     */
    public abstract Builder alertcCodes(@Nullable List<Integer> alertcCodes);

    /**
     * Incident's geometry index start point.
     *
     * @param geometryIndexStart start index
     */
    public abstract Builder geometryIndexStart(@Nullable Integer geometryIndexStart);

    /**
     * Incident's geometry index end point.
     *
     * @param geometryIndexEnd end index
     */
    public abstract Builder geometryIndexEnd(@Nullable Integer geometryIndexEnd);

    /**
     * Time the incident was created/updated in ISO8601 format.
     *
     * @param creationTime ISO8601 format
     */
    public abstract Builder creationTime(@Nullable String creationTime);

    /**
     * Start time in ISO8601 format.
     *
     * @param startTime ISO8601 format
     */
    public abstract Builder startTime(@Nullable String startTime);

    /**
     * End time in ISO8601 format.
     *
     * @param endTime ISO8601 format
     */
    public abstract Builder endTime(@Nullable String endTime);

    /**
     * Build a new instance of {@link Incident}.
     *
     * @return a new instance of {@link Incident}.
     */
    public abstract Incident build();
  }
}
