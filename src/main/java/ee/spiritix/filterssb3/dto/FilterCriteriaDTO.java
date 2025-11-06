package ee.spiritix.filterssb3.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;

@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "filterType",
    use = JsonTypeInfo.Id.NAME,
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AmountCriteriaDTO.class, name = AMOUNT),
    @JsonSubTypes.Type(value = DateCriteriaDTO.class, name = DATE),
    @JsonSubTypes.Type(value = TextCriteriaDTO.class, name = TEXT)
})
public abstract class FilterCriteriaDTO {
}
