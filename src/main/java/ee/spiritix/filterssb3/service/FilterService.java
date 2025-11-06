package ee.spiritix.filterssb3.service;

import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.mapper.TypeMapper;
import ee.spiritix.filterssb3.repository.FilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  public void add(FilterDTO filterDto) {
    Filter filter = (Filter) typeMapper.mapObject(filterDto, Filter.class);
    filterRepository.save(filter);
  }

  /*public List<Object> filterObjectsUsingFilterWithId(List<Object> objects, Long filterId) {
    return objects.stream()
        .filter(object -> {
          Filter filter = filterRepository.findById(filterId).orElseThrow();
          return filter.getFilterFunction().apply(object);
        })
        .toList();
  }*/
}
