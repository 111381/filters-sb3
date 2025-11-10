package ee.spiritix.filterssb3.service;

import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.DateConditionType;
import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.constant.TextConditionType;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.entity.DateCriteria;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.entity.FilterCriteria;
import ee.spiritix.filterssb3.entity.TextCriteria;
import ee.spiritix.filterssb3.external.model.FilterableObject;
import ee.spiritix.filterssb3.mapper.TypeMapper;
import ee.spiritix.filterssb3.repository.FilterRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterService {

  private final FilterRepository filterRepository;
  private final TypeMapper typeMapper;

  public List<FilterDTO> getAllFilters() {
    return typeMapper.mapList(filterRepository.findAll(), FilterDTO.class);
  }

  public FilterDTO add(FilterDTO filterDto) {
    if (filterDto.getCriteriaList() == null || filterDto.getCriteriaList().isEmpty()) {
      throw new IllegalArgumentException("Filter must have at least one criteria");
    }
    Filter filter = (Filter) typeMapper.mapObject(filterDto, Filter.class);
    return (FilterDTO) typeMapper.mapObject(filterRepository.save(filter), FilterDTO.class);
  }

  public List<FilterableObject> filterObjectsUsingFilterWithId(List<FilterableObject> objectsList, Long filterId) {
    Filter filter = filterRepository.findById(filterId).orElseThrow();
    FilterFunction filterFunction = mapEntityToFunction(filter);

    return objectsList.stream()
        .filter(filterFunction::filterBySelection)
        .filter(filterableObject -> filterFunction.filterCriteria.stream()
            .allMatch(filterCriteriaInterFace -> filterCriteriaInterFace.applyCriteria(filterableObject)))
        .toList();
  }

  private FilterFunction mapEntityToFunction(Filter entity) {
    return FilterFunction.builder()
        .selection(entity.getSelection())
        .filterCriteria(entity.getFilterCriteria().stream()
            .map(this::mapCriteriaToFunction)
            .toList())
        .build();
  }

  private FilterCriteriaInterface mapCriteriaToFunction(FilterCriteria entity) {
    if (entity instanceof AmountCriteria amountEntity) {
      return AmountCriteriaFunction.builder()
          .conditionType(AmountConditionType.valueOf(amountEntity.getConditionType().getValue().toUpperCase()))
          .amountValue(amountEntity.getAmountValue())
          .build();
    } else if (entity instanceof DateCriteria dateEntity) {
      return DateCriteriaFunction.builder()
          .conditionType(DateConditionType.valueOf(dateEntity.getConditionType().getValue().toUpperCase()))
          .dateValue(dateEntity.getDateValue())
          .build();
    } else if (entity instanceof TextCriteria textEntity) {
      return TextCriteriaFunction.builder()
          .conditionType(TextConditionType.valueOf(textEntity.getConditionType().getValue().toUpperCase()))
          .textValue(textEntity.getTextValue())
          .build();
    }
    throw new IllegalArgumentException("Unknown FilterCriteria type: " + entity.getClass().getName());
  }

  @Builder
  private static class FilterFunction {
    private SelectionType selection;
    private List<FilterCriteriaInterface> filterCriteria;

    private boolean filterBySelection(FilterableObject object) {
      return object.getSelection() == selection;
    }
  }

  private interface FilterCriteriaInterface {
    boolean applyCriteria(FilterableObject object);
  }

  @Builder
  private static class AmountCriteriaFunction implements FilterCriteriaInterface {
    private AmountConditionType conditionType;
    private BigDecimal amountValue;

    @Override
    public boolean applyCriteria(FilterableObject object) {
      return switch (conditionType) {
        case GREATER_THAN -> object.getAmount().compareTo(amountValue) > 0;
        case LESS_THAN -> object.getAmount().compareTo(amountValue) < 0;
        case EQUALS -> object.getAmount().compareTo(amountValue) == 0;
        case NOT_EQUALS -> object.getAmount().compareTo(amountValue) != 0;
      };
    }
  }

  @Builder
  private static class DateCriteriaFunction implements FilterCriteriaInterface {
    private DateConditionType conditionType;
    private LocalDate dateValue;

    @Override
    public boolean applyCriteria(FilterableObject object) {
      return switch (conditionType) {
        case IS -> object.getDate().isEqual(dateValue);
        case IS_NOT -> !object.getDate().isEqual(dateValue);
        case IS_AFTER -> object.getDate().isAfter(dateValue);
        case IS_BEFORE -> object.getDate().isBefore(dateValue);
      };
    }
  }

  @Builder
  private static class TextCriteriaFunction implements FilterCriteriaInterface {
    private TextConditionType conditionType;
    private String textValue;

    @Override
    public boolean applyCriteria(FilterableObject object) {
      return switch (conditionType) {
        case EQUALS -> object.getTitle().equals(textValue);
        case CONTAINS -> object.getTitle().contains(textValue);
        case NOT_CONTAINS -> !object.getTitle().contains(textValue);
        case NOT_EQUALS -> !object.getTitle().equals(textValue);
      };
    }
  }
}
