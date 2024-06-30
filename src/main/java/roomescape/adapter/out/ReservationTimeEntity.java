package roomescape.adapter.out;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "reservation_time")
public class ReservationTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String startAt;

  @OneToMany(mappedBy = "reservationTime")
  private List<ReservationEntity> reservationEntities = new ArrayList<>();


public ReservationTimeEntity() {
  }

  public ReservationTimeEntity(Long id, String startAt) {
    this.id = id;
    this.startAt = startAt;
  }

  public Long getId() {
    return id;
  }

  public String getStartAt() {
    return startAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationTimeEntity that = (ReservationTimeEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static ReservationTimeEntity of(Long id, String startAt) {
    return new ReservationTimeEntity(id, startAt);
  }
}
