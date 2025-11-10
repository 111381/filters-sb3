package ee.spiritix.filterssb3.repository;

import ee.spiritix.filterssb3.constant.AmountConditionType;
import ee.spiritix.filterssb3.constant.DateConditionType;
import ee.spiritix.filterssb3.constant.SelectionType;
import ee.spiritix.filterssb3.constant.TextConditionType;
import ee.spiritix.filterssb3.entity.AmountCriteria;
import ee.spiritix.filterssb3.entity.DateCriteria;
import ee.spiritix.filterssb3.entity.Filter;
import ee.spiritix.filterssb3.entity.TextCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static ee.spiritix.filterssb3.constant.SelectionType.S1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FilterRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private FilterRepository filterRepository;

  @Test
  void save_ShouldPersistFilter_WithTextCriteria() {
    Filter filter = new Filter();
    filter.setName("Text Filter");
    filter.setSelection(S1);

    TextCriteria textCriteria = TextCriteria.builder()
        .filter(filter)
        .conditionType(TextConditionType.CONTAINS)
        .textValue("test value")
        .build();

    filter.setFilterCriteria(List.of(textCriteria));

    Filter savedFilter = filterRepository.save(filter);
    entityManager.flush();

    assertThat(savedFilter.getId()).isNotNull();
    assertThat(savedFilter.getName()).isEqualTo("Text Filter");
    assertThat(savedFilter.getFilterCriteria()).hasSize(1);
    assertThat(savedFilter.getFilterCriteria().get(0)).isInstanceOf(TextCriteria.class);
  }

  @Test
  void save_ShouldPersistFilter_WithDateCriteria() {
    Filter filter = new Filter();
    filter.setName("Date Filter");
    filter.setSelection(SelectionType.S2);

    DateCriteria dateCriteria = DateCriteria.builder()
        .filter(filter)
        .conditionType(DateConditionType.IS_AFTER)
        .dateValue(LocalDate.of(2024, 1, 1))
        .build();

    filter.setFilterCriteria(List.of(dateCriteria));

    Filter savedFilter = filterRepository.save(filter);
    entityManager.flush();

    assertThat(savedFilter.getId()).isNotNull();
    assertThat(savedFilter.getFilterCriteria()).hasSize(1);
    assertThat(savedFilter.getFilterCriteria().get(0)).isInstanceOf(DateCriteria.class);

    DateCriteria savedCriteria = (DateCriteria) savedFilter.getFilterCriteria().get(0);
    assertThat(savedCriteria.getDateValue()).isEqualTo(LocalDate.of(2024, 1, 1));
  }

  @Test
  void save_ShouldPersistFilter_WithAmountCriteria() {
    Filter filter = new Filter();
    filter.setName("Amount Filter");
    filter.setSelection(S1);

    AmountCriteria amountCriteria = AmountCriteria.builder()
        .filter(filter)
        .conditionType(AmountConditionType.GREATER_THAN)
        .amountValue(new BigDecimal("100.50"))
        .build();

    filter.setFilterCriteria(List.of(amountCriteria));

    Filter savedFilter = filterRepository.save(filter);
    entityManager.flush();

    assertThat(savedFilter.getId()).isNotNull();
    assertThat(savedFilter.getFilterCriteria()).hasSize(1);
    assertThat(savedFilter.getFilterCriteria().get(0)).isInstanceOf(AmountCriteria.class);

    AmountCriteria savedCriteria = (AmountCriteria) savedFilter.getFilterCriteria().get(0);
    assertThat(savedCriteria.getAmountValue()).isEqualByComparingTo(new BigDecimal("100.50"));
  }

  @Test
  void save_ShouldPersistFilter_WithMultipleCriteria() {
    Filter filter = new Filter();
    filter.setName("Multi Criteria Filter");
    filter.setSelection(S1);

    TextCriteria textCriteria = TextCriteria.builder()
        .filter(filter)
        .conditionType(TextConditionType.EQUALS)
        .textValue("test")
        .build();

    DateCriteria dateCriteria = DateCriteria.builder()
        .filter(filter)
        .conditionType(DateConditionType.IS_BEFORE)
        .dateValue(LocalDate.of(2024, 12, 31))
        .build();

    filter.setFilterCriteria(List.of(textCriteria, dateCriteria));

    Filter savedFilter = filterRepository.save(filter);
    entityManager.flush();

    assertThat(savedFilter.getId()).isNotNull();
    assertThat(savedFilter.getFilterCriteria()).hasSize(2);
  }

  @Test
  void findAll_ShouldReturnAllFilters() {
    Filter filter1 = new Filter();
    filter1.setName("Filter 1");
    filter1.setSelection(S1);
    entityManager.persist(filter1);

    Filter filter2 = new Filter();
    filter2.setName("Filter 2");
    filter2.setSelection(SelectionType.S2);
    entityManager.persist(filter2);

    entityManager.flush();

    List<Filter> filters = filterRepository.findAll();

    assertThat(filters).hasSize(2);
    assertThat(filters).extracting(Filter::getName)
        .containsExactlyInAnyOrder("Filter 1", "Filter 2");
  }

  @Test
  void delete_ShouldRemoveFilter_AndItsCriteria() {
    Filter filter = new Filter();
    filter.setName("Filter to Delete");
    filter.setSelection(S1);

    TextCriteria textCriteria = TextCriteria.builder()
        .filter(filter)
        .conditionType(TextConditionType.CONTAINS)
        .textValue("test")
        .build();

    filter.setFilterCriteria(List.of(textCriteria));

    Filter savedFilter = entityManager.persistAndFlush(filter);
    Long filterId = savedFilter.getId();

    filterRepository.deleteById(filterId);
    entityManager.flush();

    Filter deletedFilter = entityManager.find(Filter.class, filterId);
    assertThat(deletedFilter).isNull();
  }
}
