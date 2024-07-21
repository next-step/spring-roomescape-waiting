package roomescape.waiting.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;
import roomescape.waiting.domain.ReservationWaiting;

public interface ReservationWaitingRepository extends JpaRepository<ReservationWaiting, Long> {

    List<ReservationWaiting> findAllByUser(User user);

    boolean existsByUser(User user);

    boolean existsByUserAndDateAndTimeAndTheme(User user, LocalDate date, ReservationTime reservationTime, Theme theme);
}
