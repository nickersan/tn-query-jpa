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
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Test;

public class NameMappingsTest
{
  @Test
  void shouldGetFieldsFromClass()
  {
    Root<Subject> root = mockRoot();

    Map<String, Expression<?>> nameMappings = NameMappings.forFields(Subject.class, mockCriteriaQuery(root));

    assertEquals(Set.of("oneValue", "twoValue"), nameMappings.keySet());

    verify(root).get("oneValue");
    verify(root).get("twoValue");
  }

  @Test
  void shouldGetFieldsFromClassExcludingIgnored()
  {
    Root<Subject> root = mockRoot();

    Map<String, Expression<?>> nameMappings = NameMappings.forFields(Subject.class, mockCriteriaQuery(root), List.of("twoValue"));

    assertEquals(Set.of("oneValue"), nameMappings.keySet());

    verify(root).get("oneValue");
  }

  private static CriteriaQuery<Subject> mockCriteriaQuery(Root<Subject> root)
  {
    @SuppressWarnings("unchecked")
    CriteriaQuery<Subject> criteriaQuery = mock(CriteriaQuery.class);
    when(criteriaQuery.from(Subject.class)).thenReturn(root);

    return criteriaQuery;
  }

  @SuppressWarnings("unchecked")
  private static Root<Subject> mockRoot()
  {
    Root<Subject> root = mock(Root.class);
    when(root.get(anyString())).thenReturn(mock(Path.class));

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
