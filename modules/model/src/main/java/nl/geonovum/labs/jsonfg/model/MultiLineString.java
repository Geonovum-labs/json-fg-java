package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class MultiLineString extends AbstractGeometry implements GeometryGeoJSON {

  public static final String TYPE = "MultiLineString";

  private final Coordinate[][] coordinates;

  @Override
  public String getType() {
    return TYPE;
  }
}
