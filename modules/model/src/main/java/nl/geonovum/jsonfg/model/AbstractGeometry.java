package nl.geonovum.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
abstract class AbstractGeometry implements Geometry {

  private final CoordRefSys[] coordRefSys;

  private final Coordinate[] bbox;
}
