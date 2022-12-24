package com.tn.query.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

import com.tn.query.QueryParser;

public abstract class AbstractQueryableRepository<T> implements QueryableRepository<T>
{
  private final CriteriaQuery<T> criteriaQuery;
  private final EntityManager entityManager;
  private final QueryParser<Predicate> queryParser;

  public AbstractQueryableRepository(EntityManager entityManager, CriteriaQuery<T> criteriaQuery, QueryParser<Predicate> queryParser)
  {
    this.entityManager = entityManager;
    this.criteriaQuery = criteriaQuery;
    this.queryParser = queryParser;
  }

  @Override
  public List<T> findWhere(String predicate)
  {
    return this.entityManager.createQuery(this.criteriaQuery.where(this.queryParser.parse(predicate))).getResultList();
  }
}
