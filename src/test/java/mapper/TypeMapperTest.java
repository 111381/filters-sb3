package mapper;

import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.mapper.TypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
