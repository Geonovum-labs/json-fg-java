package nl.geonovum.jsonfg.core;

import nl.geonovum.jsonfg.model.CompatibilityMode;

public interface TypeMapping<T, R> {

  Class<T> getType();

  R mapInstance(T instance, CompatibilityMode compatibilityMode);
}
