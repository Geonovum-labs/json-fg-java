package nl.geonovum.labs.jsonfg.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.geonovum.labs.jsonfg.jackson.serializers.CoordRefSysSerializer;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"conformsTo", "type"})
public abstract class FeatureCollectionMixin {

  @JsonSerialize(using = CoordRefSysSerializer.class)
  abstract CoordRefSys[] getCoordRefSys();
}
