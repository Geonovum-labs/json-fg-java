package nl.geonovum.labs.jsonfg.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Coordinate {

  private final double x;

  private final double y;

  private final double z;

  public Coordinate(double x, double y) {
    this(x, y, Double.NaN);
  }
}
