package nl.geonovum.jsonfg.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public final class URI {

  public static final URI CRS84 = new URI(Prefix.CRS_OGC, "CRS84");

  public static final URI CRS84H = new URI(Prefix.CRS_OGC, "CRS84h");

  public static final URI JSONFG102_CORE = new URI(Prefix.SPEC_JSONFG102, "core");

  public static final URI JSONFG102_3D = new URI(Prefix.SPEC_JSONFG102, "3d");

  private final Prefix prefix;

  private final String localName;

  public String getURI() {
    return prefix.getUri()
        .concat(localName);
  }

  public String getCURIE() {
    return String.format("[%s:%s]", prefix.getAlias(), localName);
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class Prefix {

    public static final Prefix CRS_OGC = new Prefix("OGC", "http://www.opengis.net/def/crs/OGC/0/");

    public static final Prefix CRS_EPSG = new Prefix("EPSG", "http://www.opengis.net/def/crs/EPSG/0/");

    public static final Prefix SPEC_JSONFG102 = new Prefix("ogc-json-fg-1-0.2", "http://www.opengis.net/spec/json-fg-1/0.2/conf/");

    private final String alias;

    private final String uri;
  }
}
