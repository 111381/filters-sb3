package ee.spiritix.filterssb3.constant;

public enum AmountConditionType {
  GREATER_THAN("greater_than"),
  LESS_THAN("less_than"),
  EQUALS("equals"),
  NOT_EQUALS("not_equals");

  private String value;

  AmountConditionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
