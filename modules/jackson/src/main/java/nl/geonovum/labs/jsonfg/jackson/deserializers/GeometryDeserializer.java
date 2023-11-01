package nl.geonovum.labs.jsonfg.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Coordinate;
import nl.geonovum.labs.jsonfg.model.Geometry;
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

public final class GeometryDeserializer extends JsonDeserializer<Geometry> {

  private static final String TYPE_KEY = "type";

  private static final String COORD_REF_SYS_KEY = "coordRefSys";

  private static final String COORDINATES_KEY = "coordinates";

  @Override
  public Geometry deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    var node = parser.getCodec()
        .readTree(parser);

    if (node instanceof ObjectNode objectNode) {
      return createGeometry(objectNode);
    }

    throw new ParseException("Geometry is not an object node.");
  }

  private Geometry createGeometry(JsonNode node) {
    var typeNode = node.get(TYPE_KEY);

    if (!typeNode.isTextual()) {
      throw new ParseException("Geometry type is missing or not a string.");
    }

    var type = typeNode.asText();

    return switch (type) {
      case "Point" -> createPoint(node);
      case "MultiPoint" -> createMultiPoint(node);
      case "LineString" -> createLineString(node);
      case "MultiLineString" -> createMultiLineString(node);
      case "Polygon" -> createPolygon(node);
      case "MultiPolygon" -> createMultiPolygon(node);
      case "Polyhedron" -> createPolyhedron(node);
      case "MultiPolyhedron" -> createMultiPolyhedron(node);
      case "Prism" -> createPrism(node);
      case "MultiPrism" -> createMultiPrism(node);
      default -> throw new ParseException("Unknown geometry type: " + type);
    };
  }

  private Point createPoint(JsonNode node) {
    return Point.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinate(node.get(COORDINATES_KEY)))
        .build();
  }

  private MultiPoint createMultiPoint(JsonNode node) {
    return MultiPoint.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinates(node.get(COORDINATES_KEY)))
        .build();
  }

  private LineString createLineString(JsonNode node) {
    return LineString.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinates(node.get(COORDINATES_KEY)))
        .build();
  }

  private MultiLineString createMultiLineString(JsonNode node) {
    return MultiLineString.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinatesArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private Polygon createPolygon(JsonNode node) {
    return Polygon.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinatesArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private MultiPolygon createMultiPolygon(JsonNode node) {
    return MultiPolygon.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinatesArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private Polyhedron createPolyhedron(JsonNode node) {
    return Polyhedron.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinatesArrayArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private MultiPolyhedron createMultiPolyhedron(JsonNode node) {
    return MultiPolyhedron.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .coordinates(createCoordinatesArrayArrayArrayArray(node.get(COORDINATES_KEY)))
        .build();
  }

  private Prism createPrism(JsonNode node) {
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

    var base = createGeometry(baseNode);

    if (!Prism.baseSupports(base)) {
      throw new ParseException("Prism 'base' type is not allowed: " + base.getType());
    }

    return Prism.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .base(base)
        .lower(lowerNode != null ? lowerNode.asDouble() : null)
        .upper(upperNode.asDouble())
        .build();
  }

  private MultiPrism createMultiPrism(JsonNode node) {
    var prismsNode = node.get("prisms");

    if (prismsNode == null || !prismsNode.isArray()) {
      throw new ParseException("Property 'prisms' is missing or not an array");
    }

    var prims = createNodeStream(prismsNode)
        .map(this::createPrism)
        .toArray(Prism[]::new);

    return MultiPrism.builder()
        .coordRefSys(createCoordRefSys(node.get(COORD_REF_SYS_KEY)))
        .prisms(prims)
        .build();
  }

  private Coordinate[][][][][] createCoordinatesArrayArrayArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(this::createCoordinatesArrayArrayArray)
        .toArray(Coordinate[][][][][]::new);
  }

  private Coordinate[][][][] createCoordinatesArrayArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(this::createCoordinatesArrayArray)
        .toArray(Coordinate[][][][]::new);
  }

  private Coordinate[][][] createCoordinatesArrayArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(this::createCoordinatesArray)
        .toArray(Coordinate[][][]::new);
  }

  private Coordinate[][] createCoordinatesArray(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(this::createCoordinates)
        .toArray(Coordinate[][]::new);
  }

  private Coordinate[] createCoordinates(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    return createNodeStream(node)
        .map(this::createCoordinate)
        .toArray(Coordinate[]::new);
  }

  private Coordinate createCoordinate(JsonNode node) {
    if (!node.isArray()) {
      throw new ParseException("Coordinates is not an array.");
    }

    if (node.size() < 2 || node.size() > 3) {
      throw new ParseException("Coordinate array must contain 2 or 3 items.");
    }

    return new Coordinate(
        createOrdinate(node.get(0)),
        createOrdinate(node.get(1)),
        Optional.ofNullable(node.get(2))
            .map(this::createOrdinate)
            .orElse(Double.NaN));
  }

  private double createOrdinate(JsonNode node) {
    if (node instanceof NumericNode) {
      return node.asDouble();
    }

    throw new ParseException("Ordinate is not numeric.");
  }

  private CoordRefSys[] createCoordRefSys(JsonNode node) {
    if (node == null) {
      return null;
    }

    // TODO
    return new CoordRefSys[]{new CoordRefSys(URI.CRS84)};
  }

  private static Stream<JsonNode> createNodeStream(JsonNode node) {
    var spliterator = Spliterators.spliterator(node.elements(), node.size(), Spliterator.ORDERED);
    return StreamSupport.stream(spliterator, false);
  }
}
