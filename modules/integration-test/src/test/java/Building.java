import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Polygon;

@Getter
@Builder
public class Building {

  private final String id;

  private final int constructionYear;

  private final double surface;

  private final Polygon perimeter;
}
