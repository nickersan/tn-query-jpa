package com.tn.query.jpa;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableSet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

public class NameMappings
{
  private NameMappings()
  {
  }

  public static <T> Map<String, Expression<?>> forFields(Root<T> root)
  {
    return forFields(root, emptyList());
  }

  public static <T> Map<String, Expression<?>> forFields(Root<T> root, Collection<String> ignored)
  {
    return fieldNames(root.getJavaType(), ignored).stream().collect(toMap(identity(), expression(root)));
  }

  private static <T> Function<String, Expression<?>> expression(Root<T> root)
  {
    return root::get;
  }

  private static Collection<String> fieldNames(Class<?> subject, Collection<String> ignored)
  {
    return Stream.of(subject.getDeclaredFields())
      .filter(field -> !field.isSynthetic())
      .map(Field::getName)
      .filter(fieldName -> !ignored.contains(fieldName))
      .collect(toUnmodifiableSet());
  }
}
