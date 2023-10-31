package nl.geonovum.jsonfg.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import nl.geonovum.jsonfg.core.TypeMapping;
import nl.geonovum.jsonfg.jackson.mixins.FeatureCollectionMixin;
import nl.geonovum.jsonfg.jackson.mixins.FeatureMixin;
import nl.geonovum.jsonfg.jackson.mixins.GeometryMixin;
import nl.geonovum.jsonfg.jackson.serializers.CoordinateSerializer;
import nl.geonovum.jsonfg.jackson.serializers.TypeSerializer;
import nl.geonovum.jsonfg.jackson.serializers.URISerializer;
import nl.geonovum.jsonfg.model.CompatibilityMode;
import nl.geonovum.jsonfg.model.Coordinate;
import nl.geonovum.jsonfg.model.Feature;
import nl.geonovum.jsonfg.model.FeatureCollection;
import nl.geonovum.jsonfg.model.Geometry;
import nl.geonovum.jsonfg.model.URI;

public final class JSONFGModule extends SimpleModule {

  public JSONFGModule() {
    this(Options.builder().build());
  }

  public JSONFGModule(Options options) {
    addSerializer(Coordinate.class, new CoordinateSerializer());
    addSerializer(URI.class, new URISerializer());
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
