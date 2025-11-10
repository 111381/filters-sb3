package ee.spiritix.filterssb3.dto;

import ee.spiritix.filterssb3.constant.SelectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {

  private Long id;
  private String name;
  private SelectionType selection;
  private List<FilterCriteriaDTO> criteriaList;
}
