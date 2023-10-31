package nl.geonovum.labs.jsonfg.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import nl.geonovum.labs.jsonfg.model.URI;

public final class URISerializer extends JsonSerializer<URI> {

  @Override
  public void serialize(URI uri, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(uri.getCURIE());
  }
}
