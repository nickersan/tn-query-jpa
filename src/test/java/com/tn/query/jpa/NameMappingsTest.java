package com.tn.query.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Test;

class NameMappingsTest
{
  @Test
  void shouldGetFieldsFromClass()
  {
    Root<Subject> root = mockRoot();

    Map<String, Expression<?>> nameMappings = NameMappings.forFields(root);

    assertEquals(Set.of("oneValue", "twoValue"), nameMappings.keySet());

    verify(root).get("oneValue");
    verify(root).get("twoValue");
  }

  @Test
  void shouldGetFieldsFromClassExcludingIgnored()
  {
    Root<Subject> root = mockRoot();

    Map<String, Expression<?>> nameMappings = NameMappings.forFields(root, List.of("twoValue"));

    assertEquals(Set.of("oneValue"), nameMappings.keySet());

    verify(root).get("oneValue");
  }

  @SuppressWarnings("unchecked")
  private static Root<Subject> mockRoot()
  {
    Root<Subject> root = mock(Root.class);
    when(root.get(anyString())).thenReturn(mock(Path.class));
    when(root.getJavaType()).thenAnswer(invocation -> Subject.class);

    return root;
  }


  @SuppressWarnings("unused")
  private static class Subject
  {
    private String oneValue;

    @Column(name = "two_value")
    private String twoValue;
  }
}
