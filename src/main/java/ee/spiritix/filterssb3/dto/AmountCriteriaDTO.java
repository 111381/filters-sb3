package ee.spiritix.filterssb3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmountCriteriaDTO extends FilterCriteriaDTO {

  private String filterType;
  private String conditionType;
  private BigDecimal value;
}
