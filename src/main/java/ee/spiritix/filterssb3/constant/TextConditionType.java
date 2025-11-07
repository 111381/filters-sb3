package ee.spiritix.filterssb3.constant;

import lombok.Getter;

@Getter
public enum TextConditionType {
  EQUALS("equals"),
  CONTAINS("contains"),
  NOT_CONTAINS("not_contains"),
  NOT_EQUALS("not_equals");

  private final String value;

  TextConditionType(String value) {
    this.value = value;
  }
}
