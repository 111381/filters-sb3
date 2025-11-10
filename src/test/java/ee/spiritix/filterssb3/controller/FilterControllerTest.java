package ee.spiritix.filterssb3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.dto.AmountCriteriaDTO;
import ee.spiritix.filterssb3.dto.DateCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.dto.TextCriteriaDTO;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.service.FilterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilterController.class)
class FilterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FilterService filterService;

  @Test
  void getAllFilters_ShouldReturnEmptyList_WhenNoFiltersExist() throws Exception {
    when(filterService.getAllFilters()).thenReturn(List.of());

    mockMvc.perform(get("/api/filters"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getAllFilters_ShouldReturnFiltersList_WhenFiltersExist() throws Exception {
    FilterDTO filter1 = FilterDTO.builder()
        .id(1L)
        .name("Filter 1")
        .selection(SelectionType.valueOf("S1"))
        .build();

    FilterDTO filter2 = FilterDTO.builder()
        .id(2L)
        .name("Filter 2")
        .selection(SelectionType.valueOf("S2"))
        .build();

    when(filterService.getAllFilters()).thenReturn(List.of(filter1, filter2));

    mockMvc.perform(get("/api/filters"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Filter 1")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Filter 2")));
  }

  @Test
  void addFilter_ShouldCreateFilter_WithTextCriteria() throws Exception {
    TextCriteriaDTO textCriteria = TextCriteriaDTO.builder()
        .filterType(TEXT)
        .conditionType("contains")
        .value("test")
        .build();

    FilterDTO inputFilter = FilterDTO.builder()
        .name("Text Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria))
        .build();

    FilterDTO savedFilter = FilterDTO.builder()
        .id(1L)
        .name("Text Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria))
        .build();

    when(filterService.add(any(FilterDTO.class))).thenReturn(savedFilter);

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputFilter)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Text Filter")))
        .andExpect(jsonPath("$.selection", is("S1")))
        .andExpect(jsonPath("$.criteriaList", hasSize(1)))
        .andExpect(jsonPath("$.criteriaList[0].filterType", is(TEXT)))
        .andExpect(jsonPath("$.criteriaList[0].conditionType", is("contains")))
        .andExpect(jsonPath("$.criteriaList[0].value", is("test")));
  }

  @Test
  void addFilter_ShouldCreateFilter_WithDateCriteria() throws Exception {
    DateCriteriaDTO dateCriteria = DateCriteriaDTO.builder()
        .filterType(DATE)
        .conditionType("after")
        .value(LocalDate.of(2024, 1, 1))
        .build();

    FilterDTO inputFilter = FilterDTO.builder()
        .name("Date Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(dateCriteria))
        .build();

    FilterDTO savedFilter = FilterDTO.builder()
        .id(1L)
        .name("Date Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(dateCriteria))
        .build();

    when(filterService.add(any(FilterDTO.class))).thenReturn(savedFilter);

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputFilter)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Date Filter")))
        .andExpect(jsonPath("$.criteriaList[0].filterType", is(DATE)))
        .andExpect(jsonPath("$.criteriaList[0].conditionType", is("after")))
        .andExpect(jsonPath("$.criteriaList[0].value", is("2024-01-01")));
  }

  @Test
  void addFilter_ShouldCreateFilter_WithAmountCriteria() throws Exception {
    AmountCriteriaDTO amountCriteria = AmountCriteriaDTO.builder()
        .filterType(AMOUNT)
        .conditionType("greater_than")
        .value(new BigDecimal("100.50"))
        .build();

    FilterDTO inputFilter = FilterDTO.builder()
        .name("Amount Filter")
        .selection(SelectionType.valueOf("S2"))
        .criteriaList(List.of(amountCriteria))
        .build();

    FilterDTO savedFilter = FilterDTO.builder()
        .id(1L)
        .name("Amount Filter")
        .selection(SelectionType.valueOf("S2"))
        .criteriaList(List.of(amountCriteria))
        .build();

    when(filterService.add(any(FilterDTO.class))).thenReturn(savedFilter);

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputFilter)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Amount Filter")))
        .andExpect(jsonPath("$.criteriaList[0].filterType", is(AMOUNT)))
        .andExpect(jsonPath("$.criteriaList[0].conditionType", is("greater_than")))
        .andExpect(jsonPath("$.criteriaList[0].value", is(100.50)));
  }

  @Test
  void addFilter_ShouldCreateFilter_WithMultipleCriteria() throws Exception {
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

    FilterDTO inputFilter = FilterDTO.builder()
        .name("Multi Criteria Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria, dateCriteria))
        .build();

    FilterDTO savedFilter = FilterDTO.builder()
        .id(1L)
        .name("Multi Criteria Filter")
        .selection(SelectionType.valueOf("S1"))
        .criteriaList(List.of(textCriteria, dateCriteria))
        .build();

    when(filterService.add(any(FilterDTO.class))).thenReturn(savedFilter);

    mockMvc.perform(post("/api/filters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputFilter)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.criteriaList", hasSize(2)));
  }
}