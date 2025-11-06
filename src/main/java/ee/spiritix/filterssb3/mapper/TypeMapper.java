package ee.spiritix.filterssb3.mapper;

import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.DateConditionType;
import ee.spiritix.filterssb3.constant.TextConditionType;
import ee.spiritix.filterssb3.dto.AmountCriteriaDTO;
import ee.spiritix.filterssb3.dto.DateCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterCriteriaDTO;
import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.dto.TextCriteriaDTO;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.entity.DateCriteria;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.entity.FilterCriteria;
import ee.spiritix.filterssb3.entity.TextCriteria;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;

import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.AMOUNT;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.DATE;
import static ee.spiritix.filterssb3.constant.FilterCriteriaTypes.TEXT;

@Component
public class TypeMapper {

  private final ModelMapper modelMapper;

  public TypeMapper() {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
        .setMatchingStrategy(MatchingStrategies.STRICT);
    configureDtoToFilterMappings();
    configureFilterToDTOMappings();
  }

  private void configureDtoToFilterMappings() {
    TypeMap<FilterDTO, Filter> filterDtoToEntityMap = modelMapper.createTypeMap(FilterDTO.class, Filter.class);
    filterDtoToEntityMap.addMappings(mapper ->
      mapper.skip(Filter::setFilterCriteria)
    );
    filterDtoToEntityMap.setPostConverter(context -> {
      FilterDTO source = context.getSource();
      Filter destination = context.getDestination();

      if (source.getCriteriaList() != null) {
        List<FilterCriteria> criteria = source.getCriteriaList().stream()
            .map(this::mapCriteriaToEntity)
            .toList();

        criteria.forEach(filterCriteria -> addCriteria(destination, filterCriteria));
      }

      return destination;
    });
  }

  private void configureFilterToDTOMappings() {
    TypeMap<Filter, FilterDTO> filterToEntityDtoMap = modelMapper.createTypeMap(Filter.class, FilterDTO.class);
    filterToEntityDtoMap.addMappings(mapper ->
        mapper.skip(FilterDTO::setCriteriaList)
    );
    filterToEntityDtoMap.setPostConverter(context -> {
      Filter source = context.getSource();
      FilterDTO destination = context.getDestination();

      if (source.getFilterCriteria() != null) {
        List<FilterCriteriaDTO> criteriaDtos = source.getFilterCriteria().stream()
            .map(this::mapCriteriaToDto)
            .toList();

        destination.setCriteriaList(criteriaDtos);
      }

      return destination;
    });
  }

  public void addCriteria(Filter filter, FilterCriteria criteria) {
    filter.getFilterCriteria().add(criteria);
    criteria.setFilter(filter);
  }

  private FilterCriteria mapCriteriaToEntity(FilterCriteriaDTO dto) {
    if (dto instanceof AmountCriteriaDTO amountDto) {
      return AmountCriteria.builder()
          .conditionType(AmountConditionType.valueOf(amountDto.getConditionType().toUpperCase()))
          .amountValue(amountDto.getValue()).build();
    } else if (dto instanceof DateCriteriaDTO dateDto) {
      return DateCriteria.builder()
          .conditionType(DateConditionType.valueOf(dateDto.getConditionType().toUpperCase()))
          .dateValue(dateDto.getValue())
          .build();
    } else if (dto instanceof TextCriteriaDTO textDto) {
      return TextCriteria.builder()
          .conditionType(TextConditionType.valueOf(textDto.getConditionType().toUpperCase()))
          .textValue(textDto.getValue())
          .build();
    }
    throw new IllegalArgumentException("Unknown FilterCriteriaDTO type: " + dto.getClass().getName());
  }

  private FilterCriteriaDTO mapCriteriaToDto(FilterCriteria entity) {
    if (entity instanceof AmountCriteria amountEntity) {
      return AmountCriteriaDTO.builder()
          .filterType(AMOUNT)
          .conditionType(amountEntity.getConditionType().getValue())
          .value(amountEntity.getAmountValue())
          .build();
    } else if (entity instanceof DateCriteria dateEntity) {
      return DateCriteriaDTO.builder()
          .filterType(DATE)
          .conditionType(dateEntity.getConditionType().getValue())
          .value(dateEntity.getDateValue())
          .build();
    } else if (entity instanceof TextCriteria textEntity) {
      return TextCriteriaDTO.builder()
          .filterType(TEXT)
          .conditionType(textEntity.getConditionType().getValue())
          .value(textEntity.getTextValue())
          .build();
    }
    throw new IllegalArgumentException("Unknown FilterCriteria type: " + entity.getClass().getName());
  }

  public <T> Object mapObject(Object source, Class<T> targetClass) {
    return modelMapper.map(source, targetClass);
  }

  public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
    return source
        .stream()
        .map(element -> modelMapper.map(element, targetClass))
        .toList();
  }
}
