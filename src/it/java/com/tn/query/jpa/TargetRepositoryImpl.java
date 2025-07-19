package com.tn.query.jpa;

import jakarta.persistence.EntityManager;

public class TargetRepositoryImpl extends AbstractQueryableRepository<Target>
{
  public TargetRepositoryImpl(EntityManager entityManager)
  {
    super(entityManager);
  }
}
