# tn-query-jpa

TN Query JPA provides an implementation of [tn-query](https://github.com/nickersan/tn-query#readme) that builds instances of `javax.persistence.criteria.CriteriaQuery` that can be 
used with JPA repositories.

## Usage

Each JPA repository should extend `com.tn.query.jpa.AbstractQueryableRepository`; an instance of `com.tn.query.jpa.JpaQueryParser` should be passed to the JPA repository when it is 
created.

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
A JPA repository could be created as follows:
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

And would be used:
```java
List<Person> people = personRepository.findWhere("((firstName = John && sex = MALE) || (firstName = Jane && sex = FEMALE)) && lastName = Smith");
```