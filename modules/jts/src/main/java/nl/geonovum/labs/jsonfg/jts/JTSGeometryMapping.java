package nl.geonovum.labs.jsonfg.jts;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import nl.geonovum.labs.jsonfg.core.GeometryMapping;
import nl.geonovum.labs.jsonfg.model.CompatibilityMode;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Coordinate;
import nl.geonovum.labs.jsonfg.model.Geometry;
import nl.geonovum.labs.jsonfg.model.URI;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class JTSGeometryMapping implements GeometryMapping<org.locationtech.jts.geom.Geometry> {

  @Override
  public Class<org.locationtech.jts.geom.Geometry> getType() {
    return org.locationtech.jts.geom.Geometry.class;
  }

  @Override
  public Geometry mapInstance(org.locationtech.jts.geom.Geometry geometry, CompatibilityMode compatibilityMode) {
    if (geometry instanceof Point point) {
      return nl.geonovum.labs.jsonfg.model.Point.builder()
          .coordRefSys(mapCoordRefSys(point.getSRID()))
          .coordinates(mapCoordinate(point.getCoordinate()))
          .build();
    }

    if (geometry instanceof MultiPoint multiPoint) {
      return nl.geonovum.labs.jsonfg.model.MultiPoint.builder()
          .coordRefSys(mapCoordRefSys(multiPoint.getSRID()))
          .coordinates(mapCoordinates(multiPoint.getCoordinates()))
          .build();
    }

    if (geometry instanceof LineString lineString) {
      return nl.geonovum.labs.jsonfg.model.LineString.builder()
          .coordRefSys(mapCoordRefSys(lineString.getSRID()))
          .coordinates(Arrays.stream(lineString.getCoordinates())
              .map(c -> new Coordinate(c.getX(), c.getY(), c.getZ()))
              .toArray(Coordinate[]::new))
          .build();
    }

    if (geometry instanceof MultiLineString multiLineString) {
      var coordinates = IntStream.range(0, multiLineString.getNumGeometries())
          .mapToObj(i -> mapCoordinates(multiLineString.getGeometryN(i)
              .getCoordinates()))
          .toArray(Coordinate[][]::new);

      return nl.geonovum.labs.jsonfg.model.MultiLineString.builder()
          .coordRefSys(mapCoordRefSys(multiLineString.getSRID()))
          .coordinates(coordinates)
          .build();
    }

    if (geometry instanceof Polygon polygon) {
      return nl.geonovum.labs.jsonfg.model.Polygon.builder()
          .coordRefSys(mapCoordRefSys(polygon.getSRID()))
          .coordinates(mapPolygonCoordinates(polygon))
          .build();
    }

    if (geometry instanceof MultiPolygon multiPolygon) {
      var coordinates = IntStream.range(0, multiPolygon.getNumGeometries())
          .mapToObj(i -> mapPolygonCoordinates((Polygon) multiPolygon.getGeometryN(i)))
          .toArray(Coordinate[][][]::new);

      return nl.geonovum.labs.jsonfg.model.MultiPolygon.builder()
          .coordRefSys(mapCoordRefSys(multiPolygon.getSRID()))
          .coordinates(coordinates)
          .build();
    }

    throw new RuntimeException("Could not map geometry class: " + geometry.getClass());
  }

  private static CoordRefSys[] mapCoordRefSys(int srid) {
    if (srid == 0 || srid == 4326) {
      return new CoordRefSys[]{new CoordRefSys(URI.CRS84)};
    }

    return new CoordRefSys[]{new CoordRefSys(new URI(URI.Prefix.CRS_EPSG, String.valueOf(srid)))};
  }

  private static Coordinate[][] mapPolygonCoordinates(Polygon polygon) {
    var exteriorRing = mapLinearRingCoordinates(polygon.getExteriorRing());
    var interiorRings = IntStream.range(0, polygon.getNumInteriorRing())
        .mapToObj(i -> mapLinearRingCoordinates(polygon.getInteriorRingN(i)));

    return Stream.concat(Stream.<Coordinate[]>of(exteriorRing), interiorRings)
        .toArray(Coordinate[][]::new);
  }

  private static Coordinate[] mapLinearRingCoordinates(LinearRing linearRing) {
    return mapCoordinates(linearRing.getCoordinates());
  }

  private static Coordinate[] mapCoordinates(org.locationtech.jts.geom.Coordinate[] coordinates) {
    return Arrays.stream(coordinates)
        .map(JTSGeometryMapping::mapCoordinate)
        .toArray(Coordinate[]::new);
  }

  private static Coordinate mapCoordinate(org.locationtech.jts.geom.Coordinate coordinate) {
    return new Coordinate(coordinate.getX(), coordinate.getY(), coordinate.getZ());
  }
}
