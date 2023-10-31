package nl.geonovum.jsonfg.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import nl.geonovum.jsonfg.model.Coordinate;

public final class CoordinateSerializer extends JsonSerializer<Coordinate> {

  @Override
  public void serialize(Coordinate coordinate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeNumber(coordinate.getX());
    jsonGenerator.writeNumber(coordinate.getY());

    if (!Double.isNaN(coordinate.getZ())) {
      jsonGenerator.writeNumber(coordinate.getY());
    }

    jsonGenerator.writeEndArray();
  }
}
