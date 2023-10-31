package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class Prism extends AbstractGeometry {

  public static final String TYPE = "Prism";

  private final GeometryGeoJSON base;

  private final Double lower;

  private final Double upper;

  @Override
  public String getType() {
    return TYPE;
  }
}
