package com.tn.query.jpa;

import static java.util.Collections.emptyList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tn.query.QueryException;
import com.tn.query.QueryParseException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = JpaPredicateFactoryIntegrationTest.TestConfiguration.class)
class JpaPredicateFactoryIntegrationTest
{
  @Autowired
  TargetRepository targetRepository;

  @BeforeEach
  void deleteAll()
  {
    this.targetRepository.deleteAll();
  }

  @Test
  @Transactional
  void shouldMatchBoolean()
  {
    Target target = new Target();
    target.booleanValue = true;

    this.targetRepository.save(target);

    assertFindWhere("booleanValue = true", target);
    assertFindWhere("booleanValue = false", emptyList());
    assertFindWhere("booleanValue = X", emptyList());

    assertFindWhere("booleanValue != true", emptyList());
    assertFindWhere("booleanValue != false", target);
    assertFindWhere("booleanValue != X", target);

    assertFindWhere("booleanValue > true", emptyList());
    assertFindWhere("booleanValue > false", target);
    assertFindWhere("booleanValue > X", target);

    assertFindWhere("booleanValue >= true", target);
    assertFindWhere("booleanValue >= false", target);
    assertFindWhere("booleanValue >= X", target);

    assertFindWhere("booleanValue < true", emptyList());
    assertFindWhere("booleanValue < false", emptyList());
    assertFindWhere("booleanValue < X", emptyList());

    assertFindWhere("booleanValue <= true", target);
    assertFindWhere("booleanValue <= false", emptyList());
    assertFindWhere("booleanValue <= X", emptyList());

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("booleanValue ≈ true"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("booleanValue !≈ false"));

    assertFindWhere("booleanValue ∈ [true, false]", List.of(target));
    assertFindWhere("booleanValue ∈ [true]", List.of(target));
    assertFindWhere("booleanValue ∈ [false]", emptyList());
    assertFindWhere("booleanValue ∈ X", emptyList());
  }

  @Test
  void shouldMatchByte()
  {
    Target target = new Target();
    target.byteValue = 1;

    this.targetRepository.save(target);

    assertFindWhere("byteValue = 1", target);
    assertFindWhere("byteValue = 2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue = X"));

    assertFindWhere("byteValue != 1");
    assertFindWhere("byteValue != 2", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue != X"));

    assertFindWhere("byteValue > 0", target);
    assertFindWhere("byteValue > 1");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue > X"));

    assertFindWhere("byteValue >= 0", target);
    assertFindWhere("byteValue >= 1", target);
    assertFindWhere("byteValue >= 2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue >= X"));

    assertFindWhere("byteValue < 2", target);
    assertFindWhere("byteValue < 1");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue < X"));

