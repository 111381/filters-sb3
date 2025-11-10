package ee.spiritix.filterssb3.mapper;

import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.DateConditionType;
import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.constant.TextConditionType;
import ee.spiritix.filterssb3.dto.AmountCriteriaDTO;
import ee.spiritix.filterssb3.dto.DateCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.dto.TextCriteriaDTO;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.entity.DateCriteria;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.entity.TextCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;
import static ee.spiritix.filterssb3.constant.SelectionType.S1;
import static ee.spiritix.filterssb3.constant.SelectionType.S2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeMapperTest {

  private TypeMapper typeMapper;

  @BeforeEach
  void setup() {
    this.typeMapper = new TypeMapper();
  }

  @Test
  @DisplayName("Should map object from source to target class")
  void testMapObject_Success() {
    // Given
    FilterDTO sourceDto = new FilterDTO();
    sourceDto.setSelection(SelectionType.S1);
    sourceDto.setName("Test Filter");

    // When
    Filter result = (Filter) typeMapper.mapObject(sourceDto, Filter.class);

    // Then
    assertNotNull(result);
    assertEquals(sourceDto.getSelection(), result.getSelection());
    assertEquals(sourceDto.getName(), result.getName());
  }

  @Test
  @DisplayName("Should map object with null source gracefully")
  void testMapObject_NullSource() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      typeMapper.mapObject(null, Filter.class);
    });
  }

  @Test
  @DisplayName("Should map object with null target class")
  void testMapObject_NullTargetClass() {
    // Given
    FilterDTO sourceDto = new FilterDTO();
    sourceDto.setSelection(SelectionType.S1);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      typeMapper.mapObject(sourceDto, null);
    });
  }

  @Test
  @DisplayName("Should map empty list successfully")
  void testMapList_EmptyList() {
    // Given
    List<FilterDTO> emptyList = new ArrayList<>();

    // When
    List<Filter> result = typeMapper.mapList(emptyList, Filter.class);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("Should map list with single element")
  void testMapList_SingleElement() {
    // Given
    FilterDTO dto = new FilterDTO();
    dto.setSelection(SelectionType.S1);
    dto.setName("Single Filter");
    List<FilterDTO> sourceList = List.of(dto);

    // When
    List<Filter> result = typeMapper.mapList(sourceList, Filter.class);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(dto.getSelection(), result.get(0).getSelection());
    assertEquals(dto.getName(), result.get(0).getName());
  }

  @Test
  @DisplayName("Should map list with multiple elements")
  void testMapList_MultipleElements() {
    // Given
    FilterDTO dto1 = new FilterDTO();
    dto1.setSelection(SelectionType.S1);
    dto1.setName("Filter 1");

    FilterDTO dto2 = new FilterDTO();
    dto2.setSelection(SelectionType.S2);
    dto2.setName("Filter 2");

    FilterDTO dto3 = new FilterDTO();
    dto3.setSelection(SelectionType.S3);
    dto3.setName("Filter 3");

    List<FilterDTO> sourceList = Arrays.asList(dto1, dto2, dto3);

    // When
    List<Filter> result = typeMapper.mapList(sourceList, Filter.class);

    // Then
    assertNotNull(result);
    assertEquals(3, result.size());

    assertEquals(dto1.getSelection(), result.get(0).getSelection());
    assertEquals(dto1.getName(), result.get(0).getName());

    assertEquals(dto2.getSelection(), result.get(1).getSelection());
    assertEquals(dto2.getName(), result.get(1).getName());

    assertEquals(dto3.getSelection(), result.get(2).getSelection());
    assertEquals(dto3.getName(), result.get(2).getName());
  }

  @Test
  @DisplayName("Should throw exception when mapping null list")
  void testMapList_NullList() {
    // When & Then
    assertThrows(NullPointerException.class, () -> {
      typeMapper.mapList(null, Filter.class);
    });
  }

  @Test
  @DisplayName("Should throw exception when mapping list with null target class")
  void testMapList_NullTargetClass() {
    // Given
    List<FilterDTO> sourceList = List.of(new FilterDTO());

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
      typeMapper.mapList(sourceList, null);
    });
  }

  @Test
  @DisplayName("Should handle reverse mapping from entity to DTO")
  void testMapObject_ReverseMapping() {
    // Given
    Filter filter = new Filter();
    filter.setSelection(SelectionType.S1);
    filter.setName("Test Filter");

    // When
    FilterDTO result = (FilterDTO) typeMapper.mapObject(filter, FilterDTO.class);

    // Then
    assertNotNull(result);
    assertEquals(filter.getSelection(), result.getSelection());
    assertEquals(filter.getName(), result.getName());
  }

  @Test
  @DisplayName("Should handle reverse list mapping from entity to DTO")
  void testMapList_ReverseMapping() {
    // Given
    Filter filter1 = new Filter();
    filter1.setSelection(SelectionType.S1);
    filter1.setName("Filter 1");

    Filter filter2 = new Filter();
    filter2.setSelection(SelectionType.S2);
    filter2.setName("Filter 2");

    List<Filter> sourceList = Arrays.asList(filter1, filter2);

    // When
    List<FilterDTO> result = typeMapper.mapList(sourceList, FilterDTO.class);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(filter1.getSelection(), result.get(0).getSelection());
    assertEquals(filter1.getName(), result.get(0).getName());
    assertEquals(filter2.getSelection(), result.get(1).getSelection());
    assertEquals(filter2.getName(), result.get(1).getName());
  }

  @Test
  void mapDtoToEntity_ShouldMapBasicFilterProperties() {
    FilterDTO dto = FilterDTO.builder()
        .name("Test Filter")
        .selection(S1)
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getName()).isEqualTo("Test Filter");
    assertThat(entity.getSelection()).isEqualTo(S1);
  }

  @Test
  void mapDtoToEntity_ShouldMapTextCriteria() {
    TextCriteriaDTO textCriteria = TextCriteriaDTO.builder()
        .filterType(TEXT)
        .conditionType("contains")
        .value("test value")
        .build();

    FilterDTO dto = FilterDTO.builder()
        .name("Text Filter")
        .selection(S1)
        .criteriaList(List.of(textCriteria))
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).hasSize(1);
    assertThat(entity.getFilterCriteria().get(0)).isInstanceOf(TextCriteria.class);

    TextCriteria mappedCriteria = (TextCriteria) entity.getFilterCriteria().get(0);
    assertThat(mappedCriteria.getTextValue()).isEqualTo("test value");
    assertThat(mappedCriteria.getConditionType()).isEqualTo(TextConditionType.CONTAINS);
  }

  @Test
  void mapDtoToEntity_ShouldMapDateCriteria() {
    DateCriteriaDTO dateCriteria = DateCriteriaDTO.builder()
        .filterType(DATE)
        .conditionType("is_after")
        .value(LocalDate.of(2024, 1, 1))
        .build();

    FilterDTO dto = FilterDTO.builder()
        .name("Date Filter")
        .selection(S1)
        .criteriaList(List.of(dateCriteria))
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).hasSize(1);
    assertThat(entity.getFilterCriteria().get(0)).isInstanceOf(DateCriteria.class);

    DateCriteria mappedCriteria = (DateCriteria) entity.getFilterCriteria().get(0);
    assertThat(mappedCriteria.getDateValue()).isEqualTo(LocalDate.of(2024, 1, 1));
    assertThat(mappedCriteria.getConditionType()).isEqualTo(DateConditionType.IS_AFTER);
  }

  @Test
  void mapDtoToEntity_ShouldMapAmountCriteria() {
    AmountCriteriaDTO amountCriteria = AmountCriteriaDTO.builder()
        .filterType(AMOUNT)
        .conditionType("greater_than")
        .value(new BigDecimal("100.50"))
        .build();

    FilterDTO dto = FilterDTO.builder()
        .name("Amount Filter")
        .selection(S1)
            .criteriaList(List.of(amountCriteria))
            .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).hasSize(1);
    assertThat(entity.getFilterCriteria().get(0)).isInstanceOf(AmountCriteria.class);

    AmountCriteria mappedCriteria = (AmountCriteria) entity.getFilterCriteria().get(0);
    assertThat(mappedCriteria.getAmountValue()).isEqualByComparingTo(new BigDecimal("100.50"));
    assertThat(mappedCriteria.getConditionType()).isEqualTo(AmountConditionType.GREATER_THAN);
  }

  @Test
  void mapEntityToDto_ShouldMapTextCriteria() {
    Filter filter = new Filter();
    filter.setId(1L);
    filter.setName("Text Filter");
    filter.setSelection(S1);

    TextCriteria textCriteria = TextCriteria.builder()
        .filter(filter)
        .conditionType(TextConditionType.EQUALS)
        .textValue("test")
        .build();

    filter.setFilterCriteria(List.of(textCriteria));

    FilterDTO dto = (FilterDTO) typeMapper.mapObject(filter, FilterDTO.class);

    assertThat(dto.getName()).isEqualTo("Text Filter");
    assertThat(dto.getCriteriaList()).hasSize(1);
    assertThat(dto.getCriteriaList().get(0)).isInstanceOf(TextCriteriaDTO.class);

    TextCriteriaDTO mappedCriteria = (TextCriteriaDTO) dto.getCriteriaList().get(0);
    assertThat(mappedCriteria.getFilterType()).isEqualTo(TEXT);
    assertThat(mappedCriteria.getConditionType()).isEqualTo("equals");
    assertThat(mappedCriteria.getValue()).isEqualTo("test");
  }

  @Test
  void mapEntityToDto_ShouldMapDateCriteria() {
    Filter filter = new Filter();
    filter.setId(1L);
    filter.setName("Date Filter");
    filter.setSelection(S2);

    DateCriteria dateCriteria = DateCriteria.builder()
        .filter(filter)
        .conditionType(DateConditionType.IS_BEFORE)
        .dateValue(LocalDate.of(2024, 12, 31))
        .build();

    filter.setFilterCriteria(List.of(dateCriteria));

    FilterDTO dto = (FilterDTO) typeMapper.mapObject(filter, FilterDTO.class);

    assertThat(dto.getCriteriaList()).hasSize(1);
    assertThat(dto.getCriteriaList().get(0)).isInstanceOf(DateCriteriaDTO.class);

    DateCriteriaDTO mappedCriteria = (DateCriteriaDTO) dto.getCriteriaList().get(0);
    assertThat(mappedCriteria.getFilterType()).isEqualTo(DATE);
    assertThat(mappedCriteria.getValue()).isEqualTo(LocalDate.of(2024, 12, 31));
  }

  @Test
  void mapEntityToDto_ShouldMapAmountCriteria() {
    Filter filter = new Filter();
    filter.setId(1L);
    filter.setName("Amount Filter");
    filter.setSelection(S1);

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .filter(filter)
        .conditionType(AmountConditionType.LESS_THAN)
        .amountValue(new BigDecimal("50.00"))
        .build();

    filter.setFilterCriteria(List.of(amountCriteria));

    FilterDTO dto = (FilterDTO) typeMapper.mapObject(filter, FilterDTO.class);

    assertThat(dto.getCriteriaList()).hasSize(1);
    assertThat(dto.getCriteriaList().get(0)).isInstanceOf(AmountCriteriaDTO.class);

    AmountCriteriaDTO mappedCriteria = (AmountCriteriaDTO) dto.getCriteriaList().get(0);
    assertThat(mappedCriteria.getFilterType()).isEqualTo(AMOUNT);
    assertThat(mappedCriteria.getValue()).isEqualByComparingTo(new BigDecimal("50.00"));
  }

  @Test
  void mapList_ShouldMapMultipleEntities() {
    Filter filter1 = new Filter();
    filter1.setId(1L);
    filter1.setName("Filter 1");
    filter1.setSelection(S1);

    Filter filter2 = new Filter();
    filter2.setId(2L);
    filter2.setName("Filter 2");
    filter2.setSelection(S2);

    List<Filter> filters = List.of(filter1, filter2);

    List<FilterDTO> dtos = typeMapper.mapList(filters, FilterDTO.class);

    assertThat(dtos).hasSize(2);
    assertThat(dtos.get(0).getName()).isEqualTo("Filter 1");
    assertThat(dtos.get(1).getName()).isEqualTo("Filter 2");
  }

  @Test
  void mapDtoToEntity_ShouldHandleMultipleCriteria() {
    TextCriteriaDTO textCriteria = TextCriteriaDTO.builder()
        .filterType(TEXT)
        .conditionType("contains")
        .value("test")
        .build();

    DateCriteriaDTO dateCriteria = DateCriteriaDTO.builder()
        .filterType(DATE)
        .conditionType("is_after")
        .value(LocalDate.of(2024, 1, 1))
        .build();

    FilterDTO dto = FilterDTO.builder()
        .name("Multi Criteria Filter")
        .selection(S1)
        .criteriaList(List.of(textCriteria, dateCriteria))
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).hasSize(2);
    assertThat(entity.getFilterCriteria().get(0)).isInstanceOf(TextCriteria.class);
    assertThat(entity.getFilterCriteria().get(1)).isInstanceOf(DateCriteria.class);
  }

  @Test
  void mapDtoToEntity_ShouldHandleEmptyCriteriaList() {
    FilterDTO dto = FilterDTO.builder()
        .name("Empty Filter")
        .selection(S1)
        .criteriaList(new ArrayList<>())
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).isEmpty();
  }

  @Test
  void mapDtoToEntity_ShouldHandleNullCriteriaList() {
    FilterDTO dto = FilterDTO.builder()
        .name("Null Criteria Filter")
        .selection(S1)
        .build();

    Filter entity = (Filter) typeMapper.mapObject(dto, Filter.class);

    assertThat(entity.getFilterCriteria()).isNotNull();
  }

  @Test
  void addCriteria_ShouldEstablishBidirectionalRelationship() {
    Filter filter = new Filter();
    filter.setName("Test Filter");
    filter.setSelection(S1);

    TextCriteria textCriteria = TextCriteria.builder()
        .conditionType(TextConditionType.CONTAINS)
        .textValue("test")
        .build();

    typeMapper.addCriteria(filter, textCriteria);

    assertThat(filter.getFilterCriteria()).contains(textCriteria);
    assertThat(textCriteria.getFilter()).isEqualTo(filter);
  }
}
