import java.util.HashMap;
import nl.geonovum.jsonfg.core.FeatureMapping;
import nl.geonovum.jsonfg.core.MappingUtil;
import nl.geonovum.jsonfg.jts.JTSGeometryMapping;
import nl.geonovum.jsonfg.model.CompatibilityMode;
import nl.geonovum.jsonfg.model.Feature;
import nl.geonovum.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.jsonfg.model.URI;

public class BuildingMapping implements FeatureMapping<Building> {

  private final JTSGeometryMapping geometryMapping = new JTSGeometryMapping();

  @Override
  public Class<Building> getType() {
    return Building.class;
  }

  @Override
  public Feature mapInstance(Building building, CompatibilityMode compatibilityMode) {
    var properties = new HashMap<String, Object>();
    properties.put("constructionYear", building.getConstructionYear());
    properties.put("surface", building.getSurface());

    var featureBuilder = Feature.builder()
        .conformsTo(new URI[]{new URI(URI.Prefix.SPEC_JSONFG102, "core")})
        .id(building.getId())
        .properties(properties);

    var geometry = geometryMapping.mapInstance(building.getPerimeter(), compatibilityMode);

    if (MappingUtil.isGeoJSONCompatible(geometry)) {
      featureBuilder.geometry((GeometryGeoJSON) geometry);
    } else {
      featureBuilder.place(geometry);
    }

    return featureBuilder.build();
  }
}
