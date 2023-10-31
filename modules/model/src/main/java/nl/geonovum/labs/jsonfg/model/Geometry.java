package nl.geonovum.labs.jsonfg.model;

public interface Geometry {

  String getType();

  CoordRefSys[] getCoordRefSys();

  Coordinate[] getBbox();
}
