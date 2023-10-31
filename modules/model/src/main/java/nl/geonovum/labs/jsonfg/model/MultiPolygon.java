package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class MultiPolygon extends AbstractGeometry implements GeometryGeoJSON {

  public static final String TYPE = "MultiPolygon";

  private final Coordinate[][][] coordinates;

  @Override
  public String getType() {
    return TYPE;
  }
}
