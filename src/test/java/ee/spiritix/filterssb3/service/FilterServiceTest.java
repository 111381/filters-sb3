package ee.spiritix.filterssb3.service;

import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.dto.AmountCriteriaDTO;
import ee.spiritix.filterssb3.dto.DateCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.dto.TextCriteriaDTO;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.mapper.TypeMapper;
import ee.spiritix.filterssb3.repository.FilterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

  @Mock
  private FilterRepository filterRepository;

  @Mock
  private TypeMapper typeMapper;

  @InjectMocks
  private FilterService filterService;

  private FilterDTO filterDTO;
  private Filter filterEntity;

  @BeforeEach
  void setUp() {
    filterDTO = FilterDTO.builder()
        .id(1L)
        .name("Test Filter")
        .selection(SelectionType.valueOf("S1"))
        .build();

    filterEntity = new Filter();
    filterEntity.setId(1L);
    filterEntity.setName("Test Filter");
  }

  @Test
  void getAllFilters_ShouldReturnEmptyList_WhenNoFiltersExist() {
    when(filterRepository.findAll()).thenReturn(List.of());
    when(typeMapper.mapList(any(), eq(FilterDTO.class))).thenReturn(List.of());

    List<FilterDTO> result = filterService.getAllFilters();

    assertThat(result).isEmpty();
    verify(filterRepository, times(1)).findAll();
  }

  @Test
  void getAllFilters_ShouldReturnListOfFilters_WhenFiltersExist() {
    List<Filter> filters = List.of(filterEntity);
    List<FilterDTO> filterDTOs = List.of(filterDTO);

    when(filterRepository.findAll()).thenReturn(filters);
    when(typeMapper.mapList(filters, FilterDTO.class)).thenReturn(filterDTOs);

    List<FilterDTO> result = filterService.getAllFilters();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Test Filter");
    verify(filterRepository, times(1)).findAll();
    verify(typeMapper, times(1)).mapList(filters, FilterDTO.class);
  }

  @Test
  void add_ShouldSaveAndReturnFilter() {
    filterDTO.setCriteriaList(List.of(TextCriteriaDTO.builder().build()));

    when(typeMapper.mapObject(filterDTO, Filter.class)).thenReturn(filterEntity);
    when(filterRepository.save(filterEntity)).thenReturn(filterEntity);
    when(typeMapper.mapObject(filterEntity, FilterDTO.class)).thenReturn(filterDTO);

    FilterDTO result = filterService.add(filterDTO);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Test Filter");
    verify(typeMapper, times(1)).mapObject(filterDTO, Filter.class);
    verify(filterRepository, times(1)).save(filterEntity);
    verify(typeMapper, times(1)).mapObject(filterEntity, FilterDTO.class);
  }

  @Test
  void add_ShouldHandleFilterWithTextCriteria() {
    TextCriteriaDTO textCriteria = TextCriteriaDTO.builder()
        .filterType(TEXT)
        .conditionType("contains")
        .value("test")
        .build();

    FilterDTO filterWithCriteria = FilterDTO.builder()
        .name("Text Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria))
        .build();

    when(typeMapper.mapObject(filterWithCriteria, Filter.class)).thenReturn(filterEntity);
    when(filterRepository.save(filterEntity)).thenReturn(filterEntity);
    when(typeMapper.mapObject(filterEntity, FilterDTO.class)).thenReturn(filterWithCriteria);

    FilterDTO result = filterService.add(filterWithCriteria);

    assertThat(result).isNotNull();
    assertThat(result.getCriteriaList()).hasSize(1);
    verify(filterRepository, times(1)).save(any(Filter.class));
  }

  @Test
  void add_ShouldHandleFilterWithDateCriteria() {
    DateCriteriaDTO dateCriteria = DateCriteriaDTO.builder()
        .filterType(DATE)
        .conditionType("after")
        .value(LocalDate.of(2024, 1, 1))
        .build();

    FilterDTO filterWithCriteria = FilterDTO.builder()
        .name("Date Filter")
        .selection(SelectionType.valueOf("S2"))
        .criteriaList(List.of(dateCriteria))
        .build();

    when(typeMapper.mapObject(filterWithCriteria, Filter.class)).thenReturn(filterEntity);
    when(filterRepository.save(filterEntity)).thenReturn(filterEntity);
    when(typeMapper.mapObject(filterEntity, FilterDTO.class)).thenReturn(filterWithCriteria);

    FilterDTO result = filterService.add(filterWithCriteria);

    assertThat(result).isNotNull();
    assertThat(result.getCriteriaList()).hasSize(1);
  }

  @Test
  void add_ShouldHandleFilterWithAmountCriteria() {
    AmountCriteriaDTO amountCriteria = AmountCriteriaDTO.builder()
        .filterType(AMOUNT)
        .conditionType("greater_than")
        .value(new BigDecimal("100.00"))
        .build();

    FilterDTO filterWithCriteria = FilterDTO.builder()
        .name("Amount Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(amountCriteria))
        .build();

    when(typeMapper.mapObject(filterWithCriteria, Filter.class)).thenReturn(filterEntity);
    when(filterRepository.save(filterEntity)).thenReturn(filterEntity);
    when(typeMapper.mapObject(filterEntity, FilterDTO.class)).thenReturn(filterWithCriteria);

    FilterDTO result = filterService.add(filterWithCriteria);

    assertThat(result).isNotNull();
    assertThat(result.getCriteriaList()).hasSize(1);
  }

  @Test
  void add_ShouldHandleFilterWithMultipleCriteria() {
    TextCriteriaDTO textCriteria = TextCriteriaDTO.builder()
        .filterType(TEXT)
        .conditionType("contains")
        .value("test")
        .build();

    DateCriteriaDTO dateCriteria = DateCriteriaDTO.builder()
        .filterType(DATE)
        .conditionType("after")
        .value(LocalDate.of(2024, 1, 1))
        .build();

    FilterDTO filterWithMultipleCriteria = FilterDTO.builder()
        .name("Multi Criteria Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria, dateCriteria))
        .build();

    when(typeMapper.mapObject(filterWithMultipleCriteria, Filter.class)).thenReturn(filterEntity);
    when(filterRepository.save(filterEntity)).thenReturn(filterEntity);
    when(typeMapper.mapObject(filterEntity, FilterDTO.class)).thenReturn(filterWithMultipleCriteria);

    FilterDTO result = filterService.add(filterWithMultipleCriteria);

    assertThat(result).isNotNull();
    assertThat(result.getCriteriaList()).hasSize(2);
  }
}