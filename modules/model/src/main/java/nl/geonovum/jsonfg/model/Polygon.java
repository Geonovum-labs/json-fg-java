package nl.geonovum.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class Polygon extends AbstractGeometry implements GeometryGeoJSON {

  public static final String TYPE = "Polygon";

  private final Coordinate[][] coordinates;

  @Override
  public String getType() {
    return TYPE;
  }
}
