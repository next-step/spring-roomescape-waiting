package roomescape.apply.reservationwaiting.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.apply.reservationwaiting.domain.ReservationWaiting;

public interface ReservationWaitingJpaRepository extends JpaRepository<ReservationWaiting, Long> {
}
