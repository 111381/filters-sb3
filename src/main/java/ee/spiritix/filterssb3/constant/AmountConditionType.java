package ee.spiritix.filterssb3.constant;

import lombok.Getter;

@Getter
public enum AmountConditionType {
  GREATER_THAN("greater_than"),
  LESS_THAN("less_than"),
  EQUALS("equals"),
  NOT_EQUALS("not_equals");

  private final String value;

  AmountConditionType(String value) {
    this.value = value;
  }
}
