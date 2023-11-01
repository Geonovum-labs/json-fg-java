package nl.geonovum.labs.jsonfg.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import nl.geonovum.labs.jsonfg.model.Geometry;

public final class GeometryDeserializer extends JsonDeserializer<Geometry> {

  @Override
  public Geometry deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    var node = parser.getCodec()
        .readTree(parser);

    if (node instanceof ObjectNode objectNode) {
      return ParseUtil.parseGeometry(objectNode);
    }

    throw new ParseException("Geometry is not an object node.");
  }
}
