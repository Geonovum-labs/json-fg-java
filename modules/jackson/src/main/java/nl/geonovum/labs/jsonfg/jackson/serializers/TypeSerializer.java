package nl.geonovum.labs.jsonfg.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import nl.geonovum.labs.jsonfg.core.TypeMapping;
import nl.geonovum.labs.jsonfg.model.CompatibilityMode;

@RequiredArgsConstructor
public final class TypeSerializer<T> extends JsonSerializer<Object> {

  private final TypeMapping<T, ?> mapper;

  private final CompatibilityMode compatibilityMode;

  @Override
  public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    var typedValue = mapper.getType()
        .cast(value);
    var mappedValue = mapper.mapInstance(typedValue, compatibilityMode);
    jsonGenerator.writeObject(mappedValue);
  }
}
