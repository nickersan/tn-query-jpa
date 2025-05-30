package com.tn.query.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface QueryableRepository<T>
{
  Iterable<T> findWhere(String query);

  Page<T> findWhere(String query, PageRequest pageRequest);
}
