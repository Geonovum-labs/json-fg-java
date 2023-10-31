package nl.geonovum.labs.jsonfg.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class CoordRefSys {

  private final URI uri;

  public CoordRefSys(URI uri) {
    this.uri = uri;
  }

  public URI getURI() {
    return uri;
  }
}
