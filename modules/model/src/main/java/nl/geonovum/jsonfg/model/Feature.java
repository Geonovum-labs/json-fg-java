package nl.geonovum.jsonfg.model;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class Feature {

  public static final String TYPE = "Feature";

  private final URI[] conformsTo;

  private final Object id;

  private final String featureType;

  private final String featureSchema;

  private final Time time;

  private final CoordRefSys[] coordRefSys;

  private final Geometry place;

  private final GeometryGeoJSON geometry;

  private final Map<String, Object> properties;

  public String getType() {
    return TYPE;
  }
}
