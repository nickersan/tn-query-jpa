package com.tn.query.jpa;

import org.springframework.data.repository.CrudRepository;

public interface TargetRepository extends CrudRepository<Target, Integer>, QueryableRepository<Target>
{
}
