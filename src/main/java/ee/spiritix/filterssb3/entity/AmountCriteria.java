package ee.spiritix.filterssb3.entity;

import ee.spiritix.filterssb3.constant.AmountConditionType;
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

import java.math.BigDecimal;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "amount_criteria")
@DiscriminatorValue(AMOUNT)
public class AmountCriteria extends FilterCriteria {

  @Enumerated(EnumType.STRING)
  @Column(name = "condition_type")
  private AmountConditionType conditionType;

  @Column(name = "amount_value")
  private BigDecimal amountValue;
}
