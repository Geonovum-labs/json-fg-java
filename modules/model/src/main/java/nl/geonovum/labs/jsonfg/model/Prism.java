package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class Prism extends AbstractGeometry {

  public static final String TYPE = "Prism";

  private final Geometry base;

  private final Double lower;

  private final Double upper;

  @Override
  public String getType() {
    return TYPE;
  }

  public static boolean baseSupports(Geometry geometry) {
    return geometry instanceof GeometryGeoJSON
        && !(geometry instanceof GeometryCollection);
  }
}
