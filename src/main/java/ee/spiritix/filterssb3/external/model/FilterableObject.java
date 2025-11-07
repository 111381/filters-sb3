package ee.spiritix.filterssb3.external.model;

import ee.spiritix.filterssb3.constant.SelectionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FilterableObject {

  private BigDecimal amount;
  private String title;
  private LocalDate date;
  private SelectionType selection;
}
