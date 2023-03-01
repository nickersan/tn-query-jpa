package com.tn.query.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import com.tn.query.AbstractQueryParser;
import com.tn.query.Mapper;
import com.tn.query.QueryParseException;

public class JpaQueryParser extends AbstractQueryParser<Predicate>
{
  private static final String LIKE_WILDCARD = "%";

  private final CriteriaBuilder criteriaBuilder;
  private final Map<String, Expression<?>> nameMappings;

  public JpaQueryParser(CriteriaBuilder criteriaBuilder, Map<String, Expression<?>> nameMappings, Collection<Mapper> valueMappers)
  {
    super(valueMappers);
    this.criteriaBuilder = criteriaBuilder;
    this.nameMappings = nameMappings;
  }

  @Override
  protected Predicate equal(String left, Object right)
  {
    return this.criteriaBuilder.equal(nameMapping(left), right);
  }

  @Override
  protected Predicate notEqual(String left, Object right)
  {
    return this.criteriaBuilder.notEqual(nameMapping(left), right);
  }

  @Override
  protected Predicate greaterThan(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.greaterThan(nameMapping(left), comparable(right));
  }

  @Override
  protected Predicate greaterThanOrEqual(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.greaterThanOrEqualTo(nameMapping(left), comparable(right));
  }

  @Override
  protected Predicate lessThan(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.lessThan(nameMapping(left), comparable(right));
  }

  @Override
  protected Predicate lessThanOrEqual(String left, Object right)
  {
    //noinspection unchecked
    return this.criteriaBuilder.lessThanOrEqualTo(nameMapping(left), comparable(right));
  }

  @Override
  protected Predicate like(String left, Object right)
  {
    return this.criteriaBuilder.like(nameMapping(left), replaceWildcard(right));
  }

  @Override
  protected Predicate notLike(String left, Object right)
  {
    return this.criteriaBuilder.notLike(nameMapping(left), replaceWildcard(right));
  }

  @Override
  protected Predicate in(String left, List<?> right)
  {
    CriteriaBuilder.In<Object> in = this.criteriaBuilder.in(nameMapping(left));
    right.forEach(in::value);

    return in;
  }

  @Override
  protected Predicate and(Predicate left, Predicate right)
  {
    return this.criteriaBuilder.and(new Predicate[]{left, right});
  }

  @Override
  protected Predicate or(Predicate left, Predicate right)
  {
    return this.criteriaBuilder.or(new Predicate[]{left, right});
  }

  @Override
  protected Predicate parenthesis(Predicate node)
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
    checkLikeable(value);

    return value.toString().replace(WILDCARD, LIKE_WILDCARD);
  }
}
