package nl.geonovum.labs.jsonfg.integrationtest;

import java.util.HashMap;
import nl.geonovum.labs.jsonfg.core.FeatureMapping;
import nl.geonovum.labs.jsonfg.core.MappingUtil;
import nl.geonovum.labs.jsonfg.jts.JTSGeometryMapping;
import nl.geonovum.labs.jsonfg.model.CompatibilityMode;
import nl.geonovum.labs.jsonfg.model.Feature;
import nl.geonovum.labs.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.labs.jsonfg.model.URI;

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
