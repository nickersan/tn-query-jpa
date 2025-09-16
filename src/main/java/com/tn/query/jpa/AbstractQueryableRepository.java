package com.tn.query.jpa;

import static java.util.Collections.emptyList;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.tn.query.DefaultQueryParser;
import com.tn.query.QueryParser;
import com.tn.query.ValueMappers;

public abstract class AbstractQueryableRepository<T> implements QueryableRepository<T>
{
  private final EntityManager entityManager;
  private final Class<T> entityType;

  public AbstractQueryableRepository(EntityManager entityManager)
  {
    this(entityManager, emptyList());
  }

  public AbstractQueryableRepository(EntityManager entityManager, Collection<String> ignoredFieldNames)
  {
    this.entityManager = entityManager;
    this.entityType = entryType();
 }

  @Override
  public Iterable<T> findWhere(String query)
  {
    return this.entityManager.createQuery(entityCriteriaQuery(query, null)).getResultList();
  }

  @Override
  public Iterable<T> findWhere(String query, Sort sort)
  {
    return this.entityManager.createQuery(entityCriteriaQuery(query, sort)).getResultList();
  }

  @Override
  public Page<T> findWhere(String query, PageRequest pageRequest)
  {
    return asPage(
      pageRequest,
      this.entityManager.createQuery(entityCriteriaQuery(query, pageRequest.getSort()))
        .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
        .setMaxResults(pageRequest.getPageSize())
        .getResultList(),
      this.entityManager.createQuery(countCriteriaQuery(query))
        .getSingleResult()
    );
  }

  protected EntityManager entityManager()
  {
    return this.entityManager;
  }

  private CriteriaQuery<T> entityCriteriaQuery(String query, Sort sort)
  {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<T> entityCriteriaQuery = criteriaBuilder.createQuery(this.entityType);
    Root<T> root = entityCriteriaQuery.from(this.entityType);
    entityCriteriaQuery.where(queryParser(criteriaBuilder, root).parse(query));

    if (sort != null) entityCriteriaQuery.orderBy(asOrder(sort, criteriaBuilder, root));

    return entityCriteriaQuery;
  }

  private CriteriaQuery<Long> countCriteriaQuery(String query)
  {
    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> countCriteriaQuery = criteriaBuilder.createQuery(Long.class);
    Root<T> root = countCriteriaQuery.from(this.entityType);
    countCriteriaQuery.where(queryParser(criteriaBuilder, root).parse(query));
    countCriteriaQuery.select(criteriaBuilder.count(root));

    return countCriteriaQuery;
  }

  private List<Order> asOrder(Sort sort, CriteriaBuilder criteriaBuilder, Root<T> root)
  {
    return sort.get()
      .map(asOrder(criteriaBuilder, root))
      .toList();
  }

  private Function<Sort.Order, Order> asOrder(CriteriaBuilder criteriaBuilder, Root<T> root)
  {
    return order -> order.getDirection().isAscending()
      ? criteriaBuilder.asc(root.get(order.getProperty()))
      : criteriaBuilder.desc(root.get(order.getProperty()));
  }

  private Page<T> asPage(PageRequest pageRequest, List<T> elements, long count)
  {
    return new PageImpl<>(
      elements,
      pageRequest,
      count
    );
  }

  private Class<T> entryType()
  {
    //noinspection unchecked
    return (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  private QueryParser<Predicate> queryParser(CriteriaBuilder criteriaBuilder, Root<T> root)
  {
    return new DefaultQueryParser<>(
      new JpaPredicateFactory(criteriaBuilder, NameMappings.forFields(root)),
      ValueMappers.forFields(entityType)
    );
  }
}
