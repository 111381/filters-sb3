package ee.spiritix.filterssb3.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilterDTO {

  private String name;
  private String selection;
  private List<FilterCriteriaDTO> criteriaList;
}
