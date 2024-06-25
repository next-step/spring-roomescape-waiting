package roomescape.apply.reservation.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    @Query("SELECT r.id FROM Reservation r WHERE r.id = :id")
    Optional<Long> findIdById(@Param("id") long id);

    void deleteById(long id);

    @Query("""
                SELECT
                    r.id
                FROM
                    Reservation r
                WHERE
                    r.time.id = :timeId
                    AND r.theme.id = :themeId
            """)
    Optional<Long> findIdByTimeIdAndThemeId(@Param("timeId") long timeId, @Param("themeId") long themeId);

    @Query("SELECT r.id FROM Reservation r WHERE r.time.id = :timeId")
    Optional<Long> findIdByTimeId(@Param("timeId") long timeId);

    @Query("SELECT r.id FROM Reservation r WHERE r.theme.id = :themeId")
    Optional<Long> findIdByThemeId(@Param("themeId") long themeId);

    @Query("""
                SELECT
                    r.time.id
                FROM
                    Reservation r
               WHERE
                   r.reservationDate.date = :date
                   AND r.theme.id = :themeId
            """)
    List<Long> findReservedTimeIdsInDateAndThemeId(@Param("date") String date, @Param("themeId") long themeId);

    List<Reservation> searchReservationsBySearchParams(ReservationSearchParams searchParams);
}
