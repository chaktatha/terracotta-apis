package org.terracotta.entity;

public interface DynamicConfigurationParser<T> {
  Class<T> getType();
  T unmarshallDynamicConfigurationEntity(byte[] configBytes);
  byte[] marshallDynamicConfigurationEntity(T entity);
}
