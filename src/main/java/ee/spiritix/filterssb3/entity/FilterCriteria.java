package ee.spiritix.filterssb3.entity;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "filter_criteria")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "filter_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class FilterCriteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "filter_id", nullable = false)
  private Filter filter;
}
