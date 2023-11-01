package nl.geonovum.labs.jsonfg.model;

import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public final class URI {

  private static final Pattern CURIE_PATTERN = Pattern.compile("^\\[([\\w-.]+):([\\w-.]+)]$");

  public static final URI CRS84 = new URI(Prefix.CRS_OGC, "CRS84");

  public static final URI CRS84H = new URI(Prefix.CRS_OGC, "CRS84h");

  private final Prefix prefix;

  private final String value;

  public String getValue(boolean preferCURIE) {
    if (preferCURIE && prefix != null) {
      return String.format("[%s:%s]", prefix.getAlias(), value);
    }

    return value;
  }

  public static URI fromString(String value) {
    var matcher = CURIE_PATTERN.matcher(value);

    if (matcher.matches()) {
      var prefix = Prefix.byAlias(matcher.group(1));
      var localName = matcher.group(2);
      return new URI(prefix, localName);
    }

    return new URI(null, value);
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

    public static Prefix byAlias(String alias) {
      return switch (alias) {
        case "OGC" -> CRS_OGC;
        case "EPSG" -> CRS_EPSG;
        case "ogc-json-fg-1-0.2" -> SPEC_JSONFG102;
        default -> throw new IllegalArgumentException("Unknown URI prefix: " + alias);
      };
    }
  }
}
