package com.tn.query.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings({"LombokEqualsAndHashCodeInspection", "com.haulmont.jpb.LombokEqualsAndHashCodeInspection"})
@Entity
@EqualsAndHashCode(exclude = "id")
@ToString
public class Target
{
  @Id
  @GeneratedValue
  private Long id;

  boolean booleanValue;
  byte byteValue;
  char charValue;
  Date dateValue;
  double doubleValue;
  int intValue;
  LocalDate localDateValue;
  LocalDateTime localDateTimeValue;
  long longValue;
  short shortValue;
  String stringValue;
}
