package nl.geonovum.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class GeometryCollection extends AbstractGeometry implements GeometryGeoJSON {

  public static final String TYPE = "GeometryCollection";

  private final GeometryGeoJSON[] geometries;

  @Override
  public String getType() {
    return TYPE;
  }
}
