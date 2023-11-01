package nl.geonovum.labs.jsonfg.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import nl.geonovum.labs.jsonfg.jackson.deserializers.FeatureCollectionDeserializer;
import nl.geonovum.labs.jsonfg.jackson.deserializers.FeatureDeserializer;
import nl.geonovum.labs.jsonfg.jackson.deserializers.GeometryDeserializer;
import nl.geonovum.labs.jsonfg.jackson.serializers.CoordinateSerializer;
import nl.geonovum.labs.jsonfg.jackson.serializers.URISerializer;
import nl.geonovum.labs.jsonfg.core.TypeMapping;
import nl.geonovum.labs.jsonfg.jackson.mixins.FeatureCollectionMixin;
import nl.geonovum.labs.jsonfg.jackson.mixins.FeatureMixin;
import nl.geonovum.labs.jsonfg.jackson.mixins.GeometryMixin;
import nl.geonovum.labs.jsonfg.jackson.serializers.TypeSerializer;
import nl.geonovum.labs.jsonfg.model.CompatibilityMode;
import nl.geonovum.labs.jsonfg.model.Coordinate;
import nl.geonovum.labs.jsonfg.model.Feature;
import nl.geonovum.labs.jsonfg.model.FeatureCollection;
import nl.geonovum.labs.jsonfg.model.Geometry;
import nl.geonovum.labs.jsonfg.model.URI;

public final class JSONFGModule extends SimpleModule {

  public JSONFGModule() {
    this(Options.builder().build());
  }

  public JSONFGModule(Options options) {
    addSerializer(Coordinate.class, new CoordinateSerializer());
    addSerializer(URI.class, new URISerializer());
    addDeserializer(Geometry.class, new GeometryDeserializer());
    addDeserializer(Feature.class, new FeatureDeserializer());
    addDeserializer(FeatureCollection.class, new FeatureCollectionDeserializer());
    setMixInAnnotation(Geometry.class, GeometryMixin.class);
    setMixInAnnotation(Feature.class, FeatureMixin.class);
    setMixInAnnotation(FeatureCollection.class, FeatureCollectionMixin.class);

    options.getTypeMappings()
        .forEach(typeMapping -> addSerializer(typeMapping.getType(),
            new TypeSerializer<>(typeMapping, options.getCompatibilityMode())));
  }

  @Getter
  @Builder
  public static class Options {

    @Singular
    private final List<TypeMapping<?, ?>> typeMappings;

    @Builder.Default
    private CompatibilityMode compatibilityMode = CompatibilityMode.DEFAULT;
  }
}
