package ee.spiritix.filterssb3.dto;

import ee.spiritix.filterssb3.constant.SelectionType;
import lombok.Data;

import java.util.List;

@Data
public class FilterDTO {

  private String name;
  private SelectionType selection;
  private List<FilterCriteriaDTO> criteriaList;
}
