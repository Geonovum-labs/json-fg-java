package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class MultiPolyhedron extends AbstractGeometry {

  public static final String TYPE = "MultiPolyhedron";

  private final Coordinate[][][][][] coordinates;

  @Override
  public String getType() {
    return TYPE;
  }
}
