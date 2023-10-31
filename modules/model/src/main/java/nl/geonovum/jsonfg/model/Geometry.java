package nl.geonovum.jsonfg.model;

public interface Geometry {

  String getType();

  CoordRefSys[] getCoordRefSys();

  Coordinate[] getBbox();
}
