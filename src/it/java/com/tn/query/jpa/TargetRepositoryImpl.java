package com.tn.query.jpa;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import com.tn.query.QueryParser;

public class TargetRepositoryImpl extends AbstractQueryableRepository<Target>
{
  public TargetRepositoryImpl(EntityManager entityManager, CriteriaQuery<Target> criteriaQuery, QueryParser<Predicate> queryParser)
  {
    super(entityManager, criteriaQuery, queryParser);
  }
}
