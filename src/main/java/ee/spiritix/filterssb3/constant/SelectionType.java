package ee.spiritix.filterssb3.constant;
import lombok.Getter;

@Getter
public enum SelectionType {

  S1("s1"),
  S2("s2"),
  S3("s3");

  private final String value;

  SelectionType(String value) {
    this.value = value;
  }
}
