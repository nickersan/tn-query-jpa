package com.tn.query.jpa;

import java.util.List;

public interface QueryableRepository<T>
{
  List<T> findWhere(String query);
}
