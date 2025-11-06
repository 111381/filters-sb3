package ee.spiritix.filterssb3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateCriteriaDTO extends FilterCriteriaDTO {

  private String filterType;
  private String conditionType;
  private LocalDate value;
}
