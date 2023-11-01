package nl.geonovum.labs.jsonfg.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Optional;
import nl.geonovum.labs.jsonfg.model.Feature;

public final class FeatureDeserializer extends JsonDeserializer<Feature> {

  @Override
  public Feature deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    var node = parser.getCodec()
        .readTree(parser);

    if (node instanceof ObjectNode objectNode) {
      return createFeature(objectNode);
    }

    throw new ParseException("Geometry is not an object node.");
  }

  private Feature createFeature(JsonNode node) {
    var typeNode = node.get("type");

    if (!(Feature.TYPE.equals(typeNode.asText()))) {
      throw new ParseException("Type is missing or not equal to 'Feature'.");
    }

    var featureBuilder = Feature.builder();

    Optional.ofNullable(node.get("id"))
        .ifPresent(idNode -> featureBuilder.id(ParseUtil.parseFeatureID(idNode)));

    Optional.ofNullable(node.get("featureType"))
        .map(JsonNode::asText)
        .ifPresent(featureBuilder::featureType);

    Optional.ofNullable(node.get("featureSchema"))
        .map(JsonNode::asText)
        .ifPresent(featureBuilder::featureSchema);

    Optional.ofNullable(node.get("conformsTo"))
        .ifPresent(conformsToNode -> featureBuilder.conformsTo(ParseUtil.parseURIs(conformsToNode)));

    Optional.ofNullable(node.get("time"))
        .ifPresent(timeNode -> {
          // TODO
        });

    Optional.ofNullable(node.get("coordRefSys"))
        .map(ParseUtil::parseCoordRefSys)
        .ifPresent(featureBuilder::coordRefSys);

    Optional.ofNullable(node.get("place"))
        .map(ParseUtil::parseGeometry)
        .ifPresent(featureBuilder::place);

    Optional.ofNullable(node.get("geometry"))
        .map(ParseUtil::parseGeometryGeoJSON)
        .ifPresent(featureBuilder::geometry);

    Optional.ofNullable(node.get("properties"))
        .map(ParseUtil::parseProperties)
        .ifPresent(featureBuilder::properties);

    return featureBuilder.build();
  }
}
