# JSON-FG for Java

This library aims to make encoding and decoding of JSON-FG documents as easy as possible. This library is experimental
and was developed as a community effort during the [OGC’s October 2023 Open Standards Code Sprint](https://developer.ogc.org/sprints/22/)
in London. This library is compatible with [v0.2.1](https://docs.ogc.org/DRAFTS/21-045.html) of the JSON-FG specification.

## Getting started

The following example is based on JTS (Java Topology Suite), combined with the Jackson encoding library. Currently, only
JTS and Jackson integrations have been implemented. Other integrations could be added in the future.

No artifacts are being published (yet), so the project has to be compiled locally using Maven:

```bash
mvn clean install
```

Include the following dependencies in the application's POM file:

```xml
<dependencies>
  <dependency>
    <groupId>nl.geonovum.labs.jsonfg</groupId>
    <artifactId>json-fg-jackson</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>nl.geonovum.labs.jsonfg</groupId>
    <artifactId>json-fg-jts</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
```
### Encoding features

The library contains basic abstractions which align with the JSON-FG document schemas. These abstractions can be 
instantiated by using builder classes. The result is an immutable object, which can be encoded by a JSON serialization
library, such as Jackson.

```java
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.geonovum.labs.jsonfg.jackson.JSONFGModule;
import nl.geonovum.labs.jsonfg.model.CoordRefSys;
import nl.geonovum.labs.jsonfg.model.Feature;
import nl.geonovum.labs.jsonfg.model.Point;

public class Example {
  
  public String encode(Geometry geometry) {
    var jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule());

    Feature feature = Feature.builder()
        .id("123")
        .coordRefSys(new CoordRefSys[]{new CoordRefSys(URI.CRS84)})
        .geometry(Point.builder()
            .coordinates(new Coordinate(-0.12459925, 51.5007790))
            .build())
        .build();

    return jsonMapper.writeValueAsString(feature);
  }
}
```

### Encoding a JTS Geometry object

When you would just want to encode a JTS Geometry object to a JSON-FG representation, you can use the standard JTS
type mapping.

```java
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.geonovum.labs.jsonfg.jackson.JSONFGModule;
import nl.geonovum.labs.jsonfg.jts.JTSGeometryMapping;
import org.locationtech.jts.geom.Geometry;

public class Example {
  
  public String encode(Geometry geometry) {
    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule(Options.builder()
        .typeMapping(new JTSGeometryMapping())
        .build()));
    return jsonMapper.writeValueAsString(geometry);
  }
}
```

## Design goals

- It should be easy to use this library in conjunction with popular existing tools, such as Jackson and JTS.
- It should not introduce needless extra dependencies. It should leverage libraries which are already available.
- It should be possible to incorporate this library in different types of applications, without doing any assumptions
  about specific frameworks/libraries being used.

## License

This library is published under the [MIT License](LICENSE.md).