package ee.spiritix.filterssb3.constant;

import lombok.Getter;

@Getter
public enum DateConditionType {
  IS("is"),
  IS_NOT("is_not"),
  IS_AFTER("is_after"),
  IS_BEFORE("is_before");

  private final String value;

  DateConditionType(String value) {
    this.value = value;
  }
}
