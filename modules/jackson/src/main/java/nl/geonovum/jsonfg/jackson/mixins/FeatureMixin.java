package nl.geonovum.jsonfg.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import nl.geonovum.jsonfg.jackson.serializers.CoordRefSysSerializer;
import nl.geonovum.jsonfg.model.CoordRefSys;
import nl.geonovum.jsonfg.model.Geometry;
import nl.geonovum.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.jsonfg.model.Time;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"conformsTo", "type"})
public abstract class FeatureMixin {

  @JsonInclude
  Time time;

  @JsonSerialize(using = CoordRefSysSerializer.class)
  abstract CoordRefSys[] getCoordRefSys();

  @JsonInclude
  Geometry place;

  @JsonInclude
  @JsonIgnoreProperties({"coordRefSys"})
  GeometryGeoJSON geometry;

  @JsonInclude
  Map<String, Object> properties;
}
