package ee.spiritix.filterssb3.entity;

import ee.spiritix.filterssb3.constant.TextConditionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "text_criteria")
@DiscriminatorValue(TEXT)
public class TextCriteria extends FilterCriteria {

  @Enumerated(EnumType.STRING)
  @Column(name = "condition_type")
  private TextConditionType conditionType;

  @Column(name = "text_value", length = 500)
  private String textValue;
}
