package nl.geonovum.labs.jsonfg.core;

import nl.geonovum.labs.jsonfg.model.CompatibilityMode;

public interface TypeMapping<T, R> {

  Class<T> getType();

  R mapInstance(T instance, CompatibilityMode compatibilityMode);
}
