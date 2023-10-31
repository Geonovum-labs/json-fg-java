package nl.geonovum.jsonfg.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import nl.geonovum.jsonfg.model.CoordRefSys;

public final class CoordRefSysSerializer extends JsonSerializer<CoordRefSys[]> {

  @Override
  public void serialize(CoordRefSys[] coordRefSys, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    if (coordRefSys.length == 1) {
      jsonGenerator.writeObject(coordRefSys[0].getURI());
      return;
    }

    jsonGenerator.writeStartArray();
    for (CoordRefSys crs: coordRefSys) {
      jsonGenerator.writeObject(crs.getURI());
    }
    jsonGenerator.writeEndArray();
  }
}
