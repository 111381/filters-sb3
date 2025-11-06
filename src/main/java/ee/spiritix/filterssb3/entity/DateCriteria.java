package ee.spiritix.filterssb3.entity;

import ee.spiritix.filterssb3.constant.DateConditionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "date_criteria")
@DiscriminatorValue(DATE)
public class DateCriteria extends FilterCriteria {

  @Enumerated(EnumType.STRING)
  @Column(name = "condition_type")
  private DateConditionType conditionType;

  @Column(name = "date_value")
  private LocalDate dateValue;
}
