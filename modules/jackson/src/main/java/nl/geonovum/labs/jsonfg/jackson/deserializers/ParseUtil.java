package nl.geonovum.labs.jsonfg.jackson.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NumericNode;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Coordinate;
import nl.geonovum.labs.jsonfg.model.Geometry;
import nl.geonovum.labs.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.labs.jsonfg.model.LineString;
import nl.geonovum.labs.jsonfg.model.MultiLineString;
import nl.geonovum.labs.jsonfg.model.MultiPoint;
import nl.geonovum.labs.jsonfg.model.MultiPolygon;
import nl.geonovum.labs.jsonfg.model.MultiPolyhedron;
import nl.geonovum.labs.jsonfg.model.MultiPrism;
import nl.geonovum.labs.jsonfg.model.Point;
import nl.geonovum.labs.jsonfg.model.Polygon;
import nl.geonovum.labs.jsonfg.model.Polyhedron;
import nl.geonovum.labs.jsonfg.model.Prism;
import nl.geonovum.labs.jsonfg.model.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParseUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String TYPE_KEY = "type";

  private static final String COORD_REF_SYS_KEY = "coordRefSys";

  private static final String COORDINATES_KEY = "coordinates";

  public static Object parseFeatureID(JsonNode node) {
    return node.isNumber() ? node.numberValue() : node.asText();
  }

  public static URI parseURI(JsonNode node) {
    if (!node.isTextual()) {
      throw new ParseException("URI value is not textual.");
    }

    return URI.fromString(node.textValue());
  }

  public static URI[] parseURIs(JsonNode node) {
    if (node.isArray()) {
      return createNodeStream(node)
          .map(ParseUtil::parseURI)
          .toArray(URI[]::new);
    }

    return new URI[]{parseURI(node)};
  }

  public static CoordRefSys[] parseCoordRefSys(JsonNode node) {
    if (node == null) {
      return null;
    }

    if (node.isArray()) {
      return createNodeStream(node)
          .map(n -> new CoordRefSys(parseURI(n)))
          .toArray(CoordRefSys[]::new);
    }

    return new CoordRefSys[]{new CoordRefSys(parseURI(node))};
  }

  public static Map<String, Object> parseProperties(JsonNode node) {
    if (!node.isObject()) {
      throw new ParseException("Properties is not an object.");
    }

    try {
      return (Map<String, Object>) OBJECT_MAPPER.treeToValue(node, Map.class);
    } catch (JsonProcessingException e) {
      throw new ParseException("Could not parse properties.", e);
    }
  }

  public static GeometryGeoJSON parseGeometryGeoJSON(JsonNode node) {
    var geometry = parseGeometry(node);

    if (geometry instanceof GeometryGeoJSON geometryGeoJSON) {
      return geometryGeoJSON;
    }

    throw new ParseException("Geometry is not GeoJSON-compatible.");
  }

  public static Geometry parseGeometry(JsonNode node) {
    var typeNode = node.get(TYPE_KEY);

    if (!typeNode.isTextual()) {
      throw new ParseException("Geometry type is missing or not a string.");
    }

    var type = typeNode.asText();

    return switch (type) {
      case "Point" -> parsePoint(node);
      case "MultiPoint" -> parseMultiPoint(node);
      case "LineString" -> parseLineString(node);
      case "MultiLineString" -> parseMultiLineString(node);
      case "Polygon" -> parsePolygon(node);
      case "MultiPolygon" -> parseMultiPolygon(node);
      case "Polyhedron" -> parsePolyhedron(node);
      case "MultiPolyhedron" -> parseMultiPolyhedron(node);
      case "Prism" -> parsePrism(node);
      case "MultiPrism" -> parseMultiPrism(node);
      default -> throw new ParseException("Unknown geometry type: " + type);
    };
  }

  private static Point parsePoint(JsonNode node) {
    return Point.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinate(node.get(COORDINATES_KEY)))
        .build();
  }

  private static MultiPoint parseMultiPoint(JsonNode node) {
    return MultiPoint.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinates(node.get(COORDINATES_KEY)))
        .build();
  }

  private static LineString parseLineString(JsonNode node) {
    return LineString.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinates(node.get(COORDINATES_KEY)))
        .build();
  }

  private static MultiLineString parseMultiLineString(JsonNode node) {
    return MultiLineString.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinatesArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private static Polygon parsePolygon(JsonNode node) {
    return Polygon.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinatesArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private static MultiPolygon parseMultiPolygon(JsonNode node) {
    return MultiPolygon.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinatesArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private static Polyhedron parsePolyhedron(JsonNode node) {
    return Polyhedron.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinatesArrayArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private static MultiPolyhedron parseMultiPolyhedron(JsonNode node) {
    return MultiPolyhedron.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(parseCoordinatesArrayArrayArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private static Prism parsePrism(JsonNode node) {
    var baseNode = node.get("base");

    if (baseNode == null || !baseNode.isObject()) {
      throw new ParseException("Prism 'base' is missing or not an object.");
    }

    var lowerNode = node.get("lower");

    if (lowerNode != null && !lowerNode.isNumber()) {
      throw new ParseException("Prism 'lower' value is not a number.");
    }

    var upperNode = node.get("upper");

    if (upperNode == null || !upperNode.isNumber()) {
      throw new ParseException("Prism 'upper' value is missing or not a number.");
    }

    var base = parseGeometry(baseNode);

    if (!Prism.baseSupports(base)) {
      throw new ParseException("Prism 'base' type is not allowed: " + base.getType());
    }

    return Prism.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .base(base)
        .lower(lowerNode != null ? lowerNode.asDouble() : null)
        .upper(upperNode.asDouble())
        .build();
  }

  private static MultiPrism parseMultiPrism(JsonNode node) {
    var prismsNode = node.get("prisms");

    if (prismsNode == null || !prismsNode.isArray()) {
      throw new ParseException("Property 'prisms' is missing or not an array");
    }

    var prims = createNodeStream(prismsNode)
        .map(ParseUtil::parsePrism)
        .toArray(Prism[]::new);

    return MultiPrism.builder()
        .coordRefSys(parseCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .prisms(prims)
        .build();
  }

  private static Coordinate[][][][][] parseCoordinatesArrayArrayArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(ParseUtil::parseCoordinatesArrayArrayArray)
        .toArray(Coordinate[][][][][]::new);
  }

  private static Coordinate[][][][] parseCoordinatesArrayArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(ParseUtil::parseCoordinatesArrayArray)
        .toArray(Coordinate[][][][]::new);
  }

  private static Coordinate[][][] parseCoordinatesArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(ParseUtil::parseCoordinatesArray)
        .toArray(Coordinate[][][]::new);
  }

  private static Coordinate[][] parseCoordinatesArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(ParseUtil::parseCoordinates)
        .toArray(Coordinate[][]::new);
  }

  private static Coordinate[] parseCoordinates(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(ParseUtil::parseCoordinate)
        .toArray(Coordinate[]::new);
  }

  private static Coordinate parseCoordinate(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    if (node.size() < 2 || node.size() > 3) {
      throw new ParseException("Coordinate array must contain 2 or 3 items.");
    }

    return new Coordinate(
        parseOrdinate(node.get(0)),
        parseOrdinate(node.get(1)),
        Optional.ofNullable(node.get(2))
            .map(ParseUtil::parseOrdinate)
            .orElse(Double.NaN));
  }

  private static double parseOrdinate(JsonNode node) {
    if (node instanceof NumericNode) {
      return node.asDouble();
    }

    throw new ParseException("Ordinate is not numeric.");
  }

  private static Stream<JsonNode> createNodeStream(JsonNode node) {
    var spliterator = Spliterators.spliterator(node.elements(), node.size(), Spliterator.ORDERED);
    return StreamSupport.stream(spliterator, false);
  }
}
