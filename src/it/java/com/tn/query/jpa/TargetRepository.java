package com.tn.query.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TargetRepository extends CrudRepository<Target, Integer>, PagingAndSortingRepository<Target, Integer>, QueryableRepository<Target>
{
}
