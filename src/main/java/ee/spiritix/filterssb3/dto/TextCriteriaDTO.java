package ee.spiritix.filterssb3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextCriteriaDTO extends FilterCriteriaDTO {

  private String filterType;
  private String conditionType;
  private String value;
}
