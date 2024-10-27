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

  Boolean booleanValue;
  Byte byteValue;
  Character charValue;
  Date dateValue;
  Double doubleValue;
  Float floatValue;
  Integer intValue;
  LocalDate localDateValue;
  LocalDateTime localDateTimeValue;
  Long longValue;
  Short shortValue;
  String stringValue;
}
