package com.tn.query.jpa;

public interface QueryableRepository<T>
{
  Iterable<T> findWhere(String query);
}
