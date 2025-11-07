package ee.spiritix.filterssb3.service;

import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.DateConditionType;
import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.constant.TextConditionType;
import ee.spiritix.filterssb3.dto.AmountCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.dto.TextCriteriaDTO;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.entity.DateCriteria;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.entity.TextCriteria;
import ee.spiritix.filterssb3.external.model.FilterableObject;
import ee.spiritix.filterssb3.repository.FilterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class FilterServiceIntegrationTest {

  @Autowired
  private FilterService filterService;

  @Autowired
  private FilterRepository filterRepository;

  private List<FilterableObject> testObjects;

  @BeforeEach
  void setUp() {
    // Clear database before each test
    filterRepository.deleteAll();

    // Create test data
    testObjects = List.of(
        new FilterableObject(BigDecimal.valueOf(100), "Apple Product", LocalDate.now().minusDays(10), SelectionType.S1),
        new FilterableObject(BigDecimal.valueOf(200), "Banana Item", LocalDate.now().minusDays(5), SelectionType.S1),
        new FilterableObject(BigDecimal.valueOf(300), "Cherry Good", LocalDate.now(), SelectionType.S1),
        new FilterableObject(BigDecimal.valueOf(150), "Apple Special", LocalDate.now().plusDays(5), SelectionType.S2),
        new FilterableObject(BigDecimal.valueOf(250), "Orange Item", LocalDate.now().plusDays(10), SelectionType.S2),
        new FilterableObject(BigDecimal.valueOf(350), "Grape Product", LocalDate.now().plusDays(15), SelectionType.S3)
    );
  }

  @Test
  @DisplayName("Should add filters with DTO and retrieve all filters")
  void testAddAndGetAllFilters() {
    // Given
    FilterDTO filterDTO1 = FilterDTO.builder()
        .name("Test Filter1")
        .selection(SelectionType.S1)
        .criteriaList(List.of(
            AmountCriteriaDTO.builder()
                .filterType("amount")
                .conditionType("greater_than")
                .value(BigDecimal.valueOf(200))
                .build()
        ))
        .build();
    FilterDTO filterDTO2 = FilterDTO.builder()
        .name("Test Filter2")
        .selection(SelectionType.S3)
        .criteriaList(List.of(
            TextCriteriaDTO.builder()
                .filterType("text")
                .conditionType("contains")
                .value("Item")
                .build()
        ))
        .build();

    // When
    filterService.add(filterDTO1);
    filterService.add(filterDTO2);
    List<FilterDTO> allFilters = filterService.getAllFilters();

    // Then
    assertThat(allFilters).hasSize(2);
    assertThat(allFilters.get(0).getName()).isEqualTo("Test Filter1");
    assertThat(allFilters.get(0).getSelection()).isEqualTo(SelectionType.S1);
    assertThat(allFilters.get(1).getName()).isEqualTo("Test Filter2");
    assertThat(allFilters.get(1).getSelection()).isEqualTo(SelectionType.S3);
  }

  @Test
  @DisplayName("Should filter by amount greater than condition")
  void testFilterByAmountGreaterThan() {
    // Given
    Filter filter = new Filter();
    filter.setName("Amount Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.GREATER_THAN)
        .amountValue(BigDecimal.valueOf(150))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2)
        .allMatch(obj -> obj.getAmount().compareTo(BigDecimal.valueOf(150)) > 0)
        .allMatch(obj -> obj.getSelection() == SelectionType.S1);
  }

  @Test
  @DisplayName("Should filter by amount less than condition")
  void testFilterByAmountLessThan() {
    // Given
    Filter filter = new Filter();
    filter.setName("Amount Less Than Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.LESS_THAN)
        .amountValue(BigDecimal.valueOf(250))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).allMatch(obj -> obj.getAmount().compareTo(BigDecimal.valueOf(250)) < 0);
  }

  @Test
  @DisplayName("Should filter by amount equals condition")
  void testFilterByAmountEquals() {
    // Given
    Filter filter = new Filter();
    filter.setName("Amount Equals Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.EQUALS)
        .amountValue(BigDecimal.valueOf(200))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
  }

  @Test
  @DisplayName("Should filter by amount not equals condition")
  void testFilterByAmountNotEquals() {
    // Given
    Filter filter = new Filter();
    filter.setName("Amount Not Equals Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.NOT_EQUALS)
        .amountValue(BigDecimal.valueOf(200))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).noneMatch(obj -> obj.getAmount().compareTo(BigDecimal.valueOf(200)) == 0);
  }

  @Test
  @DisplayName("Should filter by date IS condition")
  void testFilterByDateIs() {
    // Given
    LocalDate targetDate = LocalDate.now();
    Filter filter = new Filter();
    filter.setName("Date Is Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    DateCriteria dateCriteria = DateCriteria.builder()
        .conditionType(DateConditionType.IS)
        .dateValue(targetDate)
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(dateCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getDate()).isEqualTo(targetDate);
  }

  @Test
  @DisplayName("Should filter by date IS_AFTER condition")
  void testFilterByDateIsAfter() {
    // Given
    LocalDate targetDate = LocalDate.now().minusDays(6);
    Filter filter = new Filter();
    filter.setName("Date After Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    DateCriteria dateCriteria = DateCriteria.builder()
        .conditionType(DateConditionType.IS_AFTER)
        .dateValue(targetDate)
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(dateCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).allMatch(obj -> obj.getDate().isAfter(targetDate));
  }

  @Test
  @DisplayName("Should filter by date IS_BEFORE condition")
  void testFilterByDateIsBefore() {
    // Given
    LocalDate targetDate = LocalDate.now().minusDays(4);
    Filter filter = new Filter();
    filter.setName("Date Before Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    DateCriteria dateCriteria = DateCriteria.builder()
        .conditionType(DateConditionType.IS_BEFORE)
        .dateValue(targetDate)
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(dateCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).allMatch(obj -> obj.getDate().isBefore(targetDate));
  }

  @Test
  @DisplayName("Should filter by date IS_NOT condition")
  void testFilterByDateIsNot() {
    // Given
    LocalDate targetDate = LocalDate.now();
    Filter filter = new Filter();
    filter.setName("Date Is Not Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    DateCriteria dateCriteria = DateCriteria.builder()
        .conditionType(DateConditionType.IS_NOT)
        .dateValue(targetDate)
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(dateCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).noneMatch(obj -> obj.getDate().isEqual(targetDate));
  }

  @Test
  @DisplayName("Should filter by text EQUALS condition")
  void testFilterByTextEquals() {
    // Given
    Filter filter = new Filter();
    filter.setName("Text Equals Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.EQUALS)
        .textValue("Apple Product")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Apple Product");
  }

  @Test
  @DisplayName("Should filter by text CONTAINS condition")
  void testFilterByTextContains() {
    // Given
    Filter filter = new Filter();
    filter.setName("Text Contains Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.CONTAINS)
        .textValue("Product")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1).allMatch(obj -> obj.getTitle().contains("Product"));
  }

  @Test
  @DisplayName("Should filter by text NOT_CONTAINS condition")
  void testFilterByTextNotContains() {
    // Given
    Filter filter = new Filter();
    filter.setName("Text Not Contains Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.NOT_CONTAINS)
        .textValue("Product")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).noneMatch(obj -> obj.getTitle().contains("Product"));
  }

  @Test
  @DisplayName("Should filter by text NOT_EQUALS condition")
  void testFilterByTextNotEquals() {
    // Given
    Filter filter = new Filter();
    filter.setName("Text Not Equals Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.NOT_EQUALS)
        .textValue("Apple Product")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(2).noneMatch(obj -> obj.getTitle().equals("Apple Product"));
  }

  @Test
  @DisplayName("Should filter by multiple criteria - ALL must match")
  void testFilterByMultipleCriteria() {
    // Given
    Filter filter = new Filter();
    filter.setName("Multi Criteria Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    // Amount > 150 AND contains "Item"
    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.GREATER_THAN)
        .amountValue(BigDecimal.valueOf(150))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.CONTAINS)
        .textValue("Item")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1); // Only "Banana Item" with amount 200
    assertThat(result.get(0).getTitle()).isEqualTo("Banana Item");
    assertThat(result.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
  }

  @Test
  @DisplayName("Should filter with complex multiple criteria")
  void testComplexMultipleCriteriaFilter() {
    // Given
    Filter filter = new Filter();
    filter.setName("Complex Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    // Amount < 250 AND Date before today AND Contains "Banana"
    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.LESS_THAN)
        .amountValue(BigDecimal.valueOf(250))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    DateCriteria dateCriteria = DateCriteria.builder()
        .conditionType(DateConditionType.IS_BEFORE)
        .dateValue(LocalDate.now())
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(dateCriteria);

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.CONTAINS)
        .textValue("Banana")
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(textCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("Banana Item");
  }

  @Test
  @DisplayName("Should return empty list when no objects match filter")
  void testNoMatchingObjects() {
    // Given
    Filter filter = new Filter();
    filter.setName("No Match Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .conditionType(AmountConditionType.GREATER_THAN)
        .amountValue(BigDecimal.valueOf(1000))
        .filter(filter)
        .build();
    filter.getFilterCriteria().add(amountCriteria);

    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filter.getId());

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should throw exception when filter not found")
  void testFilterNotFound() {
    // When & Then
    assertThrows(Exception.class, () ->
        filterService.filterObjectsUsingFilterWithId(testObjects, 999L)
    );
  }

  @Test
  @DisplayName("Should handle empty object list")
  void testEmptyObjectList() {
    // Given
    Filter filter = new Filter();
    filter.setName("Empty List Filter");
    filter.setSelection(SelectionType.S1);
    filter.setFilterCriteria(new ArrayList<>());
    filter = filterRepository.save(filter);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(new ArrayList<>(), filter.getId());

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should throw exception when adding filter with empty criteria list")
  void testAddFilterWithEmptyCriteriaList() {
    // Given
    FilterDTO filterDTO = FilterDTO.builder()
        .name("Filter Without Criteria")
        .selection(SelectionType.S1)
        .criteriaList(new ArrayList<>())
        .build();

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> filterService.add(filterDTO)
    );

    assertThat(exception.getMessage()).isEqualTo("Filter must have at least one criteria");
  }

  @Test
  @DisplayName("Should throw exception when adding filter with null criteria list")
  void testAddFilterWithNullCriteriaList() {
    // Given
    FilterDTO filterDTO = FilterDTO.builder()
        .name("Filter With Null Criteria")
        .selection(SelectionType.S1)
        .criteriaList(null)
        .build();

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> filterService.add(filterDTO)
    );

    assertThat(exception.getMessage()).isEqualTo("Filter must have at least one criteria");
  }

  @Test
  @DisplayName("Should add filter with multiple criteria using DTO")
  void testAddFilterWithMultipleCriteriaDTO() {
    // Given
    FilterDTO filterDTO = FilterDTO.builder()
        .name("DTO Filter with Criteria")
        .selection(SelectionType.S2)
        .criteriaList(List.of(
            AmountCriteriaDTO.builder()
                .filterType("amount")
                .conditionType("greater_than")
                .value(BigDecimal.valueOf(200))
                .build(),
            TextCriteriaDTO.builder()
                .filterType("text")
                .conditionType("contains")
                .value("Item")
                .build()
        ))
        .build();

    // When
    filterService.add(filterDTO);
    List<FilterDTO> allFilters = filterService.getAllFilters();

    // Then
    assertThat(allFilters).hasSize(1);
    FilterDTO savedFilter = allFilters.get(0);
    assertThat(savedFilter.getName()).isEqualTo("DTO Filter with Criteria");
    assertThat(savedFilter.getSelection()).isEqualTo(SelectionType.S2);
    assertThat(savedFilter.getCriteriaList()).hasSize(2);
  }

  @Test
  @DisplayName("Should correctly map and apply filter from DTO")
  void testFilterFromDTOAppliedCorrectly() {
    // Given
    FilterDTO filterDTO = FilterDTO.builder()
        .name("DTO Based Filter")
        .selection(SelectionType.S2)
        .criteriaList(List.of(
            AmountCriteriaDTO.builder()
                .filterType("amount")
                .conditionType("greater_than")
                .value(BigDecimal.valueOf(200))
                .build()
        ))
        .build();

    long filterId = filterService.add(filterDTO);

    // When
    List<FilterableObject> result = filterService.filterObjectsUsingFilterWithId(testObjects, filterId);

    // Then
    assertThat(result).hasSize(1); // Only S2 with amount 250
    assertThat(result.get(0).getSelection()).isEqualTo(SelectionType.S2);
    assertThat(result.get(0).getAmount()).isGreaterThan(BigDecimal.valueOf(200));
  }
}