    assertFindWhere("byteValue <= 2", target);
    assertFindWhere("byteValue <= 1", target);
    assertFindWhere("byteValue <= 0");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue ≈ 2"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue !≈ 1"));

    assertFindWhere("byteValue ∈ [1, 2]", target);
    assertFindWhere("byteValue ∈ [1]", target);
    assertFindWhere("byteValue ∈ [2]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("byteValue ∈ X"));
  }

  @Test
  void shouldMatchChar()
  {
    Target target = new Target();
    target.charValue = 'b';

    this.targetRepository.save(target);

    assertFindWhere("charValue = b", target);
    assertFindWhere("charValue = c");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue = bc"));

    assertFindWhere("charValue != b");
    assertFindWhere("charValue != c", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue = bc"));

    assertFindWhere("charValue > a", target);
    assertFindWhere("charValue > b");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue > bc"));

    assertFindWhere("charValue >= a", target);
    assertFindWhere("charValue >= b", target);
    assertFindWhere("charValue >= c");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue >= bc"));

    assertFindWhere("charValue < c", target);
    assertFindWhere("charValue < b");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue < bc"));

    assertFindWhere("charValue <= c", target);
    assertFindWhere("charValue <= b", target);
    assertFindWhere("charValue <= a");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue <= bc"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue ≈ b"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue !≈ c"));

    assertFindWhere("charValue ∈ [a, b, c]", target);
    assertFindWhere("charValue ∈ [b]", target);
    assertFindWhere("charValue ∈ [a]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("charValue ∈ ab"));
  }

  @Test
  void shouldMatchDate()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, Calendar.FEBRUARY, 5, 0, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    Target target = new Target();
    target.dateValue = calendar.getTime();

    this.targetRepository.save(target);

    assertFindWhere("dateValue = 2021-02-05", target);
    assertFindWhere("dateValue = 2021-02-06");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue = X"));

    assertFindWhere("dateValue != 2021-02-05");
    assertFindWhere("dateValue != 2021-02-06", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue != X"));

    assertFindWhere("dateValue > 2021-02-04", target);
    assertFindWhere("dateValue > 2021-02-05");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue > X"));

    assertFindWhere("dateValue >= 2021-02-04", target);
    assertFindWhere("dateValue >= 2021-02-05", target);
    assertFindWhere("dateValue >= 2021-02-06");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue >= X"));

    assertFindWhere("dateValue < 2021-02-06", target);
    assertFindWhere("dateValue < 2021-02-05");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue < X"));

    assertFindWhere("dateValue <= 2021-02-06", target);
    assertFindWhere("dateValue <= 2021-02-05", target);
    assertFindWhere("dateValue <= 2021-02-04");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ≈ 2021-02-06"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue !≈ 2021-02-05"));

    assertFindWhere("dateValue ∈ [2021-02-04, 2021-02-05, 2021-02-06]", target);
    assertFindWhere("dateValue ∈ [2021-02-05]", target);
    assertFindWhere("dateValue ∈ [2021-02-04]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ∈ X"));
  }

  @Test
  void shouldMatchDateTimeWithMinutes()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, Calendar.FEBRUARY, 5, 10, 15, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    Target target = new Target();
    target.dateValue = calendar.getTime();

    this.targetRepository.save(target);

    assertFindWhere("dateValue = 2021-02-05T10:15", target);
    assertFindWhere("dateValue = 2021-02-05T10:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue = X"));

    assertFindWhere("dateValue != 2021-02-05T10:15");
    assertFindWhere("dateValue != 2021-02-05T10:16", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue != X"));
    
    assertFindWhere("dateValue > 2021-02-05T10:14", target);
    assertFindWhere("dateValue > 2021-02-05T10:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue > X"));

    assertFindWhere("dateValue >= 2021-02-05T10:14", target);
    assertFindWhere("dateValue >= 2021-02-05T10:15", target);
    assertFindWhere("dateValue >= 2021-02-05T10:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue >= X"));

    assertFindWhere("dateValue < 2021-02-05T10:16", target);
    assertFindWhere("dateValue < 2021-02-05T10:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue < X"));
    
    assertFindWhere("dateValue <= 2021-02-05T10:16", target);
    assertFindWhere("dateValue <= 2021-02-05T10:15", target);
    assertFindWhere("dateValue <= 2021-02-05T10:14");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ≈ 2021-02-06T10:16"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue !≈ 2021-02-05T10:15"));

    assertFindWhere("dateValue ∈ [2021-02-05T10:14, 2021-02-05T10:15, 2021-02-05T10:16]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:15]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:14]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ∈ X"));
  }

  @Test
  void shouldMatchDateTimeWithSeconds()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, Calendar.FEBRUARY, 5, 10, 15, 16);
    calendar.set(Calendar.MILLISECOND, 0);

    Target target = new Target();
    target.dateValue = calendar.getTime();
    
    this.targetRepository.save(target);

    assertFindWhere("dateValue = 2021-02-05T10:15:16", target);
    assertFindWhere("dateValue = 2021-02-05T10:15:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue = X"));

    assertFindWhere("dateValue != 2021-02-05T10:15:16");
    assertFindWhere("dateValue != 2021-10-05T10:15:15", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue != X"));

    assertFindWhere("dateValue > 2021-02-05T10:15:15", target);
    assertFindWhere("dateValue > 2021-02-05T10:15:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue > X"));

    assertFindWhere("dateValue >= 2021-02-05T10:15:15", target);
    assertFindWhere("dateValue >= 2021-02-05T10:15:16", target);
    assertFindWhere("dateValue >= 2021-02-05T10:15:17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue >= X"));

    assertFindWhere("dateValue < 2021-02-05T10:15:17", target);
    assertFindWhere("dateValue < 2021-02-05T10:15:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue < X"));

    assertFindWhere("dateValue <= 2021-02-05T10:15:17", target);
    assertFindWhere("dateValue <= 2021-02-05T10:15:16", target);
    assertFindWhere("dateValue <= 2021-02-05T10:15:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ≈ 2021-02-06T10:15:16"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue !≈ 2021-02-05T10:15:15"));

    assertFindWhere("dateValue ∈ [2021-02-05T10:15:15, 2021-02-05T10:15:16, 2021-02-05T10:15:17]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:15:16]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:15:17]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ∈ X"));
  }

  @Test
  void shouldMatchDateTimeWithMilliseconds()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2021, Calendar.FEBRUARY, 5, 10, 15, 16);
    calendar.set(Calendar.MILLISECOND, 17);

    Target target = new Target();
    target.dateValue = calendar.getTime();

    this.targetRepository.save(target);

    assertFindWhere("dateValue = 2021-02-05T10:15:16.17", target);
    assertFindWhere("dateValue = 2021-02-05T10:15:16.18");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue = X"));

    assertFindWhere("dateValue != 2021-02-05T10:15:16.17");
    assertFindWhere("dateValue != 2021-10-05T10:15:16.16", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue != X"));

    assertFindWhere("dateValue > 2021-02-05T10:15:16.16", target);
    assertFindWhere("dateValue > 2021-02-05T10:15:16.17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue > X"));

    assertFindWhere("dateValue >= 2021-02-05T10:15:16.16", target);
    assertFindWhere("dateValue >= 2021-02-05T10:15:16.17", target);
    assertFindWhere("dateValue >= 2021-02-05T10:15:16.18");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue >= X"));

    assertFindWhere("dateValue < 2021-02-05T10:15:16.18", target);
    assertFindWhere("dateValue < 2021-02-05T10:15:16.17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue < X"));

    assertFindWhere("dateValue <= 2021-02-05T10:15:16.17", target);
    assertFindWhere("dateValue <= 2021-02-05T10:16:16.18", target);
    assertFindWhere("dateValue <= 2021-02-05T10:15:16.16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ≈ 2021-02-06T10:15:16.17"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue !≈ 2021-02-05T10:16:16.18"));

    assertFindWhere("dateValue ∈ [2021-02-05T10:15:16.16, 2021-02-05T10:15:16.17, 2021-02-05T10:15:16.18]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:15:16.17]", target);
    assertFindWhere("dateValue ∈ [2021-02-05T10:15:16.16]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("dateValue ∈ X"));
  }

  @Test
  void shouldMatchDouble()
  {
    Target target = new Target();
    target.doubleValue = 1.2;

    this.targetRepository.save(target);
    
    assertFindWhere("doubleValue = 1.2", target);
    assertFindWhere("doubleValue = 1.3");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue = X"));

    assertFindWhere("doubleValue != 1.2");
    assertFindWhere("doubleValue != 1.3", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue != X"));

    assertFindWhere("doubleValue > 1.1", target);
    assertFindWhere("doubleValue > 1.2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue > X"));

    assertFindWhere("doubleValue >= 1.1", target);
    assertFindWhere("doubleValue >= 1.2", target);
    assertFindWhere("doubleValue >= 1.3");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue >= X"));

    assertFindWhere("doubleValue < 1.3", target);
    assertFindWhere("doubleValue < 1.2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue < X"));

    assertFindWhere("doubleValue <= 1.3", target);
    assertFindWhere("doubleValue <= 1.2", target);
    assertFindWhere("doubleValue <= 1.1");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue ≈ 1.3"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue !≈ 1.2"));

    assertFindWhere("doubleValue ∈ [1.1, 1.2, 1.3]", target);
    assertFindWhere("doubleValue ∈ [1.2]", target);
    assertFindWhere("doubleValue ∈ [1.1]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("doubleValue ∈ X"));
  }

  @Test
  void shouldMatchFloat()
  {
    Target target = new Target();
    target.floatValue = 1.2F;

    this.targetRepository.save(target);

    assertFindWhere("floatValue = 1.2", target);
    assertFindWhere("floatValue = 1.3");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue = X"));

    assertFindWhere("floatValue != 1.2");
    assertFindWhere("floatValue != 1.3", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue != X"));

    assertFindWhere("floatValue > 1.1", target);
    assertFindWhere("floatValue > 1.2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue > X"));

    assertFindWhere("floatValue >= 1.1", target);
    assertFindWhere("floatValue >= 1.2", target);
    assertFindWhere("floatValue >= 1.3");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue >= X"));

    assertFindWhere("floatValue < 1.3", target);
    assertFindWhere("floatValue < 1.2");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue < X"));

    assertFindWhere("floatValue <= 1.3", target);
    assertFindWhere("floatValue <= 1.2", target);
    assertFindWhere("floatValue <= 1.1");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue ≈ 1.3"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue !≈ 1.2"));

    assertFindWhere("floatValue ∈ [1.1, 1.2, 1.3]", target);
    assertFindWhere("floatValue ∈ [1.2]", target);
    assertFindWhere("floatValue ∈ [1.1]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("floatValue ∈ X"));
  }

  @Test
  void shouldMatchInt()
  {
    Target target = new Target();
    target.intValue = 10;

    this.targetRepository.save(target);

    assertFindWhere("intValue = 10", target);
    assertFindWhere("intValue = 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue = X"));

    assertFindWhere("intValue != 10");
    assertFindWhere("intValue != 11", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue != X"));

    assertFindWhere("intValue > 9", target);
    assertFindWhere("intValue > 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue > X"));

    assertFindWhere("intValue >= 9", target);
    assertFindWhere("intValue >= 10", target);
    assertFindWhere("intValue >= 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue >= X"));

    assertFindWhere("intValue < 11", target);
    assertFindWhere("intValue < 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue < X"));

    assertFindWhere("intValue <= 11", target);
    assertFindWhere("intValue <= 10", target);
    assertFindWhere("intValue <= 9");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue ≈ 11"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue !≈ 12"));

    assertFindWhere("intValue ∈ [9, 10, 11]", target);
    assertFindWhere("intValue ∈ [10]", target);
    assertFindWhere("intValue ∈ [9]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("intValue ∈ X"));
  }

  @Test
  void shouldMatchLocalDate()
  {
    LocalDate localDate = LocalDate.of(2021, Month.FEBRUARY, 5);

    Target target = new Target();
    target.localDateValue = localDate;

    this.targetRepository.save(target);
    
    assertFindWhere("localDateValue = 2021-02-05", target);
    assertFindWhere("localDateValue = 2021-10-06");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue = X"));

    assertFindWhere("localDateValue != 2021-02-05");
    assertFindWhere("localDateValue != 2021-10-06", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue != X"));

    assertFindWhere("localDateValue > 2021-02-04", target);
    assertFindWhere("localDateValue > 2021-02-05");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue > X"));

    assertFindWhere("localDateValue >= 2021-02-04", target);
    assertFindWhere("localDateValue >= 2021-02-05", target);
    assertFindWhere("localDateValue >= 2021-02-06");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue >= X"));

    assertFindWhere("localDateValue < 2021-02-06", target);
    assertFindWhere("localDateValue < 2021-02-05");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue < X"));

    assertFindWhere("localDateValue <= 2021-02-05", target);
    assertFindWhere("localDateValue <= 2021-02-06", target);
    assertFindWhere("localDateValue <= 2021-02-04");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue ≈ 2021-02-06"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue !≈ 2021-02-05"));

    assertFindWhere("localDateValue ∈ [2021-02-04, 2021-02-05, 2021-02-06]", target);
    assertFindWhere("localDateValue ∈ [2021-02-05]", target);
    assertFindWhere("localDateValue ∈ [2021-02-04]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateValue ∈ X"));
  }

  @Test
  void shouldMatchLocalDateTimeWithMinutes()
  {
    LocalDateTime localDateTime = LocalDateTime.of(2021, Month.FEBRUARY, 5, 10, 15);

    Target target = new Target();
    target.localDateTimeValue = localDateTime;

    this.targetRepository.save(target);

    assertFindWhere("localDateTimeValue = 2021-02-05T10:15", target);
    assertFindWhere("localDateTimeValue = 2021-02-05T10:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue = X"));

    assertFindWhere("localDateTimeValue != 2021-02-05T10:15");
    assertFindWhere("localDateTimeValue != 2021-10-05T10:14", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue != X"));

    assertFindWhere("localDateTimeValue > 2021-02-05T10:14", target);
    assertFindWhere("localDateTimeValue > 2021-02-05T10:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue > X"));

    assertFindWhere("localDateTimeValue >= 2021-02-05T10:14", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue >= X"));

    assertFindWhere("localDateTimeValue < 2021-02-05T10:16", target);
    assertFindWhere("localDateTimeValue < 2021-02-05T10:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue < X"));

    assertFindWhere("localDateTimeValue <= 2021-02-05T10:16", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:14");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ≈ 2021-02-05T10:16"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue !≈ 2021-02-05T10:15"));

    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:14, 2021-02-05T10:15, 2021-02-05T10:16]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:14]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ∈ X"));
  }

  @Test
  void shouldMatchLocalDateTimeWithSeconds()
  {
    LocalDateTime localDateTime = LocalDateTime.of(2021, Month.FEBRUARY, 5, 10, 15, 16);

    Target target = new Target();
    target.localDateTimeValue = localDateTime;

    this.targetRepository.save(target);

    assertFindWhere("localDateTimeValue = 2021-02-05T10:15:16", target);
    assertFindWhere("localDateTimeValue = 2021-02-05T10:15:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue = X"));

    assertFindWhere("localDateTimeValue != 2021-02-05T10:15:16");
    assertFindWhere("localDateTimeValue != 2021-10-05T10:15:15", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue != X"));

    assertFindWhere("localDateTimeValue > 2021-02-05T10:15:15", target);
    assertFindWhere("localDateTimeValue > 2021-02-05T10:15:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue > X"));

    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:15", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:16", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue >= X"));

    assertFindWhere("localDateTimeValue < 2021-02-05T10:15:17", target);
    assertFindWhere("localDateTimeValue < 2021-02-05T10:15:16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue < X"));

    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15:17", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15:16", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15:15");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ≈ 2021-02-05T10:15:16"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue !≈ 2021-02-05T10:15:15"));

    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:15, 2021-02-05T10:15:16, 2021-02-05T10:15:17]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:16]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:17]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ∈ X"));
  }

  @Test
  void shouldMatchLocalDateTimeWithMilliseconds()
  {
    LocalDateTime localDateTime = LocalDateTime.of(2021, Month.FEBRUARY, 5, 10, 15, 16, 170_000_000);

    Target target = new Target();
    target.localDateTimeValue = localDateTime;

    this.targetRepository.save(target);

    assertFindWhere("localDateTimeValue = 2021-02-05T10:15:16.17", target);
    assertFindWhere("localDateTimeValue = 2021-02-05T10:15:16.18");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue = X"));

    assertFindWhere("localDateTimeValue != 2021-02-05T10:15:16.17");
    assertFindWhere("localDateTimeValue != 2021-10-05T10:15:16.16", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue != X"));

    assertFindWhere("localDateTimeValue > 2021-02-05T10:15:16.16", target);
    assertFindWhere("localDateTimeValue > 2021-02-05T10:15:16.17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue > X"));

    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:16.16", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:16.17", target);
    assertFindWhere("localDateTimeValue >= 2021-02-05T10:15:16.18");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue >= X"));

    assertFindWhere("localDateTimeValue < 2021-02-05T10:15:16.18", target);
    assertFindWhere("localDateTimeValue < 2021-02-05T10:15:16.17");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue < X"));

    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15:16.17", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:16:16.18", target);
    assertFindWhere("localDateTimeValue <= 2021-02-05T10:15:16.16");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ≈ 2021-02-05T10:15:16.18"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue !≈ 2021-02-05T10:15:16.17"));

    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:16.16, 2021-02-05T10:15:16.17, 2021-02-05T10:15:16.18]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:16.17]", target);
    assertFindWhere("localDateTimeValue ∈ [2021-02-05T10:15:16.16]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("localDateTimeValue ∈ X"));
  }

  @Test
  void shouldMatchLong()
  {
    Target target = new Target();
    target.longValue = 10L;

    this.targetRepository.save(target);

    assertFindWhere("longValue = 10", target);
    assertFindWhere("longValue = 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue = X"));

    assertFindWhere("longValue != 10");
    assertFindWhere("longValue != 11", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue != X"));

    assertFindWhere("longValue > 9", target);
    assertFindWhere("longValue > 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue > X"));

    assertFindWhere("longValue >= 9", target);
    assertFindWhere("longValue >= 10", target);
    assertFindWhere("longValue >= 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue >= X"));

    assertFindWhere("longValue < 11", target);
    assertFindWhere("longValue < 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue < X"));

    assertFindWhere("longValue <= 11", target);
    assertFindWhere("longValue <= 10", target);
    assertFindWhere("longValue <= 9");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue ≈ 11"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue !≈ 10"));

    assertFindWhere("longValue ∈ [9, 10, 11]", target);
    assertFindWhere("longValue ∈ [10]", target);
    assertFindWhere("longValue ∈ [9]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("longValue ∈ X"));
  }

  @Test
  void shouldMatchShort()
  {
    Target target = new Target();
    target.shortValue = 10;

    this.targetRepository.save(target);

    assertFindWhere("shortValue = 10", target);
    assertFindWhere("shortValue = 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue = X"));

    assertFindWhere("shortValue != 10");
    assertFindWhere("shortValue != 11", target);
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue != X"));

    assertFindWhere("shortValue > 9", target);
    assertFindWhere("shortValue > 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue > X"));

    assertFindWhere("shortValue >= 9", target);
    assertFindWhere("shortValue >= 10", target);
    assertFindWhere("shortValue >= 11");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue >= X"));

    assertFindWhere("shortValue < 11", target);
    assertFindWhere("shortValue < 10");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue < X"));

    assertFindWhere("shortValue <= 11", target);
    assertFindWhere("shortValue <= 10", target);
    assertFindWhere("shortValue <= 9");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue <= X"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue ≈ 11"));

    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue !≈ 10"));

    assertFindWhere("shortValue ∈ [9, 10, 11]", target);
    assertFindWhere("shortValue ∈ [10]", target);
    assertFindWhere("shortValue ∈ [9]");
    assertThrows(QueryException.class, () -> this.targetRepository.findWhere("shortValue ∈ X"));
  }

  @Test
  void shouldMatchString()
  {
    Target target = new Target();
    target.stringValue = "BB";

    this.targetRepository.save(target);

    assertFindWhere("stringValue = BB", target);
    assertFindWhere("stringValue = AB");

    assertFindWhere("stringValue != BB");
    assertFindWhere("stringValue != AB", target);

    assertFindWhere("stringValue > AB", target);
    assertFindWhere("stringValue > BB");

    assertFindWhere("stringValue >= AB", target);
    assertFindWhere("stringValue >= BB", target);
    assertFindWhere("stringValue >= BC");

    assertFindWhere("stringValue < BC", target);
    assertFindWhere("stringValue < BB");

    assertFindWhere("stringValue <= BC", target);
    assertFindWhere("stringValue <= BB", target);
    assertFindWhere("stringValue <= AB");

    assertFindWhere("stringValue ≈ B*", target);
    assertFindWhere("stringValue ≈ *B", target);
    assertFindWhere("stringValue ≈ *B*", target);
    assertFindWhere("stringValue ≈ *A*");
    assertFindWhere("stringValue ≈ *A*");
    assertFindWhere("stringValue ≈ *A*");

    assertFindWhere("stringValue !≈ B*");
    assertFindWhere("stringValue !≈ *B");
    assertFindWhere("stringValue !≈ *B*");
    assertFindWhere("stringValue !≈ *A*", target);
    assertFindWhere("stringValue !≈ *A*", target);
    assertFindWhere("stringValue !≈ *A*", target);

    assertFindWhere("stringValue ∈ [AB, BB, BC]", target);
    assertFindWhere("stringValue ∈ [BB]", target);
    assertFindWhere("stringValue ∈ [AB]");
  }

  @Test
  void shouldMatchNull()
  {
    Target target = new Target();

    this.targetRepository.save(target);

    assertFindWhere("booleanValue = null", target);
    assertFindWhere("byteValue = null", target);
    assertFindWhere("charValue = null", target);
    assertFindWhere("dateValue = null", target);
    assertFindWhere("doubleValue = null", target);
    assertFindWhere("floatValue = null", target);
    assertFindWhere("intValue = null", target);
    assertFindWhere("localDateValue = null", target);
    assertFindWhere("localDateTimeValue = null", target);
    assertFindWhere("longValue = null", target);
    assertFindWhere("shortValue = null", target);
    assertFindWhere("stringValue = null", target);
  }

  @Test
  void shouldNotMatchUnknownField()
  {
    assertThrows(QueryParseException.class, () -> this.targetRepository.findWhere("unknown = anything"));
  }

  @Test
  void shouldMatchWithAnd()
  {
    Target target = new Target();
    target.stringValue = "Testing";
    target.intValue = 123;

    this.targetRepository.save(target);

    assertFindWhere("stringValue = Testing && intValue = 123", target);
    assertFindWhere("stringValue = X && intValue = 123");
    assertFindWhere("stringValue = Testing && intValue = 0");
  }

  @Test
  void shouldMatchWithOr()
  {
    Target target = new Target();
    target.stringValue = "Testing";
    target.intValue = 123;

    this.targetRepository.save(target);

    assertFindWhere("stringValue = Testing || intValue = 123", target);
    assertFindWhere("stringValue = X || intValue = 123", target);
    assertFindWhere("stringValue = Testing || intValue = 0", target);
    assertFindWhere("stringValue = X || intValue = 0");
  }

  @Test
  void shouldMatchMultipleLogicalOperatorsWithParenthesis()
  {
    Target target = new Target();
    target.booleanValue = true;
    target.stringValue = "Testing";
    target.intValue = 123;

    this.targetRepository.save(target);

    assertFindWhere("booleanValue = true || (stringValue = X && intValue = 1)", target);
    assertFindWhere("booleanValue = false || (stringValue = Testing && intValue = 123)", target);
    assertFindWhere("booleanValue = false || (stringValue = X && intValue = 1)");
  }

  @Test
  void shouldPaginateResults()
  {
    Target target1 = new Target();
    target1.stringValue = "AA";

    Target target2 = new Target();
    target2.stringValue = "BB";

    Target target3 = new Target();
    target3.stringValue = "AA";

    Target target4 = new Target();
    target4.stringValue = "AA";

    this.targetRepository.saveAll(List.of(target1, target2, target3, target4));

    assertPage(List.of(target1, target3), 0, 2, 3, this.targetRepository.findWhere("stringValue = AA", PageRequest.of(0, 2)));
    assertPage(List.of(target4), 1, 2, 3, this.targetRepository.findWhere("stringValue = AA", PageRequest.of(1, 2)));

    // Invoking Spring implementation to ensure use of Page is correct.
    assertPage(List.of(target1, target2, target3), 0, 3, 4, this.targetRepository.findAll(PageRequest.of(0, 3)));
    assertPage(List.of(target4), 1, 3, 4, this.targetRepository.findAll(PageRequest.of(1, 3)));
  }

  @Test
  void shouldPaginateResultsAndSort()
  {
    Target target1 = new Target();
    target1.stringValue = "AA";

    Target target2 = new Target();
    target2.stringValue = "BB";

    Target target3 = new Target();
    target3.stringValue = "AA";

    Target target4 = new Target();
    target4.stringValue = "AA";

    this.targetRepository.saveAll(List.of(target1, target2, target3, target4));

    Sort sort = Sort.by(Sort.Direction.DESC, "id");

    assertPage(List.of(target4, target3), 0, 2, 3, this.targetRepository.findWhere("stringValue = AA", PageRequest.of(0, 2, sort)));
    assertPage(List.of(target1), 1, 2, 3, this.targetRepository.findWhere("stringValue = AA", PageRequest.of(1, 2, sort)));
  }

  private void assertPage(List<Target> elements, int pageNumber, int pageSize, int total, Page<Target> page)
  {
    assertEquals(elements,  page.getContent());
    assertEquals(pageNumber, page.getNumber());
    assertEquals(pageSize, page.getSize());
    assertEquals(total, page.getTotalElements());
  }

  private void assertFindWhere(String query, Target... expected)
  {
    assertFindWhere(query, List.of(expected));
  }

  private void assertFindWhere(String query, List<Target> expected)
  {
    assertEquals(expected, this.targetRepository.findWhere(query));
  }

  @Configuration
  @EnableAutoConfiguration
  @EnableJpaRepositories("com.tn.query.jpa")
  static class TestConfiguration
  {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    TargetRepositoryImpl targetRepositoryImpl(EntityManager entityManager)
    {
      return new TargetRepositoryImpl(entityManager);
    }
  }
}
