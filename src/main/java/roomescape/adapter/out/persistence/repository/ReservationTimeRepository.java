package roomescape.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.adapter.out.ReservationTimeEntity;

public interface ReservationTimeRepository extends JpaRepository<ReservationTimeEntity, Long> {

  Optional<ReservationTimeEntity> findByStartAt(String startAt);

  @Query("SELECT rt FROM reservation_time rt LEFT JOIN rt.reservationEntities r WHERE r.date != :date OR r.theme.id != :themeId OR r.id IS NULL")
  List<ReservationTimeEntity> findAvailableReservationTimes(@Param("date") String date, @Param("themeId") Long themeId);
}
