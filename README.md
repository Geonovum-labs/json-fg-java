# JSON-FG for Java

This library aims to make encoding and decoding of JSON-FG documents as easy as possible. This library is experimental
and was developed as a community effort during the [OGC’s October 2023 Open Standards Code Sprint](https://developer.ogc.org/sprints/22/)
in London. This library is compatible with [v0.2.1](https://docs.ogc.org/DRAFTS/21-045.html) of the JSON-FG specification.

## Getting started

The following example is based on JTS, combined with the Jackson encoder. Currently, only JTS en Jackson integrations
have been implemented. Other integrations could be added in the future.

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

### Encoding a JTS Geometry object

When you would just want to encode a JTS Geometry object to a JSON-FG representation, you can use the standard type
binding.

```java
import com.fasterxml.jackson.databind.json.JsonMapper;
import nl.geonovum.labs.jsonfg.jackson.JSONFGModule;
import org.locationtech.jts.geom.Geometry;

public class Example {
  
  public void encode(Geometry geometry) {
    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JSONFGModule());
    String document = jsonMapper.writeValueAsString(geometry);
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