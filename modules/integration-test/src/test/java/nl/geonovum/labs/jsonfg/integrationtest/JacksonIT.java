package nl.geonovum.labs.jsonfg.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import nl.geonovum.labs.jsonfg.jackson.JSONFGModule;
import nl.geonovum.labs.jsonfg.jackson.JSONFGModule.Options;
import nl.geonovum.labs.jsonfg.jts.JTSGeometryMapping;
import nl.geonovum.labs.jsonfg.model.Coordinate;
import nl.geonovum.labs.jsonfg.model.Feature;
import nl.geonovum.labs.jsonfg.model.Point;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

class JacksonIT {

  private static final GeometryFactory GF = new GeometryFactory(
      new PrecisionModel(PrecisionModel.FLOATING), 4326);

  @Test
  void serializeGeometry() throws IOException {
    var jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule());
    var point = createPoint();
    var doc = jsonMapper.writeValueAsString(point);

    assertThat(doc).isEqualTo("{\"type\":\"Point\",\"coordinates\":[-0.12459925,51.500779]}");
  }

  @Test
  void serializeFeature() throws IOException {
    var jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule());
    var feature = createFeature();
    var doc = jsonMapper.writeValueAsString(feature);

    assertThat(doc).isEqualTo("{\"type\":\"Feature\",\"id\":\"123\",\"time\":null,\"place\":null," +
        "\"geometry\":{\"type\":\"Point\",\"coordinates\":[-0.12459925,51.500779]},\"properties\":null}");
  }

  @Test
  void serializeGeometryWithJTSBinding() throws IOException {
    var jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule(Options.builder()
        .typeMapping(new JTSGeometryMapping())
        .build()));
    var geometry = createJTSPoint();
    var doc = jsonMapper.writeValueAsString(geometry);

    assertThat(doc).isEqualTo("{\"type\":\"Point\",\"coordRefSys\":\"[OGC:CRS84]\"," +
        "\"coordinates\":[-0.12459925,51.500779]}");
  }

  @Test
  void serializeFeatureWithJTSBinding() throws IOException {
    var jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule(Options.builder()
        .typeMapping(new JTSGeometryMapping())
        .typeMapping(new BuildingMapping())
        .build()));
    var building = Building.builder()
        .id("123")
        .constructionYear(1995)
        .surface(265.43)
        .perimeter(createJTSPolygon())
        .build();
    var doc = jsonMapper.writeValueAsString(building);

    assertThat(doc).isEqualTo("{\"conformsTo\":[\"[ogc-json-fg-1-0.2:core]\"],\"type\":\"Feature\"," +
        "\"id\":\"123\",\"time\":null,\"place\":null,\"geometry\":{\"type\":\"Polygon\"," +
        "\"coordinates\":[[[-0.130430186,51.50024234],[-0.130430186,51.49970113],[-0.129663756,51.49970113]," +
        "[-0.129663756,51.50024234],[-0.130430186,51.50024234]]]},\"properties\":{\"surface\":265.43," +
        "\"constructionYear\":1995}}");
  }

  private Feature createFeature() {
    return Feature.builder()
        .id("123")
        .geometry(createPoint())
        .build();
  }

  private Point createPoint() {
    return Point.builder()
        .coordinates(new Coordinate(-0.12459925, 51.5007790))
        .build();
  }

  private org.locationtech.jts.geom.Point createJTSPoint() {
    return GF.createPoint(new org.locationtech.jts.geom.Coordinate(-0.12459925, 51.5007790));
  }

  private org.locationtech.jts.geom.Polygon createJTSPolygon() {
    return GF.createPolygon(new org.locationtech.jts.geom.Coordinate[]{
        new org.locationtech.jts.geom.Coordinate(-0.130430186, 51.50024234),
        new org.locationtech.jts.geom.Coordinate(-0.130430186, 51.49970113),
        new org.locationtech.jts.geom.Coordinate(-0.129663756, 51.49970113),
        new org.locationtech.jts.geom.Coordinate(-0.129663756, 51.50024234),
        new org.locationtech.jts.geom.Coordinate(-0.130430186, 51.50024234)
    });
  }
}
