package ee.spiritix.filterssb3.dto;

import ee.spiritix.filterssb3.constant.SelectionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  @NotEmpty
  private String name;
  @NotNull
  private SelectionType selection;
  @NotEmpty
  private List<FilterCriteriaDTO> criteriaList;
}
