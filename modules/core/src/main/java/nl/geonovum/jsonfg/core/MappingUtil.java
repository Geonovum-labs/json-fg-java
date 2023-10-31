package nl.geonovum.jsonfg.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.geonovum.jsonfg.model.CoordRefSys;
import nl.geonovum.jsonfg.model.Geometry;
import nl.geonovum.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.jsonfg.model.URI;

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
