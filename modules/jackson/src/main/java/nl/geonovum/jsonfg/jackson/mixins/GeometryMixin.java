package nl.geonovum.jsonfg.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.geonovum.jsonfg.jackson.serializers.CoordRefSysSerializer;
import nl.geonovum.jsonfg.model.CoordRefSys;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type"})
public abstract class GeometryMixin {

  @JsonSerialize(using = CoordRefSysSerializer.class)
  abstract CoordRefSys[] getCoordRefSys();
}
