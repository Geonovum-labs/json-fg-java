package nl.geonovum.jsonfg.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class MultiPrism extends AbstractGeometry {

  public static final String TYPE = "MultiPrism";

  private final Prism[] prisms;

  @Override
  public String getType() {
    return TYPE;
  }
}
