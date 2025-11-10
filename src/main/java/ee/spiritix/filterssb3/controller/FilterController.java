package ee.spiritix.filterssb3.controller;

import ee.spiritix.filterssb3.dto.FilterDTO;
import ee.spiritix.filterssb3.service.FilterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "${filters.frontend.url}")
@RequestMapping("/api/filters")
@RequiredArgsConstructor
public class FilterController {

  private final FilterService filterService;

  @GetMapping
  public List<FilterDTO> getAllFilters() {
    return filterService.getAllFilters();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void add(@Valid @RequestBody FilterDTO filterDto) {
    filterService.add(filterDto);
  }
}
