package nl.geonovum.labs.jsonfg.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Geometry;
import nl.geonovum.labs.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.labs.jsonfg.model.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MappingUtil {

  public static boolean isGeoJSONCompatible(Geometry geometry) {
    return geometry instanceof GeometryGeoJSON
        && geometry.getCoordRefSys().length == 1
        && isGeoJSONCompatible(geometry.getCoordRefSys()[0]);
  }

  public static boolean isGeoJSONCompatible(CoordRefSys coordRefSys) {
    var uri = coordRefSys.getURI();
    return URI.CRS84.equals(uri) || URI.CRS84H.equals(uri);
  }
}
