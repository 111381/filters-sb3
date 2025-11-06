package ee.spiritix.filterssb3.repository;

import ee.spiritix.filterssb3.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
}
