package com.example.domain.model.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * エンティティとDBのカラムを対応させるためのコンバータ.
 * LocalDateTimeとTimestampの変換を行う
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  /** {@inheritDoc} */
  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime dateTime) {
    return dateTime == null ? null : Timestamp.valueOf(dateTime);
  }

  /** {@inheritDoc} */
  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp value) {
    return value == null ? null : value.toLocalDateTime();
  }
}
