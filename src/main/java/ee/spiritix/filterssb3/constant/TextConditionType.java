package ee.spiritix.filterssb3.constant;

public enum TextConditionType {
  EQUALS("equals"),
  CONTAINS("contains"),
  NOT_CONTAINS("not_contains"),
  NOT_EQUALS("not_equals");

  private String value;

  TextConditionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
