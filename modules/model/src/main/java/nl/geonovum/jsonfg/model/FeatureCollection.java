package nl.geonovum.jsonfg.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class FeatureCollection {

  public static final String TYPE = "FeatureCollection";

  private final URI[] conformsTo;

  private final String featureType;

  private final short geometryDimension;

  private final String featureSchema;

  private final CoordRefSys[] coordRefSys;

  private final Feature[] features;

  public String getType() {
    return TYPE;
  }
}
