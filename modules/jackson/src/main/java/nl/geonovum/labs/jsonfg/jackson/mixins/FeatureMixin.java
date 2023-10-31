package nl.geonovum.labs.jsonfg.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Map;
import nl.geonovum.labs.jsonfg.jackson.serializers.CoordRefSysSerializer;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Geometry;
import nl.geonovum.labs.jsonfg.model.GeometryGeoJSON;
import nl.geonovum.labs.jsonfg.model.Time;

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
