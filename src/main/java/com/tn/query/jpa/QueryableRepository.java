package com.tn.query.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface QueryableRepository<T>
{
  Iterable<T> findWhere(String query);

  Iterable<T> findWhere(String query, Sort sort);

  Page<T> findWhere(String query, PageRequest pageRequest);
}
