package com.tn.query.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;

import com.tn.query.QueryParser;

public class TargetRepositoryImpl extends AbstractQueryableRepository<Target>
{
  public TargetRepositoryImpl(EntityManager entityManager, CriteriaQuery<Target> criteriaQuery, QueryParser<Predicate> queryParser)
  {
    super(entityManager, criteriaQuery, queryParser);
  }
}
