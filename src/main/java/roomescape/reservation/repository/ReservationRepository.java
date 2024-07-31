package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.member.Member;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndReservationTimeAndTheme(LocalDate date, ReservationTime time,
        Theme theme);

    Optional<Reservation> findByReservationTime(ReservationTime time);

    Optional<Reservation> findByTheme(Theme theme);

    List<Reservation> findByDateAndTheme(LocalDate date, Theme theme);

    List<Reservation> findByMember(Member member);

    @Query("SELECT COUNT(r) " +
        "     FROM Reservation r " +
        "     WHERE r.date = :date " +
        "       AND r.reservationTime = :time " +
        "       AND r.theme = :theme" +
        "       AND r.id < :id ")
    Long findRankByMember(Long id, LocalDate date, ReservationTime time, Theme theme);
}
