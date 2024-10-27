package com.tn.query.jpa;

import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import com.tn.query.PredicateFactory;
import com.tn.query.QueryException;
import com.tn.query.QueryParseException;

public class JpaPredicateFactory implements PredicateFactory<Predicate>
{
  private static final String LIKE_WILDCARD = "%";
  private static final String WILDCARD = "*";

  private final CriteriaBuilder criteriaBuilder;
  private final Map<String, Expression<?>> nameMappings;

  public JpaPredicateFactory(CriteriaBuilder criteriaBuilder, Map<String, Expression<?>> nameMappings)
  {
    this.criteriaBuilder = criteriaBuilder;
    this.nameMappings = nameMappings;
  }

  @Override
  public Predicate equal(String left, Object right)
  {
    return right != null ? this.criteriaBuilder.equal(nameMapping(left), right) : this.criteriaBuilder.isNull(nameMapping(left));
  }

  @Override
  public Predicate notEqual(String left, Object right)
  {
    return right != null ? this.criteriaBuilder.notEqual(nameMapping(left), right) : this.criteriaBuilder.isNotNull(nameMapping(left));
  }

  @Override
  public Predicate greaterThan(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.greaterThan(nameMapping(left), comparable(right));
  }

  @Override
  public Predicate greaterThanOrEqual(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.greaterThanOrEqualTo(nameMapping(left), comparable(right));
  }

  @Override
  public Predicate lessThan(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.lessThan(nameMapping(left), comparable(right));
  }

  @Override
  public Predicate lessThanOrEqual(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.lessThanOrEqualTo(nameMapping(left), comparable(right));
  }

  @Override
  public Predicate like(String left, Object right)
  {
    return this.criteriaBuilder.like(nameMapping(left), replaceWildcard(right));
  }

  @Override
  public Predicate notLike(String left, Object right)
  {
    return this.criteriaBuilder.notLike(nameMapping(left), replaceWildcard(right));
  }

  @Override
  public Predicate in(String left, List<?> right)
  {
    CriteriaBuilder.In<Object> in = this.criteriaBuilder.in(nameMapping(left));
    right.forEach(in::value);

    return in;
  }

  @Override
  public Predicate and(Predicate left, Predicate right)
  {
    return this.criteriaBuilder.and(new Predicate[]{left, right});
  }

  @Override
  public Predicate or(Predicate left, Predicate right)
  {
    return this.criteriaBuilder.or(new Predicate[]{left, right});
  }

  @Override
  public Predicate parenthesis(Predicate node)
  {
    //Parenthesis is handled implicitly when parsing queries.
    return node;
  }

  private <T> Expression<T> nameMapping(String left)
  {
    //noinspection unchecked
    Expression<T> nameMapping = (Expression<T>)this.nameMappings.get(left);
    if (nameMapping == null) throw new QueryParseException("Unknown name: " + left);

    return nameMapping;
  }

  @SuppressWarnings("rawtypes")
  private Comparable comparable(Object obj)
  {
    if (!(obj instanceof Comparable)) throw new QueryParseException("Cannot compare: " + obj);

    return (Comparable)obj;
  }

  private String replaceWildcard(Object value)
  {
    if (!(value instanceof String)) throw new QueryException("Like comparisons only work for string values, received: " + value);

    return value.toString().replace(WILDCARD, LIKE_WILDCARD);
  }
}
