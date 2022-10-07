# tn-query-jpa

TN Query JPA provides an implementation of [tn-query](https://github.com/nickersan/tn-query#readme) that builds instances of `javax.persistence.criteria.CriteriaQuery` that can be 
used with JPA repositories.

## Usage

Each JPA repository should extend `com.tn.query.jpa.QueryableRepository`.

For example, when using Spring Boot's JPA extension, given a class:
```java
@Entity
public class Person
{
  @Id
  @GeneratedValue
  private int id;
  private String firstName;
  private String lastName;
  private Sex sex;
  ...
}
```

A JPA repository interface could be defined as follows:
```java
public interface PersonRepository extends CrudRepository<Person, Integer>, QueryableRepository<Person>
{  
}
```

Spring Boot treats a class in the same package as the JPA repository interface with an `Impl` suffix as the base class for the dynamically created runtime instance.  In order to 
provide and implementation of `com.tn.query.jpa.QueryableRepository`, a JPA repository class that extends `com.tn.query.jpa.AbstractQueryableRepository` could be defined as 
follows:
```java
public class PersonRepositoryImpl extends AbstractQueryableRepository<Person>
{
  public TargetRepositoryImpl(EntityManager entityManager, CriteriaQuery<Person> criteriaQuery, QueryParser<Person> queryParser)
  {
    super(entityManager, criteriaQuery, queryParser);
  }
}
```

An instance of `PersonRepositoryImpl` could then be created as follows:
```java
@Bean
PersonRepositoryImpl personRepositoryImpl(EntityManager entityManager)
{
  CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
  CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);

  return new PersonRepositoryImpl(
    entityManager,
    criteriaQuery,
    new JpaQueryParser(
      entityManager.getCriteriaBuilder(),
      NameMappings.forFields(Person.class, criteriaQuery),
      ValueMappers.forFields(Person.class)
    )
  );
}
```

Spring will do all the necessary wiring; the resulting `personRepository` could be used as follows:
```java
List<Person> people = personRepository.findWhere("((firstName = John && sex = MALE) || (firstName = Jane && sex = FEMALE)) && lastName = Smith");
```